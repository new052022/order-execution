package com.order.execution.order_execution.service.impl;

import com.order.execution.order_execution.model.Order;
import com.order.execution.order_execution.repository.OrderRepository;
import com.order.execution.order_execution.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

}
