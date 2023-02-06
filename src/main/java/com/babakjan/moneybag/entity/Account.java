package com.babakjan.moneybag.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue
    private Long accountId;

    private String name;

    private String currency;

    private Long balance;

    private String color;

    private String icon;

    private Boolean includeInStatistic;

    @OneToMany
    private List<Record> records; //one account belongs to many records
}
