package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Enum.ZoneType;
import com.infotact.warehouse_management_system.Model.StorageBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageBinRepo extends JpaRepository<StorageBin,Long> {

    boolean existsByBinCode(String binCode);

    Optional<StorageBin> findByIdAndAisleId(long id, long aisleId);
}
