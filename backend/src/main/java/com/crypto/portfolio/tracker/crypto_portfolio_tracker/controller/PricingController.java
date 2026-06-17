package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.PriceHistoryPoint;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing.HistoricalPriceService;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing.PriceSnapshotService;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing.PricingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PricingController {

    private final PricingService pricingService;
    private final HistoricalPriceService historicalPriceService;
    private final PriceSnapshotService priceSnapshotService;

    public PricingController(
            PricingService pricingService,
            HistoricalPriceService historicalPriceService,
            PriceSnapshotService priceSnapshotService
    ) {
        this.pricingService = pricingService;
        this.historicalPriceService = historicalPriceService;
        this.priceSnapshotService = priceSnapshotService;
    }

    @GetMapping("/prices")
    public ResponseEntity<Map<String, Double>> getPrices(
            @RequestParam List<String> symbols
    ) {
        return ResponseEntity.ok(
                pricingService.getLivePrices(symbols)
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<PriceHistoryPoint>> getHistory(
            @RequestParam String symbol,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(
                historicalPriceService.getHistory(symbol, from, to)
        );
    }

    // NEW: matches frontend GET /api/prices/history/{symbol}
    @GetMapping("/prices/history/{symbol}")
    public ResponseEntity<List<PriceHistoryPoint>> getPriceHistoryBySymbol(
            @PathVariable String symbol,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(
                historicalPriceService.getHistory(symbol, from, to)
        );
    }

    // NEW: matches frontend POST /api/prices/refresh
    @PostMapping("/prices/refresh")
    public ResponseEntity<String> refreshPrices() {
        priceSnapshotService.captureSnapshot("BTC");
        priceSnapshotService.captureSnapshot("ETH");
        return ResponseEntity.ok("Prices refreshed successfully");
    }
}