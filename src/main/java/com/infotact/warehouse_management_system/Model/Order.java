package com.infotact.warehouse_management_system.Model;

import com.infotact.warehouse_management_system.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cusName;

    private String cusMobile;

    private String eMail;

    private String address;

    private String pinCode;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order",fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
