package com.infotact.warehouse_management_system.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AisleAddReq {

    @NotBlank(message = "Aisle name must be required")
    private String name;

    @NotNull(message = "Zone id must be required")
    @Positive(message = "Zone id must be greater than 0")
    private long zoneId;
}
