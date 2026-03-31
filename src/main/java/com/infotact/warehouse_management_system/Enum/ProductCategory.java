package com.infotact.warehouse_management_system.Enum;

import java.util.Locale;

public enum ProductCategory {
    CLOTHING(ZoneType.NORMAL),
    ELECTRONICS(ZoneType.HIGH_VALUE),
    GROCERY(ZoneType.COLD),
    FOOTWEAR(ZoneType.BULK),
    ACCESSORIES(ZoneType.FRAGILE);

    private final ZoneType zoneType;

    ProductCategory(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public ZoneType getZoneType(){
        return zoneType;
    }
}
