package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.PortfolioHoldingResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Holding;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final HoldingRepository holdingRepository;

    public PortfolioService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    public List<PortfolioHoldingResponse> getUserHoldings(User user) {

        List<Holding> holdings = holdingRepository.findByUser(user);

        return holdings.stream()
                .map(h -> new PortfolioHoldingResponse(
                        h.getAssetSymbol(),
                        h.getQuantity(),
                        h.getWalletType().name(),
                        h.getExchange() != null ? h.getExchange().getName() : null
                ))
                .collect(Collectors.toList());
    }
}
