package com.techbank.cqrs.core.domain;

import com.techbank.cqrs.core.event.BaseEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Getter
@Slf4j
public abstract class AggregateRoot {

    protected String id;
    private int version = -1;
    private final List<BaseEvent> changes = new ArrayList<>();

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getUncommittedChanges() {
        return changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, boolean newEvent) {
        try {
            Method method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            log.warn("The apply method was not found in the aggregate for {}", event.getClass().getName());
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Error applying event to aggregate", e);
        } finally {
            if (newEvent) {
                changes.add(event);
            }
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        // changes already applied, we do not want to add them again
        events.forEach(event -> applyChange(event, false));
    }
}
