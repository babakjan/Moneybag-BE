package com.babakjan.moneybag.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "records")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue
    private Long id;

    private Long amount;

    private String label;

    private String note;

    @Nonnull
    private Date date;

    @ManyToOne
    @Nonnull
    private Account account; //many records belong to one account

    @ManyToOne
    private Category category; //many records belong to one category
}
