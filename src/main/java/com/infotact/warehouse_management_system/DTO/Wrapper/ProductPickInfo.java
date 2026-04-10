package com.infotact.warehouse_management_system.DTO.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPickInfo {

    private Long proId;
    private String proName;
    private int requiredQty;
    private List<BinInfo> bins;
}
