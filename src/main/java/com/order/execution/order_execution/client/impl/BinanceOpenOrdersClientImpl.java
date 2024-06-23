package com.order.execution.order_execution.client.impl;

import com.order.execution.order_execution.client.interfaces.OpenOrdersClient;
import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OrderResponseDto;
import com.order.execution.order_execution.util.EncryptDecryptGenerator;
import com.order.execution.order_execution.util.binance.QueryParamsGenerator;
import com.order.execution.order_execution.util.binance.SignatureGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service("Binance-client")
@RequiredArgsConstructor
public class BinanceOpenOrdersClientImpl implements OpenOrdersClient {

    private final String PERPETUAL_MARKET_ORDER_URL = "https://fapi.binance.com/fapi/v1";

    public static final String ORDER = "/order";

    public static final String DELIMETER = "?";

    public static final String SIGNATURE = "&signature=";

    private final RestTemplate restTemplate;

    public static final String API_KEY_NAME = "X-MBX-APIKEY";

    private final EncryptDecryptGenerator encryptDecryptGenerator;

    private final QueryParamsGenerator queryParamsGenerator;

    public OrderResponseDto createPerpetualOrder(CreateOrderRequestDto dto) {
        dto.setTimestamp("" + new Timestamp(System.currentTimeMillis()).getTime());
        String privateKey = encryptDecryptGenerator.decryptData(dto.getPrivateKey());
        String params = queryParamsGenerator.generatePerpetualParams(dto);
        String signature = SignatureGenerator.generateSignature(privateKey, params);
        HttpHeaders headers = this.addHttpHeaders(API_KEY_NAME, encryptDecryptGenerator.decryptData(dto.getApiKey()));
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        OrderResponseDto order = restTemplate.exchange(
                PERPETUAL_MARKET_ORDER_URL + ORDER + DELIMETER + params + SIGNATURE + signature, HttpMethod.POST, entity,
                OrderResponseDto.class).getBody();
        log.info("[TRADING BOT] Time: {} | Order-execution-service | createPerpetualOrder | open order response: {} | action: {}",
                Timestamp.from(Instant.now()), dto, "send order to API Binance");
        return order;
    }

    private HttpHeaders addHttpHeaders(String apiName, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(apiName, apiKey);
        return headers;
    }

}
