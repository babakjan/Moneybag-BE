package com.babakjan.moneybag.entity;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Nonnull
    private String name;

    @Nonnull
    private String currency;

    @Nonnull
    private Long balance;

    @Nonnull
    private String color = "#6290ff";

    @Nonnull
    private String icon = "mdi-cash";

    private Boolean includeInStatistic = true;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>(); //one account belongs to many records

    public Account(CreateAccountRequest request) {
        name = request.getName();
        currency = request.getCurrency();
        balance = request.getBalance();
        color = request.getColor();
        icon = request.getIcon();
        includeInStatistic = request.getIncludeInStatistic();
        records = new ArrayList<>();
    }

    public AccountDto dto() {
        return AccountDto.builder()
                .id(id)
                .name(name)
                .currency(currency)
                .balance(balance)
                .color(color)
                .icon(icon)
                .includeInStatistic(includeInStatistic)
                .recordIds(records
                        .stream().map(Record::getId)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public void addRecord(Record record) {
        for (Record r : records) {
            if (Objects.equals(r.getId(), record.getId())) {
                return;
            }
        }
        records.add(record);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", color='" + color + '\'' +
                ", icon='" + icon + '\'' +
                ", includeInStatistic=" + includeInStatistic +
                ", records=");
        for (Record record : records) {
            result.append(" ").append(record.getId());
        }
        result.append("}");
        return result.toString();
    }
}
