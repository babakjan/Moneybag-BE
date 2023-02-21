package com.babakjan.moneybag.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalAnalytic {

    @NotNull
    private Double incomes;

    @NotNull
    private Double expenses;

    @NotNull
    private Double cashFlow;

    @NotNull
    private Double balance;

    @NotNull
    private String currency;
}
