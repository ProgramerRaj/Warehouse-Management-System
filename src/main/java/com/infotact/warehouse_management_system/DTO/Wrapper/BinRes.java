package com.infotact.warehouse_management_system.DTO.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinRes {

    private long id;
    private String binCode;
    private int maxCapacity;
    private int usedCapacity;
    private boolean active;
}
