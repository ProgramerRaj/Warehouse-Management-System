package com.infotact.warehouse_management_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = "inventories")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "bin_id")
    private StorageBin bin;

    @Column(nullable = false)
    @Positive(message = "Quantity cannot be negative")
    private int quantity;
    
}
