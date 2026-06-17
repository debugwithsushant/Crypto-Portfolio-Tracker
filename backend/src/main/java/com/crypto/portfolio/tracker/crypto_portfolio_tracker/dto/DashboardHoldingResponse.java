package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DashboardHoldingResponse {

    private Integer id;
    private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal avgCost;
    private BigDecimal currentPrice;
    private BigDecimal totalValue;
    private BigDecimal profitLoss;
}