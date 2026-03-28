package com.infotact.warehouse_management_system.Model;

import com.infotact.warehouse_management_system.Enum.BinCode;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "storage_bins")
@Data
public class StorageBin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "bin_code",unique = true)
    private String binCode;

    private int maxCapacity;

    private int usedCapacity;

    @ManyToOne
    @JoinColumn(name = "aisle_id")
    private Aisle aisle;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "bin",fetch = FetchType.LAZY)
    private List<Inventory> inventories = new ArrayList<>();
}
