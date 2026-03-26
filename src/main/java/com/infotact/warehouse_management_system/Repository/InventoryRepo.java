package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Long> {
}
