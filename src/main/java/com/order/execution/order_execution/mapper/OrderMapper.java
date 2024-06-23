package com.order.execution.order_execution.mapper;

import com.order.execution.order_execution.dto.CreateOrderRequestDto;
import com.order.execution.order_execution.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    Order toOrder(CreateOrderRequestDto dto);

}
