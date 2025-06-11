package com.example.investing.data.entity;

import com.example.investing.data.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ticker;

    private BigDecimal currentPrice;

    private BigDecimal marketVal;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;
}
