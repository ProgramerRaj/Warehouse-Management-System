package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemRes;
import com.infotact.warehouse_management_system.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderAddRes {

    private Long id;
    private OrderStatus status;
    private List<OrderItemRes> items;
    private Double orderTotalAmount;
}
