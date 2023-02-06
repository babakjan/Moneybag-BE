package com.babakjan.moneybag.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    private Long id;
    private String name;
    private String currency;
    private Long balance;
    private String color;
    private String icon;
    private Boolean includeInStatistic;
}
