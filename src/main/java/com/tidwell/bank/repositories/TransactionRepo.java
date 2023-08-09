package com.tidwell.bank.repositories;

import com.tidwell.bank.models.MonetaryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<MonetaryTransaction, Long> {
}
