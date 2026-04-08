package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderFulfillRes {

    private Long orderId;
    private OrderStatus previousStatus;
    private OrderStatus currentStatus;
    private String message;
}
