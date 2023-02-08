package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.record.CreateRecordRequest;
import com.babakjan.moneybag.dto.record.RecordDto;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.exception.CategoryNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecordDto> getAll() {
        return RecordService.recordsToDto(recordService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecordDto getById(@PathVariable Long id) throws RecordNotFoundException {
        return recordService.getById(id).dto();
    }

    //delete by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long id) throws RecordNotFoundException {
        recordService.deleteById(id);
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecordDto create(@RequestBody CreateRecordRequest request) throws CategoryNotFoundException, AccountNotFoundException {
        return recordService.save(request).dto();
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecordDto update(@PathVariable Long id, @RequestBody RecordDto request)
            throws RecordNotFoundException, CategoryNotFoundException, AccountNotFoundException {
        return recordService.update(id, request).dto();
    }
}
