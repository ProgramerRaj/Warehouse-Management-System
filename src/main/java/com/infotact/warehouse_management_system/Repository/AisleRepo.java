package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Enum.AisleName;
import com.infotact.warehouse_management_system.Model.Aisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AisleRepo extends JpaRepository<Aisle,Long> {

    boolean existsByNameAndZoneId(AisleName name, long zoneId);
}
