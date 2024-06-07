package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Order;
import com.kn.auth.models.OrderItem;
import com.kn.auth.models.TimeValue;
import com.kn.auth.repositories.BuyerRepository;
import com.kn.auth.repositories.OrderItemRepository;
import com.kn.auth.repositories.OrderRepository;
import com.kn.auth.repositories.SellerRepository;
import com.kn.auth.responses.ClvResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    /**
     * @param authenticationId AOP Around aspect to get authentication id from
     *                         security context
     * @return Buyer by authentication id
     */
    public Buyer getByAuthenticationId(@AuthenticatedId int authenticationId) {
        return buyerRepository.findByAuthenticationId(authenticationId).get();
    }

    /**
     * @param authenticationId AOP Around aspect to get authentication id from
     *                         security context
     * @return Buyer by authentication id with safe fields to read
     */
    public Buyer getByAuthenticationIdSafe(@AuthenticatedId int authenticationId) {
        return buyerRepository.findByAuthenticationId(authenticationId)
                .map(entity -> entity.safeReturn())
                .orElse(null);
    }

    /**
     * @param id table buyer id
     * @return Buyer by id
     */
    public Buyer getById(int id) {
        return buyerRepository.findById(id).get();
    }

    /**
     * @param buyer created buyer FULL UPDATE
     * @return Buyer created object
     * @throws IllegalArgumentException
     */
    public Buyer create(Buyer buyer) throws IllegalArgumentException {
        return buyerRepository.save(buyer);
    }

    /**
     * @param buyer buyer for deletion
     */
    public void delete(Buyer buyer) {
        buyerRepository.delete(buyer);
    }

    /**
     * @param updatedBuyer     updated buyer FULL UPDATE
     * @param authenticationId
     * @return Buyer updated buyer
     */
    public Buyer update(Buyer updatedBuyer) {
        return buyerRepository.save(updatedBuyer);
    }

    /**
     * @param updatedBuyer     updated buyer by user SAFE UPDATE
     * @param authenticationId
     * @return Buyer updated buyer
     */
    public Buyer safeUpdate(Buyer updatedBuyer, @AuthenticatedId int authenticationId) {
        return buyerRepository
                .save(buyerRepository.findByAuthenticationId(authenticationId).get().safeUpdate(updatedBuyer));
    }

    public BigDecimal getLoyaltyIndexAuthentication(@AuthenticatedId int authenticationId) {
        return getLoyaltyIndex(getByAuthenticationId(authenticationId));
    }

    /*
     * Distinct sellers that product was purchased / All sellers + 1
     * The squared difference of orders creation dates in days
     */
    public BigDecimal getLoyaltyIndex(Buyer buyer) {
        List<Long> datesInDays = new ArrayList<>();
        Set<Integer> distinctSellers = new HashSet<>();
        for (Order order : buyer.getOrders()) {
            for (OrderItem orderItem : order.getOrderItems()) {
                distinctSellers.add(orderItem.getProduct().getSeller().getId());
            }
            long epochSeconds = order.getCreatedTime().toInstant(ZoneOffset.UTC).getEpochSecond();
            long daysSinceEpoch = epochSeconds / (24 * 60 * 60);
            datesInDays.add(Long.valueOf(daysSinceEpoch));
        }

        if (datesInDays.size() <= 1)
            return new BigDecimal(distinctSellers.size());

        List<Long> shiftedDatesInDays = new ArrayList<>(datesInDays);
        for (int i = 1; i < datesInDays.size(); i++)
            shiftedDatesInDays.set(i, datesInDays.get(i) - datesInDays.get(0));
        shiftedDatesInDays.set(0, 0L);

        double datesMean = shiftedDatesInDays.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double sumOfSquaredDifferences = shiftedDatesInDays.stream()
                .mapToDouble(num -> Math.pow(num - datesMean, 2))
                .sum();

        return new BigDecimal(distinctSellers.size())
                .divide(new BigDecimal(sellerRepository.findAll().size() + 1), 10, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(sumOfSquaredDifferences + 1));
    }

    /*
     * CLV - Customer Lifetime Value p. 171, 2015, Neil T. B. et al.
     * In price
     * Value for future cashflows from the customer relationship
     * M - average profit from each order item + add here payment method and
     * shipping profitability
     * R - Difference(Previous spendings, Gap spendings) / Date gap between in days
     * purchases order
     */
    public ClvResponse getCLVs(@AuthenticatedId int authenticationId) {
        Buyer buyer = getByAuthenticationId(authenticationId);
        List<Order> sortedDateOrders = buyer.getOrders().stream()
                .sorted(Comparator.comparing(Order::getCreatedTime))
                .collect(Collectors.toList());

        BigDecimal i = BigDecimal.ONE;
        BigDecimal previousSpending = BigDecimal.ZERO;
        BigDecimal previousMargin = BigDecimal.ZERO;
        BigDecimal previousDiscount = BigDecimal.ZERO;
        LocalDateTime previousDate = null;

        ClvResponse clvResponse = new ClvResponse();

        BigDecimal marginAll = BigDecimal.ZERO;
        BigDecimal retentionAll = BigDecimal.ZERO;
        BigDecimal discountAll = BigDecimal.ZERO;
        BigDecimal spendingsAll = BigDecimal.ZERO;

        for (Order order : sortedDateOrders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                spendingsAll = spendingsAll.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getAmount())));
            }
        }

        for (Order order : sortedDateOrders) {
            i = i.add(BigDecimal.ONE);

            BigDecimal margin = BigDecimal.ZERO;
            BigDecimal retention = BigDecimal.ZERO;
            BigDecimal discount = BigDecimal.ZERO;
            BigDecimal spendings = BigDecimal.ZERO;

            for (OrderItem orderItem : order.getOrderItems()) {
                margin = margin.add(orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getCommissionPercentage()).divide(new BigDecimal(100), 10,
                                RoundingMode.HALF_UP))
                        .multiply(new BigDecimal(orderItem.getAmount())));

                spendings = spendings.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getAmount())));

                discount = discount.add(new BigDecimal(((double) orderItem.getCommissionPercentage() / 2) / 100.0));
            }

            if (i.equals(BigDecimal.ONE)) {
                previousMargin = margin;
                previousSpending = spendings;
                previousDiscount = discount;
                previousDate = order.getCreatedTime();
                continue;
            }

            BigDecimal timeDelta = new BigDecimal(
                    getMillisecondsDifferenceToDays(order.getCreatedTime(), previousDate));
            if (timeDelta.add(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
                retention = retention.add(spendings.subtract(previousSpending)
                        .divide(timeDelta.add(BigDecimal.ONE), 10, RoundingMode.HALF_UP));
            }

            if (!spendingsAll.equals(BigDecimal.ZERO)) {
                retention = retention.divide(spendingsAll, 10, RoundingMode.HALF_UP);
            }

            // Check for division by zero
            BigDecimal divisor = BigDecimal.ONE.add(discount).subtract(retention);
            if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                divisor = BigDecimal.ONE;
            }

            clvResponse.getCLVsGapsStatic().add(new TimeValue(order.getCreatedTime(),
                    margin.multiply(retention.divide(divisor, 10, RoundingMode.HALF_UP))));

            divisor = BigDecimal.ONE.add(discount.subtract(previousDiscount)).subtract(retention);
            if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                divisor = BigDecimal.ONE;
            }

            clvResponse.getCLVsGapsDelta().add(new TimeValue(order.getCreatedTime(),
                    margin.subtract(previousMargin)
                            .multiply(retention.divide(divisor, 10, RoundingMode.HALF_UP))));

            previousMargin = margin;
            previousSpending = spendings;
            previousDiscount = discount;

            marginAll = marginAll.add(margin);
            retentionAll = retentionAll.add(retention);
            discountAll = discountAll.add(discount);
        }

        BigDecimal averageMargin = marginAll.divide(i, 10, RoundingMode.HALF_UP);
        BigDecimal averageRetention = retentionAll.divide(i, 10, RoundingMode.HALF_UP);
        BigDecimal averageDiscount = discountAll.divide(i, 10, RoundingMode.HALF_UP);

        BigDecimal divisor = BigDecimal.ONE.add(averageDiscount).subtract(averageRetention);
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            divisor = BigDecimal.ONE;
        }

        clvResponse.setCLVsAverage(averageMargin.multiply(averageRetention.divide(divisor, 10, RoundingMode.HALF_UP)));

        return clvResponse;
    }

    public static double getMillisecondsDifferenceToDays(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 != null && dateTime2 != null) {
            double millisecondsInDay = 24 * 60 * 60 * 1000;
            Duration duration = Duration.between(dateTime1, dateTime2);
            return ((double) duration.toMillis()) / millisecondsInDay;
        }
        return 0;
    }
}
