package com.babakjan.moneybag.repository;

import com.babakjan.moneybag.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends
        JpaRepository<Record, Long>,
        PagingAndSortingRepository<Record, Long>,
        JpaSpecificationExecutor<Record> {


        @Query("select sum(r.amount) " +
                "from Record r " +
                "where (r.account.user.id = :userId or :userId is null) and (r.account.includeInStatistic) and (r.amount > 0)"
        )
        Double getTotalIncomes(@Param("userId") Long userId);

        @Query("select sum(r.amount) " +
                "from Record r " +
                "where (r.account.user.id = :userId or :userId is null) and (r.account.includeInStatistic) and (r.amount < 0)"
        )
        Double getTotalExpenses(@Param("userId") Long userId);
}
