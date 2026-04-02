package com.infotact.warehouse_management_system.DTO.Wrapper;

import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRes {

    private long id;
    private ZoneName name;
    private ZoneType type;
    private boolean active;
    private List<AisleRes> aisles;
}
