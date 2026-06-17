package com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ScamToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScamTokenRepository extends JpaRepository<ScamToken, Integer> {

    Optional<ScamToken> findByContractAddressAndChain(
            String contractAddress,
            String chain
    );
}
