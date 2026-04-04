package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.ZoneAddReq;
import com.infotact.warehouse_management_system.DTO.Request.ZoneUpdateReq;
import com.infotact.warehouse_management_system.DTO.Response.ZoneAddRes;
import com.infotact.warehouse_management_system.DTO.Response.ZoneDeletedRes;
import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import com.infotact.warehouse_management_system.Exception.WarehouseNotFoundEx;
import com.infotact.warehouse_management_system.Exception.ZoneAlreadyExistsEx;
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
    public ZoneAddRes updateZoneById(long id, ZoneUpdateReq req){

        Zone zone = zoneRepo.findById(id)
                .orElseThrow(() -> new ZoneNotFoundEx("Zone not found with id: " + id));

        // already deleted
        if (!zone.isActive()) {
            throw new RuntimeException(
                    "Zone already deleted with id: " + id
            );
        }

        ZoneName newName = req.getName();
        ZoneType newType = req.getType();
        long warehouseId = zone.getWarehouse().getId();

        // nothing to update
        if (newName == null && newType == null) {
            throw new RuntimeException("Nothing to update");
        }

        // no changes
        if (
                (newName == null || newName.equals(zone.getName())) &&
                (newType == null || newType.equals(zone.getType()))
        ) {
            throw new RuntimeException("No changes found");
        }

        // duplicate check
        if(newName != null && newType != null){
           if(zoneRepo.existsByNameAndTypeAndWarehouseId(newName,newType,warehouseId)){
               throw new ZoneAlreadyExistsEx("zone already exists with name and type in this warehouse");
           }
           else if (zoneRepo.existsByNameAndWarehouseId(newName,warehouseId)) {
                throw new ZoneAlreadyExistsEx("zone already exists with name "+newName);
           }
           else if (zoneRepo.existsByTypeAndWarehouseId(newType,warehouseId)){
               throw new ZoneAlreadyExistsEx("zone already exists with type "+newType);
           }
        }
        else if (newName == null && zoneRepo.existsByTypeAndWarehouseId(newType,warehouseId)) {
            throw new ZoneAlreadyExistsEx("Zone already exists with type "+newType+
                    " in this warehouse with id: "+warehouseId);
        }
        else if (newType == null && zoneRepo.existsByNameAndWarehouseId(newName,warehouseId)) {
            throw new ZoneAlreadyExistsEx("Zone already exists with name "+newName+
                    " in this warehouse with id: "+warehouseId);
        }

        // update
        if (newName != null) {
            zone.setName(newName);
        }
        if (newType != null) {
            zone.setType(newType);
        }

        // save
        zoneRepo.save(zone);
        return new ZoneAddRes(
                zone.getId(),
                zone.getName(),
                zone.getType(),
                zone.isActive(),
                zone.getWarehouse().getId()
        );
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
