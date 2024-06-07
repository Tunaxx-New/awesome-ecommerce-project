package com.kn.auth.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import jakarta.persistence.OneToMany;
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
@Table(name = TableNameConstants.AUTHENTICATION, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" }),
        @UniqueConstraint(name = "uc_email", columnNames = { "email" })
})
public class Authentication implements UserDetails {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(columnDefinition = "BOOL DEFAULT false")
    private Boolean isEmailConfirmed;

    @OneToOne(mappedBy = "authentication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Buyer buyer;

    @OneToOne(mappedBy = "authentication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Seller seller;

    @OneToOne(mappedBy = "authentication", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthenticationToken token;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authentication_role", joinColumns = @JoinColumn(name = "authentication_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authentication_transparent_policy", joinColumns = @JoinColumn(name = "authentication_id"), inverseJoinColumns = @JoinColumn(name = "authentication_transparent_policy_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TransparentPolicy> authenticationTransparentPolicies;

    @OneToMany(mappedBy = "authentication", fetch = FetchType.EAGER)
    private List<TransparentPolicyHistory> transparentPolicyHistories;

    @CreationTimestamp
    private LocalDateTime registeredTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}