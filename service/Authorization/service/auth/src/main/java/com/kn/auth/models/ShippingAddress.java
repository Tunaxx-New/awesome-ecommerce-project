package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.constants.TableNameConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = TableNameConstants.SHIPPING_ADRESSES, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class ShippingAddress {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    private String title;

    @Embedded
    private Address address;

    @Embedded
    private GeoLocation geoLocation;

    @OneToMany(mappedBy = "shippingAddress")
    @JsonIgnore
    private List<Cart> carts;

    @OneToMany(mappedBy = "shippingAddress")
    @JsonIgnore
    private List<Order> orders;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
