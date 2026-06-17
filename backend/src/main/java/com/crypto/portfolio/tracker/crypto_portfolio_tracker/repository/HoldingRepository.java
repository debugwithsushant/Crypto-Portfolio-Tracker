package com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Holding;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Integer> {
    List<Holding> findByUser(User user);
    Optional<Holding> findByUserAndAssetSymbolAndExchangeAndWalletType(
            User user,
            String assetSymbol,
            Exchange exchange,
            Holding.WalletType walletType
    );
    Optional<Holding> findByUserAndAssetSymbolAndExchange(
            User user,
            String assetSymbol,
            Exchange exchange
    );
}
