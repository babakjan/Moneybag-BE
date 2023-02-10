package com.babakjan.moneybag.repository;

import com.babakjan.moneybag.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query(value = "SELECT record FROM Record record WHERE record.account.id IN" +
            "(SELECT account.id FROM Account account WHERE account.user.id = :userId)"
    )
    List<Record> filteredRecords(@Param("userId") Long userId);
}
