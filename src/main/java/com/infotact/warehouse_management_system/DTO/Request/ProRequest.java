package com.infotact.warehouse_management_system.DTO.Request;


import com.infotact.warehouse_management_system.Enum.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private ProductCategory category;

    @Positive(message = "SellingPrice must be greater than 0")
    private Double sellingPrice;

    @Positive(message = "Product MRP must be required")
    private Double mrp;

    @Max(value = 99)
    @Min(value = 0)
    @NotNull(message = "Product discount is required")
    private Double discount;

    @NotNull(message = "Product quantity is required")
    private Integer quantity;

    private Boolean active = false;
}
