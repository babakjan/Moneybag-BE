package com.babakjan.moneybag.DTO;

import com.babakjan.moneybag.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountDTO {
    private String name;

    private String currency;

    private Long balance;

    private String color;

    private String icon;

    private Boolean includeInStatistic;

    public Account toAccount() {
        return Account.builder()
                .name(name)
                .currency(currency)
                .balance(balance)
                .color(color)
                .icon(icon)
                .includeInStatistic(includeInStatistic)
                .build();
    }
}
