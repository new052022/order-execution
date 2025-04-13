package com.order.execution.order_execution.client.interfaces;

import com.order.execution.order_execution.dto.AccountBalanceDto;
import com.order.execution.order_execution.dto.CloseOrdersRequestDto;
import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OpenOrdersResponseDto;
import com.order.execution.order_execution.dto.OpenPositionResponseDto;
import com.order.execution.order_execution.dto.OrderResponseDto;

import java.util.List;

public interface OpenOrdersClient {

    List<AccountBalanceDto> getBalances(String encodedSecretKey, String encodedApiKey);

    OrderResponseDto createPerpetualOrder(CreateOrderRequestDto dto);

    String getOpenOrdersParams(String secretKey, String time);

    List<OpenOrdersResponseDto> getOpenOrders(String encodedSecretKey, String encodedApiKey);

    List<OpenPositionResponseDto> getOpenPositions(String encodedSecretKey, String encodedApiKey);

    String deleteOrders(CloseOrdersRequestDto request);

}
