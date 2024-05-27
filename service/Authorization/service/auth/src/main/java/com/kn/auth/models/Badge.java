package com.kn.auth.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kn.auth.constants.TableNameConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = TableNameConstants.BADGE, uniqueConstraints = {
        @UniqueConstraint(name = "uc_id", columnNames = { "id" })
})
public class Badge {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer id;

    private String title;
    private String description;
    private String imageSource;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "badge_tag", joinColumns = @JoinColumn(name = "badge_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @OnDelete(action=OnDeleteAction.CASCADE)
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany(mappedBy = "badges")
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Buyer> buyer;
    
    @CreationTimestamp
    private LocalDateTime createdTime;
}
