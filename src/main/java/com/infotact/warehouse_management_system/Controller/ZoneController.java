package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.ZoneAddReq;
import com.infotact.warehouse_management_system.DTO.Response.ZoneAddRes;
import com.infotact.warehouse_management_system.DTO.Response.ZoneDeletedRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.ZoneRes;
import com.infotact.warehouse_management_system.Repository.ZoneRepo;
import com.infotact.warehouse_management_system.Service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/get-by/{id}")
    public ResponseEntity<?> getZoneById(@PathVariable long id){
        ZoneAddRes res = zoneService.getZoneById(id);
        return ResponseEntity.ok(res);
    }
    @DeleteMapping("/delete/by-id/{id}")
    public ResponseEntity<?> deleteZoneById(@PathVariable long id){
        ZoneDeletedRes res = zoneService.deleteZoneById(id);
        return ResponseEntity.ok(res);
    }
    @PutMapping("/update/by-id/{id}")
    public ResponseEntity<?> updateZoneById(
            @PathVariable long id,
            @RequestBody @Valid ZoneAddReq req){
        ZoneAddRes res = zoneService.updateZoneById(id, req);
        return ResponseEntity.ok(res);
    }
}
