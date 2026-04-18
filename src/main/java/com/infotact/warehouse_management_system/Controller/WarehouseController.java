package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.WarehouseUpdateReq;
import com.infotact.warehouse_management_system.DTO.Request.WarehouseAddReq;
import com.infotact.warehouse_management_system.DTO.Response.*;
import com.infotact.warehouse_management_system.Service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouses")
@Validated
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-warehouse")
    public ResponseEntity<WarehouseAddRes> addWarehouse(
            @RequestBody @Valid WarehouseAddReq req){
        WarehouseAddRes response = warehouseService.addWarehouse(req);

        return ResponseEntity.ok(response);
    }

    // ADMIN + OPERATOR
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/warehouse-info/{id}")
    public ResponseEntity<WarehouseInfo> getWarehouseInfo(
            @PathVariable long id){
        WarehouseInfo response = warehouseService.getWarehouseInfo(id);
        return ResponseEntity.ok(response);
    }

    // ADMIN + OPERATOR
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllWarehouse(){
        WarehouseGetRes res = warehouseService.getAllWarehouse();
        return ResponseEntity.ok(res);
    }

    // ADMIN + OPERATOR
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/get/warehouse/by-id/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable Long id){

        WarehouseGetByIdRes res = warehouseService.getWarehouseById(id);
            return ResponseEntity.ok(res);
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/warehouse/by-id/{id}")
    public ResponseEntity<?> updateWarehouse(
            @PathVariable Long id,
            @RequestBody @Valid WarehouseUpdateReq req){

        WarehouseAddRes res = warehouseService.updateWarehouse(id,req);
        return ResponseEntity.ok(res);
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/warehouse/by-id/{id}")
    public ResponseEntity<?> deleteWarehouseById(@PathVariable Long id){
        WarehouseDeletedRes res = warehouseService.deleteWarehouseById(id);
        return ResponseEntity.ok(res);
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restore/warehouse/by-id/{id}")
    public ResponseEntity<?> restoreWarehouseById(@PathVariable Long id){
        return ResponseEntity.ok(warehouseService.restoreWarehouseById(id));
    }
}
