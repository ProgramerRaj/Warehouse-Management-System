package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseAddReq {

    @NotBlank(message = "Warehouse name must be required")
    private String name;

    @NotNull(message = "Warehouse location must be required")
    private WarehouseLocation location;
}
