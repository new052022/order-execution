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

@Service("Binance")
@RequiredArgsConstructor
public class BinanceExecuteOrderServiceImpl implements ExecuteOrderService {

    private OpenOrdersClient openOrdersClient;

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto handleOrderRequest(CreateOrderRequestDto dto) {
        Order order = orderMapper.toOrder(dto);
        orderService.saveOrder(order);
        return openOrdersClient.createPerpetualOrder(dto);
    }

    @Autowired
    @Qualifier(value = "Binance-client")
    public void setOpenOrdersClient(OpenOrdersClient openOrdersClient) {
        this.openOrdersClient = openOrdersClient;
    }

}
