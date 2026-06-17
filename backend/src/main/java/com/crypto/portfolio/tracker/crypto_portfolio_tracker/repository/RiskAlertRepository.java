package com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.RiskAlert;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RiskAlertRepository extends JpaRepository<RiskAlert, Integer> {
    List<RiskAlert> findByUser(User user, Sort sort);
    Optional<RiskAlert> findByUserAndAssetSymbolAndAlertType(
            User user,
            String assetSymbol,
            RiskAlert.AlertType alertType
    );
}
