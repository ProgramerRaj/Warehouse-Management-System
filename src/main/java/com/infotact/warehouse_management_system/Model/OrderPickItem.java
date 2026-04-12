package com.infotact.warehouse_management_system.Model;

import com.infotact.warehouse_management_system.Enum.PickStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_pick_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_order_product_bin",
                        columnNames = {"order_id", "product_id", "bin_id"}
                )
        })
@Data
@NoArgsConstructor
public class OrderPickItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bin_id", nullable = false)
    private StorageBin bin;

    // Picked Quantity
    @Column(name = "picked_qty", nullable = false)
    private Integer pickedQty;

    // Status -> PICKED / PACKED
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PickStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Auto set Date and Time
    @PrePersist
    public void prePersist() {
        LocalDateTime time = LocalDateTime.now();
        this.createdAt = time;
        this.updatedAt = time;

        if (this.status == null) {
            this.status = PickStatus.PICKED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
