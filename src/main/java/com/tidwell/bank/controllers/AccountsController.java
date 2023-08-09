package com.tidwell.bank.controllers;

import com.tidwell.bank.models.Account;
import com.tidwell.bank.models.MonetaryTransaction;
import com.tidwell.bank.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/accounts")
public class AccountsController {

    private final static Logger logger = LoggerFactory.getLogger(AccountsController.class);
    private final AccountService accountService;

    @Autowired
    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        try {
            return new ResponseEntity<>(accountService.getAccounts(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error retrieving all accounts; ensure database stability/integrity.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("account/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccount(id);

        return account.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("{customerId}")
    public ResponseEntity<List<Account>> getAccountsForCustomer(@PathVariable Long customerId) {
        return new ResponseEntity<>(accountService.getAccountsForCustomer(customerId), HttpStatus.OK);
    }

    @PostMapping("/addAccount")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        try {
            return new ResponseEntity<>(accountService.create(account), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account newAccount) {
        try {
            return new ResponseEntity<>(accountService.update(id, newAccount), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<HttpStatus> transferFunds(@RequestBody MonetaryTransaction transaction) {
        try {
            accountService.transferFunds(transaction);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> depositFunds(@RequestBody MonetaryTransaction transaction) {
        try {
            accountService.depositFunds(transaction);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdrawFunds(@RequestBody MonetaryTransaction transaction) {
        try {
            accountService.withdrawFunds(transaction);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
