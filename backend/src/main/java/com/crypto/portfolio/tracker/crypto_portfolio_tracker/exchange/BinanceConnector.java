package com.crypto.portfolio.tracker.crypto_portfolio_tracker.exchange;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ApiKey;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BinanceConnector implements ExchangeConnector{

    private static final String BASE_URL = "https://api.binance.com";

    @Override
    public Map<String, Object> getBalance(String apiKey, String secretKey) throws Exception{
        Map<String, Object> balance = new HashMap<>();
        balance.put("BTC", 0.5);
        balance.put("LTC", 0.5);
        return balance;
    }

    @Override
    public List<Map<String, Object>> fetchTransactions(ApiKey apiKey) throws Exception{
        return new ArrayList<>();
    }

    @Override
    public boolean validateKeys(ApiKey apiKey) throws Exception{
        return true;
    }
}
