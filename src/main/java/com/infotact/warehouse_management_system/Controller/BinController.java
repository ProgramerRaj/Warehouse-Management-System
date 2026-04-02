package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.BinAddReq;
import com.infotact.warehouse_management_system.DTO.Request.BinUpdateReq;
import com.infotact.warehouse_management_system.DTO.Response.BinAddRes;
import com.infotact.warehouse_management_system.Service.StorageBinService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aisles/bins")
@Validated
public class BinController {

    @Autowired
    private StorageBinService storageBinService;

    @PostMapping("/add-bin")
    public ResponseEntity<BinAddRes> addBin(
            @RequestBody @Valid BinAddReq request){
        BinAddRes response = storageBinService.addBin(request);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-bin/by-id/{id}")
    public ResponseEntity<?> getBinById(@PathVariable long id){
        return ResponseEntity.ok(storageBinService.getBinById(id));
    }
    @DeleteMapping("/delete-bin/by-id/{id}")
    public ResponseEntity<?> deleteBinById(@PathVariable long id){
        return ResponseEntity.ok(storageBinService.deleteBinById(id));
    }
    @PutMapping("/update-bin/by-id/{id}")
    public ResponseEntity<?> updateBinById(
            @PathVariable long id,
            @RequestBody @Valid BinUpdateReq req){
        return ResponseEntity.ok(storageBinService.updateBinById(id, req));
    }
}
