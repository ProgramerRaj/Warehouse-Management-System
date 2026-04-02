package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZoneDeletedRes {

    private long id;
    private ZoneName name;
    private ZoneType type;
    private boolean active;
    private long warehouseId;
    private String message;
}
