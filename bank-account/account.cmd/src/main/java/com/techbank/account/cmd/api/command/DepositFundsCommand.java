package com.techbank.account.cmd.api.command;

import com.techbank.cqrs.core.command.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepositFundsCommand extends BaseCommand {

    private double amount;
}
