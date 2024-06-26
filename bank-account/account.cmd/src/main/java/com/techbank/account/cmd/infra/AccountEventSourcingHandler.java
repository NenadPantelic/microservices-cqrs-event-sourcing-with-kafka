package com.techbank.account.cmd.infra;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.event.BaseEvent;
import com.techbank.cqrs.core.handler.EventSourcingHandler;
import com.techbank.cqrs.core.infra.EventStore;
import com.techbank.cqrs.core.producer.EventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Slf4j
@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;
    private final EventProducer eventProducer;


    public AccountEventSourcingHandler(EventStore eventStore, EventProducer eventProducer) {
        this.eventStore = eventStore;
        this.eventProducer = eventProducer;
    }

    @Override
    public void save(AggregateRoot aggregateRoot) {
        log.info("Saving {}", aggregateRoot);
        eventStore.saveEvents(aggregateRoot.getId(), aggregateRoot.getUncommittedChanges(), aggregateRoot.getVersion());
        aggregateRoot.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        log.info("Get account aggregate by id {}", id);
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var latestVersion = events.stream().map(BaseEvent::getVersion).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }

        return aggregate;
    }

    @Override
    public void republishEvents() {
        log.info("Republishing events...");
        var aggregateIds = eventStore.getAggregateIds();
        for (var aggregateId : aggregateIds) {
            // we do not want to republish events for inactive aggregates
            var aggregate = getById(aggregateId);
            if (aggregate == null || !aggregate.isActive()) {
                continue;
            }

            var events = eventStore.getEvents(aggregateId);
            for (var event : events) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }

    }
}
