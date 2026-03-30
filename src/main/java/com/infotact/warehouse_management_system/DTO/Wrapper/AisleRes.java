package com.infotact.warehouse_management_system.DTO.Wrapper;

import com.infotact.warehouse_management_system.Enum.AisleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AisleRes {

    private long id;
    private AisleName name;
    private List<BinRes> bins;
}
