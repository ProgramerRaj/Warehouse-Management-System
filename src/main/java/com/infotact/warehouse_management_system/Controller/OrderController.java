package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.OrderAddReq;
import com.infotact.warehouse_management_system.DTO.Request.PickRequest;
import com.infotact.warehouse_management_system.DTO.Response.OrderAddRes;
import com.infotact.warehouse_management_system.DTO.Response.OrderPickingRes;
import com.infotact.warehouse_management_system.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/order/create")
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderAddReq req)
    {
       OrderAddRes res = orderService.createOrder(req);
       return ResponseEntity.ok(res);
    }

    // OPERATOR + ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/order/{orderId}/picking")
    public ResponseEntity<OrderPickingRes> orderPicking(
            @PathVariable Long orderId){

        return ResponseEntity.ok(
                orderService.orderPicking(orderId)
        );
    }

    // OPERATOR + ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @PutMapping("/order/{orderId}/pick")
    public ResponseEntity<?> orderPicked(
            @PathVariable Long orderId,
            @RequestBody @Valid PickRequest req){

        return ResponseEntity.ok(
                orderService.orderPicked(orderId, req));
    }

    // OPERATOR + ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @PutMapping("/order/{orderId}/pack")
    public ResponseEntity<?> orderPacked(
            @PathVariable Long orderId){

        return ResponseEntity.ok(
                orderService.orderPacked(orderId)
        );
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/order/{orderId}/ship")
    public ResponseEntity<?> orderShip(
            @PathVariable Long orderId){

        return ResponseEntity.ok(orderService.orderShipped(orderId));
    }
}
