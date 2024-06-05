package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.constants.TableNameConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Table(name = TableNameConstants.CATEGORY, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class Category {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parentCategory;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<Product> products;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
