package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.BinanceTradeRawDTO;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Trade;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinanceTradeSyncService {

    private final TradeRepository tradeRepository;
    private final BinanceTradeMapper mapper;

    public BinanceTradeSyncService(
            TradeRepository tradeRepository,
            BinanceTradeMapper mapper
    ) {
        this.tradeRepository = tradeRepository;
        this.mapper = mapper;
    }

    public void syncMockTrades(
            User user,
            Exchange exchange,
            List<BinanceTradeRawDTO> rawTrades
    ) {
        for (BinanceTradeRawDTO dto : rawTrades) {
            Trade trade = mapper.toTrade(dto, user, exchange);
            tradeRepository.save(trade);
        }
    }
}
