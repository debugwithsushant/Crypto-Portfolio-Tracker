package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.ApiKeyRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public ApiKey connectExchange(User user,
                                  Exchange exchange,
                                  String apiKey,
                                  String apiSecret,
                                  String label) throws Exception{
        String encryptedKey = EncryptionUtil.encrypt(apiKey);
        String encryptedSecret = EncryptionUtil.encrypt(apiSecret);

        ApiKey key = new ApiKey();
        key.setUser(user);
        key.setExchange(exchange);
        key.setApiKey(encryptedKey);
        key.setApiSecret(encryptedSecret);
        key.setLabel(label);

        return apiKeyRepository.save(key);
    }
}
