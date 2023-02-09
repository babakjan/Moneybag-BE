package com.babakjan.moneybag.dto.account;

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
public class CreateAccountRequest {

    @Size(min = 1, max = 20)
    private String name;

    @Size(min = 1, max = 20)
    private String currency;

    @NotNull
    private Long balance;

    @Pattern(
            regexp = "^#[1-9abcdefABCDEF]{6}|^#[1-9abcdefABCDEF]{3}",
            message = "Color must be in the HEX format (#XXX or #XXXXXX)"
    )
    private String color = "#388E3C";

    @Size(max = 20)
    private String icon = "mdi-cash";

    private Boolean includeInStatistic = true;

    @NotNull
    private Long userId;
}
