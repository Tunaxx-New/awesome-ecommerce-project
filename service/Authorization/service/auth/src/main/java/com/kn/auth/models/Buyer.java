package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.annotations.SafeToUpdate;
import com.kn.auth.annotations.SensetiveData;
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
@Table(name = TableNameConstants.BUYERS, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class Buyer extends SafeUpdate<Buyer> {
    @SensetiveData
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @SensetiveData
    @OneToOne(targetEntity = Authentication.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "authentication_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Authentication authentication;

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "buyer_badge", joinColumns = @JoinColumn(name = "buyer_id"), inverseJoinColumns = @JoinColumn(name = "badge_id"))
    @OnDelete(action=OnDeleteAction.CASCADE)
    private List<Badge> badges = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    @JsonIgnore
    private List<Order> orders;

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
    private LocalDateTime registeredTime;
}
