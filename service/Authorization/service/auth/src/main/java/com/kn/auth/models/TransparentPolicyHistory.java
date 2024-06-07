package com.kn.auth.models;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.constants.TableNameConstants;

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
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableNameConstants.TRANSPARENT_POLICY_HISTORY, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class TransparentPolicyHistory {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = TransparentPolicy.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "transparent_policy_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TransparentPolicy transparentPolicy;

    @ManyToOne(targetEntity = Authentication.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "authentication_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Authentication authentication;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
