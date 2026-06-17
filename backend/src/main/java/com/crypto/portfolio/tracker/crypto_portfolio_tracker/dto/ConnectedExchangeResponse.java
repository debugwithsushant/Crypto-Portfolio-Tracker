package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConnectedExchangeResponse {

    private Integer exchangeId;
    private String exchangeName;
    private String baseUrl;
}
