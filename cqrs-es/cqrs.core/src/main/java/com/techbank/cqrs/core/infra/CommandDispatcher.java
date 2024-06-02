package com.techbank.cqrs.core.infra;

import com.techbank.cqrs.core.command.BaseCommand;
import com.techbank.cqrs.core.command.CommandHandlerMethod;

public interface CommandDispatcher {

    // to register command methods
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);

    // sends/dispatches commands
    void send(BaseCommand command);
}
