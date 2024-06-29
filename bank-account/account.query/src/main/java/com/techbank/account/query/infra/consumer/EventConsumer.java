package com.techbank.account.query.infra.consumer;

import com.techbank.account.common.event.AccountClosedEvent;
import com.techbank.account.common.event.AccountOpenedEvent;
import com.techbank.account.common.event.FundsDepositedEvent;
import com.techbank.account.common.event.FundsWithdrawnEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    void consume(@Payload AccountOpenedEvent event, Acknowledgment ack);

    void consume(@Payload FundsDepositedEvent event, Acknowledgment ack);

    void consume(@Payload FundsWithdrawnEvent event, Acknowledgment ack);

    void consume(@Payload AccountClosedEvent event, Acknowledgment ack);
}
