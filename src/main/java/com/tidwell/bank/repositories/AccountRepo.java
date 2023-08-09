package com.tidwell.bank.repositories;

import com.tidwell.bank.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    List<Account> findAccountsByCustomerId(Long customerId);
}
