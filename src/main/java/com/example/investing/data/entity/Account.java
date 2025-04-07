package com.example.investing.data.entity;

import com.example.investing.data.enums.AccountOwner;
import com.example.investing.data.enums.AccountType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Account {
    private Long id;
    private AccountOwner owner;
    private AccountType accountType;
    private BigDecimal totalBalance;
    private List<Transaction> transactions;
    private Map<Currency, BigDecimal> balances;
}
