package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.AisleName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AisleAddRes {

    private long aisleId;
    private AisleName aisleName;
    private boolean active;
    private long zoneId;
}
