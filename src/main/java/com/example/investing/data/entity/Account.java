package com.example.investing.data.entity;

import com.example.investing.data.enums.AccountOwner;
import com.example.investing.data.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountOwner owner;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal totalBalance;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL)
    private List<Transaction> receivedTransactions;

    @ElementCollection
    @CollectionTable(name = "account_balances", joinColumns = @JoinColumn(name = "account_id"))
    @MapKeyJoinColumn(name = "currency_id")
    @Column(name = "balance")
    private Map<Currency, BigDecimal> balances;
}
