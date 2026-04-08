package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.DTO.Request.OrderAddReq;
import com.infotact.warehouse_management_system.DTO.Request.OrderFulfillReq;
import com.infotact.warehouse_management_system.DTO.Response.OrderAddRes;
import com.infotact.warehouse_management_system.DTO.Response.OrderFulfillRes;
import com.infotact.warehouse_management_system.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    //create order
    @PostMapping("/order-create")
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderAddReq req)
    {
       OrderAddRes res = orderService.createOrder(req);
       return ResponseEntity.ok(res);
    }

    // update order status in single API
    @PutMapping("/fulfill/by-id/{orderId}")
    public ResponseEntity<OrderFulfillRes> fulfillOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderFulfillReq req){

        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, req)
        );
    }
}
