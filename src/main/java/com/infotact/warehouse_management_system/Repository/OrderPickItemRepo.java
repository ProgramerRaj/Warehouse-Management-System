package com.infotact.warehouse_management_system.Repository;

import com.infotact.warehouse_management_system.Model.OrderPickItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPickItemRepo extends JpaRepository<OrderPickItem, Long> {

    @Query("SELECT SUM(p.pickedQty) FROM OrderPickItem p WHERE p.orderId = :orderId AND p.product.id = :productId")
    Integer sumPickedQty(Long orderId, Long productId);

    Optional<OrderPickItem> findByOrderIdAndProductIdAndBinId(Long orderId, long id, @NotNull(message = "Please provide bin ID") @Positive(message = "Please provide valid bin ID") Integer binId);

    List<OrderPickItem> findByOrderId(Long orderId);
}
