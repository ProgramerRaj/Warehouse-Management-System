package com.infotact.warehouse_management_system.Model;

import com.infotact.warehouse_management_system.Enum.ProductCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "selling_price")
    private double sellingPrice;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private boolean active;

    private String name;

    private double mrp;

    private double discount;

    @Column(unique = true)
    private String sku; // --- Stock keeping Unit ----

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product",fetch = FetchType.LAZY)
    private List<Inventory> inventories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}