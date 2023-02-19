package com.babakjan.moneybag.entity;

import lombok.Data;

@Data
public class RecordAnalyticByCategory {

    private Double amount;

    private Long categoryId;

    private String categoryName;

    private Long numberOfRecords;

    public RecordAnalyticByCategory(Double amount, Long categoryId, String categoryName, Long numberOfRecords) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.numberOfRecords = numberOfRecords;
    }
}
