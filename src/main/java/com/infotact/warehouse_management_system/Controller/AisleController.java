package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.AisleAddReq;
import com.infotact.warehouse_management_system.DTO.Request.AisleUpdateReq;
import com.infotact.warehouse_management_system.DTO.Response.AisleAddRes;
import com.infotact.warehouse_management_system.Service.AisleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zone/aisles")
public class AisleController {

    @Autowired
    private AisleService aisleService;

    @PostMapping("/aisle-add")
    public ResponseEntity<AisleAddRes> addAisle(
            @RequestBody @Valid AisleAddReq request
    ){
        AisleAddRes response = aisleService.addAisle(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-aisle/by-id/{id}")
    public ResponseEntity<?> getAisleById(@PathVariable long id){
        return ResponseEntity.ok(aisleService.getAisleById(id));
    }
    @PutMapping("/update-aisle/by-id/{id}")
    public ResponseEntity<?> updateAisleById(
            @PathVariable long id,
            @RequestBody @Valid AisleUpdateReq req){
        return ResponseEntity.ok(aisleService.updateAisleById(id, req));
    }
    @DeleteMapping("/delete-aisle/by-id/{id}")
    public ResponseEntity<?> deleteAisleById(@PathVariable long id){
        return ResponseEntity.ok(aisleService.deleteAisleById(id));
    }
    @PutMapping("/restore-aisle/by-id/{id}")
    public ResponseEntity<?> restoreAisleById(@PathVariable long id){
        return ResponseEntity.ok(aisleService.restoreAisleById(id));
    }
}
