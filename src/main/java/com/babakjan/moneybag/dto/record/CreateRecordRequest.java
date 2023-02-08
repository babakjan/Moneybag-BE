package com.babakjan.moneybag.dto.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecordRequest {

    private Long amount;

    private String label;

    private String note;

    private Date date;

    private Long accountId;

    private Long categoryId;
}
