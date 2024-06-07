package com.kn.auth.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.enums.EmailError;
import com.kn.auth.enums.ErrorMessage;
import com.kn.auth.enums.PasswordError;
import com.kn.auth.exceptions.DatabaseIntegrityUniquenessException;
import com.kn.auth.exceptions.EmailValidationException;
import com.kn.auth.exceptions.PasswordValidationException;
import com.kn.auth.models.Authentication;
import com.kn.auth.models.Badge;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Cart;
import com.kn.auth.models.Order;
import com.kn.auth.models.OrderItem;
import com.kn.auth.models.Seller;
import com.kn.auth.models.TransparentPolicy;
import com.kn.auth.models.TransparentPolicyHistory;
import com.kn.auth.repositories.AuthenticationRepository;
import com.kn.auth.repositories.BadgeRepository;
import com.kn.auth.repositories.OrderRepository;
import com.kn.auth.repositories.RoleRepository;
import com.kn.auth.repositories.TransparentPolicyHistoryRepository;
import com.kn.auth.repositories.TransparentPolicyRepository;
import com.kn.auth.responses.ProfileResponse;
import com.kn.auth.utils.StringUtil;
import com.kn.auth.validators.EmailValidator;
import com.kn.auth.validators.PasswordValidator;
import com.kn.auth.services.BadgeService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final AuthenticationRepository authenticationRepository;
        private final AuthenticationManager authenticationManager;
        private final TransparentPolicyRepository transparentPolicyRepository;
        private final BadgeRepository badgeRepository;

        private final JwtService jwtService;
        private final BuyerService buyerService;
        private final CartService cartService;
        private final DatabaseService databaseService;
        private final RoleRepository roleRepository;
        private final OrderRepository orderRepository;

        private final TransparentPolicyHistoryRepository transparentPolicyHistoryRepository;

        @Autowired
        private final PasswordEncoder passwordEncoder;

        /**
         * @param email    - String email of user
         * @param password - String password of user
         * @return String - JWT token
         * @throws PasswordValidationException
         * @throws EmailValidationException
         * @throws DatabaseIntegrityUniquenessException - user already exist in db
         * @throws JwtException
         * @throws Exception
         */
        public String register(String email, String password)
                        throws PasswordValidationException,
                        EmailValidationException,
                        DatabaseIntegrityUniquenessException,
                        JwtException,
                        Exception {

                PasswordError passwordError = new PasswordValidator().validate(password);
                if (passwordError != PasswordError.CORRECT)
                        throw new PasswordValidationException(passwordError);

                EmailError emailError = new EmailValidator().validate(email);
                if (emailError != EmailError.CORRECT)
                        throw new EmailValidationException(emailError);

                Authentication authentication = Authentication.builder()
                                .email(email)
                                .password(passwordEncoder.encode(password))
                                .roles(List.of(roleRepository.findByName(com.kn.auth.enums.Role.USER).get(),
                                                roleRepository.findByName(com.kn.auth.enums.Role.BUYER).get()))
                                .build();
                try {
                        authenticationRepository.save(authentication);
                } catch (DataIntegrityViolationException e) {
                        String constraintName = StringUtil.extractSubstring(e.getMessage(), "key '(.*?)'");
                        String column = databaseService.contstraintColumn(constraintName);
                        throw new DatabaseIntegrityUniquenessException(password, e, column);
                }
                try {
                        Buyer buyer = Buyer.builder()
                                        .authentication(authentication)
                                        .badges(List.of(badgeRepository.findById(1).get()))
                                        .build();
                        buyer = buyerService.create(buyer);
                        Cart cart = Cart.builder()
                                        .buyer(buyer)
                                        .build();
                        cartService.create(cart);
                } catch (Exception e) {
                        authenticationRepository.delete(authentication);
                        throw e;
                }
                return jwtService.generateToken(authentication);
        }

        /**
         * @param email    - String email of user
         * @param password - String password of user
         * @return Authentication from Security core
         * @throws DisabledException
         * @throws LockedException
         * @throws BadCredentialsException
         */
        private org.springframework.security.core.Authentication authenticateSecurity(String email, String password)
                        throws DisabledException, LockedException, BadCredentialsException {
                return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }

        /**
         * @param email    - String email of user
         * @param password - String password of user
         * @return String - JWT token
         * @throws UsernameNotFoundException
         * @throws AuthenticationException
         */
        public String authenticate(String email, String password)
                        throws UsernameNotFoundException, AuthenticationException {
                Authentication authentication = authenticationRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                ErrorMessage.USER_NOT_FOUND.getValue()));
                authenticateSecurity(email, password);
                return jwtService.generateToken(authentication);
        }

        /**
         * @param authentication authentication model
         * @return Authentication saved model
         */
        public Authentication create(Authentication authentication) {
                return authenticationRepository.save(authentication);
        }

        /**
         * @param email - String email
         *              Deletes user from db by email
         */
        public void deleteByEmail(String email) {
                authenticationRepository.deleteByEmail(email);
        }

        /**
         * @param authentication
         */
        public void delete(Authentication authentication) {
                authenticationRepository.delete(authentication);
        }

        /**
         * @param email - String email
         * @return Authentication found user by email
         */
        public Authentication findByEmail(String email) {
                return authenticationRepository.findByEmail(email).get();
        }

        /**
         * @param authenticatedId - int Aspected id of authentication with authorized
         *                        token
         * @return Authentication found user by id
         */
        public ProfileResponse findByAspectId(@AuthenticatedId int authenticatedId) {
                Authentication profile = authenticationRepository.findById(authenticatedId).get();
                List<TransparentPolicy> buyerTransparentPolicies = new ArrayList<>(
                                profile.getAuthenticationTransparentPolicies());
                if (buyerTransparentPolicies.stream().anyMatch(policy -> {
                        return policy.getName().getValue().equals(
                                        com.kn.auth.enums.TransparentPolicy.AVAILABLE_PRODUCT_ORDERS.getValue());
                })) {
                        List<Order> orders = new ArrayList<>();
                        boolean isDate = buyerTransparentPolicies.stream().anyMatch(policy -> {
                                return policy.getName().getValue().equals(
                                                com.kn.auth.enums.TransparentPolicy.AVAILABLE_PRODUCT_ORDERS_DATE
                                                                .getValue());
                        });
                        List<Order> orders_ = orderRepository.findAllByBuyerId(profile.getBuyer().getId()).get();
                        for (Order order : orders_) {
                                if (!isDate)
                                        order.setCreatedTime(null);
                                order.setShippingAddress(null);
                                order.setPaymentMethod(null);
                                order.setBuyer(null);
                                orders.add(order);
                        }
                        System.out.println(orders.size() + "TUNAXX");
                        profile.getBuyer().setOrders(orders);
                }

                if (profile.getBuyer() != null) {
                        profile.getBuyer()
                                        .setCommissionPercentage(BadgeService.calculatePercentage(profile.getBuyer(),
                                                        profile.getBuyer().getCommissionPercentage()));
                        profile.getBuyer()
                                        .setCommissionPercentage(BadgeService.calculatePercentage(
                                                        profile.getBuyer().getCommissionPercentage(),
                                                        profile.getTransparentPolicyHistories()));
                }
                if (profile.getSeller() != null) {
                        profile.getSeller().setCommissionPercentage(BadgeService
                                        .calculatePercentage(profile.getSeller(),
                                                        profile.getSeller().getCommissionPercentage(),
                                                        profile.getBuyer().getBadges()));
                        profile.getSeller().setCommissionPercentage(BadgeService
                                        .calculatePercentage(profile.getSeller().getCommissionPercentage(),
                                                        profile.getTransparentPolicyHistories()));
                }

                Integer buyerId = profile.getBuyer().getId();
                List<Order> orders = orderRepository
                                .findAllByBuyerId(buyerId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
                                System.out.println(orders.size() + " TUNAXX");
                return ProfileResponse.builder().authentication(profile).orders(orders)
                                .build();
        }

        public List<TransparentPolicy> changeTransparentPolicies(List<TransparentPolicy> transparentPolicies,
                        @AuthenticatedId int authenticationId) {
                Authentication authentication = authenticationRepository.findById(authenticationId).get();

                transparentPolicyHistoryRepository.deleteAllByAuthenticationId(authenticationId);
                List<TransparentPolicyHistory> newTransparentPolicyHistories = new ArrayList<>();
                for (TransparentPolicy transparentPolicy : transparentPolicies) {
                        newTransparentPolicyHistories.add(TransparentPolicyHistory.builder()
                                        .authentication(authentication)
                                        .transparentPolicy(transparentPolicy).build());
                }
                transparentPolicyHistoryRepository.saveAll(newTransparentPolicyHistories);
                authentication.setAuthenticationTransparentPolicies(transparentPolicies);
                return authenticationRepository.save(authentication).getAuthenticationTransparentPolicies();
        }
}
