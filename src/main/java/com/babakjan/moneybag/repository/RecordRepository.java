package com.babakjan.moneybag.repository;

import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.entity.TimeSeriesEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecordRepository extends
        JpaRepository<Record, Long>,
        PagingAndSortingRepository<Record, Long>,
        JpaSpecificationExecutor<Record> {

        /**
         * Get total incomes from accounts (included in statistics) by user id. If user id is null, by all users.
         * @param userId user id
         * @param dateGe dateGe dateGe date greater or equal than (inclusive)
         * @param dateLt dateLt date lower than (exclusive)
         * @return total incomes
         */
        @Query("select coalesce(sum(r.amount), 0) " +
                "from Record r " +
                "where (r.account.user.id = :userId or :userId is null) " +
                "and (r.account.includeInStatistic) and (r.amount > 0) " +
                "and (:dateGe is null or r.date >= :dateGe)" +
                "and (:dateLt is null or r.date < :dateLt)"
        )
        Double getTotalIncomes(@Param("userId") Long userId, @Param("dateGe") Date dateGe, @Param("dateLt") Date dateLt);

        /**
         * Get total expenses from accounts (included in statistics) by user id. If user id is null, by all users.
         * @param userId user id
         * @param dateGe dateGe dateGe date greater or equal than (inclusive)
         * @param dateLt dateLt date lower than (exclusive)
         * @return total expenses
         */
        @Query("select coalesce(sum(r.amount), 0) " +
                "from Record r " +
                "where (r.account.user.id = :userId or :userId is null) " +
                "and (r.account.includeInStatistic) " +
                "and (r.amount < 0) " +
                "and (:dateGe is null or r.date >= :dateGe)" +
                "and (:dateLt is null or r.date < :dateLt)"
        )
        Double getTotalExpenses(@Param("userId") Long userId, @Param("dateGe") Date dateGe, @Param("dateLt") Date dateLt);

        /**
         * Get time series of balance evolution by user. Include only accounts, which are included in statistics.
         * @param userId user id
         * @param dateGe dateGe dateGe date greater or equal than (inclusive)
         * @param dateLt dateLt date lower than (exclusive)
         * @return time series of total balance evolution
         */
        @Query("select new com.babakjan.moneybag.entity.TimeSeriesEntry(sum(r.amount), min(r.date))" +
                "from Record r " +
                "where (r.account.user.id = :userId or :userId is null) " +
                "and r.account.includeInStatistic " +
                "and (:dateGe is null or r.date >= :dateGe) " +
                "and (:dateLt is null or r.date < :dateLt) " +
                "group by function('date_format', r.date, '%Y %m %d') " +
                "order by max(r.date)"
        )
        List<TimeSeriesEntry> getSpendingEvolution(
                @Param("userId") Long userId, @Param("dateGe") Date dateGe, @Param("dateLt") Date dateLt);
}
