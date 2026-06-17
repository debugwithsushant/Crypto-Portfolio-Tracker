package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TradeHistoryResponse {

    private Integer id;
    private String assetSymbol;
    private String side;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal fee;
    private String exchangeName;
    private LocalDateTime executedAt;
}