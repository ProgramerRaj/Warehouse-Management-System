package com.infotact.warehouse_management_system.DTO.Wrapper;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import lombok.Data;

@Data
public class WarehouseRes {
    private long id;
    private String name;
    private WarehouseLocation location;
    private int totalZones;
    private int totalAisles;
    private int totalBins;
}
