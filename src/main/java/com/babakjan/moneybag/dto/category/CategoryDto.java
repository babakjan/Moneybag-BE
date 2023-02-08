package com.babakjan.moneybag.dto.category;

import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;

    private String icon;

    private List<Long> recordIds;

    public CategoryDto(Category category) {
        id = category.getId();
        name = category.getName();
        icon = category.getIcon();
        recordIds = category.getRecords()
                .stream().map(Record::getId)
                .collect(Collectors.toList());
    }
}
