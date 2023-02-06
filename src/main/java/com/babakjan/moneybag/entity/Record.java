package com.babakjan.moneybag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue
    private Long recordId;

    private Long amount;

    private String label;

    private String note;

    private Date created;

    @ManyToOne
    private Account account; //many records belong to one account

    @ManyToOne
    private Category category; //many records belong to one category
}
