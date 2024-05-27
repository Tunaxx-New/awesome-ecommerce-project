package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.annotations.SafeToUpdate;
import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.wrappers.SafeUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = TableNameConstants.PRODUCT, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class Product extends SafeUpdate<Product> {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = Seller.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "seller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Seller seller;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems;

    @SafeToUpdate
    private String name;
    @SafeToUpdate
    private String description;
    @SafeToUpdate
    private String characteristics;
    @SafeToUpdate
    private BigDecimal price;
    @SafeToUpdate
    private String image_filename;

    @SafeToUpdate
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "product_tag", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @OnDelete(action=OnDeleteAction.CASCADE)
    private List<Tag> tags = new ArrayList<>();

    @SafeToUpdate
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @OnDelete(action=OnDeleteAction.CASCADE)
    private List<Category> categories = new ArrayList<>();

    @SafeToUpdate
    private LocalDate expirationDate;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
