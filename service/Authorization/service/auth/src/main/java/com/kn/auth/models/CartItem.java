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
@Table(name = TableNameConstants.CART_ITEM, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class CartItem extends SafeUpdate<CartItem> {
    @SensetiveData
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = Cart.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "cart_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Cart cart;

    @SafeToUpdate
    @ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @SafeToUpdate
    private int amount;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
