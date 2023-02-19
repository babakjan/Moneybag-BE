package com.babakjan.moneybag.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAnalyticDto {
    private CategoryDto category;

    private Double amount;

    private Long numberOfRecords;
}
