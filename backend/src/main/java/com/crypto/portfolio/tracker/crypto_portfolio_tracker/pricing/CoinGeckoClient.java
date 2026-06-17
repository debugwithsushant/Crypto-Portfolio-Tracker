package com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CoinGeckoClient {

    private static final String BASE_URL =
            "https://api.coingecko.com/api/v3/simple/price";

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Map<String, Object>> fetchPrices(String ids) {

        String url = BASE_URL +
                "?ids=" + ids +
                "&vs_currencies=usd";

        return restTemplate.getForObject(url, Map.class);
    }
}
