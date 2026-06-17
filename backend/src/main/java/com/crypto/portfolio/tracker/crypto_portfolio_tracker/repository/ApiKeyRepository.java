package com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ApiKey;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {

    List<ApiKey> findByUser(User user);
    Optional<ApiKey> findByUserAndExchange(User user, Exchange exchange);
    void deleteByUserAndExchange(User user, Exchange exchange);
}
