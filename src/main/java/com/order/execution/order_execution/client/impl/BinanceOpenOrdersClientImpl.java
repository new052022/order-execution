package com.order.execution.order_execution.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.execution.order_execution.client.interfaces.OpenOrdersClient;
import com.order.execution.order_execution.dto.AccountBalanceDto;
import com.order.execution.order_execution.dto.CloseOrdersRequestDto;
import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.DeleteOrderDto;
import com.order.execution.order_execution.dto.OpenOrdersResponseDto;
import com.order.execution.order_execution.dto.OpenPositionResponseDto;
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

    private final static String GENERAL_BINANCE_API = "https://fapi.binance.com";

    private final static String BALANCE = "/fapi/v2/balance";

    private final static String DELETE_ORDERS = "/fapi/v1/order";

    public static final String ORDER = "/order";

    public static final String DELIMETER = "?";

    public static final String SIGNATURE = "&signature=";

    private final RestTemplate restTemplate;

    public static final String API_KEY_NAME = "X-MBX-APIKEY";

    private static final String OPEN_ORDERS_URL = "/fapi/v1/openOrders";

    private static final String OPEN_POSITIONS_URL = "/fapi/v3/positionRisk";

    private static final String TICKER_PRICE_URL = "/fapi/v2/ticker/price";

    private final ObjectMapper objectMapper;

    private final EncryptDecryptGenerator encryptDecryptGenerator;

    private final QueryParamsGenerator queryParamsGenerator;

    @Override
    @SneakyThrows
    public List<AccountBalanceDto> getBalances(String encodedSecretKey, String encodedApiKey) {
        String time = "" + new Timestamp(System.currentTimeMillis()).getTime();
        String secretKey = encryptDecryptGenerator.decryptData(encodedSecretKey);
        String apiKey = encryptDecryptGenerator.decryptData(encodedApiKey);
        String params = this.getOpenOrdersParams(secretKey, time);
        String requestUrl = this.getRequestUrl(BALANCE, params);
        HttpHeaders headers = this.addHttpHeaders(API_KEY_NAME, apiKey);
        String openOrdersResponse = restTemplate.exchange(
                requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
        List<AccountBalanceDto> ordersList = objectMapper.readValue(openOrdersResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, AccountBalanceDto.class));
        log.info("[TRADING BOT] Time: {} | Order-service | getBalances" +
                        " | number of assets in balance : {} | action: {}",
                Timestamp.from(Instant.now()), ordersList.size(), "fetch account balances");
        return ordersList;
    }

    @Override
    @SneakyThrows
    public List<OpenPositionResponseDto> getOpenPositions(String encodedSecretKey, String encodedApiKey) {
        String time = "" + new Timestamp(System.currentTimeMillis()).getTime();
        String secretKey = encryptDecryptGenerator.decryptData(encodedSecretKey);
        String apiKey = encryptDecryptGenerator.decryptData(encodedApiKey);
        String params = this.getOpenOrdersParams(secretKey, time);
        String requestUrl = this.getRequestUrl(OPEN_POSITIONS_URL, params);
        HttpHeaders headers = this.addHttpHeaders(API_KEY_NAME, apiKey);
        String openOrdersResponse = restTemplate.exchange(
                requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
        List<OpenPositionResponseDto> ordersList = objectMapper.readValue(openOrdersResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OpenPositionResponseDto.class));
        log.info("[TRADING BOT] Time: {} | Order-service | getOpenPositions" +
                        " | number of open positions : {} | action: {}",
                Timestamp.from(Instant.now()), ordersList.size(), "fetch open positions");
        return ordersList;
    }

    @Override
    @SneakyThrows
    public List<OpenOrdersResponseDto> getOpenOrders(String encodedSecretKey, String encodedApiKey) {
        String time = "" + new Timestamp(System.currentTimeMillis()).getTime();
        String secretKey = encryptDecryptGenerator.decryptData(encodedSecretKey);
        String apiKey = encryptDecryptGenerator.decryptData(encodedApiKey);
        String params = this.getOpenOrdersParams(secretKey, time);
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

        // Проверяем и корректируем stopPrice для всех ордеров, если он установлен
        if (dto.getStopPrice() != null && !dto.getStopPrice().isEmpty()) {
            dto = adjustStopPriceIfNeeded(dto);
        }

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

            // Создаем частичный Take Profit ордер, если флаг установлен
            if (order != null && dto.getShouldPartialClose() != null && dto.getShouldPartialClose()) {
                createPartialTakeProfitOrder(dto);
            }
        } catch (Exception e) {
            log.info("[TRADING BOT] Time: {} | Order-execution-service | createPerpetualOrder (Binance) | Failed order response: {}",
                    Timestamp.from(Instant.now()), e.getMessage());
        }
        log.info("[TRADING BOT] Time: {} | Order-execution-service | createPerpetualOrder (Binance) | open order response: {} | action: {}",
                Timestamp.from(Instant.now()), dto, "send order to API Binance");
        return order;
    }

    /**
     * Корректирует stopPrice для ордера, чтобы избежать ошибки "Order would immediately trigger"
     */
    private CreateOrderRequestDto adjustStopPriceIfNeeded(CreateOrderRequestDto dto) {
        try {
            String symbol = dto.getSymbol();
            double currentPrice = getCurrentMarketPrice(symbol);

            if (currentPrice == 0) {
                log.warn("[TRADING BOT] Time: {} | Order-execution-service | adjustStopPriceIfNeeded (Binance) | " +
                        "Cannot adjust stopPrice - failed to fetch current market price. Using original stopPrice",
                        Timestamp.from(Instant.now()));
                return dto;
            }

            double requestedStopPrice = Double.parseDouble(dto.getStopPrice());
            double minPriceOffset = 0.01; // 1% минимальный отступ
            double adjustedStopPrice = requestedStopPrice;
            boolean wasAdjusted = false;

            // Определяем тип ордера и направление
            String orderType = dto.getType();
            String side = dto.getSide();

            // Для TAKE_PROFIT и STOP_MARKET ордеров
            if (orderType != null && (orderType.contains("TAKE_PROFIT") || orderType.contains("STOP"))) {
                if (side.equals("BUY")) {
                    // Для BUY ордера (закрытие SHORT или открытие LONG)
                    if (orderType.contains("TAKE_PROFIT")) {
                        // TP для SHORT: stopPrice должен быть ниже текущей цены
                        double maxAllowedPrice = currentPrice * (1 - minPriceOffset);
                        if (requestedStopPrice >= currentPrice) {
                            adjustedStopPrice = maxAllowedPrice;
                            wasAdjusted = true;
                        }
                    } else if (orderType.contains("STOP")) {
                        // Stop Loss для LONG: stopPrice должен быть ниже текущей цены
                        double maxAllowedPrice = currentPrice * (1 - minPriceOffset);
                        if (requestedStopPrice >= currentPrice) {
                            adjustedStopPrice = maxAllowedPrice;
                            wasAdjusted = true;
                        }
                    }
                } else { // SELL
                    // Для SELL ордера (закрытие LONG или открытие SHORT)
                    if (orderType.contains("TAKE_PROFIT")) {
                        // TP для LONG: stopPrice должен быть выше текущей цены
                        double minAllowedPrice = currentPrice * (1 + minPriceOffset);
                        if (requestedStopPrice <= currentPrice) {
                            adjustedStopPrice = minAllowedPrice;
                            wasAdjusted = true;
                        }
                    } else if (orderType.contains("STOP")) {
                        // Stop Loss для SHORT: stopPrice должен быть выше текущей цены
                        double minAllowedPrice = currentPrice * (1 + minPriceOffset);
                        if (requestedStopPrice <= currentPrice) {
                            adjustedStopPrice = minAllowedPrice;
                            wasAdjusted = true;
                        }
                    }
                }
            }

            if (wasAdjusted) {
                dto.setStopPrice(String.format("%.8f", adjustedStopPrice));
                log.warn("[TRADING BOT] Time: {} | Order-execution-service | adjustStopPriceIfNeeded (Binance) | " +
                        "StopPrice adjusted | Symbol: {} | Type: {} | Side: {} | Current Price: {} | " +
                        "Requested StopPrice: {} | Adjusted StopPrice: {} | Offset: {}%",
                        Timestamp.from(Instant.now()), symbol, orderType, side, currentPrice,
                        requestedStopPrice, adjustedStopPrice, minPriceOffset * 100);
            } else {
                log.info("[TRADING BOT] Time: {} | Order-execution-service | adjustStopPriceIfNeeded (Binance) | " +
                        "StopPrice is valid | Symbol: {} | Type: {} | Side: {} | Current Price: {} | StopPrice: {}",
                        Timestamp.from(Instant.now()), symbol, orderType, side, currentPrice, requestedStopPrice);
            }

        } catch (Exception e) {
            log.error("[TRADING BOT] Time: {} | Order-execution-service | adjustStopPriceIfNeeded (Binance) | " +
                    "Failed to adjust stopPrice: {}. Using original stopPrice",
                    Timestamp.from(Instant.now()), e.getMessage());
        }

        return dto;
    }

    /**
     * Создает ордер для частичного закрытия позиции (Take Profit)
     */
    private void createPartialTakeProfitOrder(CreateOrderRequestDto originalDto) {
        try {
            // Вычисляем количество для частичного закрытия
            double originalQuantity = Double.parseDouble(originalDto.getQuantity());
            double partialQuantity = originalQuantity * originalDto.getPartialClosePercent();

            // Получаем текущую рыночную цену
            String symbol = originalDto.getSymbol();
            double currentPrice = getCurrentMarketPrice(symbol);

            if (currentPrice == 0) {
                log.error("[TRADING BOT] Time: {} | Order-execution-service | createPartialTakeProfitOrder (Binance) | " +
                        "Cannot create TP order - failed to fetch current market price",
                        Timestamp.from(Instant.now()));
                return;
            }

            // Определяем безопасную цену для TP с учетом направления позиции
            // Минимальный отступ от текущей цены - 1%
            double safeTpPrice;
            double minPriceOffset = 0.01; // 1% минимальный отступ

            if (originalDto.getSide().equals("BUY")) {
                // Для LONG позиции: TP должен быть выше текущей цены
                double minAllowedTpPrice = currentPrice * (1 + minPriceOffset);
                safeTpPrice = Math.max(originalDto.getPartialClosePrice(), minAllowedTpPrice);

                // Проверяем, что заданная цена не слишком близко к текущей
                if (originalDto.getPartialClosePrice() <= currentPrice) {
                    log.warn("[TRADING BOT] Time: {} | Order-execution-service | createPartialTakeProfitOrder (Binance) | " +
                            "Requested TP price ({}) is below or equal to current price ({}). Using safe TP: {}",
                            Timestamp.from(Instant.now()), originalDto.getPartialClosePrice(), currentPrice, safeTpPrice);
                }
            } else {
                // Для SHORT позиции: TP должен быть ниже текущей цены
                double maxAllowedTpPrice = currentPrice * (1 - minPriceOffset);
                safeTpPrice = Math.min(originalDto.getPartialClosePrice(), maxAllowedTpPrice);

                // Проверяем, что заданная цена не слишком близко к текущей
                if (originalDto.getPartialClosePrice() >= currentPrice) {
                    log.warn("[TRADING BOT] Time: {} | Order-execution-service | createPartialTakeProfitOrder (Binance) | " +
                            "Requested TP price ({}) is above or equal to current price ({}). Using safe TP: {}",
                            Timestamp.from(Instant.now()), originalDto.getPartialClosePrice(), currentPrice, safeTpPrice);
                }
            }

            log.info("[TRADING BOT] Time: {} | Order-execution-service | createPartialTakeProfitOrder (Binance) | " +
                    "TP Price Calculation | Symbol: {} | Side: {} | Current Price: {} | Requested TP: {} | Safe TP: {} | Offset: {}%",
                    Timestamp.from(Instant.now()), symbol, originalDto.getSide(), currentPrice,
                    originalDto.getPartialClosePrice(), safeTpPrice, minPriceOffset * 100);

            // Создаем DTO для TP ордера
            CreateOrderRequestDto tpDto = CreateOrderRequestDto.builder()
                    .symbol(originalDto.getSymbol())
                    .side(originalDto.getSide().equals("BUY") ? "SELL" : "BUY") // Противоположная сторона
                    .positionSide(originalDto.getPositionSide())
                    .type("TAKE_PROFIT_MARKET") // Используем TAKE_PROFIT_MARKET для автоматического исполнения
                    .quantity(String.format("%.8f", partialQuantity)) // Форматируем количество
                    .stopPrice(String.format("%.8f", safeTpPrice)) // Используем безопасную цену
                    .workingType("MARK_PRICE") // Используем mark price для избежания манипуляций
                    .reduceOnly("true") // Только для закрытия позиции
                    .apiKey(originalDto.getApiKey())
                    .privateKey(originalDto.getPrivateKey())
                    .timestamp("" + new Timestamp(System.currentTimeMillis()).getTime())
                    .build();

            // Отправляем TP ордер
            String params = queryParamsGenerator.generatePerpetualParams(tpDto);
            String privateKey = encryptDecryptGenerator.decryptData(tpDto.getPrivateKey());
            String signature = SignatureGenerator.generateSignature(privateKey, params);
            HttpHeaders headers = this.addHttpHeaders(API_KEY_NAME, encryptDecryptGenerator.decryptData(tpDto.getApiKey()));
            HttpEntity<Object> entity = new HttpEntity<>(headers);

            OrderResponseDto tpOrder = restTemplate.exchange(
                    PERPETUAL_MARKET_ORDER_URL + ORDER + DELIMETER + params + SIGNATURE + signature,
                    HttpMethod.POST, entity, OrderResponseDto.class).getBody();

            log.info("[TRADING BOT] Time: {} | Order-execution-service | createPartialTakeProfitOrder (Binance) | " +
                    "Partial TP order created | Safe TP Price: {} | Quantity: {} | Percent: {}% | Reason: {} | Mode: {}",
                    Timestamp.from(Instant.now()),
                    safeTpPrice,
                    String.format("%.8f", partialQuantity),
                    originalDto.getPartialClosePercent() * 100,
                    originalDto.getPartialCloseReason(),
                    originalDto.getTrailingMode());

        } catch (Exception e) {
            log.error("[TRADING BOT] Time: {} | Order-execution-service | createPartialTakeProfitOrder (Binance) | " +
                    "Failed to create partial TP order: {} | Reason: {}",
                    Timestamp.from(Instant.now()), e.getMessage(), originalDto.getPartialCloseReason());
        }
    }

    // Метод для получения текущей рыночной цены по символу
    private double getCurrentMarketPrice(String symbol) {
        try {
            String requestUrl = GENERAL_BINANCE_API + TICKER_PRICE_URL + "?symbol=" + symbol;
            String response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class).getBody();
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            return Double.parseDouble(responseMap.get("price").toString());
        } catch (Exception e) {
            log.error("[TRADING BOT] Time: {} | Order-execution-service | getCurrentMarketPrice (Binance) | " +
                    "Failed to fetch current market price: {} | Symbol: {}",
                    Timestamp.from(Instant.now()), e.getMessage(), symbol);
            return 0;
        }
    }

    @SneakyThrows
    public String deleteOrders(CloseOrdersRequestDto request) {
        StringBuilder builder = new StringBuilder();
        request.getOrigClientOrderIdList().forEach(clientOrder -> {
            request.setTimestamp("" + new Timestamp(System.currentTimeMillis()).getTime());
            String privateKey = encryptDecryptGenerator.decryptData(request.getPrivateKey());
            String params = queryParamsGenerator.generateDeleteParams(DeleteOrderDto.builder()
                    .origClientOrderId(clientOrder.getOrderId())
                    .timestamp(request.getTimestamp())
                    .symbol(clientOrder.getSymbol())
                    .build());
            String signature = SignatureGenerator.generateSignature(privateKey, params);
            HttpHeaders headers = this.addHttpHeaders(API_KEY_NAME, encryptDecryptGenerator.decryptData(request.getApiKey()));
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            String order = null;
            String url = GENERAL_BINANCE_API + DELETE_ORDERS + DELIMETER + params + SIGNATURE + signature;
            log.info("url for orders canceling: {}", url);
            try {
                order = restTemplate.exchange(
                        url, HttpMethod.DELETE, entity, String.class).getBody();
            } catch (Exception e) {
                log.info("[TRADING BOT] Time: {} | Order-execution-service | deleteOrders (Binance) | Failed order response: {}",
                        Timestamp.from(Instant.now()), e.getMessage());
            }
            log.info("orders were deleted: {}", order);
            builder.append(order);
        });
        return builder.toString();
    }

    @Override
    public String getOpenOrdersParams(String secretKey, String time) {
        TreeMap<String, String> parameters = new TreeMap<>();
        parameters.put(TIMESTAMP, time);
        String valueToDigest = this.getMessageToDigest(parameters);
        String signature = SignatureGenerator.generateSignature(secretKey, valueToDigest);
        return valueToDigest + "&signature=" + signature;
    }

    private String getRequestUrl(String openOrdersUrl, String params) {
        return GENERAL_BINANCE_API + openOrdersUrl + "?" + params;
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
