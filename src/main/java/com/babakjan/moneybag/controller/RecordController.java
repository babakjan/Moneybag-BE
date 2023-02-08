package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.record.RecordDto;
import com.babakjan.moneybag.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/records")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordDto> getAll() {
        return recordService.getAll();
    }
}
