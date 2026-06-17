package com.crypto.portfolio.tracker.crypto_portfolio_tracker.pricing;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.PriceHistoryPoint;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.PriceSnapshot;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.PriceSnapshotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricalPriceService {

    private final PriceSnapshotRepository repository;

    public HistoricalPriceService(PriceSnapshotRepository repository) {
        this.repository = repository;
    }

    public List<PriceHistoryPoint> getHistory(
            String symbol,
            LocalDate from,
            LocalDate to
    ) {

        List<PriceSnapshot> snapshots;

        if (from != null && to != null) {
            snapshots = repository
                    .findByAssetSymbolAndCapturedAtBetweenOrderByCapturedAtAsc(
                            symbol,
                            from.atStartOfDay(),
                            to.atTime(23, 59, 59)
                    );
        } else {
            snapshots = repository
                    .findByAssetSymbolOrderByCapturedAtAsc(symbol);
        }

        return snapshots.stream()
                .map(s -> new PriceHistoryPoint(
                        s.getCapturedAt(),
                        s.getPriceUsd()
                ))
                .collect(Collectors.toList());
    }
}
