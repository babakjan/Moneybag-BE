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
public class UpdateAccountRequest {
    private Long id;

    @Size(max = 20)
    private String name;

    @Size(max = 20)
    private String currency;

    private Long balance;

    @Pattern(
            regexp = "^#[1-9abcdefABCDEF]{6}|^#[1-9abcdefABCDEF]{3}",
            message = "Color must be in the HEX format (#XXX or #XXXXXX)"
    )
    private String color;

    @Size(max = 20)
    private String icon;

    private Boolean includeInStatistic;

    private Long userId;
}
