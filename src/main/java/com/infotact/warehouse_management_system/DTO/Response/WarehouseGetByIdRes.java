package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import lombok.Data;

@Data
public class WarehouseGetByIdRes {

    private long id;
    private String name;
    private WarehouseLocation location;
    private int totalZones;
    private int totalAisles;
    private int totalBins;
}
