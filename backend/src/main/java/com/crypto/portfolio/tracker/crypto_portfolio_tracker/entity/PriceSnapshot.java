package com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "price_snapshots",
        indexes = {
                @Index(name = "idx_price_asset_time", columnList = "asset_symbol, captured_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "asset_symbol", nullable = false, length = 20)
    private String assetSymbol;

    @Column(name = "price_usd", nullable = false, precision = 19, scale = 8)
    private BigDecimal priceUsd;

    @Column(name = "market_cap", precision = 19, scale = 2)
    private BigDecimal marketCap;

    @Column(nullable = false, length = 50)
    private String source;

    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt;
}
