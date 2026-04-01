package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.DTO.Wrapper.WarehouseRes;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WarehouseGetRes {

    private int totalWarehouse;
    private List<WarehouseRes> warehouseList;
}
