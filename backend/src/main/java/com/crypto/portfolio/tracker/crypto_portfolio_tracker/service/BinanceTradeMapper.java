package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.BinanceTradeRawDTO;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class BinanceTradeMapper {

    public Trade toTrade(
            BinanceTradeRawDTO dto,
            User user,
            Exchange exchange
    ) {
        return Trade.builder()
                .user(user)
                .exchange(exchange)
                .assetSymbol(dto.getSymbol())
                .side(Trade.Side.valueOf(dto.getSide()))
                .quantity(new BigDecimal(dto.getQty()))
                .price(new BigDecimal(dto.getPrice()))
                .fee(dto.getFee() != null ? new BigDecimal(dto.getFee()) : BigDecimal.ZERO)
                .executedAt(LocalDateTime.parse(dto.getTime()))
                .build();
    }
}
