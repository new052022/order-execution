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
public class OrderResponseDto {

    private long orderId;
    private String symbol;
    private String status;
    private String clientOrderId;
    private String price;
    private String avgPrice;
    private String origQty;
    private String executedQty;
    private String cumQty;
    private String cumQuote;
    private String timeInForce;
    private String type;
    private boolean reduceOnly;
    private boolean closePosition;
    private String side;
    private String positionSide;
    private String stopPrice;
    private String workingType;
    private boolean priceProtect;
    private String origType;
    private String priceMatch;
    private String selfTradePreventionMode;
    private long goodTillDate;
    private long updateTime;

    @Override
    public String toString() {
        return "OrderResponseDto{" +
                "orderId=" + orderId +
                ", symbol='" + symbol + '\'' +
                ", status='" + status + '\'' +
                ", clientOrderId='" + clientOrderId + '\'' +
                ", price='" + price + '\'' +
                ", avgPrice='" + avgPrice + '\'' +
                ", origQty='" + origQty + '\'' +
                ", executedQty='" + executedQty + '\'' +
                ", type='" + type + '\'' +
                ", reduceOnly=" + reduceOnly +
                ", closePosition=" + closePosition +
                ", side='" + side + '\'' +
                ", positionSide='" + positionSide + '\'' +
                ", stopPrice='" + stopPrice + '\'' +
                ", origType='" + origType + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

}
