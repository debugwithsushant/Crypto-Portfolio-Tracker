package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.HoldingRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RealizedPnLService {

    private final TradeRepository tradeRepository;
    private final HoldingRepository holdingRepository;

    public RealizedPnLService(
            TradeRepository tradeRepository,
            HoldingRepository holdingRepository
    ) {
        this.tradeRepository = tradeRepository;
        this.holdingRepository = holdingRepository;
    }

    public BigDecimal calculate(User user) {

        List<Trade> sellTrades =
                tradeRepository.findByUserAndSide(user, Trade.Side.SELL);

        BigDecimal totalRealizedPnL = BigDecimal.ZERO;

        for (Trade trade : sellTrades) {

            Holding holding = holdingRepository
                    .findByUserAndAssetSymbolAndExchange(
                            user,
                            trade.getAssetSymbol(),
                            trade.getExchange()
                    )
                    .orElse(null);

            if (holding == null) continue;

            BigDecimal avgCost = holding.getAvgCost();

            BigDecimal pnl =
                    trade.getPrice()
                            .subtract(avgCost)
                            .multiply(trade.getQuantity());

            if (trade.getFee() != null) {
                pnl = pnl.subtract(trade.getFee());
            }

            totalRealizedPnL = totalRealizedPnL.add(pnl);
        }

        return totalRealizedPnL;
    }
}
