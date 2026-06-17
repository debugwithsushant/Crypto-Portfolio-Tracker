package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/key")
public class ApiKeyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping("/connect")
    public String connectExchange(@RequestBody ApiKey request) {
        try {
            User user = userRepository.findById(request.getUser().getId()).orElseThrow(() -> new Exception("User not found"));
            Exchange exchange = exchangeRepository.findById(request.getExchange().getId()).orElseThrow(() -> new Exception("Exchange Not found"));

            ApiKey savedKey = apiKeyService.connectExchange(request.getUser(), request.getExchange(), request.getApiKey(), request.getApiSecret(), request.getLabel());

            return savedKey.getLabel() + " Exchange connect successfully";
        } catch (Exception e) {
            return "Exchange connect failed: " + e.getMessage();
        }
    }
}
