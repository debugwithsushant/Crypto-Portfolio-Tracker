package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinanceTradeRawDTO {

    private String symbol;
    private String side;
    private String qty;
    private String price;
    private String fee;
    private String time;
}
