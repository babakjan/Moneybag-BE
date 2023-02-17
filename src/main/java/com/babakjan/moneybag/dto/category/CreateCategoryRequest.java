package com.babakjan.moneybag.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @Size(max = 20)
    private String icon = "mid-shape-outline";

    @Pattern(
            regexp = "^#[0-9abcdefABCDEF]{6}|^#[0-9abcdefABCDEF]{3}",
            message = "Color must be in the HEX format (#XXX or #XXXXXX)"
    )
    private String color;
}
