package com.infotact.warehouse_management_system.DTO.Request;

import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import lombok.Data;

@Data
public class ZoneUpdateReq {

    private ZoneName name;

    private ZoneType type;
}
