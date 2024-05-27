package com.kn.auth.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kn.auth.constants.TableNameConstants;

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
@Table(name = TableNameConstants.ORDER_ITEM, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Order order;

    @OneToOne(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductReview productReview;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private int amount;
    private BigDecimal price;

    private Integer commissionPercentage;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "order_item_buyer_transparent_policy", joinColumns = @JoinColumn(name = "buyer_order_item_id"), inverseJoinColumns = @JoinColumn(name = "buyer_transparent_policy_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TransparentPolicy> buyerTransparentPolicies;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "order_item_seller_transparent_policy", joinColumns = @JoinColumn(name = "seller_order_item_id"), inverseJoinColumns = @JoinColumn(name = "seller_transparent_policy_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TransparentPolicy> sellerTransparentPolicies;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
