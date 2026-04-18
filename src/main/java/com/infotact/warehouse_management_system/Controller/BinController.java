package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.BinAddReq;
import com.infotact.warehouse_management_system.DTO.Request.BinUpdateReq;
import com.infotact.warehouse_management_system.DTO.Response.BinAddRes;
import com.infotact.warehouse_management_system.Service.StorageBinService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aisles/bins")
@Validated
public class BinController {

    @Autowired
    private StorageBinService storageBinService;

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-bin")
    public ResponseEntity<BinAddRes> addBin(
            @RequestBody @Valid BinAddReq request){
        BinAddRes response = storageBinService.addBin(request);

        return ResponseEntity.ok(response);
    }

    // ADMIN + OPERATOR
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/get-bin/by-id/{id}")
    public ResponseEntity<?> getBinById(@PathVariable long id){
        return ResponseEntity.ok(storageBinService.getBinById(id));
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-bin/by-id/{id}")
    public ResponseEntity<?> deleteBinById(@PathVariable long id){
        return ResponseEntity.ok(storageBinService.deleteBinById(id));
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-bin/by-id/{id}")
    public ResponseEntity<?> updateBinById(
            @PathVariable long id,
            @RequestBody @Valid BinUpdateReq req){
        return ResponseEntity.ok(storageBinService.updateBinById(id, req));
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restore-bin/by-id/{id}")
    public ResponseEntity<?> restoreBinById(
            @PathVariable long id){
        return ResponseEntity.ok(storageBinService.restoreBinById(id));
    }
}
