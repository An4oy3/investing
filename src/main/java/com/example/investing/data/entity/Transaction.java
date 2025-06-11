package com.example.investing.data.entity;

import com.example.investing.data.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Currency used in the transaction (e.g., USD, EUR, BTC).
     */
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    /**
     * destination Account(Sender when TransactionType = SELL, Buyer when BUY)
     */
    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
    /**
     * Balance of the destination's account after the transaction (destination account balance).
     */
    private BigDecimal destinationAccountBalance;
    /**
     * The date when the transaction was created or executed.
     */
    private LocalDate date;
    /**
     * Type of transaction (e.g., SELL, BUY, TRANSFER, WITHDRAWAL).
     */
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    /**
     * Exchange rate with USD applied during the transaction (e.g., for currency conversion).
     */
    private BigDecimal exchangeRate;
    /**
     * The transaction amount in the USD.
     */
    private BigDecimal amount;
    /**
     * Amount represented in tokens.
     */
    private BigDecimal tokenAmount;
    /**
     * The account from which the funds were sent (source account).
     */
    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;
    /**
     * Balance of the source's account after the transaction (source account balance).
     */
    private BigDecimal sourceAccountBalance;
    /**
     * Bitcoin exchange rate at the time of the transaction (if relevant).
     */
    private BigDecimal bitcoinRate;

}
