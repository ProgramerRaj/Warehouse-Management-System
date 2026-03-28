package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import com.infotact.warehouse_management_system.Model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse,Long> {

    // Find warehouse with name & location
    boolean existsByNameAndLocation(String name, WarehouseLocation location);
}
