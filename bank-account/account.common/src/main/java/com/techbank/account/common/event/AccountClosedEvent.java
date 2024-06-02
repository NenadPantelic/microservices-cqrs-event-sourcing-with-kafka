package com.techbank.account.common.event;

import com.techbank.cqrs.core.event.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class AccountClosedEvent extends BaseEvent {
}
