package com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.PriceSnapshot;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.PriceSnapshotRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PriceSnapshotService {

    private final CoinGeckoClient client;
    private final PriceSnapshotRepository repository;

    public PriceSnapshotService(
            CoinGeckoClient client,
            PriceSnapshotRepository repository
    ) {
        this.client = client;
        this.repository = repository;
    }

    public void captureSnapshot(String symbol) {

        String geckoId = CoinGeckoCoin.toGeckoId(symbol);

        Map<String, Map<String, Object>> response =
                client.fetchPrices(geckoId);

        Double price = ((Number) response
                .get(geckoId)
                .get("usd")).doubleValue();

        PriceSnapshot snapshot = PriceSnapshot.builder()
                .assetSymbol(symbol)
                .priceUsd(BigDecimal.valueOf(price))
                .marketCap(null) // later
                .source("COINGECKO")
                .capturedAt(LocalDateTime.now())
                .build();

        repository.save(snapshot);
    }
}
