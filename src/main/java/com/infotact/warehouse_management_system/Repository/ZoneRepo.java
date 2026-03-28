package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import com.infotact.warehouse_management_system.Model.Aisle;
import com.infotact.warehouse_management_system.Model.Zone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepo extends JpaRepository<Zone,Long> {

    // Check zone type already exists or not in existing warehouse
    boolean existsByTypeAndWarehouseId(ZoneType type, long warehouseId);

    // Check zone name already exists or not in existing warehouse
    boolean existsByNameAndWarehouseId(ZoneName name, long warehouseId);
}
