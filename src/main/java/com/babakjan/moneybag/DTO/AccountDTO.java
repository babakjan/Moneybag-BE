package com.babakjan.moneybag.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private Long id;

    private String name;

    private String currency;

    private Long balance;

    private String color;

    private String icon;

    private Boolean includeInStatistic;

    private List<Long> recordIds;
}
