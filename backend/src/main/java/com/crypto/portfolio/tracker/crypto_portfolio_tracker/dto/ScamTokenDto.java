package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ScamToken;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScamTokenDto {

    private Integer id;
    private String contractAddress;
    private String chain;
    private ScamToken.RiskLevel riskLevel;
    private String source;
    private LocalDateTime lastSeen;
}
