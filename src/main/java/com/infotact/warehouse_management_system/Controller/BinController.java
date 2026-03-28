package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.BinAddReq;
import com.infotact.warehouse_management_system.DTO.Response.BinAddRes;
import com.infotact.warehouse_management_system.Service.StorageBinService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
