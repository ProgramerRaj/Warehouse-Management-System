package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.ProductAddReq;
import com.infotact.warehouse_management_system.DTO.Request.ProductReceiveReq;
import com.infotact.warehouse_management_system.DTO.Response.ProResponse;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import com.infotact.warehouse_management_system.Exception.InventoryNotFoundEx;
import com.infotact.warehouse_management_system.Exception.ProductNotFoundEx;
import com.infotact.warehouse_management_system.Exception.WarehouseNotFoundEx;
import com.infotact.warehouse_management_system.Model.*;
import com.infotact.warehouse_management_system.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    WarehouseRepo warehouseRepo;

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    StorageBinRepo binRepo;

    @Autowired
    ZoneRepo zoneRepo;

    // add product
    @Transactional
    public ProResponse addProduct(long warehouseId, ProductAddReq request){

        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundEx(
                        "Warehouse not found with id: " + warehouseId));

        Optional<Product> existingProduct = productRepo
                .findByNameAndCategoryAndWarehouseId(
                        request.getName(),
                        request.getCategory(),
                        warehouseId);

        if (existingProduct.isPresent()) {
            return handleExistingProduct(existingProduct.get(), warehouseId, request);
        }

        Product product = createProduct(request, warehouse);

        StorageBin bin = findAvailableBin(
                warehouseId,
                request.getCategory().getZoneType(),
                request.getQuantity()
        );

        createInventory(product, bin, request.getQuantity());

        return buildResponse(product);
    }
    // get product by id
    @Transactional
    public ProResponse getProduct(long id){
        Product p = productRepo.findById(id).
                orElseThrow(()-> new ProductNotFoundEx("Product not found with id: "+id));

        // Set product response
        ProResponse response = new ProResponse(
                p.getId(), p.getName(),
                p.getMrp(), p.getDiscount(),
                p.getSellingPrice(), p.getDescription(),
                p.getSku(),
                p.getCategory(), p.isActive(),p.getWarehouse().getId()
        );
        return response;
    }
    @Transactional
    public String receiveProductQua(ProductReceiveReq req){

        Product product = productRepo.findByIdAndWarehouseId(req.getProId(), req.getWarehouseId())
                .orElseThrow(()-> new ProductNotFoundEx("Product not found with id: "+req.getProId()+
                        " in warehouse with id: "+req.getWarehouseId()));

        if(!product.isActive()){
            throw new RuntimeException("Product already deleted with id: "+product.getId());
        }

        List<Inventory> inventories = inventoryRepo.findByProductId(product.getId());
        Long warehouseId = req.getWarehouseId();

        for(Inventory inventory : inventories) {

            StorageBin bin = inventory.getBin();
            Long binWarehouseId = bin.getAisle().getZone().getWarehouse().getId();

                if (!binWarehouseId.equals(warehouseId)) {
                    continue;
                }

            int availableSpace = bin.getMaxCapacity() - bin.getUsedCapacity();

            if (availableSpace >= req.getQuantity()) {

                // update bin capacity and inventory
                bin.setUsedCapacity(bin.getUsedCapacity() + req.getQuantity());
                inventory.setQuantity(inventory.getQuantity() + req.getQuantity());

                binRepo.save(bin);
                inventoryRepo.save(inventory);

                // response
                return "Product quantity successfully received with id: " + req.getProId();
            }
        }
        // No space -> find new bin
        StorageBin newBin = findAvailableBin(
                warehouseId,
                product.getCategory().getZoneType(),
                req.getQuantity()
        );

        createInventory(product, newBin, req.getQuantity());

        // response
        return "Product quantity successfully received with id: " + req.getProId();
    }
    // Local methods
    private String generateSKU(String proCategory,String proName){
        String catCode = proCategory.substring(0,3).toUpperCase();
        String proCode = proName.substring(0,3).toUpperCase();

        //Unique number
        long uniqueNum = System.currentTimeMillis() % 100000;

        String sku = catCode + "-" +proCode + "-" + uniqueNum;

        return sku;
    }
    private ProResponse handleExistingProduct(Product product,
                                              Long warehouseId,
                                              ProductAddReq request) {

        List<Inventory> inventoryList = inventoryRepo.findByProductId(product.getId());

        for (Inventory inv : inventoryList) {

            StorageBin bin = inv.getBin();
            Long binWarehouseId = bin.getAisle().getZone().getWarehouse().getId();
            {
            if (!binWarehouseId.equals(warehouseId))
                continue;
            }
            int availableSpace = bin.getMaxCapacity() - bin.getUsedCapacity();

            if (availableSpace >= request.getQuantity()) {

                inv.setQuantity(inv.getQuantity() + request.getQuantity());
                bin.setUsedCapacity(bin.getUsedCapacity() + request.getQuantity());

                binRepo.save(bin);
                inventoryRepo.save(inv);

                return buildResponse(product);
            }
        }

        // No space -> find new bin
        StorageBin newBin = findAvailableBin(
                warehouseId,
                request.getCategory().getZoneType(),
                request.getQuantity()
        );

        createInventory(product, newBin, request.getQuantity());

        return buildResponse(product);
    }
    private StorageBin findAvailableBin(Long warehouseId,
                                        ZoneType zoneType,
                                        Integer quantity) {

        Zone zone = zoneRepo.findByTypeAndWarehouseId(zoneType, warehouseId);

        if (zone == null) {
            throw new RuntimeException("Zone not found for type: " + zoneType);
        }

        for (Aisle aisle : zone.getAisles()) {
            for (StorageBin bin : aisle.getBins()) {

                int availableSpace = bin.getMaxCapacity() - bin.getUsedCapacity();

                if (availableSpace >= quantity) {
                    return bin;
                }
            }
        }

        throw new RuntimeException("No space available in this zone");
    }
    private void createInventory(Product product,
                                 StorageBin bin,
                                 Integer quantity) {

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setBin(bin);
        inventory.setQuantity(quantity);

        bin.setUsedCapacity(bin.getUsedCapacity() + quantity);

        binRepo.save(bin);
        inventoryRepo.save(inventory);
    }
    private Product createProduct(ProductAddReq request, Warehouse warehouse) {

        String sku;
        do {
            sku = generateSKU(request.getCategory().name(), request.getName());
        } while (productRepo.existsBySku(sku));

        Product product = new Product();
        product.setActive(request.getActive());
        product.setMrp(request.getMrp());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDiscount(request.getDiscount());
        product.setDescription(request.getDescription());
        product.setSku(sku);
        product.setWarehouse(warehouse);

        double discRate = (request.getMrp() * request.getDiscount()) / 100;
        product.setSellingPrice(request.getMrp() - discRate);

        return productRepo.save(product);
    }
    private ProResponse buildResponse(Product product) {
        return new ProResponse(
                product.getId(),
                product.getName(),
                product.getMrp(),
                product.getDiscount(),
                product.getSellingPrice(),
                product.getDescription(),
                product.getSku(),
                product.getCategory(),
                product.isActive(),
                product.getWarehouse().getId()
        );
    }
}
