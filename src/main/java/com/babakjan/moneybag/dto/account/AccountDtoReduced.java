package com.babakjan.moneybag.dto.account;

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
public class AccountDtoReduced {
    private Long id;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String currency;

    @Pattern(
            regexp = "^#[0-9abcdefABCDEF]{6}|^#[0-9abcdefABCDEF]{3}",
            message = "Color must be in the HEX format (#XXX or #XXXXXX)"
    )
    private String color;

    @Size(max = 40)
    private String icon;
}