package com.techbank.cqrs.core.exception;

public class AggregateNotFoundException extends RuntimeException {

    public AggregateNotFoundException() {
    }

    public AggregateNotFoundException(String message) {
        super(message);
    }

    public AggregateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
