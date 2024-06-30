package com.techbank.account.query.api.controller;

import com.techbank.account.query.api.dto.AccountLookupResponse;
import com.techbank.account.query.api.dto.BalanceFilterAccountRequest;
import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.api.query.FindAccountByHolderQuery;
import com.techbank.account.query.api.query.FindAccountByIdQuery;
import com.techbank.account.query.api.query.FindAccountsWithBalanceQuery;
import com.techbank.account.query.api.query.FindAllAccountsQuery;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.infra.QueryDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/bank-accounts-lookup")
public class AccountLookupController {

    private final QueryDispatcher queryDispatcher;

    public AccountLookupController(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        log.info("Received a request to get all accounts.");
        try {
            List<BankAccount> bankAccounts = queryDispatcher.send(new FindAllAccountsQuery());
            if (bankAccounts == null || bankAccounts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .bankAccounts(bankAccounts)
                    .message(String.format("Successfully returned %d accounts", bankAccounts.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to retrieve all accounts...";
            log.error(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable("id") String id) {
        log.info("Received a request to get an account by id.");
        try {
            List<BankAccount> bankAccounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            if (bankAccounts == null || bankAccounts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .bankAccounts(bankAccounts)
                    .message("Successfully returned bank account")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = String.format("Failed to retrieve an account with id %s", id);
            log.error(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountHolder}/accounts")
    public ResponseEntity<AccountLookupResponse> getAccountsByHolder(@PathVariable("accountHolder") String accountHolder) {
        log.info("Received a request to get all accounts of the account holder.");
        try {
            List<BankAccount> bankAccounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));
            if (bankAccounts == null || bankAccounts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .bankAccounts(bankAccounts)
                    .message(String.format("Successfully returned %d accounts", bankAccounts.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = String.format("Failed to retrieve accounts of the holder %s", accountHolder);
            log.error(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/filer-by-balance")
    public ResponseEntity<AccountLookupResponse> getAccountsByBalanceFiltering(@RequestBody BalanceFilterAccountRequest balanceFilterAccountRequest) {
        log.info("Received a request to get all accounts filtered by balance.");
        try {
            EqualityType equalityType = EqualityType.valueOf(balanceFilterAccountRequest.getEqualityType());
            double balance = balanceFilterAccountRequest.getBalance();

            List<BankAccount> bankAccounts = queryDispatcher.send(
                    new FindAccountsWithBalanceQuery(equalityType, balance)
            );
            if (bankAccounts == null || bankAccounts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .bankAccounts(bankAccounts)
                    .message(String.format("Successfully returned %d accounts", bankAccounts.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to retrieve accounts by applying the balance filter.";
            log.error(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
