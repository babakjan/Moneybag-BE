package com.babakjan.moneybag.DTO;

import com.babakjan.moneybag.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryDTO {
    private String name;

    private String icon;

    public Category toCategory() {
        return Category.builder()
                .name(name)
                .icon(icon)
                .records(new ArrayList<>())
                .build();
    }
}
