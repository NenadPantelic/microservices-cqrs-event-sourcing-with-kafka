package com.techbank.account.cmd.api.command;

import com.techbank.cqrs.core.command.BaseCommand;

public class CloseAccountCommand extends BaseCommand {

    public CloseAccountCommand(String id) {
        super(id);
    }
}
