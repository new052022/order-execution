package com.order.execution.order_execution.repository;

import com.order.execution.order_execution.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

}
