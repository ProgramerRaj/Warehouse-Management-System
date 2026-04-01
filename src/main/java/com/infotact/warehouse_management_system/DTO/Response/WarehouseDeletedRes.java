package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import lombok.Data;

@Data
public class WarehouseDeletedRes {

    private long id;
    private String name;
    private WarehouseLocation location;
    private boolean active;
    private String message;
}
