package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseUpdateReq {

    @NotBlank(message = "warehouse name required")
    private String name;

    @NotNull(message = "warehouse location required")
    private WarehouseLocation location;
}
