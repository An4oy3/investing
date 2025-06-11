package com.example.investing.service;

import com.example.investing.data.entity.Account;
import com.example.investing.data.entity.Currency;
import com.example.investing.data.entity.Transaction;
import com.example.investing.data.enums.AccountOwner;
import com.example.investing.data.enums.AccountType;
import com.example.investing.data.enums.CurrencyType;
import com.example.investing.data.enums.TransactionType;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CSVParserService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final Map<String, Currency> currencies = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Parse the CSV file from classpath resource "data.csv" and store
     * the parsed transactions in memory.
     */
    public void parseInitialData() {
        try (InputStream in = new ClassPathResource("/templates/Investing.csv").getInputStream()) {
            parse(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV", e);
        }
    }

    /**
     * Parse a CSV input stream into transactions.
     *
     * @param input CSV data stream
     * @return list of parsed transactions
     */
    public List<Transaction> parse(InputStream input) {
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(input, StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] line;
            boolean first = true;
            while ((line = reader.readNext()) != null) {
                if (first) { // skip header row
                    first = false;
                    continue;
                }
                Transaction transaction = parseLine(line);
                transactions.add(transaction);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error while parsing CSV", e);
        }
        return new ArrayList<>(transactions);
    }

    /**
     * Add a single CSV row to the list of transactions.
     *
     * @param csvRow row data separated by commas
     */
    public void addRow(String csvRow) {
        try (CSVReader reader = new CSVReader(new StringReader(csvRow))) {
            String[] data = reader.readNext();
            if (data != null) {
                Transaction transaction = parseLine(data);
                transactions.add(transaction);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error while adding CSV row", e);
        }
    }

    /**
     * Get already parsed transactions.
     */
    public List<Transaction> getInitialData() {
        return new ArrayList<>(transactions);
    }

    private Transaction parseLine(String[] data) {
        int idx = 0;
        LocalDate date = LocalDate.parse(data[idx++], DATE_FORMATTER);
        Currency currency = getOrCreateCurrency(data[idx++]);
        Account destinationAccount = getOrCreateAccount(data[idx++]);
        TransactionType type = TransactionType.valueOf(data[idx++].trim().toUpperCase());
        BigDecimal exchangeRate = new BigDecimal(data[idx++]);
        BigDecimal amount = new BigDecimal(data[idx++]);
        idx++; // skip amount in rubles
        BigDecimal tokenAmount = new BigDecimal(data[idx++]);
        BigDecimal destinationBalance = new BigDecimal(data[idx++]);
        Account sourceAccount = getOrCreateAccount(data[idx++]);
        BigDecimal sourceBalance = new BigDecimal(data[idx++]);
        BigDecimal bitcoinRate = new BigDecimal(data[idx]);

        return Transaction.builder()
                .date(date)
                .currency(currency)
                .destinationAccount(destinationAccount)
                .transactionType(type)
                .exchangeRate(exchangeRate)
                .amount(amount)
                .tokenAmount(tokenAmount)
                .destinationAccountBalance(destinationBalance)
                .sourceAccount(sourceAccount)
                .sourceAccountBalance(sourceBalance)
                .bitcoinRate(bitcoinRate)
                .build();
    }

    private Currency getOrCreateCurrency(String ticker) {
        return currencies.computeIfAbsent(ticker, this::createCurrency);
    }

    private Currency createCurrency(String ticker) {
        return Currency.builder()
                .name(ticker)
                .ticker(ticker)
                .currencyType(CurrencyType.CRYPTO)
                .build();
    }

    private Account getOrCreateAccount(String descriptor) {
        return accounts.computeIfAbsent(descriptor, this::createAccount);
    }

    /**
     * Create a new account from descriptor string.
     * Expected format: "OWNER TYPE" (e.g., "ALIAKSANDR SPOT").
     */
    private Account createAccount(String descriptor) {
        String[] parts = descriptor.trim().split("\\s+");
        AccountOwner owner = parts.length > 0 ? AccountOwner.valueOf(parts[0].toUpperCase()) : AccountOwner.ALIAKSANDR;
        AccountType type = parts.length > 1 ? AccountType.valueOf(parts[1].toUpperCase()) : AccountType.MAIN;
        return Account.builder()
                .owner(owner)
                .accountType(type)
                .totalBalance(BigDecimal.ZERO)
                .build();
    }
}