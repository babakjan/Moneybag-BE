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
public class RecordDto {

    private Long id;

    private Long amount; //TODO change to double

    @Size(min = 1, max = 20)
    private String label;

    @Size(max = 20)
    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    private Long accountId;

    private Long categoryId;
}
