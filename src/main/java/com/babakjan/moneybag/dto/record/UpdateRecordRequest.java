package com.babakjan.moneybag.dto.record;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecordRequest {

    private Long id;

    private Double amount;

    @Size(min = 1, max = 40)
    private String label;

    @Size(max = 128)
    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    private Long accountId;

    private Long categoryId;
}