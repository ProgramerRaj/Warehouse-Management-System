package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.DTO.Wrapper.ZoneRes;
import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseInfo {

    private long id;
    private String name;
    private WarehouseLocation location;
    private boolean active;
    private List<ZoneRes> zones;
}
