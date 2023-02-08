package com.babakjan.moneybag.dto.account;

import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    public AccountDto(Account account) {
        id = account.getId();
        name = account.getName();
        currency = account.getCurrency();
        balance = account.getBalance();
        color = account.getColor();
        icon = account.getIcon();
        includeInStatistic = account.getIncludeInStatistic();
        recordIds = account.getRecords()
                .stream().map(Record::getId)
                .collect(Collectors.toList());
    }
}
