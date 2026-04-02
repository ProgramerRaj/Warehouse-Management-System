package com.infotact.warehouse_management_system.Model;

import com.infotact.warehouse_management_system.Enum.ZoneName;
import com.infotact.warehouse_management_system.Enum.ZoneType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zones")
@Data
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private ZoneName name;

    @Enumerated(EnumType.STRING)
    private ZoneType type;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zone", fetch = FetchType.LAZY)
    private List<Aisle> aisles = new ArrayList<>();
}
