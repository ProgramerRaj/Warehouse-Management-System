package com.infotact.warehouse_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "zones")
@Data
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String type;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zone")
    private List<Aisle> aisles;
}
