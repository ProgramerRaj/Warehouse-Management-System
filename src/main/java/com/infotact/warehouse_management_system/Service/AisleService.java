package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.AisleAddReq;
import com.infotact.warehouse_management_system.DTO.Request.AisleUpdateReq;
import com.infotact.warehouse_management_system.DTO.Response.AisleAddRes;
import com.infotact.warehouse_management_system.DTO.Response.AisleDelRes;
import com.infotact.warehouse_management_system.Enum.AisleName;
import com.infotact.warehouse_management_system.Exception.AisleAlreadyExistsEx;
import com.infotact.warehouse_management_system.Exception.AisleNotFoundEx;
import com.infotact.warehouse_management_system.Exception.ZoneNotFoundEx;
import com.infotact.warehouse_management_system.Model.Aisle;
import com.infotact.warehouse_management_system.Model.Zone;
import com.infotact.warehouse_management_system.Repository.AisleRepo;
import com.infotact.warehouse_management_system.Repository.ZoneRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AisleService {

    @Autowired
    private AisleRepo aisleRepo;

    @Autowired
    private ZoneRepo zoneRepo;

    @Transactional
    public AisleAddRes addAisle(AisleAddReq req){

        Zone zone = zoneRepo.findById(req.getZoneId()).
                orElseThrow(()->new ZoneNotFoundEx("Zone not found with id: "+req.getZoneId()));

        if(aisleRepo.existsByNameAndZoneId(req.getName(), req.getZoneId())){
                throw new RuntimeException(req.getName()+ "aisle already exists in zone "+zone.getName());
        }

        Aisle aisle = new Aisle();
        aisle.setName(req.getName());
        aisle.setActive(true);
        aisle.setZone(zone);
        aisle = aisleRepo.save(aisle);

        return new AisleAddRes(aisle.getId(),
                aisle.getName(),
                aisle.isActive(),
                aisle.getZone().getId());
    }
    @Transactional
    public AisleAddRes getAisleById(long id){
        Aisle aisle = aisleRepo.findById(id)
                .orElseThrow(()-> new AisleNotFoundEx("Aisle not found with id: "+id));

        return new AisleAddRes(
                aisle.getId(),
                aisle.getName(),
                aisle.isActive(),
                aisle.getZone().getId()
        );
    }
    @Transactional
    public AisleDelRes deleteAisleById(long id){
        Aisle aisle = aisleRepo.findById(id)
                .orElseThrow(()-> new AisleNotFoundEx("Aisle not found with id: "+id));
        if(!aisle.isActive()){
            throw new RuntimeException("This Aisle already deleted with id: "+id);
        }

        aisle.setActive(false);
        aisleRepo.save(aisle);

        //response
        return new AisleDelRes(
                aisle.getId(),
                aisle.getName(),
                aisle.isActive(),
                aisle.getZone().getId(),
                "Aisle successfully deleted with id: "+id
        );
    }
    @Transactional
    public AisleAddRes updateAisleById(long id, AisleUpdateReq req){

        Aisle aisle = aisleRepo.findById(id)
                .orElseThrow(()-> new AisleNotFoundEx("Aisle not found with id: "+id));

        if(!aisle.isActive()){
            throw new RuntimeException("Aisle already deleted with id: "+id+"\nSo you can't update it now");
        }

        AisleName newName = req.getName();
        long zoneId = aisle.getZone().getId();

        // new name same as old name
        if(newName == null || newName.equals(aisle.getName())){
            throw new RuntimeException("No changes found");
        }
        // new name exists in own zone
        if(aisleRepo.existsByNameAndZoneId(newName,zoneId)){
            throw new AisleAlreadyExistsEx("Aisle already exists with name "+newName+
                    " in this zone with id: "+zoneId);
        }

        aisle.setName(req.getName());
        aisleRepo.save(aisle);

        //response
        return new AisleAddRes(
                aisle.getId(),
                aisle.getName(),
                aisle.isActive(),
                aisle.getZone().getId());
    }
    @Transactional
    public String restoreAisleById(long id){
        Aisle aisle = aisleRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("This aisle not found with id: "+id));
        if(aisle.isActive()){
           throw new RuntimeException("Aisle already restored with id: "+id);
        }

        aisle.setActive(true);
        aisleRepo.save(aisle);

        return "Aisle successfully restored with id: "+id;
    }
}
