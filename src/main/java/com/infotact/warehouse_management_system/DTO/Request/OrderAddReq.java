package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemReq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderAddReq {

    @NotNull(message = "Warehouse id must be required")
    private Long warehouseId;

    @NotEmpty(message = "Order items can't be empty")
    @Valid
    private List<OrderItemReq> orderItems;
}
