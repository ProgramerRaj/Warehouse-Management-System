package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {

    Optional<OrderItem> findByOrderIdAndProductId(long orderId, long productId);
}
