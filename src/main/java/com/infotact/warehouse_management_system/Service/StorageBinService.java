package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.BinAddReq;
import com.infotact.warehouse_management_system.DTO.Response.BinAddRes;
import com.infotact.warehouse_management_system.Enum.BinCode;
import com.infotact.warehouse_management_system.Exception.AisleNotFoundEx;
import com.infotact.warehouse_management_system.Exception.BinExistsEx;
import com.infotact.warehouse_management_system.Model.Aisle;
import com.infotact.warehouse_management_system.Model.StorageBin;
import com.infotact.warehouse_management_system.Model.Zone;
import com.infotact.warehouse_management_system.Repository.AisleRepo;
import com.infotact.warehouse_management_system.Repository.StorageBinRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageBinService {

    @Autowired
    private StorageBinRepo storageBinRepo;

    @Autowired
    private AisleRepo aisleRepo;

    @Transactional
    public BinAddRes addBin(BinAddReq req){

        Aisle aisle = aisleRepo.findById(req.getAisleId()).
                orElseThrow(()-> new AisleNotFoundEx("Aisle not found with id: "+req.getAisleId()));

        // Generate updated bin-code
        String binCode = generateBinCode(req.getBinCode(), aisle);

        if(storageBinRepo.existsByBinCode(binCode)){
            throw new BinExistsEx(binCode+ " already exists");
        }
        StorageBin bin  = new StorageBin();
        bin.setBinCode(binCode);
        bin.setMaxCapacity(req.getMaxCapacity());
        bin.setUsedCapacity(0);
        bin.setAisle(aisle);

        bin = storageBinRepo.save(bin);

        return new BinAddRes(bin.getId(), bin.getBinCode(),
                bin.getMaxCapacity(),bin.getUsedCapacity(),
                bin.getAisle().getId());
    }
    // Local methode
    private String generateBinCode(BinCode binCode, Aisle aisle){

        Zone zone = aisle.getZone();

        // Ex: ZA-A1-B1
        String merge = zone.getName().toString() + "-" + aisle.getName().toString() + "-" + binCode.toString();
        return merge;
    }
}
