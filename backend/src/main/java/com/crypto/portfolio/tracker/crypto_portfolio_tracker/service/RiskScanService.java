package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.*;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskScanService {

    private final HoldingRepository holdingRepository;
    private final ScamTokenRepository scamTokenRepository;
    private final RiskAlertRepository riskAlertRepository;

    public RiskScanService(
            HoldingRepository holdingRepository,
            ScamTokenRepository scamTokenRepository,
            RiskAlertRepository riskAlertRepository
    ) {
        this.holdingRepository = holdingRepository;
        this.scamTokenRepository = scamTokenRepository;
        this.riskAlertRepository = riskAlertRepository;
    }

    public void scanUserHoldings(User user) {

        List<Holding> holdings = holdingRepository.findByUser(user);

        for (Holding holding : holdings) {

            // NOTE:
            // For now assetSymbol == contractAddress (mock assumption)
            // Real world: map symbol → contract address
            scamTokenRepository
                    .findByContractAddressAndChain(
                            holding.getAssetSymbol(),
                            "ETH"   // assume ETH chain for now
                    )
                    .ifPresent(scamToken -> createAlertIfNotExists(
                            user,
                            holding.getAssetSymbol(),
                            scamToken
                    ));
        }
    }

    private void createAlertIfNotExists(
            User user,
            String assetSymbol,
            ScamToken scamToken
    ) {

        RiskAlert.AlertType type = RiskAlert.AlertType.CONTRACT_RISK;

        riskAlertRepository
                .findByUserAndAssetSymbolAndAlertType(
                        user,
                        assetSymbol,
                        type
                )
                .orElseGet(() -> {

                    String details =
                            "Token flagged as " + scamToken.getRiskLevel() +
                                    " risk by " + scamToken.getSource();

                    RiskAlert alert = RiskAlert.builder()
                            .user(user)
                            .assetSymbol(assetSymbol)
                            .alertType(type)
                            .details(details)
                            .build();

                    return riskAlertRepository.save(alert);
                });
    }
}
