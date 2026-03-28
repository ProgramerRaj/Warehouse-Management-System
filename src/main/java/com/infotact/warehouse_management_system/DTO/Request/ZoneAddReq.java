package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ZoneAddReq {

    @NotNull(message = "Zone name must be required")
    private ZoneName name;

    @NotNull(message = "Zone type must be required")
    private ZoneType type;

    @NotNull(message = "Warehouse id must be required")
    @Positive(message = "Warehouse id must be greater than 0")
    private long warehouseId;
}
