package com.infotact.warehouse_management_system.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZoneAddRes {

    private long zoneId;
    private String zoneName;
    private String zoneType;
    private long warehouseId;
}
