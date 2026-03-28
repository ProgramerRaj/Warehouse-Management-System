package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.AisleAddReq;
import com.infotact.warehouse_management_system.DTO.Response.AisleAddRes;
import com.infotact.warehouse_management_system.Service.AisleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
