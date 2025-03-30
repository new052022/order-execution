package com.order.execution.order_execution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenPositionResponseDto {

    // Maps to "symbol": "ADAUSDT"
    private String symbol;

    // Maps to "positionSide": "BOTH"
    private String positionSide;

    // Maps to "positionAmt": "30"
    private String positionAmt; // Или используйте BigDecimal для точности

    // Maps to "entryPrice": "0.385"
    private String entryPrice; // Или используйте BigDecimal

    // Maps to "breakEvenPrice": "0.385077"
    private String breakEvenPrice; // Или используйте BigDecimal

    // Maps to "markPrice": "0.41047590"
    private String markPrice; // Или используйте BigDecimal

    // Maps to "unRealizedProfit": "0.76427700"
    private String unRealizedProfit; // Или используйте BigDecimal

    // Maps to "liquidationPrice": "0"
    private String liquidationPrice; // Или используйте BigDecimal

    // Maps to "isolatedMargin": "0"
    private String isolatedMargin; // Или используйте BigDecimal

    // Maps to "notional": "12.31427700"
    private String notional; // Или используйте BigDecimal

    // Maps to "marginAsset": "USDT"
    private String marginAsset;

    // Maps to "isolatedWallet": "0"
    private String isolatedWallet; // Или используйте BigDecimal

    // Maps to "initialMargin": "0.61571385"
    private String initialMargin; // Или используйте BigDecimal

    // Maps to "maintMargin": "0.08004280"
    private String maintMargin; // Или используйте BigDecimal

    // Maps to "positionInitialMargin": "0.61571385"
    private String positionInitialMargin; // Или используйте BigDecimal

    // Maps to "openOrderInitialMargin": "0"
    private String openOrderInitialMargin; // Или используйте BigDecimal

    // Maps to "adl": 2
    private Integer adl; // Используем Integer, так как значение целое

    // Maps to "bidNotional": "0"
    private String bidNotional; // Или используйте BigDecimal

    // Maps to "askNotional": "0"
    private String askNotional; // Или используйте BigDecimal

    // Maps to "updateTime": 1720736417660 (Unix timestamp in milliseconds)
    private Long updateTime;

}
