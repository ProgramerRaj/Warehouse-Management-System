package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.WarehouseAddReq;
import com.infotact.warehouse_management_system.Exception.WarehouseExistsEx;
import com.infotact.warehouse_management_system.Model.Warehouse;
import com.infotact.warehouse_management_system.Repository.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepo warehouseRepo;

    // Add warehouse
    public String addWarehouse(WarehouseAddReq req){

        // Check warehouse already exists or not
        if(warehouseRepo.existsByNameAndLocation(req.getName(), req.getLocation())){
            throw new WarehouseExistsEx("Warehouse already exists with same name and location");
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setName(req.getName());
        warehouse.setLocation(req.getLocation());

        warehouseRepo.save(warehouse);

        return req.getName()+" warehouse added successfully at location "+req.getLocation();
    }
}
