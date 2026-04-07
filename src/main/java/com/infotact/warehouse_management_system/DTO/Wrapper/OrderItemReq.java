package com.infotact.warehouse_management_system.DTO.Wrapper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemReq {

    @NotNull(message = "Product id must be required")
    private Long proId;

    @NotNull(message = "Product quantity must be required")
    @Positive(message = "Product quantity must be greater than 0")
    private Integer quantity;
}
