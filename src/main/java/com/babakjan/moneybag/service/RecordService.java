package com.babakjan.moneybag.service;

import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public List<Record> getAll() {
        return recordRepository.findAll();
    }

    public Optional<Record> getById(Long id) {
        return recordRepository.findById(id);
    }
}
