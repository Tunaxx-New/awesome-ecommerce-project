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

        BigDecimal i = new BigDecimal(1);
        BigDecimal previousSpending = new BigDecimal(0);
        BigDecimal previousMargin = new BigDecimal(0);
        BigDecimal previousDiscount = new BigDecimal(0);
        LocalDateTime previousDate = null;

        ClvResponse clvResponse = new ClvResponse();

        BigDecimal marginAll = new BigDecimal(0);
        BigDecimal retentionAll = new BigDecimal(0);
        BigDecimal discountAll = new BigDecimal(0);
        BigDecimal spendingsAll = new BigDecimal(0);

        for (Order order : sortedDateOrders)
            for (OrderItem orderItem : order.getOrderItems())
                spendingsAll = spendingsAll.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getAmount())));
        // Each order is time gap
        for (Order order : sortedDateOrders) {
            i = i.add(new BigDecimal(1));
            // Margin - average profit generated per transaction
            BigDecimal margin = new BigDecimal(0);
            // Retention - retention value
            BigDecimal retention = new BigDecimal(0);
            // Discount - retention value
            BigDecimal discount = new BigDecimal(0);

            BigDecimal spendings = new BigDecimal(0);
            BigDecimal j = new BigDecimal(0);
            for (OrderItem orderItem : order.getOrderItems()) {
                // Margin calculation
                margin = margin.add(orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getCommissionPercentage()).divide(new BigDecimal(100), 10,
                                RoundingMode.HALF_UP))
                        .multiply(new BigDecimal(orderItem.getAmount())));

                // Retention spendings
                spendings = spendings.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getAmount())));

                // Discount
                discount = discount.add(new BigDecimal(((double) orderItem.getCommissionPercentage() / 2) / 100.0));
            }

            if (i.equals(new BigDecimal(1))) {
                previousMargin = margin;
                previousSpending = spendings;
                previousDiscount = discount;
                previousDate = order.getCreatedTime();
                continue;
            }

            BigDecimal timeDelta = new BigDecimal(
                    getMillisecondsDifferenceToDays(order.getCreatedTime(), previousDate));
            if (timeDelta.add(new BigDecimal(1)).compareTo(new BigDecimal(0)) != 0)
                retention = retention
                        .add(spendings.subtract(previousSpending).divide(timeDelta.add(new BigDecimal(1)), 10,
                                RoundingMode.HALF_UP));
            if (!spendingsAll.equals(new BigDecimal(0))) {
                if (spendingsAll.compareTo(new BigDecimal(0)) != 0)
                    retention = retention.divide(spendingsAll, 10, RoundingMode.HALF_UP);
            }

            // Formula with static and retention difference
            if (!discount.subtract(retention).equals(new BigDecimal(-1)))
                retention = retention.add(new BigDecimal(1));
            clvResponse.getCLVsGapsStatic().add(new TimeValue(order.getCreatedTime(),
                    margin.multiply(retention.divide(new BigDecimal(1).add(discount).subtract(retention), 10,
                            RoundingMode.HALF_UP))));

            // Formula with difference with previous value
            if (previousDiscount.subtract(retention).equals(new BigDecimal(-1)))
                retention = retention.add(new BigDecimal(1));
            clvResponse.getCLVsGapsDelta().add(new TimeValue(order.getCreatedTime(),
                    margin.subtract(previousMargin).multiply(retention
                            .divide(new BigDecimal(1).add(discount.subtract(previousDiscount)).subtract(retention), 10,
                                    RoundingMode.HALF_UP))));

            previousMargin = margin;
            previousSpending = spendings;
            previousDiscount = discount;

            marginAll.add(margin);
            retentionAll.add(retention);
            discountAll.add(discount);
        }

        // Formula with average dividing
        clvResponse
                .setCLVsAverage(
                        marginAll.divide(i, 10, RoundingMode.HALF_UP)
                                .multiply(retentionAll.divide(i, 10, RoundingMode.HALF_UP)
                                        .divide(new BigDecimal(1).add(discountAll.divide(i, 10, RoundingMode.HALF_UP))
                                                .subtract(retentionAll.divide(i, 10, RoundingMode.HALF_UP)), 10,
                                                RoundingMode.HALF_UP)));
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
