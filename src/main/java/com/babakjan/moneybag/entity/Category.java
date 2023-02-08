package com.babakjan.moneybag.entity;

import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String icon;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>(); // one category belongs to many records

    public Category(CreateCategoryRequest request) {
        name = request.getName();
        icon = request.getIcon();
        records = new ArrayList<>();
    }
}
