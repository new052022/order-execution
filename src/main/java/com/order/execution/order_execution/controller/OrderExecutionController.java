package com.order.execution.order_execution.controller;

import com.order.execution.order_execution.dto.OrderRequestDto;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/Order-execution")
@Tag(name = "Order-execution controller")
public class OrderExecutionController {

    @PostMapping("/perpetual-order")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto dto){

        log.info("[TRADING BOT] Time: {} | Order-execution-service | createOrder" + " | symbol: {} " +
                        "| order size: {} " + " | entry price: {} " + " | TP: {} " + " | SL: {} " + " | UserId: {}" +
                        "| result: {}",
                Timestamp.from(Instant.now()), dto.getSymbol(),dto.getVolume(),
                dto.getCurrentEntryPrice(),dto.getTakeProfitPrice(), dto.getStopLossPrice(), dto.getUserId(), " got OrderRequestDto");

        return ResponseEntity.ok(executeOrderService.handleOrderRequest(dto));
    }

}
