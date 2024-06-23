package com.order.execution.order_execution.controller;

import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OrderResponseDto;
import com.order.execution.order_execution.service.interfaces.ExecuteOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order-execution")
@Tag(name = "Order-execution controller")
public class OrderExecutionController {

    private final Map<String, ExecuteOrderService> executeOrderServiceMap;

    @PostMapping("/perpetual-order")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto dto) {
        log.info("[TRADING BOT] Time: {} | Order-execution-service | createOrder" + " | symbol: {} " +
                        "| order size: {} " + " | entry price: {} " + "| result: {}",
                Timestamp.from(Instant.now()), dto.getSymbol(), dto.getQuantity(),
                dto.getPrice()," CreateOrderRequestDto is in Order execution service.");
        return ResponseEntity.ok(executeOrderServiceMap.get(dto.getExchange()).handleOrderRequest(dto));
    }

}
