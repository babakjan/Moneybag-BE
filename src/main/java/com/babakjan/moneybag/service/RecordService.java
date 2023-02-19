package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.record.CreateRecordRequest;
import com.babakjan.moneybag.dto.record.RecordDto;
import com.babakjan.moneybag.dto.record.UpdateRecordRequest;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.error.exception.AccountNotFoundException;
import com.babakjan.moneybag.error.exception.CategoryNotFoundException;
import com.babakjan.moneybag.error.exception.RecordNotFoundException;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.RecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    private final AccountService accountService;

    private final CategoryService categoryService;

    private final AuthenticationService authenticationService;

    //get all
    public List<Record> getAll() {
        authenticationService.ifNotAdminThrowAccessDenied();
        return recordRepository.findAll();
    }

    //get all filter
    public Page<Record> getAllFilter(Specification<Record> specification, Pageable pageable, Long userId)
            throws UserNotFoundException {
        if (userId == null) {
            authenticationService.ifNotAdminThrowAccessDenied();
        } else {
            authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(userId);
        }
        return recordRepository.findAll(specification, pageable);
    }

    //get by id
    public Record getById(Long id) throws RecordNotFoundException, UserNotFoundException {
        if (id == null) {
            throw new RecordNotFoundException("Record id can't be null.");
        }
        Optional<Record> optionalRecord = recordRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            throw new RecordNotFoundException(id);
        }
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(
                optionalRecord.get().getAccount().getUser().getId());

        return optionalRecord.get();
    }

    //create
    public Record save(CreateRecordRequest request)
            throws AccountNotFoundException, CategoryNotFoundException, UserNotFoundException {
        Account account = accountService.getById(request.getAccountId());

        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(account.getUser().getId());

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.getById(request.getCategoryId());
        }

        Record record = recordRepository.save(new Record(request, account, category));
        account.setBalance(account.getBalance() + record.getAmount()); //update balance
        accountService.save(account);
        categoryService.save(category);

        return record;
    }

    //update
    @Transactional
    public Record update(Long id, UpdateRecordRequest recordDto)
            throws RecordNotFoundException, CategoryNotFoundException, AccountNotFoundException, UserNotFoundException {
        //find data
        Optional<Record> optionalRecord = recordRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            throw new RecordNotFoundException(id);
        }

        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(
                optionalRecord.get().getAccount().getUser().getId());

        Account newAccount = null;
        if (null != recordDto.getAccountId()) {
            newAccount = accountService.getById(recordDto.getAccountId());
        }
        Category newCategory = null;
        if (null != recordDto.getCategoryId()) {
            newCategory = categoryService.getById(recordDto.getCategoryId());
        }

        //data changes
        if (null != newAccount) {
            optionalRecord.get().setAccount(newAccount);
        }
        if (null != newCategory) {
            optionalRecord.get().setCategory(newCategory);
        }
        if (null != recordDto.getLabel() && !"".equalsIgnoreCase(recordDto.getLabel())) {
            optionalRecord.get().setLabel(recordDto.getLabel());
        }
        if (null != recordDto.getNote() && !"".equalsIgnoreCase(recordDto.getNote())) {
            optionalRecord.get().setNote(recordDto.getNote());
        }
        if (null != recordDto.getAmount()) {
            optionalRecord.get().getAccount().setBalance(
                    optionalRecord.get().getAccount().getBalance() - optionalRecord.get().getAmount()
            );
            optionalRecord.get().setAmount(recordDto.getAmount());
            optionalRecord.get().getAccount().setBalance(
                    optionalRecord.get().getAccount().getBalance() + optionalRecord.get().getAmount()
            );
        }
        if (null != recordDto.getDate()) {
            optionalRecord.get().setDate(recordDto.getDate());
        }

        return recordRepository.save(optionalRecord.get());
    }

    //delete by id
    public void deleteById(Long id) throws RecordNotFoundException, UserNotFoundException {
        if (id == null) {
            throw new RecordNotFoundException("Record id can't be null.");
        }
        Optional<Record> optionalRecord = recordRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            throw new RecordNotFoundException(id);
        }

        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(
                optionalRecord.get().getAccount().getUser().getId()
        );

        optionalRecord.get().getAccount().setBalance(
                optionalRecord.get().getAccount().getBalance() - optionalRecord.get().getAmount()
        );
        recordRepository.deleteById(id);
    }

    public static List<RecordDto> recordsToDto(List<Record> records) {
        return records.stream().map(Record::dto).toList();
    }
}
