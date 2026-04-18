package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.ProductAddReq;
import com.infotact.warehouse_management_system.DTO.Request.ProductReceiveReq;
import com.infotact.warehouse_management_system.DTO.Response.ProResponse;
import com.infotact.warehouse_management_system.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    @Autowired
    ProductService productService;

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-product")
    public ResponseEntity<ProResponse> addProduct(
            @RequestParam(required = true) long warehouseId,
            @RequestBody @Valid ProductAddReq request
    ) {
        return ResponseEntity.ok(productService.addProduct(warehouseId, request));
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/receive-product-quantity")
    public ResponseEntity<?> receiveProductQuantity(
            @RequestBody @Valid ProductReceiveReq req){
        return ResponseEntity.ok(productService.receiveProductQua(req));
    }

    // ADMIN + OPERATOR
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/product/get")
    public ResponseEntity<ProResponse> getProduct(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) String sku
    ){
        return ResponseEntity.ok(productService.getProduct(id,barcode,sku));
    }

    // ADMIN + OPERATOR
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/product/{id}/barcodeImg")
    public ResponseEntity<byte[]> getBarcodeImage(@PathVariable Long id){

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(productService.getBarcodeImage(id));
    }

    // ADMIN + OPERATOR
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/product/{id}/qrCodeImg")
    public ResponseEntity<byte[]> getQrCodeImage(@PathVariable Long id){

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(productService.getQrCodeImage(id));
    }

}
