package com.infotact.warehouse_management_system.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BinAddReq {

    @NotBlank(message = "Storage Bin code must be required")
    private String binCode;

    @NotNull(message = "Storage bin capacity must be required")
    @Positive(message = "Storage bin capacity must be greater than 0")
    private int capacity;

    @NotNull(message = "Aisle id must be required")
    @Positive(message = "Aisle id must be greater than 0")
    private long aisleId;
}
