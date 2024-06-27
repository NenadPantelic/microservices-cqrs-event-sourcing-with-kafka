package com.techbank.account.cmd.infra;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.event.BaseEvent;
import com.techbank.cqrs.core.event.EventModel;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.techbank.cqrs.core.exception.ConcurrencyException;
import com.techbank.cqrs.core.infra.EventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoreRepository;

    public AccountEventStore(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        log.info("Saving events: aggregateId = {}, events = {}, expectedVersion = {}", aggregateId, events, expectedVersion);
        var eventStream = eventStoreRepository.findByAggregateIdentifier();
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }

        var version = expectedVersion;
        for (var event : events) {
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timestamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if (persistedEvent != null) {
                // produce event to Kafka
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        log.info("Get events by aggregateId = {}", aggregateId);
        var eventStream = eventStoreRepository.findByAggregateIdentifier();
        if (eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account ID provided.");
        }

        return eventStream.stream()
                .map(EventModel::getEventData)
                .collect(Collectors.toList());
    }
}
