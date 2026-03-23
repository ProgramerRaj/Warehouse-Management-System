package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Enum.ProductCategory;
import com.infotact.warehouse_management_system.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    //SKU already exists or not
    boolean existsBySku(String sku);

    //Product already exists or not with name and category
    boolean existsByNameAndCategory(String name, ProductCategory category);
}
