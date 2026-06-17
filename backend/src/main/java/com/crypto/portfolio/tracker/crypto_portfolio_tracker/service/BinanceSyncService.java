package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.BinanceBalanceRawDTO;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BinanceSyncService {

    private final HoldingRepository holdingRepository;
    private final BinanceBalanceMapper mapper;

    public BinanceSyncService(
            HoldingRepository holdingRepository,
            BinanceBalanceMapper mapper
    ) {
        this.holdingRepository = holdingRepository;
        this.mapper = mapper;
    }

    public void syncBalances(
            User user,
            Exchange exchange,
            List<BinanceBalanceRawDTO> balances
    ) {

        for (BinanceBalanceRawDTO dto : balances) {

            BigDecimal quantity = mapper.calculateTotalQuantity(dto);

            if (mapper.isZeroBalance(quantity)) {
                continue;
            }

            holdingRepository
                    .findByUserAndAssetSymbolAndExchangeAndWalletType(
                            user,
                            dto.getAsset(),
                            exchange,
                            Holding.WalletType.EXCHANGE
                    )
                    .ifPresentOrElse(
                            existing -> {
                                existing.setQuantity(quantity);
                                holdingRepository.save(existing);
                            },
                            () -> {
                                Holding newHolding = mapper.toNewHolding(
                                        user,
                                        exchange,
                                        dto.getAsset(),
                                        quantity
                                );
                                holdingRepository.save(newHolding);
                            }
                    );
        }
    }
}
