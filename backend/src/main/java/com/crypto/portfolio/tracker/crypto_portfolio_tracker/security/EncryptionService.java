package com.crypto.portfolio.tracker.crypto_portfolio_tracker.security;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EncryptionService {

    public String encrypt(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public String decrypt(String value) {
        return new String(Base64.getDecoder().decode(value));
    }
}
