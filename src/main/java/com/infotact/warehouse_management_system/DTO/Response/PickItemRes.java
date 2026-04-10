package com.infotact.warehouse_management_system.DTO.Response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PickItemRes {

    // Order Info
    private Long orderId;
    private Long orderItemId;

    // Product Info
    private String productName;

    // Quantity Info
    private Integer requiredQty;   // total required in order
    private Integer pickedQty;     // picked in this scan (bin)
    private Integer remainingQty;  // still pending
}
