package com.kn.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Cart;
import com.kn.auth.models.CartItem;
import com.kn.auth.models.PaymentMethod;
import com.kn.auth.models.Product;
import com.kn.auth.models.Seller;
import com.kn.auth.models.ShippingAddress;
import java.util.Arrays;
import java.util.List;

@Transactional
@Rollback
@SpringBootTest
public class CartServiceTest {
    @Autowired
    CartService cartService;
    @Autowired
    BuyerService buyerService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    ShippingAddressService shippingAddressService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    ProductService productService;
    @Autowired
    SellerService sellerService;

    Buyer buyer;
    Authentication authentication;
    Cart cart;
    int testInt = 2;
    Product product;

    @BeforeEach
    void setUp() {
        authentication = authenticationService.create(Authentication.builder().build());
        buyer = buyerService.create(Buyer.builder().authentication(authentication).build());
        ShippingAddress shippingAddress = shippingAddressService.create(ShippingAddress.builder().build());
        PaymentMethod paymentMethod = paymentMethodService.create(PaymentMethod.builder().build());
        cart = cartService.create(Cart.builder().buyer(buyer).paymentMethod(paymentMethod)
                .shippingAddress(shippingAddress).build());

        Seller seller = sellerService.create(Seller.builder().authentication(authentication).build(), authentication.getId());
        product = productService.create(Product.builder().seller(seller).build(), authentication.getId());
        List<CartItem> cartItems = Arrays.asList(
                CartItem.builder().product(product).amount(1).cart(cart).build(),
                CartItem.builder().product(product).amount(2).cart(cart).build(),
                CartItem.builder().product(product).amount(3).cart(cart).build());
        cartItemService.createMany(cartItems);
    }

    @Test
    void info() throws InterruptedException {
        Cart cart = cartService.getComposed(authentication.getId());
        assertEquals(cart.getId(), cart.getId());
        assertEquals(3, cart.getCartItems().size());
    }

    @Test
    void add() {
        assertNotNull(cartService.add(product.getId(), authentication.getId()));
    }

    @Test
    void changeItems() {
        List<CartItem> cartItems = Arrays.asList(
                CartItem.builder().product(product).amount(1).cart(cart).build(),
                CartItem.builder().product(product).amount(1).cart(cart).build(),
                CartItem.builder().product(product).amount(1).cart(cart).build(),
                CartItem.builder().product(product).amount(2).cart(cart).build(),
                CartItem.builder().product(product).amount(3).cart(cart).build());
        assertEquals(5, cartService.changeItems(cartItems, authentication.getId()).size());
        cartItems = Arrays.asList(
                CartItem.builder().product(product).amount(1).cart(cart).build(),
                CartItem.builder().product(product).amount(1).cart(cart).build());
        assertEquals(2, cartService.changeItems(cartItems, authentication.getId()).size());
        cartItems = Arrays.asList();
        assertEquals(0, cartService.changeItems(cartItems, authentication.getId()).size());
        cartItems = Arrays.asList(
                CartItem.builder().product(product).amount(1).cart(cart).build(),
                CartItem.builder().product(product).amount(1).cart(cart).build());
        assertEquals(2, cartService.changeItems(cartItems, authentication.getId()).size());
    }

    @Test
    void clear() {
        cartService.clear(authentication.getId());
        assertEquals(0, cartItemService.getAllByCartId(cart.getId()).size());
    }

    @Test
    void update() {
        ShippingAddress shippingAddress = shippingAddressService.create(ShippingAddress.builder().id(123).build());
        PaymentMethod paymentMethod = paymentMethodService.create(PaymentMethod.builder().id(123).build());
        Authentication authentication_new = Authentication.builder().id(-1).build();
        Buyer buyer = Buyer.builder().authentication(authentication_new).build();
        Cart cart = Cart.builder().paymentMethod(paymentMethod).shippingAddress(shippingAddress)
                .buyer(Buyer.builder().build()).build();
        Cart updatedCart = cartService.update(cart, authentication.getId());
        assertEquals(paymentMethod, updatedCart.getPaymentMethod());
        assertNotEquals(buyer, updatedCart.getBuyer());
    }
}