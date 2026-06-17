package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.exchange.BinanceConnector;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class HoldingService {

    @Autowired
    private HoldingRepository holdingRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private BinanceConnector binanceConnector;

    public void fetchAndSaveHolding(User user) throws Exception {
        List<ApiKey> keys = apiKeyRepository.findAll();
        for (ApiKey key : keys) {
            String apiKey = EncryptionUtil.decrypt(key.getApiKey());
            String secretKey = EncryptionUtil.decrypt(key.getApiSecret());

            Map<String, Object> balances = binanceConnector.getBalance(apiKey, secretKey);

            for (Map.Entry<String, Object> entry : balances.entrySet()) {
                String symbol = entry.getKey();
                Double quantity = (Double) entry.getValue();

                // findByUserAndAssetSymbol returns Optional, not a single Holding cast
                List<Holding> existing = holdingRepository.findByUser(user);
                Holding holding = existing.stream()
                        .filter(h -> h.getAssetSymbol().equals(symbol))
                        .findFirst()
                        .orElse(null);

                if (holding == null) {
                    holding = new Holding();
                    holding.setUser(user);
                    holding.setAssetSymbol(symbol);
                    holding.setAvgCost(BigDecimal.valueOf(0.0));
                    holding.setWalletType(Holding.WalletType.EXCHANGE);
                    holding.setExchange(key.getExchange());
                }
                holding.setQuantity(BigDecimal.valueOf(quantity));
                holdingRepository.save(holding);
            }
        }
    }

    public Holding addOrEditHolding(Holding holding) {
        return holdingRepository.save(holding);
    }
}