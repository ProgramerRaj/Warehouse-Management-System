package com.infotact.warehouse_management_system.DTO.Wrapper;

import com.infotact.warehouse_management_system.Enum.AisleName;
import com.infotact.warehouse_management_system.Enum.ZoneName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinInfo {

    private String warehouse;
    private ZoneName zone;
    private AisleName aisle;
    private Long binId;
    private int availableQty;
}
