package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WarehouseAddRes {

    private long warehouseId;
    private String warehouseName;
    private WarehouseLocation location;
}
