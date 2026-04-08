package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderFulfillReq {

    @NotNull(message = "Order status is required")
    private OrderStatus status;
}
