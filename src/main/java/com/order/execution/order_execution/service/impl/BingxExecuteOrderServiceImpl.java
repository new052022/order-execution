package com.order.execution.order_execution.service.impl;

import com.order.execution.order_execution.client.interfaces.OpenOrdersClient;
import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OrderResponseDto;
import com.order.execution.order_execution.mapper.OrderMapper;
import com.order.execution.order_execution.model.Order;
import com.order.execution.order_execution.service.interfaces.ExecuteOrderService;
import com.order.execution.order_execution.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("Bingx")
@RequiredArgsConstructor
public class BingxExecuteOrderServiceImpl implements ExecuteOrderService {

    private OpenOrdersClient openOrdersClient;

    private final OrderMapper orderMapper;

    private final OrderService orderService;

    @Override
    public OrderResponseDto handleOrderRequest(CreateOrderRequestDto dto) {
        Order order = orderMapper.toOrder(dto);
        Order savedOrder = orderService.saveOrder(order);
        OrderResponseDto perpetualOrder = openOrdersClient.createPerpetualOrder(dto);
        if (perpetualOrder != null) {
            savedOrder.setIsExecuted(true);
            orderService.saveOrder(savedOrder);
        }
        return perpetualOrder;
    }

    @Autowired
    @Qualifier(value = "Bingx-client")
    public void setOpenOrdersClient(OpenOrdersClient openOrdersClient) {
        this.openOrdersClient = openOrdersClient;
    }
}
