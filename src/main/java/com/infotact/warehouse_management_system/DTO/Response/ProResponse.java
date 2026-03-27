package com.infotact.warehouse_management_system.DTO.Response;

import com.infotact.warehouse_management_system.Enum.ProductCategory;
import lombok.Data;

@Data
public class ProResponse {

    private long productId;
    private String proName;
    private double mrp;
    private double discount;
    private double sellingPrice;
    private String description;
    private String sku;
    private ProductCategory category;
    private boolean active;

    // Parameterise constructor
    public ProResponse(long productId, String proName, double mrp, double discount, double sellingPrice, String description, String sku, ProductCategory category, boolean active) {
        this.productId = productId;
        this.proName = proName;
        this.mrp = mrp;
        this.discount = discount;
        this.sellingPrice = sellingPrice;
        this.description = description;
        this.sku = sku;
        this.category = category;
        this.active = active;
    }
}
