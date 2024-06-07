package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kn.auth.constants.TableNameConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = TableNameConstants.PAYMENT_METHODS, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class PaymentMethod {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    private String title;

    @OneToMany(mappedBy = "paymentMethod")
    @JsonIgnore
    @JsonIgnoreProperties("paymentMethod")
    private List<Cart> carts;

    @OneToMany(mappedBy = "paymentMethod")
    @JsonIgnore
    @JsonIgnoreProperties("paymentMethod")
    private List<Order> orders;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
