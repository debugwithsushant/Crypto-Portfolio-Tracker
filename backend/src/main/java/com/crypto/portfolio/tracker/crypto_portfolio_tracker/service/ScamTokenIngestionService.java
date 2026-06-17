package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ScamToken;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.ScamTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScamTokenIngestionService {

    private final ScamTokenRepository repository;

    public ScamTokenIngestionService(ScamTokenRepository repository) {
        this.repository = repository;
    }

    public void ingestMockScamToken(
            String contract,
            String chain,
            ScamToken.RiskLevel level,
            String source
    ) {

        repository.findByContractAddressAndChain(contract, chain)
                .ifPresentOrElse(
                        existing -> {
                            existing.setRiskLevel(level);
                            existing.setLastSeen(LocalDateTime.now());
                            repository.save(existing);
                        },
                        () -> {
                            ScamToken token = ScamToken.builder()
                                    .contractAddress(contract)
                                    .chain(chain)
                                    .riskLevel(level)
                                    .source(source)
                                    .lastSeen(LocalDateTime.now())
                                    .build();

                            repository.save(token);
                        }
                );
    }
}
