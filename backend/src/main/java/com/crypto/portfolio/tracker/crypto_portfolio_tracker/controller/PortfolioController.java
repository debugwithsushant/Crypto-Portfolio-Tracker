package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.PortfolioHoldingResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.TradeHistoryResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.TradeRequest;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.PortfolioService;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.TradesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository;
    private final TradesService tradesService;

    public PortfolioController(
            PortfolioService portfolioService,
            UserRepository userRepository,
            TradesService tradesService
    ) {
        this.portfolioService = portfolioService;
        this.userRepository = userRepository;
        this.tradesService = tradesService;
    }

    @GetMapping("/holdings")
    public ResponseEntity<List<PortfolioHoldingResponse>> getHoldings(
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return ResponseEntity.ok(
                portfolioService.getUserHoldings(user)
        );
    }

    @GetMapping("/trades")
    public ResponseEntity<List<TradeHistoryResponse>> getTrades(
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return ResponseEntity.ok(
                tradesService.getUserTrades(user)
        );
    }

    @PostMapping("/trades")
    public ResponseEntity<TradeHistoryResponse> addTrade(
            @RequestBody TradeRequest request,
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        TradeHistoryResponse response = tradesService.addTrade(
                user,
                request.getAssetSymbol(),
                request.getSide(),
                request.getQuantity(),
                request.getPrice(),
                request.getFee(),
                request.getExchange() != null ? request.getExchange().getId() : null,
                request.getExecutedAt()
        );

        return ResponseEntity.ok(response);
    }
}