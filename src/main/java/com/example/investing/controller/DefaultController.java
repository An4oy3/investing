package com.example.investing.controller;

import com.example.investing.data.entity.Transaction;
import com.example.investing.service.CSVParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DefaultController {
    private final CSVParserService parserService;

    @GetMapping("/test")
    public ResponseEntity<List<Transaction>> getInitialData() {
        return ResponseEntity.ok(parserService.getInitialData());
    }

    @GetMapping("/test2")
    public String test() {
        parserService.parseInitialData();
        return "Hello World";
    }

}
