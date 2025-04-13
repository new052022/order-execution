package com.order.execution.order_execution.util.binance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.execution.order_execution.dto.CloseOrdersRequestDto;
import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.dto.DeleteOrderDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class QueryParamsGenerator {
    public String generatePerpetualParams(CreateOrderRequestDto dto) {

        StringBuilder params = new StringBuilder();
        try {

            Field[] fields = CreateOrderRequestDto.class.getDeclaredFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName));
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!fieldName.equals("userId") && !fieldName.equals("exchange") && !fieldName.equals("apiKey") && !fieldName.equals("privateKey")) {
                    String fieldValue = (String) field.get(dto);
                    if (fieldValue != null && !fieldValue.isEmpty()) {
                        if (!params.isEmpty()) {
                            params.append("&");
                        }
                        params.append(fieldName).append("=").append(fieldValue);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return params.toString();
    }

    public String generateDeleteParams(DeleteOrderDto dto) {
        StringBuilder params = new StringBuilder();
        try {

            Field[] fields = DeleteOrderDto.class.getDeclaredFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName));
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!fieldName.equals("userId") && !fieldName.equals("exchange") && !fieldName.equals("apiKey") && !fieldName.equals("privateKey")) {
                    String fieldValue = (String) field.get(dto);
                    if (fieldValue != null && !fieldValue.isEmpty()) {
                        if (!params.isEmpty()) {
                            params.append("&");
                        }
                        params.append(fieldName).append("=").append(fieldValue);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return params.toString();
    }

    @SneakyThrows
    public String generatePerpetualParams(CloseOrdersRequestDto dto) {
        StringBuilder params = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper(); // Для сериализации списка в JSON

        try {
            Field[] fields = CloseOrdersRequestDto.class.getDeclaredFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName)); // Сортируем поля по имени
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                // Пропускаем ненужные поля
                if (!fieldName.equals("userId") && !fieldName.equals("exchange") &&
                        !fieldName.equals("apiKey") && !fieldName.equals("privateKey")) {

                    Object fieldValue = field.get(dto); // Получаем значение поля

                    if (fieldValue != null) {
                        if (!params.isEmpty()) {
                            params.append("&");
                        }

                        // Особая обработка для origClientOrderIdList
                        if ("origClientOrderIdList".equals(fieldName)) {
                            List<String> orderIds = (List<String>) fieldValue;
                            if (!orderIds.isEmpty()) {
                                // 1. Сериализуем список в JSON
                                String jsonOrderIds = objectMapper.writeValueAsString(orderIds);

                                // 2. Убираем все кавычки
                                jsonOrderIds = jsonOrderIds.replace("\"", "");

                                // Добавляем параметр в строку запроса
                                params.append(fieldName).append("=").append(jsonOrderIds);
                            }
                        } else {
                            // Обычная обработка для других полей
                            params.append(fieldName).append("=").append(fieldValue);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | JsonProcessingException e) {
            e.printStackTrace();
        }

        return params.toString();
    }

}
