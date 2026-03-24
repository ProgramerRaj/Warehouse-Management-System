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

    @NotNull(message = "Category is required")
    private ProductCategory category;

    @Positive(message = "Product MRP must be required")
    @NotNull(message = "Product MRP must be required")
    private Double mrp;

    @Max(value = 99)
    @Min(value = 0)
    @NotNull(message = "Product discount is required")
    private Double discount;

    @NotNull(message = "Product quantity is required")
    @Positive(message = "Please enter valid product quantity")
    private Integer quantity;

    private Boolean active = false;
}
