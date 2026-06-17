package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.BinanceBalanceRawDTO;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BinanceBalanceMapper {

    public BigDecimal calculateTotalQuantity(BinanceBalanceRawDTO dto) {
        BigDecimal free = new BigDecimal(dto.getFree());
        BigDecimal locked = new BigDecimal(dto.getLocked());
        return free.add(locked);
    }

    public boolean isZeroBalance(BigDecimal quantity) {
        return quantity.compareTo(BigDecimal.ZERO) == 0;
    }

    public Holding toNewHolding(
            User user,
            Exchange exchange,
            String assetSymbol,
            BigDecimal quantity
    ) {
        return Holding.builder()
                .user(user)
                .exchange(exchange)
                .assetSymbol(assetSymbol)
                .quantity(quantity)
                .walletType(Holding.WalletType.EXCHANGE)
                .avgCost(null)
                .build();
    }
}
