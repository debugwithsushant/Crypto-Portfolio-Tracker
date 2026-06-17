package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.AssetPnL;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.PortfolioPnLResponse;
import org.springframework.stereotype.Service;

import java.io.StringWriter;

@Service
public class PnLCsvExportService {

    public String generateCsv(PortfolioPnLResponse pnl) {

        StringWriter writer = new StringWriter();

        // Header
        writer.append("Asset Symbol,Quantity,Avg Cost,Current Price,Current Value,Unrealized PnL\n");

        for (AssetPnL asset : pnl.getAssets()) {
            writer.append(asset.getAssetSymbol()).append(",")
                    .append(asset.getQuantity().toPlainString()).append(",")
                    .append(asset.getAvgCost().toPlainString()).append(",")
                    .append(asset.getCurrentPrice().toPlainString()).append(",")
                    .append(asset.getCurrentValue().toPlainString()).append(",")
                    .append(asset.getUnrealizedPnL().toPlainString())
                    .append("\n");
        }

        // Summary lines
        writer.append("\n");
        writer.append("TOTAL UNREALIZED PnL,")
                .append(pnl.getUnrealizedPnL().toPlainString())
                .append("\n");

        writer.append("TOTAL REALIZED PnL,")
                .append(pnl.getRealizedPnL().toPlainString())
                .append("\n");

        writer.append("PORTFOLIO VALUE,")
                .append(pnl.getTotalValue().toPlainString())
                .append("\n");

        return writer.toString();
    }
}
