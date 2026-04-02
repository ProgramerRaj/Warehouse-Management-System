package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.AisleAddReq;
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
    public ResponseEntity<?> getAisleById(long id){

    }
}
