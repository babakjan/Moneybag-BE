package com.babakjan.moneybag.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;

    private String name;

    private String currency;

    private Long balance;

    private String color;

    private String icon;

    private Boolean includeInStatistic;

    private List<Long> recordIds;
}
