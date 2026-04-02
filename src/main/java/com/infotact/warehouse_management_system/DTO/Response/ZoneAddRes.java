package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZoneAddRes {

    private long zoneId;
    private ZoneName zoneName;
    private ZoneType zoneType;
    private boolean active;
    private long warehouseId;
}
