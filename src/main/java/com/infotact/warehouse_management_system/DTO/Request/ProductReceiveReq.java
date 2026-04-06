package com.infotact.warehouse_management_system.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductReceiveReq {

    @NotNull(message = "Warehouse id must be required")
    private Long warehouseId;

    @NotNull(message = "Product id must be required")
    private Long proId;

    @Positive(message = "Product quantity must be greater than 0")
    @NotNull(message = "Product quantity must be required")
    private Integer quantity;
}
