package com.infotact.warehouse_management_system.Model;

import com.infotact.warehouse_management_system.Enum.WarehouseLocation;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouses")
@Data
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WarehouseLocation location;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouse", fetch = FetchType.LAZY)
    private List<Zone> zones = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
