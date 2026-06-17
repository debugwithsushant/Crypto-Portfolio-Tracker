package com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Integer> {
    List<PriceSnapshot> findByAssetSymbolOrderByCapturedAtAsc(String assetSymbol);

    List<PriceSnapshot> findByAssetSymbolAndCapturedAtBetweenOrderByCapturedAtAsc(
            String assetSymbol,
            LocalDateTime from,
            LocalDateTime to
    );}
