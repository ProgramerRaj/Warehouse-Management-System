package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.ZoneAddReq;
import com.infotact.warehouse_management_system.DTO.Response.ZoneAddRes;
import com.infotact.warehouse_management_system.Repository.ZoneRepo;
import com.infotact.warehouse_management_system.Service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/warehouse/zones")
@RestController
@Validated
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @PostMapping("/add-zone")
    public ResponseEntity<ZoneAddRes> addZone(
            @RequestBody @Valid ZoneAddReq request){
        ZoneAddRes response = zoneService.addZone(request);
        return ResponseEntity.ok(response);
    }
}
