package com.infotact.warehouse_management_system.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehouseAddReq {

    @NotBlank(message = "Warehouse name must be required")
    private String name;

    @NotBlank(message = "Warehouse location must be required")
    private String location;
}
