package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TradeRequest {
    private String side;
    private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal fee;
    private LocalDateTime executedAt;
    private ExchangeRef exchange;

    @Getter
    @Setter
    public static class ExchangeRef {
        private Integer id;
    }
}