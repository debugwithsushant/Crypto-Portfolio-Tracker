package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.HoldingRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HoldingCostUpdateService {

    private final TradeRepository tradeRepository;
    private final HoldingRepository holdingRepository;
    private final CostBasisCalculator calculator;

    public HoldingCostUpdateService(
            TradeRepository tradeRepository,
            HoldingRepository holdingRepository,
            CostBasisCalculator calculator
    ) {
        this.tradeRepository = tradeRepository;
        this.holdingRepository = holdingRepository;
        this.calculator = calculator;
    }

    public void updateAvgCost(
            User user,
            Exchange exchange,
            String assetSymbol
    ) {

        List<Trade> trades = tradeRepository.findByUserAndAssetSymbolAndExchange(
                user, assetSymbol, exchange
        );

        BigDecimal avgCost = calculator.calculateAverageCost(trades);

        Holding holding = holdingRepository
                .findByUserAndAssetSymbolAndExchangeAndWalletType(
                        user, assetSymbol, exchange, Holding.WalletType.EXCHANGE
                )
                .orElseThrow();

        holding.setAvgCost(avgCost);
        holdingRepository.save(holding);
    }
}
