package com.order.execution.order_execution.service.interfaces;

import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OpenOrdersResponseDto;
import com.order.execution.order_execution.dto.OpenPositionResponseDto;
import com.order.execution.order_execution.dto.OrderResponseDto;

import java.util.List;

public interface ExecuteOrderService {

    OrderResponseDto handleOrderRequest(CreateOrderRequestDto dto);

    List<OpenOrdersResponseDto> handleOrderRequest(String encodedSecretKey, String encodedApiKey);

    List<OpenPositionResponseDto> handlePositionRequest(String encodedApiKey, String encodedSecretKey);
}
