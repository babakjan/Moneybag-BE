package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.record.RecordDto;
import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    //get all
    public List<RecordDto> getAll() {
        return recordRepository.findAll()
                .stream().map(RecordDto::new)
                .collect(Collectors.toList());
    }

    //get by id
    public RecordDto getById(Long id) throws RecordNotFoundException {
        Optional<Record> optionalRecord = recordRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            throw new RecordNotFoundException(id);
        }
        return new RecordDto(optionalRecord.get());
    }
}
