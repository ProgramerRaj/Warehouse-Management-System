package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.ProRequest;
import com.infotact.warehouse_management_system.DTO.Response.ProResponse;
import com.infotact.warehouse_management_system.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@Validated
public class ProductController {

    @Autowired
    ProductService productService;

    // Add product
    @PostMapping("/add")
    public ResponseEntity<ProResponse> addProduct(
            @RequestBody @Valid ProRequest request
    ) {
        return ResponseEntity.ok(productService.addProduct(request));
    }
}
