package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import lombok.Data;

@Data
public class WarehouseUpdateReq {

    private String name;

    private WarehouseLocation location;
}
