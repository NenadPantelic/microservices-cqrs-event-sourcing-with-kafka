package com.techbank.account.query.api.query;

import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.account.query.domain.BankAccountRepository;
import com.techbank.cqrs.core.domain.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountQueryHandler implements QueryHandler {

    private final BankAccountRepository accountRepository;

    public AccountQueryHandler(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        log.info("Handling FindAllAccountsQuery...");
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        log.info("Handling FindAccountByIdQuery...");
        Optional<BankAccount> bankAccountOptional = accountRepository.findById(query.getId());
        return bankAccountOptional.<List<BaseEntity>>map(List::of).orElse(null);
    }

    @Override
    public List<BaseEntity> handle(FindAccountsWithBalanceQuery query) {
        log.info("Handling FindAccountsWithBalanceQuery...");
        return query.getEqualityType() == EqualityType.GREATER_THAN ?
                accountRepository.findByBalanceGreaterThan(query.getBalance()) :
                accountRepository.findByBalanceLessThan(query.getBalance());
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        log.info("Handling FindAccountByHolderQuery...");
        Optional<BankAccount> bankAccountOptional = accountRepository.findByAccountHolder(query.getAccountHolder());
        return bankAccountOptional.<List<BaseEntity>>map(List::of).orElse(null);
    }
}
