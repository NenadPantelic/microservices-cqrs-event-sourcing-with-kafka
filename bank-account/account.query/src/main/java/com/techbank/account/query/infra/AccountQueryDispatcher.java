package com.techbank.account.query.infra;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.infra.QueryDispatcher;
import com.techbank.cqrs.core.query.BaseQuery;
import com.techbank.cqrs.core.query.QueryHandlerMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler) {
        log.info("Registering a handler for type {}", type);
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        var handlers = routes.get(query.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No query handler was registered!");
        }

        if (handlers.size() > 1) {
            throw new RuntimeException("Cannot send to more than 1 handler!");
        }

        return handlers.get(0).handle(query);
    }
}
