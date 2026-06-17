package com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceSnapshotScheduler {

    private final PriceSnapshotService snapshotService;

    public PriceSnapshotScheduler(PriceSnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    // Every 10 minutes (for demo)
    @Scheduled(cron = "0 */10 * * * *")
    public void capturePrices() {

        snapshotService.captureSnapshot("BTC");
        snapshotService.captureSnapshot("ETH");
    }
}
