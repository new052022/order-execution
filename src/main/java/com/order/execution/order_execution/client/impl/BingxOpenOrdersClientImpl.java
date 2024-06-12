package com.order.execution.order_execution.client.impl;

import com.order.execution.order_execution.client.interfaces.OpenOrdersClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("Bingx-client")
@RequiredArgsConstructor
public class BingxOpenOrdersClientImpl implements OpenOrdersClient {
}
