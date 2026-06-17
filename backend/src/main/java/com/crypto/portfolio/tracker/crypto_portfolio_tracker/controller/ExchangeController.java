package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.ConnectedExchangeResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.ExchangeConnectRequest;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final UserRepository userRepository;

    public ExchangeController(ExchangeService exchangeService, UserRepository userRepository) {
        this.exchangeService = exchangeService;
        this.userRepository = userRepository;
    }

    @PostMapping("/connect")
    public ResponseEntity<String> connectExchange(
            @RequestBody ExchangeConnectRequest request,
            Authentication authentication
    ) throws Exception {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        exchangeService.connectExchange(user, request);

        return ResponseEntity.ok("Exchange connected successfully");
    }

    @GetMapping("/connected")
    public ResponseEntity<List<ConnectedExchangeResponse>> getConnectedExchanges(
            Authentication authentication
    ) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                exchangeService.getConnectedExchanges(user)
        );
    }

    @DeleteMapping("/disconnect/{exchangeName}")
    public ResponseEntity<String> disconnectExchange(
            @PathVariable String exchangeName,
            Authentication authentication
    ) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        exchangeService.disconnectExchange(user, exchangeName.toUpperCase());

        return ResponseEntity.ok("Exchange disconnected successfully");
    }

}
