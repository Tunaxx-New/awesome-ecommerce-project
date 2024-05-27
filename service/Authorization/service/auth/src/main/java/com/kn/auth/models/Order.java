package com.kn.auth.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.annotations.SafeToUpdate;
import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.wrappers.SafeUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableNameConstants.ORDER, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class Order extends SafeUpdate<Order> {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Formula("(SELECT COALESCE(SUM(oi.price), 0) FROM " + TableNameConstants.ORDER_ITEM + " oi WHERE oi.order_id = id)")
    private BigDecimal price;

    @ManyToOne(targetEntity = Buyer.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "buyer_id")
    @JsonIgnore
    private Buyer buyer;

    @SafeToUpdate
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentMethod paymentMethod;

    @SafeToUpdate
    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShippingAddress shippingAddress;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
