package com.techbank.cqrs.core.command;

@FunctionalInterface // interface that contains only one abstract method
public interface CommandHandlerMethod<T extends BaseCommand> { // so it can handle all command types

    void handle(T command);

}
