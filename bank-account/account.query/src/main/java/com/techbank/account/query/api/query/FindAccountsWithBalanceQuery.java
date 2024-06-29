package com.techbank.account.query.api.query;

import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.cqrs.core.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class FindAccountsWithBalanceQuery extends BaseQuery {

    private EqualityType equalityType;
    private double balance;
}
