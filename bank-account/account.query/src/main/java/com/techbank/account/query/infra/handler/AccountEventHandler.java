package com.techbank.account.query.infra.handler;

import com.techbank.account.common.event.AccountClosedEvent;
import com.techbank.account.common.event.AccountOpenedEvent;
import com.techbank.account.common.event.FundsDepositedEvent;
import com.techbank.account.common.event.FundsWithdrawnEvent;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.account.query.domain.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AccountEventHandler implements EventHandler {

    private final BankAccountRepository bankAccountRepository;

    public AccountEventHandler(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void on(AccountOpenedEvent event) {
        log.info("Handling AccountOpenedEvent...");
        BankAccount bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .accountType(event.getAccountType())
                .creationDate(event.getCreationDate())
                .balance(event.getOpeningBalance())
                .build();

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        log.info("Handling FundsDepositedEvent...");
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(event.getId());
        if (bankAccountOptional.isEmpty()) {
            return;
        }

        BankAccount bankAccount = bankAccountOptional.get();
        var currentBalance = bankAccount.getBalance();
        var latestBalance = currentBalance + event.getAmount();
        bankAccount.setBalance(latestBalance);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        log.info("Handling FundsWithdrawnEvent...");
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(event.getId());
        if (bankAccountOptional.isEmpty()) {
            return;
        }

        BankAccount bankAccount = bankAccountOptional.get();
        var currentBalance = bankAccount.getBalance();
        var latestBalance = currentBalance - event.getAmount();
        bankAccount.setBalance(latestBalance);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent event) {
        log.info("Handling AccountClosedEvent...");
        bankAccountRepository.deleteById(event.getId());
    }
}
