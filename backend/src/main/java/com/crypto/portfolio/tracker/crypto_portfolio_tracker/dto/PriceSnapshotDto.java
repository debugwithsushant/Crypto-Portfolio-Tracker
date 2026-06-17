package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceSnapshotDto {

    private Integer id;
    private String assetSymbol;
    private BigDecimal priceUsd;
    private BigDecimal marketCap;
    private String source;
    private LocalDateTime capturedAt;
}
