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
public class AccountBalanceDto {

    // Maps to "accountAlias": "SgsR"
    private String accountAlias;

    // Maps to "asset": "USDT"
    private String asset;

    // Maps to "balance": "122607.35137903"
    private String balance; // Или используйте BigDecimal для точности

    // Maps to "crossWalletBalance": "23.72469206"
    private String crossWalletBalance; // Или используйте BigDecimal

    // Maps to "crossUnPnl": "0.00000000"
    private String crossUnPnl; // Или используйте BigDecimal

    // Maps to "availableBalance": "23.72469206"
    private String availableBalance; // Или используйте BigDecimal

    // Maps to "maxWithdrawAmount": "23.72469206"
    private String maxWithdrawAmount; // Или используйте BigDecimal

    // Maps to "marginAvailable": true
    private Boolean marginAvailable; // Используем Boolean для возможности null

    // Maps to "updateTime": 1617939110373 (Unix timestamp in milliseconds)
    private Long updateTime;

}
