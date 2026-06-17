package com.crypto.portfolio.tracker.crypto_portfolio_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoPortfolioTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoPortfolioTrackerApplication.class, args);
    }
}