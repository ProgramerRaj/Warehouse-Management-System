package com.infotact.warehouse_management_system.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BinUpdateReq {

    @NotNull(message = "Bin maxCapacity must be required")
    @Positive(message = "Bin maxCapacity must be greater than 0")
    private int maxCapacity;

    @NotNull(message = "Aisle id must be required")
    private long aisleId;
}
