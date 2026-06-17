package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.RiskAlertResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.RiskAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskAlertController {

    private final RiskAlertService riskAlertService;
    private final UserRepository userRepository;

    public RiskAlertController(
            RiskAlertService riskAlertService,
            UserRepository userRepository
    ) {
        this.riskAlertService = riskAlertService;
        this.userRepository = userRepository;
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<RiskAlertResponse>> getAlerts(
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return ResponseEntity.ok(
                riskAlertService.getUserAlerts(user)
        );
    }
}
