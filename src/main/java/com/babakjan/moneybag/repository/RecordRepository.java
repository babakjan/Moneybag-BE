package com.babakjan.moneybag.repository;

import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.entity.RecordAnalyticByCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends
        JpaRepository<Record, Long>,
        PagingAndSortingRepository<Record, Long>,
        JpaSpecificationExecutor<Record> {

    @Query("select new com.babakjan.moneybag.entity.RecordAnalyticByCategory(sum(r.amount), c.id, c.name, count(r)) " +
            "from Record r join Category c on r.category.id = c.id " +
            "where (r.account.user.id = :userId or :userId is null)" +
            "group by r.category.id"
    )
    List<RecordAnalyticByCategory> findRecordAnalyticByCategory(@Param("userId") Long userId);
}
