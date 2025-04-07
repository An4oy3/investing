package com.example.investing.data.entity;

import com.example.investing.data.enums.CurrencyType;

import java.math.BigDecimal;

public class Currency {
    private Long id;
    private String name;
    private String ticker;
    private BigDecimal currentPrice;
    private BigDecimal marketVal;
    private CurrencyType currencyType;
}
