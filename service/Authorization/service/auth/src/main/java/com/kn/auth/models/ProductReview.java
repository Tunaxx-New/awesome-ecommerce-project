package com.kn.auth.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.annotations.SafeToUpdate;
import com.kn.auth.annotations.SensetiveData;
import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.wrappers.SafeUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableNameConstants.PRODUCT_REVIEW, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class ProductReview extends SafeUpdate<ProductReview> {
    @SensetiveData
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = Buyer.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "buyer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Buyer buyer;

    @SensetiveData
    @OneToOne(targetEntity = OrderItem.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "order_item_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private OrderItem orderItem;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    @SafeToUpdate
    private String comment;
    @SafeToUpdate
    private int rating;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
