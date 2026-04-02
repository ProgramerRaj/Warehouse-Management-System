package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.AisleAddReq;
import com.infotact.warehouse_management_system.DTO.Response.AisleAddRes;
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
}
