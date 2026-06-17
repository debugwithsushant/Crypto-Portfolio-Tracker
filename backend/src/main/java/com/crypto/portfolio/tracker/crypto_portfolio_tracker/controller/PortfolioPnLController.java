package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.AssetPnL;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.DashboardHoldingResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.PortfolioPnLResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Holding;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.HoldingRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.PnLCsvExportService;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.PortfolioPnLService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioPnLController {

    private final PortfolioPnLService pnlService;
    private final UserRepository userRepository;
    private final PnLCsvExportService csvExportService;
    private final HoldingRepository holdingRepository;

    public PortfolioPnLController(
            PortfolioPnLService pnlService,
            UserRepository userRepository,
            PnLCsvExportService csvExportService,
            HoldingRepository holdingRepository
    ) {
        this.pnlService = pnlService;
        this.userRepository = userRepository;
        this.csvExportService = csvExportService;
        this.holdingRepository = holdingRepository;
    }

    @GetMapping("/pnl")
    public ResponseEntity<PortfolioPnLResponse> getPnL(
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return ResponseEntity.ok(
                pnlService.calculatePnL(user)
        );
    }

    // NEW: frontend dashboard expects an array with id, assetSymbol,
    // quantity, avgCost, currentPrice, totalValue, profitLoss
    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardHoldingResponse>> getDashboard(
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        PortfolioPnLResponse pnl = pnlService.calculatePnL(user);

        // map holding id by assetSymbol so we can attach DB id to each row
        Map<String, Integer> idsBySymbol = holdingRepository.findByUser(user)
                .stream()
                .collect(Collectors.toMap(
                        Holding::getAssetSymbol,
                        Holding::getId,
                        (existing, replacement) -> existing
                ));

        List<DashboardHoldingResponse> result = pnl.getAssets().stream()
                .map(a -> new DashboardHoldingResponse(
                        idsBySymbol.get(a.getAssetSymbol()),
                        a.getAssetSymbol(),
                        a.getQuantity(),
                        a.getAvgCost(),
                        a.getCurrentPrice(),
                        a.getCurrentValue(),
                        a.getUnrealizedPnL()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/pnl/export")
    public ResponseEntity<byte[]> exportPnL(Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        PortfolioPnLResponse pnl = pnlService.calculatePnL(user);

        String csv = csvExportService.generateCsv(pnl);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=portfolio.csv")
                .header("Content-Type", "text/csv")
                .body(csv.getBytes());
    }

    @PostMapping("/manual")
    public ResponseEntity<String> addHolding(
            @RequestBody com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.HoldingRequest request,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();

        Holding holding = Holding.builder()
                .user(user)
                .assetSymbol(request.getAssetSymbol())
                .quantity(request.getQuantity())
                .avgCost(request.getAvgCost())
                .walletType(Holding.WalletType.WALLET)
                .build();

        holdingRepository.save(holding);
        return ResponseEntity.ok("Holding added successfully");
    }

    @PutMapping("/manual/{id}")
    public ResponseEntity<String> updateHolding(
            @PathVariable Integer id,
            @RequestBody com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.HoldingRequest request,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();

        Holding holding = holdingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holding not found"));

        if (!holding.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to edit this holding");
        }

        holding.setAssetSymbol(request.getAssetSymbol());
        holding.setQuantity(request.getQuantity());
        holding.setAvgCost(request.getAvgCost());
        holdingRepository.save(holding);

        return ResponseEntity.ok("Holding updated successfully");
    }

    @DeleteMapping("/manual/{id}")
    public ResponseEntity<String> deleteHolding(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();

        Holding holding = holdingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holding not found"));

        if (!holding.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to delete this holding");
        }

        holdingRepository.delete(holding);
        return ResponseEntity.ok("Holding deleted successfully");
    }
}