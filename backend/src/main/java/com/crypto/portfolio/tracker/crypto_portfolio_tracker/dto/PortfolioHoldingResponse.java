package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PortfolioHoldingResponse {

    private String assetSymbol;
    private BigDecimal quantity;
    private String walletType;
    private String exchangeName;
}
