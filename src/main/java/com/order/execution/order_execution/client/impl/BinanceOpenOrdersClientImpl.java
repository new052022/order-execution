package com.order.execution.order_execution.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.execution.order_execution.client.interfaces.OpenOrdersClient;
import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.OpenOrdersResponseDto;
import com.order.execution.order_execution.dto.OrderResponseDto;
import com.order.execution.order_execution.util.EncryptDecryptGenerator;
import com.order.execution.order_execution.util.binance.QueryParamsGenerator;
import com.order.execution.order_execution.util.binance.SignatureGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("Binance-client")
@RequiredArgsConstructor
public class BinanceOpenOrdersClientImpl implements OpenOrdersClient {

    public final static String TIMESTAMP = "timestamp";

    public static String RECV_WINDOW = "recvWindow";

    private final static String PERPETUAL_MARKET_ORDER_URL = "https://fapi.binance.com/fapi/v1";

    public static final String ORDER = "/order";

    public static final String DELIMETER = "?";

    public static final String SIGNATURE = "&signature=";

    private final RestTemplate restTemplate;

    public static final String API_KEY_NAME = "X-MBX-APIKEY";

    private static final String OPEN_ORDERS_URL = "/openOrders";

    private final ObjectMapper objectMapper;

    private final EncryptDecryptGenerator encryptDecryptGenerator;

    private final QueryParamsGenerator queryParamsGenerator;

    @Override
    @SneakyThrows
    public List<OpenOrdersResponseDto> getOpenOrders(String encodedSecretKey, String encodedApiKey){
        String time = "" + new Timestamp(System.currentTimeMillis()).getTime();
        String recvWindows = "15000";
        String secretKey = encryptDecryptGenerator.decryptData(encodedSecretKey);
        String apiKey = encryptDecryptGenerator.decryptData(encodedApiKey);
        String params = this.getOpenOrdersParams(secretKey, time, recvWindows);
        String requestUrl = this.getRequestUrl(OPEN_ORDERS_URL, params);
        HttpHeaders headers = this.addHttpHeaders(API_KEY_NAME, apiKey);
        String openOrdersResponse = restTemplate.exchange(
                requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
        List<OpenOrdersResponseDto> ordersList = objectMapper.readValue(openOrdersResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OpenOrdersResponseDto.class));
        log.info("[TRADING BOT] Time: {} | Order-service | getOpenOrders" +
                        " | number of open orders : {} | action: {}",
                Timestamp.from(Instant.now()), ordersList.size(), "fetch open orders");
        return ordersList;
    }

    public OrderResponseDto createPerpetualOrder(CreateOrderRequestDto dto) {
        dto.setTimestamp("" + new Timestamp(System.currentTimeMillis()).getTime());
        String privateKey = encryptDecryptGenerator.decryptData(dto.getPrivateKey());
        String params = queryParamsGenerator.generatePerpetualParams(dto);
        String signature = SignatureGenerator.generateSignature(privateKey, params);
        HttpHeaders headers = this.addHttpHeaders(API_KEY_NAME, encryptDecryptGenerator.decryptData(dto.getApiKey()));
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        OrderResponseDto order = null;
        try {
            order = restTemplate.exchange(
                    PERPETUAL_MARKET_ORDER_URL + ORDER + DELIMETER + params + SIGNATURE + signature, HttpMethod.POST, entity,
                    OrderResponseDto.class).getBody();
        } catch (Exception e) {
            log.info("[TRADING BOT] Time: {} | Order-execution-service | createPerpetualOrder (Binance) | Failed order response: {}",
                    Timestamp.from(Instant.now()), e.getMessage());
        }
        log.info("[TRADING BOT] Time: {} | Order-execution-service | createPerpetualOrder (Binance) | open order response: {} | action: {}",
                Timestamp.from(Instant.now()), dto, "send order to API Binance");
        return order;
    }

    @Override
    public String getOpenOrdersParams(String secretKey, String time, String recvWindow) {
        TreeMap<String, String> parameters = new TreeMap<>();
        parameters.put(TIMESTAMP, time);
        parameters.put(RECV_WINDOW, recvWindow);
        String valueToDigest = this.getMessageToDigest(parameters);
        String signature = SignatureGenerator.generateSignature(secretKey, valueToDigest);
        return valueToDigest + "&signature=" + signature;
    }

    private String getRequestUrl(String openOrdersUrl, String params) {
        String urlStr = PERPETUAL_MARKET_ORDER_URL + openOrdersUrl + "?" + params;
        return urlStr;
    }

    private String getMessageToDigest(TreeMap<String, String> parameters) {
        Boolean first = true;
        String valueToDigest = "";
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            if (!first) {
                valueToDigest += "&";
            }
            first = false;
            valueToDigest += e.getKey() + "=" + e.getValue();
        }
        return valueToDigest;
    }

    private HttpHeaders addHttpHeaders(String apiName, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(apiName, apiKey);
        return headers;
    }

}
