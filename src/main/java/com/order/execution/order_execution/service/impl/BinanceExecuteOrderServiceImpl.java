package com.order.execution.order_execution.service.impl;

import com.order.execution.order_execution.client.interfaces.OpenOrdersClient;
import com.order.execution.order_execution.dto.AccountBalanceDto;
import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OpenOrdersResponseDto;
import com.order.execution.order_execution.dto.OpenPositionResponseDto;
import com.order.execution.order_execution.dto.OrderResponseDto;
import com.order.execution.order_execution.mapper.OrderMapper;
import com.order.execution.order_execution.model.Order;
import com.order.execution.order_execution.service.interfaces.ExecuteOrderService;
import com.order.execution.order_execution.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("Binance")
@RequiredArgsConstructor
public class BinanceExecuteOrderServiceImpl implements ExecuteOrderService {

    private OpenOrdersClient openOrdersClient;

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto handleOrderRequest(CreateOrderRequestDto dto) {
        Order order = orderMapper.toOrder(dto);
        Order savedOrder = orderService.saveOrder(order);
        OrderResponseDto perpetualOrder = openOrdersClient.createPerpetualOrder(dto);
        if (Objects.nonNull(perpetualOrder)) {
            savedOrder.setIsExecuted(true);
            orderService.saveOrder(savedOrder);
        }
        return perpetualOrder;
    }

    @Override
    public List<OpenOrdersResponseDto> handleOrderRequest(String encodedApiKey, String encodedSecretKey) {
        return openOrdersClient.getOpenOrders(encodedSecretKey, encodedApiKey);
    }

    @Override
    public List<OpenPositionResponseDto> handlePositionRequest(String encodedApiKey, String encodedSecretKey) {
        return openOrdersClient.getOpenPositions(encodedSecretKey, encodedApiKey);
    }

    @Override
    public List<AccountBalanceDto> handleAccountBalancesRequest(String encodedApiKey, String encodedSecretKey) {
        return openOrdersClient.getBalances(encodedSecretKey, encodedApiKey);
    }

    @Autowired
    @Qualifier(value = "Binance-client")
    public void setOpenOrdersClient(OpenOrdersClient openOrdersClient) {
        this.openOrdersClient = openOrdersClient;
    }

}
