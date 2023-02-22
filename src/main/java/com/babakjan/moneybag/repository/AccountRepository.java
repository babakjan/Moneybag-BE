package com.babakjan.moneybag.repository;

import com.babakjan.moneybag.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select sum(a.balance) " +
            "from Account a " +
            "where (a.user.id = :userId or :userId is null) " +
            "and a.includeInStatistic"
    )
    Double getTotalBalance(@Param("userId") Long userId);
}
