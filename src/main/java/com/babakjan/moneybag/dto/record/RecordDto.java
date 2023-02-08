package com.babakjan.moneybag.dto.record;

import com.babakjan.moneybag.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {

    private Long id;

    private Long amount;

    private String label;

    private String note;

    private Date date;

    private Long accountId;

    private Long categoryId;

    public RecordDto(Record record) {
        id = record.getId();
        amount = record.getAmount();
        label = record.getLabel();
        note = record.getNote();
        date = record.getDate();
        accountId = record.getAccount().getId();
        if (record.getCategory() != null) {
            categoryId = record.getCategory().getId();
        }
    }
}
