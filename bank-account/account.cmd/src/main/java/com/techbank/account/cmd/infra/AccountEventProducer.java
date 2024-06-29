package com.techbank.account.cmd.infra;

import com.techbank.cqrs.core.event.BaseEvent;
import com.techbank.cqrs.core.producer.EventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountEventProducer implements EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AccountEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(String topic, BaseEvent event) {
        log.info("Producing a message to topic {}", topic);
        kafkaTemplate.send(topic, event);
    }
}
