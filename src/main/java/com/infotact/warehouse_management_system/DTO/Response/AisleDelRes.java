package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.AisleName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AisleDelRes {

    private long id;
    private AisleName name;
    private boolean active;
    private long zoneId;
    private String message;
}
