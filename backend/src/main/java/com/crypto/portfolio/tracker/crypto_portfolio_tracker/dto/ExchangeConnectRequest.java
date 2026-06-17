package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ExchangeConnectRequest {

    private String exchangeName;
    private String apiKey;
    private String apiSecret;
    private String label;
}
