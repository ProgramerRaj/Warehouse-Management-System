package com.infotact.warehouse_management_system.DTO.Wrapper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemRes {

    private Long proId;
    private Integer quantity;
    private double price;
    private double total;
}
