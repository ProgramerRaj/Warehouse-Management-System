package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.WarehouseAddReq;
import com.infotact.warehouse_management_system.Service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/warehouses")
@Validated
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping("/add-warehouse")
    public ResponseEntity<String> addWarehouse(
            @RequestBody @Valid WarehouseAddReq req){
        String message = warehouseService.addWarehouse(req);

        return ResponseEntity.ok(message);
    }
}
