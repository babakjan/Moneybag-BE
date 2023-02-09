package com.babakjan.moneybag.dto.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @Size(min = 1, max = 20)
    private String name;

    @Size(min = 1, max = 20)
    private String icon;

    private List<Long> recordIds;
}
