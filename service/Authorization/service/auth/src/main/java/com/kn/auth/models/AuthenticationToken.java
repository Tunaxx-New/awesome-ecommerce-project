package com.kn.auth.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.constants.TableNameConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableNameConstants.AUTHENTICATION_TOKEN, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" }),
        @UniqueConstraint(name = "uc_token", columnNames = { "token" })
})
public class AuthenticationToken {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @OneToOne(targetEntity = Authentication.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "authentication_id")
    @JsonIgnore
    private Authentication authentication;

    @Column(unique = true)
    private String token;

    private LocalDateTime expiryDate;

    @CreationTimestamp
    private LocalDateTime registeredTime;
}
