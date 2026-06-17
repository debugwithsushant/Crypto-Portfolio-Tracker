package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinanceBalanceRawDTO {

    private String asset;
    private String free;
    private String locked;
}
