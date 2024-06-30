package com.techbank.account.query.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceFilterAccountRequest {
    private String equalityType;
    private double balance;
}
