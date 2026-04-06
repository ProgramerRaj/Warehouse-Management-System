package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.BinAddReq;
import com.infotact.warehouse_management_system.DTO.Request.BinUpdateReq;
import com.infotact.warehouse_management_system.DTO.Response.BinAddRes;
import com.infotact.warehouse_management_system.DTO.Response.BinDelRes;
import com.infotact.warehouse_management_system.Enum.BinCode;
import com.infotact.warehouse_management_system.Exception.AisleNotFoundEx;
import com.infotact.warehouse_management_system.Exception.BinExistsEx;
import com.infotact.warehouse_management_system.Exception.BinNotFoundEx;
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
        bin.setActive(true);
        bin.setAisle(aisle);

        bin = storageBinRepo.save(bin);

        return new BinAddRes(bin.getId(), bin.getBinCode(),
                bin.getMaxCapacity(),bin.getUsedCapacity(),
                bin.isActive(),
                bin.getAisle().getId());
    }
    // Local methode
    private String generateBinCode(BinCode binCode, Aisle aisle){

        Zone zone = aisle.getZone();

        // Ex: ZA-A1-B1
        String merge = zone.getName().toString() + "-" + aisle.getName().toString() + "-" + binCode.toString();
        return merge;
    }

    @Transactional
    public BinAddRes updateBinById(long id, BinUpdateReq req){
        StorageBin bin = storageBinRepo.findById(id)
                .orElseThrow(()-> new BinNotFoundEx("Bin not found with id: "+id));

        if(!bin.isActive()){
            throw new RuntimeException("This bin already deleted with id: "+id+"\nSo you can't update it now");
        }

        Integer newMaxCapacity = req.getMaxCapacity();

        if(newMaxCapacity == null || newMaxCapacity.equals(bin.getMaxCapacity())){
            throw new RuntimeException("No changes found");
        }

        // if new maxCapacity less than old usedCapacity
        if(req.getMaxCapacity() < bin.getUsedCapacity()){
            throw new RuntimeException("New maxCapacity is less than existing bin used capacity\nSo that you can't update it now, Because new maxCapacity of request is less than usedCapacity of existing bin with id: "+id);
        }

        bin.setMaxCapacity(req.getMaxCapacity());
        storageBinRepo.save(bin);

        //response
        return new BinAddRes(
                bin.getId(),
                bin.getBinCode(),
                bin.getMaxCapacity(),
                bin.getUsedCapacity(),
                bin.isActive(),
                bin.getAisle().getId());
    }
    @Transactional
    public BinDelRes deleteBinById(long id){
        StorageBin bin = storageBinRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Bin not found with id: "+id));

        if(!bin.isActive()){
            throw new RuntimeException("This bin already deleted with id: "+id);
        }
        bin.setActive(false);
        storageBinRepo.save(bin);

        //response
        return new BinDelRes(
                bin.getId(),
                bin.getBinCode(),
                bin.getMaxCapacity(),
                bin.getUsedCapacity(),
                bin.isActive(),
                bin.getAisle().getId()
        );
    }
    @Transactional
    public BinAddRes getBinById(long id){
        StorageBin bin = storageBinRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Bin not found with id: "+id));

        //response
        return new BinAddRes(
                bin.getId(),
                bin.getBinCode(),
                bin.getMaxCapacity(),
                bin.getUsedCapacity(),
                bin.isActive(),
                bin.getAisle().getId()
        );
    }
    @Transactional
    public String restoreBinById(long id){
        StorageBin bin = storageBinRepo.findById(id)
                .orElseThrow(()-> new BinNotFoundEx("Bin not found with id: "+id));

        if(bin.isActive()){
            throw new RuntimeException("Bin already restored with id: "+id);
        }
        bin.setActive(true);
        storageBinRepo.save(bin);

        return "Bin successfully restored with id: "+id;
    }
}
