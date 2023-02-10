package com.babakjan.moneybag.dto.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest {

    private Long id;

    @Size(min = 1, max = 20)
    private String name;

    @Size(min = 1, max = 20)
    private String icon;
}