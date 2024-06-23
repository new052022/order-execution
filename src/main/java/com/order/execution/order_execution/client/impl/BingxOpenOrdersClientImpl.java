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
import java.util.Objects;

@Slf4j
@Service("Bingx-client")
@RequiredArgsConstructor
public class BingxOpenOrdersClientImpl implements OpenOrdersClient {

    private final static String BINGX_MARKET_ORDER_URL = "https://open-api.bingx.com";

    private final static String OPEN_ORDER_PATH = "/openApi/swap/v2/trade/order";

    private final static String BINGX_API_KEY_NAME = "X-BX-APIKEY";

    public static final String SIGNATURE = "&signature=";

    private final EncryptDecryptGenerator encryptDecryptGenerator;

    private final QueryParamsGenerator queryParamsGenerator;

    private final RestTemplate restTemplate;

    @Override
    public OrderResponseDto createPerpetualOrder(CreateOrderRequestDto dto) {
        dto.setTimestamp("" + new Timestamp(System.currentTimeMillis()).getTime());
        String privateKey = encryptDecryptGenerator.decryptData(dto.getPrivateKey());
        String apiKey = encryptDecryptGenerator.decryptData(dto.getApiKey());
        String params = queryParamsGenerator.generatePerpetualParams(dto);
        String signature = SignatureGenerator.generateSignature(privateKey, params);
        String requestUrl = BINGX_MARKET_ORDER_URL + OPEN_ORDER_PATH + "?" + params + SIGNATURE + signature;
        HttpHeaders httpHeaders = this.addHttpHeaders(BINGX_API_KEY_NAME, apiKey);
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
        OrderResponseDto data = null;
        try {
            String response = Objects.requireNonNull(restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    entity,
                    String.class).getBody());
            System.out.println(response);
        } catch (Exception e) {
            log.info("[TRADING BOT] Time: {} | Order-execution-service | createPerpetualOrder (Bingx) | Failed order response: {}",
                    Timestamp.from(Instant.now()), e.getMessage());
        }
        log.info("[TRADING BOT] Time: {} | Order-execution-service | createPerpetualOrder (Bingx) | open order response: {} | action: {}",
                Timestamp.from(Instant.now()), dto, "send order to API Bingx");
        return data;
    }

    private HttpHeaders addHttpHeaders(String apiName, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(apiName, apiKey);
        return headers;
    }

}
