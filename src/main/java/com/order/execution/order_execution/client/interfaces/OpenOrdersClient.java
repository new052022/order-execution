package com.order.execution.order_execution.client.interfaces;

import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OrderResponseDto;

public interface OpenOrdersClient {

    OrderResponseDto createPerpetualOrder(CreateOrderRequestDto dto);

}
