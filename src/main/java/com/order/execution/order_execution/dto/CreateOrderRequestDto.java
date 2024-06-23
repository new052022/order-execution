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

    private String symbol;

    private String side;

    private String positionSide;

    private String type;

    private String timeInForce;

    private String quantity;

    private String reduceOnly;

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

}
