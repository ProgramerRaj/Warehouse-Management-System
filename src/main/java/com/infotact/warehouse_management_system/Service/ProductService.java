package com.infotact.warehouse_management_system.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.infotact.warehouse_management_system.DTO.Request.ProductAddReq;
import com.infotact.warehouse_management_system.DTO.Request.ProductReceiveReq;
import com.infotact.warehouse_management_system.DTO.Response.ProResponse;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import com.infotact.warehouse_management_system.Exception.ProductNotFoundEx;
import com.infotact.warehouse_management_system.Exception.WarehouseNotFoundEx;
import com.infotact.warehouse_management_system.Model.*;
import com.infotact.warehouse_management_system.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public ProResponse getProduct(Long id,
                                  String barcode,
                                  String sku){

        Product product = null;

        // Priority-based search
        if(id != null){
            product = productRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found by id"));
        }
        else if(barcode != null && !barcode.isBlank()){
            product = productRepo.findByBarcode(barcode)
                    .orElseThrow(() -> new RuntimeException("Product not found by barcode"));
        }
        else if(sku != null && !sku.isBlank()){
            product = productRepo.findBySku(sku)
                    .orElseThrow(() -> new RuntimeException("Product not found by sku"));
        }
        else{
            throw new RuntimeException("Provide at least one parameter (id/barcode/sku)");
        }

        // Set product response
        ProResponse response = new ProResponse(
                product.getId(),
                product.getName(),
                product.getMrp(),
                product.getDiscount(),
                product.getSellingPrice(),
                product.getDescription(),
                product.getSku(),
                product.getBarcode(),
                product.getCategory(),
                product.isActive(),
                product.getWarehouse().getId()
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

        int remaining = req.getQuantity();

        for (Inventory inventory : inventories) {

            StorageBin bin = inventory.getBin();
            Long binWarehouseId = bin.getAisle().getZone().getWarehouse().getId();

            if (!binWarehouseId.equals(warehouseId)) {
                continue;
            }

            int availableSpace = bin.getMaxCapacity() - bin.getUsedCapacity();

            if (availableSpace <= 0) {
                continue;
            }

            int toAdd = Math.min(availableSpace, remaining);

            inventory.setQuantity(inventory.getQuantity() + toAdd);
            bin.setUsedCapacity(bin.getUsedCapacity() + toAdd);

            remaining -= toAdd;

            if (remaining == 0) {
                break;
            }
        }

        if (remaining > 0) {
            StorageBin newBin = findAvailableBin(
                    warehouseId,
                    product.getCategory().getZoneType(),
                    remaining
            );
            createInventory(product, newBin, remaining);
        }

        // response
        return "Product quantity successfully received with id: " + req.getProId();
    }

    // Local method (Generate sku)
    private String generateSKU(String proCategory,String proName){
        String catCode = proCategory.substring(0,3).toUpperCase();
        String proCode = proName.substring(0,3).toUpperCase();

        //Unique number
        long uniqueNum = System.currentTimeMillis() % 100000;

        String sku = catCode + "-" +proCode + "-" + uniqueNum;

        return sku;
    }

    // Local method (generate barcode)
    private String generateBarcode(String sku){
        return "BC-" + sku + "-" + System.currentTimeMillis();
    }

    private ProResponse handleExistingProduct(Product product,
                                              Long warehouseId,
                                              ProductAddReq request) {

        List<Inventory> inventoryList = inventoryRepo.findByProductId(product.getId());

        int remaining = request.getQuantity();

        for (Inventory inv : inventoryList) {

            StorageBin bin = inv.getBin();
            Long binWarehouseId = bin.getAisle().getZone().getWarehouse().getId();

            if (!binWarehouseId.equals(warehouseId)) {
                continue;
            }

            int availableSpace = bin.getMaxCapacity() - bin.getUsedCapacity();

            if (availableSpace <= 0) {
                continue;
            }

            int toAdd = Math.min(availableSpace, remaining);

            inv.setQuantity(inv.getQuantity() + toAdd);
            bin.setUsedCapacity(bin.getUsedCapacity() + toAdd);

            remaining -= toAdd;

            if (remaining == 0){
                break;
            }
        }

        // If still remaining -> new bin
        if (remaining > 0) {
            StorageBin newBin = findAvailableBin(
                    warehouseId,
                    request.getCategory().getZoneType(),
                    remaining
            );
            createInventory(product, newBin, remaining);
        }

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

    // Local method
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

    // Local methode
    private Product createProduct(ProductAddReq request, Warehouse warehouse) {

        String sku;
        do {
            sku = generateSKU(request.getCategory().name(), request.getName());
        } while (productRepo.existsBySku(sku));

        String barcode;
        do {
            barcode = generateBarcode(sku);
        } while (productRepo.existsByBarcode(barcode));

        Product product = new Product();
        product.setActive(request.getActive());
        product.setMrp(request.getMrp());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDiscount(request.getDiscount());
        product.setDescription(request.getDescription());

        product.setSku(sku);
        product.setBarcode(barcode);
        product.setBarcodeImage(generateBarcodeImage(barcode));
        product.setQrCodeImage(generateQrCodeImage(barcode));

        product.setWarehouse(warehouse);

        double discRate = (request.getMrp() * request.getDiscount()) / 100;
        product.setSellingPrice(request.getMrp() - discRate);

        return productRepo.save(product);
    }

    // Local method
    private ProResponse buildResponse(Product product) {
        return new ProResponse(
                product.getId(),
                product.getName(),
                product.getMrp(),
                product.getDiscount(),
                product.getSellingPrice(),
                product.getDescription(),
                product.getSku(),
                product.getBarcode(),
                product.getCategory(),
                product.isActive(),
                product.getWarehouse().getId()
        );
    }

    // Local method
    private byte[] generateBarcodeImage(String barcode){

        if (barcode == null || barcode.isBlank()) {
            throw new IllegalArgumentException("Barcode cannot be null or empty");
        }
        try {
            Code128Writer barcodeWriter = new Code128Writer();

            BitMatrix bitMatrix = barcodeWriter.encode(
                    barcode,
                    BarcodeFormat.CODE_128,
                    300,
                    100
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error while writing barcode image", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Local method
    private byte[] generateQrCodeImage(String barcode){
        if (barcode == null || barcode.isBlank()) {
            throw new IllegalArgumentException("QR text cannot be null or empty");
        }

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    barcode,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (WriterException e) {
            throw new RuntimeException("Error while encoding QR Code", e);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing QR image", e);
        }
    }

    @Transactional
    public byte[] getBarcodeImage(Long productId){
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ProductNotFoundEx("Product not found with id: "+productId));

        byte[] barcodeImage = product.getBarcodeImage();

        if(barcodeImage == null){
            throw new RuntimeException("This product has not Barcode image with Id: "+productId);
        }
        return barcodeImage;
    }
    @Transactional
    public byte[] getQrCodeImage(Long productId){
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ProductNotFoundEx("Product not found with id: "+productId));

        byte[] qrCodeImage = product.getQrCodeImage();

        if(qrCodeImage == null){
            throw new RuntimeException("This product has not QR Code image with Id: "+productId);
        }
        return qrCodeImage;
    }
}
