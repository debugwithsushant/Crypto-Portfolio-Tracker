package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.TradeHistoryResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Trade;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.ExchangeRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.TradeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradesService {

    private final TradeRepository tradeRepository;
    private final ExchangeRepository exchangeRepository;

    public TradesService(TradeRepository tradeRepository, ExchangeRepository exchangeRepository) {
        this.tradeRepository = tradeRepository;
        this.exchangeRepository = exchangeRepository;
    }

    public List<TradeHistoryResponse> getUserTrades(User user) {

        List<Trade> trades = tradeRepository.findByUser(
                user,
                Sort.by(Sort.Direction.DESC, "executedAt")
        );

        return trades.stream()
                .map(t -> new TradeHistoryResponse(
                        t.getId(),
                        t.getAssetSymbol(),
                        t.getSide().name(),
                        t.getQuantity(),
                        t.getPrice(),
                        t.getFee(),
                        t.getExchange().getName(),
                        t.getExecutedAt()
                ))
                .collect(Collectors.toList());
    }

    public TradeHistoryResponse addTrade(
            User user,
            String assetSymbol,
            String side,
            java.math.BigDecimal quantity,
            java.math.BigDecimal price,
            java.math.BigDecimal fee,
            Integer exchangeId,
            LocalDateTime executedAt
    ) {
        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        Trade trade = Trade.builder()
                .user(user)
                .assetSymbol(assetSymbol)
                .side(Trade.Side.valueOf(side.toUpperCase()))
                .quantity(quantity)
                .price(price)
                .fee(fee)
                .exchange(exchange)
                .executedAt(executedAt != null ? executedAt : LocalDateTime.now())
                .build();

        Trade saved = tradeRepository.save(trade);

        return new TradeHistoryResponse(
                saved.getId(),
                saved.getAssetSymbol(),
                saved.getSide().name(),
                saved.getQuantity(),
                saved.getPrice(),
                saved.getFee(),
                saved.getExchange().getName(),
                saved.getExecutedAt()
        );
    }
}