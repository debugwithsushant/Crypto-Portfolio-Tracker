package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ScamToken;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.ScamTokenIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/scam")
public class ScamAdminController {

    private final ScamTokenIngestionService ingestionService;

    public ScamAdminController(ScamTokenIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    // DEV ONLY
    @PostMapping("/mock-ingest")
    public ResponseEntity<String> ingestMockData() {

        ingestionService.ingestMockScamToken(
                "0xSCAM123",
                "ETH",
                ScamToken.RiskLevel.HIGH,
                "CryptoScamDB"
        );

        ingestionService.ingestMockScamToken(
                "0xFAKE456",
                "BSC",
                ScamToken.RiskLevel.MEDIUM,
                "CommunityReports"
        );

        return ResponseEntity.ok("Mock scam tokens ingested");
    }
}
