package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.AisleName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AisleUpdateReq {

    @NotNull(message = "Aisle name must be required")
    private AisleName name;

    @NotNull(message = "Zone id must be required")
    private long zoneId;
}
