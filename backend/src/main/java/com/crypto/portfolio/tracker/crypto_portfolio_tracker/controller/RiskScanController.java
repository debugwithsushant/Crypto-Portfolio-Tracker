package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.RiskScanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
public class RiskScanController {

    private final RiskScanService riskScanService;
    private final UserRepository userRepository;

    public RiskScanController(
            RiskScanService riskScanService,
            UserRepository userRepository
    ) {
        this.riskScanService = riskScanService;
        this.userRepository = userRepository;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> scan(Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        riskScanService.scanUserHoldings(user);

        return ResponseEntity.ok("Risk scan completed");
    }
}
