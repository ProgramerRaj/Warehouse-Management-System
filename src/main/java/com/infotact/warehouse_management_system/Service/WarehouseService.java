package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.UpdateWarehouseReq;
import com.infotact.warehouse_management_system.DTO.Request.WarehouseAddReq;
import com.infotact.warehouse_management_system.DTO.Response.*;
import com.infotact.warehouse_management_system.DTO.Wrapper.AisleRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.BinRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.WarehouseRes;
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
        warehouse.setActive(true);

        warehouse = warehouseRepo.save(warehouse);

        WarehouseAddRes response = new WarehouseAddRes(warehouse.getId(),
                warehouse.getName(),
                warehouse.getLocation(),
                warehouse.isActive());
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
                    zone.getType(), zone.isActive(), aisleList);
            zoneList.add(zoneRes);
        }
        WarehouseInfo warehouseInfo = new WarehouseInfo(
                warehouse.getId(), warehouse.getName(),
                warehouse.getLocation(),
                warehouse.isActive(),
                zoneList);

        return warehouseInfo;
    }

    @Transactional
    public WarehouseGetRes getAllWarehouse(){

        // get all warehouse
        List<Warehouse> warehouses = warehouseRepo.findAll();
        if(warehouses.isEmpty()){
            throw new WarehouseNotFoundEx("No any warehouse found");
        }

        List<WarehouseRes> warehouseResList = new ArrayList<>();
        for(Warehouse w : warehouses){
            WarehouseRes warehouseRes = new WarehouseRes();
            warehouseRes.setId(w.getId());
            warehouseRes.setName(w.getName());
            warehouseRes.setLocation(w.getLocation());
            warehouseRes.setActive(w.isActive());

            int totalAisles = 0;
            int totalBins = 0;
            for(Zone z : w.getZones()){
                totalAisles += z.getAisles().size();

                for(Aisle a : z.getAisles()){
                    totalBins += a.getBins().size();
                }
            }
            warehouseRes.setTotalZones(w.getZones().size());
            warehouseRes.setTotalAisles(totalAisles);
            warehouseRes.setTotalBins(totalBins);

            warehouseResList.add(warehouseRes);
        }

        // response
        return new WarehouseGetRes(warehouseResList.size(),warehouseResList);
    }
    @Transactional
    public WarehouseGetByIdRes getWarehouseById(long id){

        Warehouse warehouse = warehouseRepo.findById(id).
                orElseThrow(()->new WarehouseNotFoundEx("Warehouse not found with id: "+id));

        int totalZones = 0;
        int totalAisles = 0;
        int totalBins = 0;

        for(Zone zone : warehouse.getZones()){
            totalAisles += zone.getAisles().size();

            for(Aisle aisle : zone.getAisles()){
                totalBins += aisle.getBins().size();
            }
        }
        totalZones = warehouse.getZones().size();

        WarehouseGetByIdRes response = new WarehouseGetByIdRes();
        response.setId(warehouse.getId());
        response.setName(warehouse.getName());
        response.setLocation(warehouse.getLocation());
        response.setActive(warehouse.isActive());
        response.setTotalZones(totalZones);
        response.setTotalAisles(totalAisles);
        response.setTotalBins(totalBins);

        return response;
    }
    @Transactional
    public WarehouseAddRes updateWarehouse(long warehouseId, UpdateWarehouseReq req){

        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(()-> new WarehouseNotFoundEx("Warehouse not found with id: "+warehouseId));

        if(!warehouse.isActive()){
            throw new RuntimeException("Warehouse already deleted with id: "+warehouseId +"\nSo you can't update in it");
        }

        // update fields
        warehouse.setName(req.getName());
        warehouse.setLocation(req.getLocation());
        warehouseRepo.save(warehouse);

        // response
        return new WarehouseAddRes(warehouse.getId(),
                warehouse.getName(), warehouse.getLocation(), warehouse.isActive());
    }

    @Transactional
    public WarehouseDeletedRes deleteWarehouseById(long id){
        Warehouse warehouse = warehouseRepo.findById(id).
                orElseThrow(()-> new WarehouseNotFoundEx("Warehouse not found with id: "+id));
        if(!warehouse.isActive()){
            throw new RuntimeException("This warehouse already deleted");
        }
        warehouse.setActive(false);
        warehouseRepo.save(warehouse);

        WarehouseDeletedRes response = new WarehouseDeletedRes();
        response.setId(warehouse.getId());
        response.setName(warehouse.getName());
        response.setLocation(warehouse.getLocation());
        response.setActive(warehouse.isActive());
        response.setMessage("Warehouse deleted successfully with id: " + id);

        return response;
    }
}
