package com.techbank.cqrs.core.command;

import com.techbank.cqrs.core.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class BaseCommand extends Message {

    public BaseCommand(String id) {
        super(id);
    }
}
