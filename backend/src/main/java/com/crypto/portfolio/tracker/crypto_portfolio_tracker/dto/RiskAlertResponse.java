package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RiskAlertResponse {

    private String assetSymbol;
    private String alertType;
    private String details;
    private LocalDateTime createdAt;
}
