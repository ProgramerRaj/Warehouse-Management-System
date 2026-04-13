package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Enum.ProductCategory;
import com.infotact.warehouse_management_system.Model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    //SKU already exists or not
    boolean existsBySku(String sku);

    //Product already exists or not with name and category
    boolean existsByNameAndCategory(String name, ProductCategory category);

    Optional<Product> findByNameAndCategoryAndWarehouseId(String name, ProductCategory category, long warehouseId);

    Optional<Product> findByIdAndWarehouseId(Long proId, Long warehouseId);

    Optional<Product> findBySku(String sku);

    boolean existsByBarcode(String barcode);

    Optional<Product> findByBarcode(String barcode);
}
