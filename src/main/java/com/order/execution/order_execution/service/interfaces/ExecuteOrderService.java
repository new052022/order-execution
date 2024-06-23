package com.order.execution.order_execution.service.interfaces;

import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OrderResponseDto;

public interface ExecuteOrderService {

    OrderResponseDto handleOrderRequest(CreateOrderRequestDto dto);

}
