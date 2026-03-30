package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.WarehouseAddReq;
import com.infotact.warehouse_management_system.DTO.Response.WarehouseAddRes;
import com.infotact.warehouse_management_system.DTO.Response.WarehouseInfo;
import com.infotact.warehouse_management_system.DTO.Wrapper.AisleRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.BinRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.ZoneRes;
import com.infotact.warehouse_management_system.Exception.WarehouseExistsEx;
import com.infotact.warehouse_management_system.Exception.WarehouseNotFoundEx;
import com.infotact.warehouse_management_system.Model.Aisle;
import com.infotact.warehouse_management_system.Model.StorageBin;
import com.infotact.warehouse_management_system.Model.Warehouse;
import com.infotact.warehouse_management_system.Model.Zone;
import com.infotact.warehouse_management_system.Repository.WarehouseRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepo warehouseRepo;

    // Add warehouse
    @Transactional
    public WarehouseAddRes addWarehouse(WarehouseAddReq req){

        // Check warehouse already exists or not
        if(warehouseRepo.existsByNameAndLocation(req.getName(), req.getLocation())){
            throw new WarehouseExistsEx("Warehouse already exists with same name and location");
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setName(req.getName());
        warehouse.setLocation(req.getLocation());

        warehouse = warehouseRepo.save(warehouse);

        WarehouseAddRes response = new WarehouseAddRes(warehouse.getId(),
                warehouse.getName(),
                warehouse.getLocation());
        return response;
    }

    @Transactional
    public WarehouseInfo getWarehouseInfo(long id){

        Warehouse warehouse = warehouseRepo.findById(id).
                orElseThrow(()->new WarehouseNotFoundEx("Warehouse not found with id: "+id));

        List<ZoneRes> zoneList = new ArrayList<>();
        for(Zone zone : warehouse.getZones()){

            List<AisleRes> aisleList = new ArrayList<>();
            for(Aisle aisle : zone.getAisles()){

                List<BinRes> binList = new ArrayList<>();
                for(StorageBin bin : aisle.getBins()){

                    BinRes binRes = new BinRes(bin.getId(),
                            bin.getBinCode(), bin.getMaxCapacity(),
                            bin.getUsedCapacity());

                    binList.add(binRes);
                }
                AisleRes aisleRes = new AisleRes(
                        aisle.getId(), aisle.getName(),
                        binList);
                aisleList.add(aisleRes);
            }
            ZoneRes zoneRes = new ZoneRes(
                    zone.getId(), zone.getName(),
                    zone.getType(),aisleList);
            zoneList.add(zoneRes);
        }
        WarehouseInfo warehouseInfo = new WarehouseInfo(
                warehouse.getId(), warehouse.getName(),
                warehouse.getLocation(), zoneList);

        return warehouseInfo;
    }
}
