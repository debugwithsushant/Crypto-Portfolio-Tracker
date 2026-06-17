package com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "scam_tokens",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"contract_address", "chain"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScamToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contract_address", nullable = false, length = 100)
    private String contractAddress;

    @Column(nullable = false, length = 50)
    private String chain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(name = "last_seen", nullable = false)
    private LocalDateTime lastSeen;

    public enum RiskLevel {
        LOW,
        MEDIUM,
        HIGH
    }
}
