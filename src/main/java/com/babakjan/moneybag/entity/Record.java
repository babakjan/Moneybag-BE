package com.babakjan.moneybag.entity;

import com.babakjan.moneybag.dto.record.CreateRecordRequest;
import com.babakjan.moneybag.dto.record.RecordDto;
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

    public Record(CreateRecordRequest request, Account account, Category category) {
        this.amount = request.getAmount();
        this.label = request.getLabel();
        this.note = request.getNote();
        this.date = request.getDate();
        if (category != null) {
            this.category = category;
            category.addRecord(this);
        }
        if (account != null) {
            account.addRecord(this);
            this.account = account;
        }
    }

    public RecordDto dto() {
        return RecordDto.builder()
                .id(id)
                .label(label)
                .note(note)
                .amount(amount)
                .date(date)
                .accountId(account.getId())
                .categoryId(category.getId())
                .build();
    }
}
