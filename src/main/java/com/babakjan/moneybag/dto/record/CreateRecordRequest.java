package com.babakjan.moneybag.dto.record;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecordRequest {

    @NotNull
    private Double amount;

    @NotNull
    @Size(min = 1, max = 40)
    private String label;

    @NotNull
    @Size(max = 128)
    private String note;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    @NotNull
    private Long accountId;

    private Long categoryId;
}
