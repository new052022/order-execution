package com.order.execution.order_execution.service.impl;

import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OrderResponseDto;
import com.order.execution.order_execution.service.interfaces.ExecuteOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("Bingx")
@RequiredArgsConstructor
public class BingxExecuteOrderServiceImpl implements ExecuteOrderService {

    @Override
    public OrderResponseDto handleOrderRequest(CreateOrderRequestDto dto) {
        return null;
    }

}
