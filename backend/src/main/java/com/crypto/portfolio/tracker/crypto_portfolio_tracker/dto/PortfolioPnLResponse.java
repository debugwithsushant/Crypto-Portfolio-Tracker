package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class PortfolioPnLResponse {

    private BigDecimal totalValue;
    private BigDecimal unrealizedPnL;
    private BigDecimal realizedPnL;
    private List<AssetPnL> assets;
}
