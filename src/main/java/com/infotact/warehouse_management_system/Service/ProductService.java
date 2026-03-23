package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.ProRequest;
import com.infotact.warehouse_management_system.DTO.Response.ProResponse;
import com.infotact.warehouse_management_system.Exception.ProductExistsEx;
import com.infotact.warehouse_management_system.Model.Product;
import com.infotact.warehouse_management_system.Repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    // add product
    @Transactional
    public ProResponse addProduct(ProRequest request){

        // Product already exists with name
        if(productRepo.existsByNameAndCategory(request.getName(),request.getCategory())){
            throw new ProductExistsEx(
                    "Product already exists with name: "+request.getName()
            );
        }
        // Generate unique sku for product-barcode
        String sku;
        do {
            sku = generateSKU(request.getCategory().name(), request.getName());
        }while(productRepo.existsBySku(sku));

        // Save product
        Product product = new Product();
        product.setActive(true);
        product.setMrp(request.getMrp());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDiscount(request.getDiscount());
        product.setDescription(request.getDescription());
        product.setSku(sku);
        product.setQuantity(request.getQuantity());

        // Calculate selling price
        double discRate = (request.getMrp() * request.getDiscount()) / 100;
        double sellingPrice = (request.getMrp() - discRate);
        product.setSellingPrice(sellingPrice);

        Product savedProduct = productRepo.save(product);

        // Set product response
        ProResponse response = new ProResponse(
                savedProduct.getId(), savedProduct.getName(),
                savedProduct.getMrp(), savedProduct.getDiscount(),
                savedProduct.getSellingPrice(), savedProduct.getDescription(),
                savedProduct.getQuantity(), savedProduct.getSku(),
                savedProduct.getCategory(), savedProduct.isActive()
        );
        return response;
    }

    // Local methode
    public String generateSKU(String proCategory,String proName){
        String catCode = proCategory.substring(0,3).toUpperCase();
        String proCode = proName.substring(0,3).toUpperCase();

        //Unique number
        long uniqueNum = System.currentTimeMillis() % 100000;

        String sku = catCode + "-" +proCode + "-" + uniqueNum;

        return sku;
    }
}
