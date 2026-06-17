package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.AssetPnL;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.PortfolioPnLResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Holding;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing.PricingService;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioPnLService {

    private final HoldingRepository holdingRepository;
    private final PricingService pricingService;
    private final RealizedPnLService realizedPnLService;

    public PortfolioPnLService(
            HoldingRepository holdingRepository,
            PricingService pricingService,
            RealizedPnLService realizedPnLService
    ) {
        this.holdingRepository = holdingRepository;
        this.pricingService = pricingService;
        this.realizedPnLService = realizedPnLService;
    }

    public PortfolioPnLResponse calculatePnL(User user) {
        BigDecimal realizedPnL = realizedPnLService.calculate(user);

        List<Holding> holdings = holdingRepository.findByUser(user);

        List<String> symbols = holdings.stream()
                .map(Holding::getAssetSymbol)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Double> prices =
                pricingService.getLivePrices(symbols);

        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalUnrealizedPnL = BigDecimal.ZERO;

        List<AssetPnL> assetPnLs = new ArrayList<>();

        for (Holding h : holdings) {

            BigDecimal qty = h.getQuantity();
            BigDecimal avgCost = h.getAvgCost();

            Double livePrice = prices.get(h.getAssetSymbol());

            // if live price is unavailable or 0 (e.g. CoinGecko rate-limited
            // and we have no cached value yet), fall back to avgCost so the
            // dashboard still shows something sensible instead of crashing.
            BigDecimal currentPrice =
                    (livePrice != null && livePrice > 0)
                            ? BigDecimal.valueOf(livePrice)
                            : avgCost;

            BigDecimal currentValue = currentPrice.multiply(qty);
            BigDecimal unrealizedPnL =
                    currentPrice.subtract(avgCost).multiply(qty);

            totalValue = totalValue.add(currentValue);
            totalUnrealizedPnL = totalUnrealizedPnL.add(unrealizedPnL);

            assetPnLs.add(new AssetPnL(
                    h.getAssetSymbol(),
                    qty,
                    avgCost,
                    currentPrice,
                    currentValue,
                    unrealizedPnL
            ));
        }

        return new PortfolioPnLResponse(
                totalValue,
                totalUnrealizedPnL,
                realizedPnL,
                assetPnLs
        );
    }
}