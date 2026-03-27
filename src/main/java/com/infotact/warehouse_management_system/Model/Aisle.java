package com.infotact.warehouse_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "aisles")
@Data
public class Aisle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "aisle",fetch = FetchType.LAZY)
    private List<StorageBin> bins;
}
