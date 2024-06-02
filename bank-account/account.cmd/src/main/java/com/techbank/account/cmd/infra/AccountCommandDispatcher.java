package com.techbank.account.cmd.infra;

import com.techbank.cqrs.core.command.BaseCommand;
import com.techbank.cqrs.core.command.CommandHandlerMethod;
import com.techbank.cqrs.core.infra.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        // the list of the handler handling that particular command type
        var handlers = routes.get(command.getClass());

        if (handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No command handler has been registered.");
        }

        if (handlers.size() > 1) {
            throw new RuntimeException("Cannot send the command to more than one handler");
        }

        handlers.get(0).handle(command);
    }

}
