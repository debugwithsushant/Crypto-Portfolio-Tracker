package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.BinanceBalanceRawDTO;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.BinanceTradeRawDTO;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.ExchangeRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.BinanceSyncService;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.BinanceTradeSyncService;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.HoldingCostUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/binance")
public class BinanceController {

    private final BinanceSyncService binanceSyncService;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;
    private final BinanceTradeSyncService binanceTradeSyncService;
    private final HoldingCostUpdateService holdingCostUpdateService;

    public BinanceController(
            BinanceSyncService binanceSyncService,
            UserRepository userRepository,
            ExchangeRepository exchangeRepository,
            BinanceTradeSyncService binanceTradeSyncService,
            HoldingCostUpdateService holdingCostUpdateService
    ) {
        this.binanceSyncService = binanceSyncService;
        this.userRepository = userRepository;
        this.exchangeRepository = exchangeRepository;
        this.binanceTradeSyncService = binanceTradeSyncService;
        this.holdingCostUpdateService = holdingCostUpdateService;
    }

    // 🔧 DEV / TEST ONLY
    @PostMapping("/mock-sync")
    public ResponseEntity<String> mockSync(Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Exchange binance = exchangeRepository
                .findByName("BINANCE")
                .orElseThrow();

        List<BinanceBalanceRawDTO> mock = List.of(
                create("BTC", "0.01", "0.00"),
                create("ETH", "0.5", "0.1"),
                create("DOGE", "0.0", "0.0")
        );

        binanceSyncService.syncBalances(user, binance, mock);

        return ResponseEntity.ok("Mock Binance sync completed");
    }

    private BinanceBalanceRawDTO create(String asset, String free, String locked) {
        BinanceBalanceRawDTO dto = new BinanceBalanceRawDTO();
        dto.setAsset(asset);
        dto.setFree(free);
        dto.setLocked(locked);
        return dto;
    }

    @PostMapping("/mock-trades-sync")
    public ResponseEntity<String> mockTradesSync(Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Exchange binance = exchangeRepository
                .findByName("BINANCE")
                .orElseThrow();

        List<BinanceTradeRawDTO> mockTrades = List.of(
                createTrade("BTC", "BUY", "0.01", "30000", "1", "2026-01-01T10:00"),
                createTrade("ETH", "BUY", "0.5", "2000", "0.5", "2026-01-02T11:00"),
                createTrade("ETH", "SELL", "0.1", "2200", "0.2", "2026-01-05T12:00")
        );

        binanceTradeSyncService.syncMockTrades(user, binance, mockTrades);

        return ResponseEntity.ok("Mock Binance trades sync completed");
    }

    private BinanceTradeRawDTO createTrade(
            String symbol, String side, String qty,
            String price, String fee, String time
    ) {
        BinanceTradeRawDTO dto = new BinanceTradeRawDTO();
        dto.setSymbol(symbol);
        dto.setSide(side);
        dto.setQty(qty);
        dto.setPrice(price);
        dto.setFee(fee);
        dto.setTime(time);
        return dto;
    }

    @PostMapping("/recalculate-cost")
    public ResponseEntity<String> recalculateCost(Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Exchange binance = exchangeRepository
                .findByName("BINANCE")
                .orElseThrow();

        holdingCostUpdateService.updateAvgCost(user, binance, "BTC");
        holdingCostUpdateService.updateAvgCost(user, binance, "ETH");

        return ResponseEntity.ok("Average cost recalculated");
    }

}
