package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.ProductAddReq;
import com.infotact.warehouse_management_system.DTO.Response.ProResponse;
import com.infotact.warehouse_management_system.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    @Autowired
    ProductService productService;

    // Add product
    @PostMapping("/add-product")
    public ResponseEntity<ProResponse> addProduct(
            @RequestParam(required = true) long warehouseId,
            @RequestBody @Valid ProductAddReq request
    ) {
        return ResponseEntity.ok(productService.addProduct(warehouseId, request));
    }

    // Get product by id
    @GetMapping("/get/{id}")
    public ResponseEntity<ProResponse> getProduct(
            @PathVariable int id
    ){
        return ResponseEntity.ok(productService.getProduct(id));
    }
}
