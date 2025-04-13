package com.order.execution.order_execution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloseOrdersRequestDto {

    private String exchange;

    private String apiKey;

    private String privateKey;

    private List<String> origClientOrderIdList;

    private String timestamp;

}
