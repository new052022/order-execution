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
public class CreateOrderRequestDto {

    /**
     * Option trading pair, e.g BTC-200730-9000-C
     */
    private String symbol;

    /**
     * Buy/sell direction: SELL, BUY
     */
    private String side;

    private String positionSide;

    /**
     * Order Type: LIMIT (Only support LIMIT)
     */
    private String type;

    /**
     * Time in force method（Default GTC）
     */
    private String timeInForce;

    /**
     * Order Quantity
     */
    private String quantity;

    /**
     * Reduce Only（Default false）
     */
    private String reduceOnly;

    /**
     * Order Price
     */
    private String price;

    private String stopPrice;

    private String closePosition;

    private String activationPrice;

    private String callbackRate;

    private String workingType;

    /**
     * time living of order
     */
    private String goodTillDate;

    private String recvWindow;

    private String timestamp;

    private String apiKey;

    private String privateKey;

    private String exchange;

    private String newClientOrderId;

}
