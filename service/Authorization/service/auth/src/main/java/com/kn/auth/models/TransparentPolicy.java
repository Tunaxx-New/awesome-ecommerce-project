package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.constants.TableNameConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = TableNameConstants.TRANSPARENT_POLICY, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class TransparentPolicy {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private com.kn.auth.enums.TransparentPolicy name;
    private String type;
    @Builder.Default
    private Integer value = 1;

    @ManyToMany(mappedBy = "authenticationTransparentPolicies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<Authentication> authentications;

    @ManyToMany(mappedBy = "buyerTransparentPolicies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<OrderItem> buyerOrders;

    @ManyToMany(mappedBy = "sellerTransparentPolicies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<OrderItem> sellerOrders;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
