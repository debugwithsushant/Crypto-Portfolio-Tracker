package com.crypto.portfolio.tracker.crypto_portfolio_tracker.config;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Exchange;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.ExchangeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final ExchangeRepository exchangeRepository;

    public DataInitializer(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public void run(String... args) {

        if (exchangeRepository.findByName("BINANCE").isEmpty()) {

            Exchange binance = Exchange.builder()
                    .name("BINANCE")
                    .baseUrl("https://api.binance.com")
                    .build();

            exchangeRepository.save(binance);
        }
    }
}
