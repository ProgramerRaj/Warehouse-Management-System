package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.WarehouseAddReq;
import com.infotact.warehouse_management_system.DTO.Response.WarehouseAddRes;
import com.infotact.warehouse_management_system.DTO.Response.WarehouseInfo;
import com.infotact.warehouse_management_system.Service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouses")
@Validated
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping("/add-warehouse")
    public ResponseEntity<WarehouseAddRes> addWarehouse(
            @RequestBody @Valid WarehouseAddReq req){
        WarehouseAddRes response = warehouseService.addWarehouse(req);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/warehouse-info/{id}")
    public ResponseEntity<WarehouseInfo> getWarehouseInfo(
            @PathVariable long id){
        WarehouseInfo response = warehouseService.getWarehouseInfo(id);
        return ResponseEntity.ok(response);
    }
}
