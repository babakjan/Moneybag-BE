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

    /**
     * Get all records. Role ADMIN is required.
     * @return list of records
     */
    public List<Record> getAll() {
        authenticationService.ifNotAdminThrowAccessDenied();
        return recordRepository.findAll();
    }

    /**
     * Get all records. Support filtering, sorting and pagination.
     * @param pageable pagination specification (page, size, sort,...)
     * @param userId user id
     * @param specification filter parameters
     * @return page of filtered records
     * @throws UserNotFoundException Authenticated user doesn't exist.
     */
    public Page<Record> getAllFilter(Specification<Record> specification, Pageable pageable, Long userId)
            throws UserNotFoundException {
        if (userId == null) {
            authenticationService.ifNotAdminThrowAccessDenied();
        } else {
            authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(userId);
        }
        return recordRepository.findAll(specification, pageable);
    }

    /**
     * Get record by id. Role ADMIN can access all records, role USER only theirs.
     * @param id record id
     * @return record of specified id
     * @throws RecordNotFoundException Record of specified id doesn't exist.
     * @throws UserNotFoundException Authenticated user doesn't exist.
     */
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

    /**
     * Create new record. Role ADMIN can create records to all accounts, role USER only to their accounts.
     * @param request record data
     * @return created record
     * @throws CategoryNotFoundException Category from request doesn't exist.
     * @throws AccountNotFoundException Account from request doesn't exist.
     * @throws UserNotFoundException Authenticated user of user from request doesn't exist.
     */
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

    /**
     * Update record by id. Role ADMIN can update all records, role USER can update only records from their accounts.
     * @param id record id
     * @param request record data (only fields, which will be changed)
     * @return updated account
     * @throws RecordNotFoundException Record of specified id doesn't exist.
     * @throws CategoryNotFoundException Category from request doesn't exist.
     * @throws AccountNotFoundException Account from request doesn't exist.
     * @throws UserNotFoundException Authenticated user of user from request doesn't exist.
     */
    @Transactional
    public Record update(Long id, UpdateRecordRequest request)
            throws RecordNotFoundException, CategoryNotFoundException, AccountNotFoundException, UserNotFoundException {
        //find data
        Optional<Record> optionalRecord = recordRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            throw new RecordNotFoundException(id);
        }

        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(
                optionalRecord.get().getAccount().getUser().getId());

        Account newAccount = null;
        if (null != request.getAccountId()) {
            newAccount = accountService.getById(request.getAccountId());
        }
        Category newCategory = null;
        if (null != request.getCategoryId()) {
            newCategory = categoryService.getById(request.getCategoryId());
        }

        //data changes
        if (null != newAccount) {
            optionalRecord.get().setAccount(newAccount);
        }
        if (null != newCategory) {
            optionalRecord.get().setCategory(newCategory);
        }
        if (null != request.getLabel() && !"".equalsIgnoreCase(request.getLabel())) {
            optionalRecord.get().setLabel(request.getLabel());
        }
        if (null != request.getNote() && !"".equalsIgnoreCase(request.getNote())) {
            optionalRecord.get().setNote(request.getNote());
        }
        if (null != request.getAmount()) {
            optionalRecord.get().getAccount().setBalance(
                    optionalRecord.get().getAccount().getBalance() - optionalRecord.get().getAmount()
            );
            optionalRecord.get().setAmount(request.getAmount());
            optionalRecord.get().getAccount().setBalance(
                    optionalRecord.get().getAccount().getBalance() + optionalRecord.get().getAmount()
            );
        }
        if (null != request.getDate()) {
            optionalRecord.get().setDate(request.getDate());
        }

        return recordRepository.save(optionalRecord.get());
    }

    /**
     * Delete record by id. Role ADMIN can delete all records, role USER only records from their accounts.
     * @param id record id
     * @throws RecordNotFoundException Record of specified id doesn't exist.
     * @throws UserNotFoundException Authenticated user doesn't exist.
     */
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

    /**
     * Map list of records to list of data transfer objects
     * @param records list of records
     * @return list of record dtos
     */
    public static List<RecordDto> recordsToDto(List<Record> records) {
        return records.stream().map(Record::dto).toList();
    }
}
