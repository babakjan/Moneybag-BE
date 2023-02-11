package com.babakjan.moneybag.entity;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @NotNull
    private User user; //many accounts belong to one user

    public Account(CreateAccountRequest request, User user) {
        this.name = request.getName();
        this.currency = request.getCurrency();
        this.balance = request.getBalance();
        this.color = request.getColor();
        this.icon = request.getIcon();
        this.includeInStatistic = request.getIncludeInStatistic();
        this.records = new ArrayList<>();
        this.user = user;
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
                .userId(user != null ? user.getId() : null)
                .recordIds(records.stream().map(Record::getId).toList())
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
                ", user={ id=" +(user != null ? user.getId() + ", email=" + user.getEmail() + " }" : "null }") +
                ", records=");
        for (Record record : records) {
            result.append(" ").append(record.getId());
        }
        result.append("}");
        return result.toString();
    }
}
