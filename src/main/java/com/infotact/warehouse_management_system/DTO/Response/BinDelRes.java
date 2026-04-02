package com.infotact.warehouse_management_system.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BinDelRes {

    private long id;
    private String binCode;
    private int maxCapacity;
    private int usedCapacity;
    private boolean active;
    private long aisleId;
}
