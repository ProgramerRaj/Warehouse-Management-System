package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Long> {

    List<Inventory> findByProductId(long id);

    Optional<Inventory> findByProductIdAndBinId(long productId, long binId);
}
