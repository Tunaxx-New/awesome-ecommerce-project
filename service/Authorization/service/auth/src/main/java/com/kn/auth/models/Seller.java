package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import jakarta.persistence.OneToMany;
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
@Table(name = TableNameConstants.SELLERS, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class Seller extends SafeUpdate<Seller> {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    @SensetiveData
    private Integer id;

    @OneToMany(mappedBy = "seller")
    @JsonIgnoreProperties("seller")
    private List<Product> products;

    @OneToOne(targetEntity = Authentication.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "authentication_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @SensetiveData
    @JsonIgnore
    private Authentication authentication;

    @Builder.Default
    private Integer commissionPercentage = 5;

    @SafeToUpdate
    private String name;

    @SafeToUpdate
    private String surname;

    @SafeToUpdate
    private String bio;

    @SafeToUpdate
    private LocalDate birthday;

    @CreationTimestamp
    @SensetiveData
    private LocalDateTime registeredTime;
}
