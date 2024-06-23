package com.order.execution.order_execution.repository;

import com.order.execution.order_execution.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findTopBySymbolAndUserIdAndPriceAndExchangeOrderByUpdatedDateDesc(
            String symbol, Long userId, String price, String Exchange);

}
