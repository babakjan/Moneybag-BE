package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/records")
    public List<Record> getAll() {
        return recordService.getAll();
    }
}
