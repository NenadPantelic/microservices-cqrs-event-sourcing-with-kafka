package com.techbank.account.cmd.api.command;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.handler.EventSourcingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountCommandHandler implements CommandHandler {

    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    public AccountCommandHandler(EventSourcingHandler<AccountAggregate> eventSourcingHandler) {
        this.eventSourcingHandler = eventSourcingHandler;
    }

    @Override
    public void handle(OpenAccountCommand command) {
        log.info("Handle OpenAccountCommand");
        var aggregate = new AccountAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        log.info("Handle DepositFundsCommand");
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.depositFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        log.info("Handle WithdrawFundsCommand");
        var aggregate = eventSourcingHandler.getById(command.getId());
        if (command.getAmount() > aggregate.getBalance()) {
            throw new IllegalStateException("Withdrawal declined, insufficient funds");
        }
        aggregate.withdrawFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        log.info("Handle WithdrawFundsCommand");
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(RestoreReadDbCommand command) {
        log.info("Handle RestoreReadDbCommand");
        eventSourcingHandler.republishEvents();
    }
}
