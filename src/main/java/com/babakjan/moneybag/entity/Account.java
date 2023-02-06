package com.babakjan.moneybag.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private String color; //TODO add default color

    @Nonnull
    private String icon; //TODO add default icon

    private Boolean includeInStatistic = true;

    @OneToMany
    private List<Record> records = new ArrayList<>(); //one account belongs to many records
}
