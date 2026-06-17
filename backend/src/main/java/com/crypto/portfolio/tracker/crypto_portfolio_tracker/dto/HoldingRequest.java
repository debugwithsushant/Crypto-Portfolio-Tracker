package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HoldingRequest {
    private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal avgCost;
}