package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.RiskAlertResponse;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.RiskAlert;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.RiskAlertRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiskAlertService {

    private final RiskAlertRepository repository;

    public RiskAlertService(RiskAlertRepository repository) {
        this.repository = repository;
    }

    public List<RiskAlertResponse> getUserAlerts(User user) {

        List<RiskAlert> alerts = repository.findByUser(
                user,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return alerts.stream()
                .map(a -> new RiskAlertResponse(
                        a.getAssetSymbol(),
                        a.getAlertType().name(),
                        a.getDetails(),
                        a.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
