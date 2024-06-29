package com.techbank.account.query.infra.consumer;

import com.techbank.account.common.event.AccountClosedEvent;
import com.techbank.account.common.event.AccountOpenedEvent;
import com.techbank.account.common.event.FundsDepositedEvent;
import com.techbank.account.common.event.FundsWithdrawnEvent;
import com.techbank.account.query.infra.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountEventConsumer implements EventConsumer {

    private final EventHandler eventHandler;

    public AccountEventConsumer(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountOpenedEvent event, Acknowledgment ack) {
        log.info("Consuming AccountOpenedEvent...");
        eventHandler.on(event);
        ack.acknowledge(); // commits an offset to Kafka (message read, move the offset)
    }

    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsDepositedEvent event, Acknowledgment ack) {
        log.info("Consuming FundsDepositedEvent...");
        eventHandler.on(event);
        ack.acknowledge(); // commits an offset to Kafka (message read, move the offset)
    }

    @KafkaListener(topics = "FundsWithdrawnEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsWithdrawnEvent event, Acknowledgment ack) {
        log.info("Consuming FundsWithdrawnEvent...");
        eventHandler.on(event);
        ack.acknowledge(); // commits an offset to Kafka (message read, move the offset)
    }

    @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountClosedEvent event, Acknowledgment ack) {
        log.info("Consuming AccountClosedEvent...");
        eventHandler.on(event);
        ack.acknowledge(); // commits an offset to Kafka (message read, move the offset)
    }
}
