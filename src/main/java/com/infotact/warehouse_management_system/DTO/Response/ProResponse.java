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
    private String barcode;
    private ProductCategory category;
    private boolean active;
    private long warehouseId;

    // Parameterise constructor
    public ProResponse(long productId, String proName, double mrp, double discount, double sellingPrice, String description, String sku, String barcode, ProductCategory category, boolean active, long warehouseId) {
        this.productId = productId;
        this.proName = proName;
        this.mrp = mrp;
        this.discount = discount;
        this.sellingPrice = sellingPrice;
        this.description = description;
        this.sku = sku;
        this.barcode = barcode;
        this.category = category;
        this.active = active;
        this.warehouseId = warehouseId;
    }
}
