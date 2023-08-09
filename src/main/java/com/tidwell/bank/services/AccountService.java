package com.tidwell.bank.services;

import com.tidwell.bank.models.Account;
import com.tidwell.bank.models.MonetaryTransaction;
import com.tidwell.bank.repositories.AccountRepo;
import com.tidwell.bank.repositories.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final static Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    @Autowired
    public AccountService(AccountRepo accountRepo, TransactionRepo transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public List<Account> getAccounts() {
        return accountRepo.findAll();
    }

    public Optional<Account> getAccount(Long id) {
        return accountRepo.findById(id);
    }

    public List<Account> getAccountsForCustomer(Long customerId) {
        return accountRepo.findAccountsByCustomerId(customerId);
    }

    public Account create(Account account) {
        return accountRepo.save(account);
    }

    @Transactional
    public Account update(Long currentAccountId, Account newAccount) {
        Account account = accountRepo.findById(currentAccountId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Account with ID %1$d does not exist in the database.", currentAccountId)
                ));
        account.setCustomerId(newAccount.getCustomerId());
        account.setStatus(newAccount.getStatus());

        return accountRepo.save(account);
    }

    public void delete(Long id) {
        accountRepo.deleteById(id);
    }

    @Transactional
    public void transferFunds(MonetaryTransaction transaction) {

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }

        Account originAccount = verifyAccount(transaction.getOriginAccountId());
        Account destinationAccount = verifyAccount(transaction.getDestinationAccountId());

        if (transaction.getAmount().compareTo(originAccount.getBalance()) > 0) {
            throw new IllegalArgumentException("Transaction amount cannot exceed account balance.");
        }

        transactionRepo.save(transaction);

        logger.info(String.format("Validation passed, beginning transfer of %1$f from account %2$d to account %3$d. (tx id: %4$d)",
                transaction.getAmount(),
                transaction.getOriginAccountId(),
                transaction.getDestinationAccountId(),
                transaction.getId()));


        originAccount.setBalance(originAccount.getBalance().subtract(transaction.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.getAmount()));

        accountRepo.save(originAccount);
        accountRepo.save(destinationAccount);

    }

    private Account verifyAccount(Long accountId) {

        return accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Account with ID %1$d does not exist in the database.", accountId)
                ));
    }

    @Transactional
    public void depositFunds(MonetaryTransaction transaction) {

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }

        Account account = verifyAccount(transaction.getDestinationAccountId());

        transactionRepo.save(transaction);

        logger.info(String.format("Validation passed, beginning deposit of %1$f into account %2$d (tx id: %3$d)",
                transaction.getAmount(),
                account.getId(),
                transaction.getId()));

        account.setBalance(account.getBalance().add(transaction.getAmount()));
        accountRepo.save(account);
    }

    @Transactional
    public void withdrawFunds(MonetaryTransaction transaction) {

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }

        Account account = verifyAccount(transaction.getOriginAccountId());

        if (transaction.getAmount().compareTo(account.getBalance()) > 0) {
            throw new IllegalArgumentException(("Withdrawal cannot exceed account balance."));

        }

        transactionRepo.save(transaction);

        logger.info(String.format("Validation passed, beginning withdrawal of %1$f from account %2$d (tx id: %3$d)",
                transaction.getAmount(),
                account.getId(),
                transaction.getId()));

        account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        accountRepo.save(account);
    }
}
