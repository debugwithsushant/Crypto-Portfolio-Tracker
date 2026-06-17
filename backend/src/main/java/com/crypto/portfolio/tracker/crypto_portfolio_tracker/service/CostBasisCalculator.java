package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Trade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class CostBasisCalculator {

    public BigDecimal calculateAverageCost(List<Trade> trades) {

        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Trade trade : trades) {
            if (trade.getSide() == Trade.Side.BUY) {

                BigDecimal qty = trade.getQuantity();
                BigDecimal cost = trade.getPrice().multiply(qty);

                if (trade.getFee() != null) {
                    cost = cost.add(trade.getFee());
                }

                totalQty = totalQty.add(qty);
                totalCost = totalCost.add(cost);
            }
        }

        if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalCost.divide(totalQty, 8, RoundingMode.HALF_UP);
    }
}
