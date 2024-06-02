package com.techbank.cqrs.core.event;

import com.techbank.cqrs.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEvent extends Message {

    // can be used to replay the event store to recreate the steps in aggregate
    // can be used for the optimistic concurrency control
    private int version;

}
