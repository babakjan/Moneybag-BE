package com.babakjan.moneybag.repository;

import com.babakjan.moneybag.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RecordRepository extends
        JpaRepository<Record, Long>,
        PagingAndSortingRepository<Record, Long>,
        JpaSpecificationExecutor<Record> {


        @Query("select coalesce(sum(r.amount), 0) " +
                "from Record r " +
                "where (r.account.user.id = :userId or :userId is null) " +
                "and (r.account.includeInStatistic) and (r.amount > 0) " +
                "and (:dateGe is null or r.date >= :dateGe)" +
                "and (:dateLt is null or r.date < :dateLt)"
        )
        Double getTotalIncomes(@Param("userId") Long userId, @Param("dateGe") Date dateGe, @Param("dateLt") Date dateLt);

        @Query("select coalesce(sum(r.amount), 0) " +
                "from Record r " +
                "where (r.account.user.id = :userId or :userId is null) " +
                "and (r.account.includeInStatistic) " +
                "and (r.amount < 0) " +
                "and (:dateGe is null or r.date >= :dateGe)" +
                "and (:dateLt is null or r.date < :dateLt)"
        )
        Double getTotalExpenses(@Param("userId") Long userId, @Param("dateGe") Date dateGe, @Param("dateLt") Date dateLt);
}
