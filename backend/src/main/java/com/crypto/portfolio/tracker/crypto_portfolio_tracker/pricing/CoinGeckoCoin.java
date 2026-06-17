package com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing;

import java.util.Map;

public enum CoinGeckoCoin {

    BTC("bitcoin"),
    ETH("ethereum");

    private final String geckoId;

    CoinGeckoCoin(String geckoId) {
        this.geckoId = geckoId;
    }

    public String getGeckoId() {
        return geckoId;
    }

    public static String toGeckoId(String symbol) {
        return CoinGeckoCoin.valueOf(symbol).getGeckoId();
    }
}
