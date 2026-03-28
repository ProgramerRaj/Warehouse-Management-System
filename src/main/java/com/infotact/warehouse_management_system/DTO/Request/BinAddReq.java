package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.BinCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BinAddReq {

    @NotNull(message = "Storage Bin code must be required")
    private BinCode binCode;

    @NotNull(message = "Storage bin capacity must be required")
    @Positive(message = "Storage bin capacity must be greater than 0")
    private int maxCapacity;

    @NotNull(message = "Aisle id must be required")
    @Positive(message = "Aisle id must be greater than 0")
    private long aisleId;
}
