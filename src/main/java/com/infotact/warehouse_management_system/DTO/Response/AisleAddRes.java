package com.infotact.warehouse_management_system.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AisleAddRes {

    private long aisleId;
    private String aisleName;
    private long zoneId;
}
