package com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PricingService {

    private final CoinGeckoClient client;

    // simple in-memory cache: symbol -> price
    private final Map<String, Double> priceCache = new ConcurrentHashMap<>();

    // last time we successfully called CoinGecko
    private volatile long lastFetchTime = 0;

    // only call CoinGecko once every 60 seconds, otherwise serve from cache
    private static final long CACHE_DURATION_MS = 60_000;

    public PricingService(CoinGeckoClient client) {
        this.client = client;
    }

    public Map<String, Double> getLivePrices(List<String> symbols) {

        long now = System.currentTimeMillis();
        boolean cacheExpired = (now - lastFetchTime) > CACHE_DURATION_MS;

        // if cache is fresh and we already have all requested symbols, just use it
        boolean allCached = symbols.stream().allMatch(priceCache::containsKey);

        if (!cacheExpired && allCached) {
            return extractRequested(symbols);
        }

        try {
            String ids = symbols.stream()
                    .map(CoinGeckoCoin::toGeckoId)
                    .collect(Collectors.joining(","));

            Map<String, Map<String, Object>> response =
                    client.fetchPrices(ids);

            Map<String, Double> freshPrices = response.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> ((Number) e.getValue().get("usd")).doubleValue()
                    ));

            // update cache using symbol keys (not gecko ids)
            for (String symbol : symbols) {
                String geckoId = CoinGeckoCoin.toGeckoId(symbol);
                if (freshPrices.containsKey(geckoId)) {
                    priceCache.put(symbol, freshPrices.get(geckoId));
                }
            }

            lastFetchTime = now;

            return extractRequested(symbols);

        } catch (Exception e) {
            // CoinGecko failed (rate limit, network issue, etc.)
            // fall back to whatever we have cached, even if stale
            System.err.println("CoinGecko fetch failed, using cached/fallback prices: " + e.getMessage());
            return extractRequested(symbols);
        }
    }

    private Map<String, Double> extractRequested(List<String> symbols) {
        Map<String, Double> result = new HashMap<>();
        for (String symbol : symbols) {
            // if we have never fetched this symbol successfully, default to 0.0
            // (dashboard will show avgCost-based values instead of live price)
            result.put(symbol, priceCache.getOrDefault(symbol, 0.0));
        }
        return result;
    }
}