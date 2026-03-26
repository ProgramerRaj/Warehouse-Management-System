package com.infotact.warehouse_management_system.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ZoneAddReq {

    @NotBlank(message = "Zone name must be required")
    private String name;

    @NotBlank(message = "Zone type must be required")
    private String type;

    @NotNull(message = "Warehouse id must be required")
    @Positive(message = "Warehouse id must be greater than 0")
    private long warehouseId;
}
