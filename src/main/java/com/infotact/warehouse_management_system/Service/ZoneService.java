package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.ZoneAddReq;
import com.infotact.warehouse_management_system.DTO.Response.ZoneAddRes;
import com.infotact.warehouse_management_system.Exception.WarehouseNotFoundEx;
import com.infotact.warehouse_management_system.Model.Warehouse;
import com.infotact.warehouse_management_system.Model.Zone;
import com.infotact.warehouse_management_system.Repository.WarehouseRepo;
import com.infotact.warehouse_management_system.Repository.ZoneRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepo zoneRepo;

    @Autowired
    private WarehouseRepo warehouseRepo;

    // Add zone of own warehouse
    @Transactional
    public ZoneAddRes addZone(ZoneAddReq req){

        // Check warehouse exists or not
        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(()-> new WarehouseNotFoundEx("Warehouse not found with id: "+req.getWarehouseId()));

        // Check zone type already exists or not
        if(zoneRepo.existsByTypeAndWarehouseId(req.getType(), req.getWarehouseId())){
            throw new RuntimeException(req.getType() + " zone type already exists in this warehouse");
        }

        // Check zone name already exists or not
        if(zoneRepo.existsByNameAndWarehouseId(req.getName(), req.getWarehouseId())){
            throw new RuntimeException(req.getName() + " already exists in this warehouse");
        }

        Zone zone = new Zone();
        zone.setName(req.getName());
        zone.setType(req.getType());
        zone.setWarehouse(warehouse);
        zone = zoneRepo.save(zone);

        return new ZoneAddRes(zone.getId(), zone.getName(), zone.getType(), zone.getWarehouse().getId());
    }
}
