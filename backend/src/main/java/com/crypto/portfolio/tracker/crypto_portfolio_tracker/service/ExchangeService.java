package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.ConnectedExchangeResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.ExchangeConnectRequest;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.ApiKey;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.ApiKeyRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.ExchangeRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.util.EncryptionUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final ApiKeyRepository apiKeyRepository;

    public ExchangeService(
            ExchangeRepository exchangeRepository,
            ApiKeyRepository apiKeyRepository
    ) {
        this.exchangeRepository = exchangeRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public void connectExchange(User user, ExchangeConnectRequest request) throws Exception {

        Exchange exchange = exchangeRepository.findByName(request.getExchangeName())
                .orElseThrow(() -> new RuntimeException("Exchange not found: " + request.getExchangeName()));

        apiKeyRepository.findByUserAndExchange(user, exchange)
                .ifPresent(k -> {
                    throw new RuntimeException("Exchange already connected");
                });

        ApiKey apiKey = ApiKey.builder()
                .user(user)
                .exchange(exchange)
                .apiKey(EncryptionUtil.encrypt(request.getApiKey()))
                .apiSecret(EncryptionUtil.encrypt(request.getApiSecret()))
                .label(request.getLabel())
                .build();

        apiKeyRepository.save(apiKey);
    }

    public List<ConnectedExchangeResponse> getConnectedExchanges(User user) {
        return apiKeyRepository.findByUser(user)
                .stream()
                .map(apiKey -> {
                    Exchange ex = apiKey.getExchange();
                    return new ConnectedExchangeResponse(
                            ex.getId(),
                            ex.getName(),
                            ex.getBaseUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    public void disconnectExchange(User user, String exchangeName) {
        Exchange exchange = exchangeRepository.findByName(exchangeName)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));
        ApiKey apiKey = apiKeyRepository.findByUserAndExchange(user, exchange)
                .orElseThrow(() -> new RuntimeException("Exchange not connected"));
        apiKeyRepository.delete(apiKey);
    }
}