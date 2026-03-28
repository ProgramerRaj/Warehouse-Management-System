package com.infotact.warehouse_management_system.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BinAddRes {

    private long binId;
    private String binCode;
    private int maxCapacity;
    private int usedCapacity;
    private long aisleId;
}
