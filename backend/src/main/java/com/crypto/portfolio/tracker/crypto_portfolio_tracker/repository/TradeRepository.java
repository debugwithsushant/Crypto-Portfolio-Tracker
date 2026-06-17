package com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Trade;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Integer> {
    List<Trade> findByUser(User user);
    List<Trade> findByUserAndAssetSymbolAndExchange(
            User user,
            String assetSymbol,
            Exchange exchange
    );
    List<Trade> findByUser(User user, Sort sort);
    List<Trade> findByUserAndSide(User user, Trade.Side side);
}
