package com.techbank.cqrs.core.infra;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.query.BaseQuery;
import com.techbank.cqrs.core.query.QueryHandlerMethod;

import java.util.List;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler);

    <U extends BaseEntity> List<U> send(BaseQuery query);
}
