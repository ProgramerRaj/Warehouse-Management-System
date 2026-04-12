package com.infotact.warehouse_management_system.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PickRequest {

    @NotBlank(message = "Please scan product barcode")
    private String barcode;

    @NotNull(message = "Please provide bin ID")
    @Positive(message = "Please provide valid bin ID")
    private Integer binId;

    @NotNull(message = "Picked quantity must be required")
    @Positive(message = "Please enter valid quantity")
    private Integer pickedQty;
}
