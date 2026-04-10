package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.DTO.Wrapper.ProductPickInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPickingRes {

    private Long orderId;
    private List<ProductPickInfo> items;
}
