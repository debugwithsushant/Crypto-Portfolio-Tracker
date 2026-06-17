package com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

    Optional<Exchange> findByName(String name);
}
