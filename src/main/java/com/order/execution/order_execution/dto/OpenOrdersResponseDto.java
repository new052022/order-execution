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
public class OpenOrdersResponseDto {

    // Maps to "avgPrice": "0.00000"
    private String avgPrice;

    // Maps to "clientOrderId": "abc"
    private String clientOrderId;

    // Maps to "cumQuote": "0"
    private String cumQuote;

    // Maps to "executedQty": "0"
    private String executedQty;

    // Maps to "orderId": 1917641
    private Long orderId;

    // Maps to "origQty": "0.40"
    private String origQty;

    // Maps to "origType": "TRAILING_STOP_MARKET"
    private String origType;

    // Maps to "price": "0"
    private String price;

    // Maps to "reduceOnly": false
    private Boolean reduceOnly; // Using Boolean wrapper class is generally safer for DTOs

    // Maps to "side": "BUY"
    private String side;

    // Maps to "positionSide": "SHORT"
    private String positionSide;

    // Maps to "status": "NEW"
    private String status;

    // Maps to "stopPrice": "9300"
    private String stopPrice;

    // Maps to "closePosition": false
    private Boolean closePosition; // Using Boolean wrapper class

    // Maps to "symbol": "BTCUSDT"
    private String symbol;

    // Maps to "time": 1579276756075 (Unix timestamp in milliseconds)
    private Long time;

    // Maps to "timeInForce": "GTC"
    private String timeInForce;

    // Maps to "type": "TRAILING_STOP_MARKET"
    private String type;

    // Maps to "activatePrice": "9020"
    private String activatePrice;

    // Maps to "priceRate": "0.3"
    private String priceRate;

    // Maps to "updateTime": 1579276756075 (Unix timestamp in milliseconds)
    private Long updateTime;

    // Maps to "workingType": "CONTRACT_PRICE"
    private String workingType;

    // Maps to "priceProtect": false
    private Boolean priceProtect; // Using Boolean wrapper class

    // Maps to "priceMatch": "NONE"
    private String priceMatch;

    // Maps to "selfTradePreventionMode": "NONE"
    private String selfTradePreventionMode;

    // Maps to "goodTillDate": 0 (Unix timestamp in milliseconds or 0)
    private Long goodTillDate;


}
