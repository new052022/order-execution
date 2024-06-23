package com.order.execution.order_execution.util.binance;

import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

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

}
