package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.WarehouseUpdateReq;
import com.infotact.warehouse_management_system.DTO.Request.WarehouseAddReq;
import com.infotact.warehouse_management_system.DTO.Response.*;
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
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllWarehouse(){
        WarehouseGetRes res = warehouseService.getAllWarehouse();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/get/warehouse/by-id/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable Long id){

        WarehouseGetByIdRes res = warehouseService.getWarehouseById(id);
            return ResponseEntity.ok(res);
    }
    @PutMapping("/update/warehouse/by-id/{id}")
    public ResponseEntity<?> updateWarehouse(
            @PathVariable Long id,
            @RequestBody @Valid WarehouseUpdateReq req){

        WarehouseAddRes res = warehouseService.updateWarehouse(id,req);
        return ResponseEntity.ok(res);
    }
    @DeleteMapping("/delete/warehouse/by-id/{id}")
    public ResponseEntity<?> deleteWarehouseById(Long id){
        WarehouseDeletedRes res = warehouseService.deleteWarehouseById(id);
        return ResponseEntity.ok(res);
    }
}
