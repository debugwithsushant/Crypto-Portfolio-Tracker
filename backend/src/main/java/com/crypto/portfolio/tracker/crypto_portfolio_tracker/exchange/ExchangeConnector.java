package com.crypto.portfolio.tracker.crypto_portfolio_tracker.exchange;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ApiKey;
import java.util.*;

public interface ExchangeConnector {
    Map<String, Object> getBalance(String apiKey, String secretKey) throws Exception;
    List<Map<String, Object>> fetchTransactions(ApiKey apiKey) throws Exception;
    boolean validateKeys(ApiKey apiKey) throws Exception;
}
