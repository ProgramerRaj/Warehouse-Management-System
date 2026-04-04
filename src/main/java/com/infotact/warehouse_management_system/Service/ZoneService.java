package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.ZoneAddReq;
import com.infotact.warehouse_management_system.DTO.Response.ZoneAddRes;
import com.infotact.warehouse_management_system.DTO.Response.ZoneDeletedRes;
import com.infotact.warehouse_management_system.Exception.WarehouseNotFoundEx;
import com.infotact.warehouse_management_system.Exception.ZoneNotFoundEx;
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

        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(()-> new WarehouseNotFoundEx("Warehouse not found with id: "+req.getWarehouseId()));

        if(zoneRepo.existsByTypeAndWarehouseId(req.getType(), req.getWarehouseId())){
            throw new RuntimeException(req.getType() + " zone type already exists in this warehouse");
        }

        if(zoneRepo.existsByNameAndWarehouseId(req.getName(), req.getWarehouseId())){
            throw new RuntimeException(req.getName() + " already exists in this warehouse");
        }

        Zone zone = new Zone();
        zone.setName(req.getName());
        zone.setType(req.getType());
        zone.setActive(true);
        zone.setWarehouse(warehouse);

        zone = zoneRepo.save(zone);

        return new ZoneAddRes(zone.getId(),
                zone.getName(),
                zone.getType(),
                zone.isActive(),
                zone.getWarehouse().getId());
    }

    @Transactional
    public ZoneAddRes getZoneById(long id){

        Zone zone = zoneRepo.findById(id).
                orElseThrow(()-> new ZoneNotFoundEx("Zone not found with id: "+id));

        return new ZoneAddRes(
                zone.getId(),
                zone.getName(),
                zone.getType(),
                zone.isActive(),
                zone.getWarehouse().getId()
        );
    }
    @Transactional
    public ZoneDeletedRes deleteZoneById(long id){
        Zone zone = zoneRepo.findById(id)
                .orElseThrow(()-> new ZoneNotFoundEx("Zone not found with id: "+id));

        if(!zone.isActive()){
            throw new RuntimeException("This zone already deleted with id: "+id);
        }

        zone.setActive(false);
        zoneRepo.save(zone);

        return new ZoneDeletedRes(
                zone.getId(),
                zone.getName(),
                zone.getType(),
                zone.isActive(),
                zone.getWarehouse().getId(),
                "Zone successfully deleted with id: "+id);
    }
    @Transactional
    public ZoneAddRes updateZoneById(long id, ZoneAddReq req){

        Zone zone = zoneRepo.findByIdAndWarehouseId(id, req.getWarehouseId())
                .orElseThrow(()-> new ZoneNotFoundEx("This zone with id: "+id+" is not exists in this warehouse with id: "+req.getWarehouseId()));
        if(!zone.isActive()){
            throw new RuntimeException("Zone already deleted with id: "+id+"\nSo you can't update it");
        }

        zone.setName(req.getName());
        zone.setType(req.getType());
        zone = zoneRepo.save(zone);

        //response
        return new ZoneAddRes(
                zone.getId(),
                zone.getName(),
                zone.getType(),
                zone.isActive(),
                zone.getWarehouse().getId());
    }
    @Transactional
    public String restoreZoneById(long id){
        Zone zone = zoneRepo.findById(id)
                .orElseThrow(()-> new ZoneNotFoundEx("Zone not found with id: "+id));

        if(zone.isActive()){
            throw new RuntimeException("This zone already restored with id: "+id);
        }

        zone.setActive(true);
        zoneRepo.save(zone);

        return "Zone successfully restored with id: "+id;
    }
}
