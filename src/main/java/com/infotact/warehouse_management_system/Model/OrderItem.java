package com.infotact.warehouse_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private double price;

    private double total;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "remaining_qua")
    private int remainingQua;
}
