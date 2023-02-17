package com.babakjan.moneybag.entity;

import com.babakjan.moneybag.dto.category.CategoryDto;
import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String icon;

    private String color = "#6290ff";

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>(); // one category belongs to many records

    public Category(CreateCategoryRequest request) {
        name = request.getName();
        icon = request.getIcon();
        records = new ArrayList<>();
    }

    public CategoryDto dto() {
        return CategoryDto.builder()
                .id(id)
                .name(name)
                .icon(icon)
                .color(color)
                .build();
    }

    public void addRecord(Record record) {
        for (Record r: records) {
            if (Objects.equals(r.getId(), record.getId())) {
                return;
            }
        }
        records.add(record);
    }
}
