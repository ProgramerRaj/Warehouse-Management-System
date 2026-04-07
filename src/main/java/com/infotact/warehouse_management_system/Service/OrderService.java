package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.OrderAddReq;
import com.infotact.warehouse_management_system.DTO.Response.OrderAddRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemReq;
import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemRes;
import com.infotact.warehouse_management_system.Enum.OrderStatus;
import com.infotact.warehouse_management_system.Exception.ProductNotFoundEx;
import com.infotact.warehouse_management_system.Exception.WarehouseNotFoundEx;
import com.infotact.warehouse_management_system.Model.Order;
import com.infotact.warehouse_management_system.Model.OrderItem;
import com.infotact.warehouse_management_system.Model.Product;
import com.infotact.warehouse_management_system.Model.Warehouse;
import com.infotact.warehouse_management_system.Repository.OrderItemRepo;
import com.infotact.warehouse_management_system.Repository.OrderRepo;
import com.infotact.warehouse_management_system.Repository.ProductRepo;
import com.infotact.warehouse_management_system.Repository.WarehouseRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    WarehouseRepo warehouseRepo;

    @Autowired
    ProductRepo productRepo;

    @Transactional
    public OrderAddRes createOrder(OrderAddReq req){

        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() ->
                        new WarehouseNotFoundEx("Warehouse not found with id: "+req.getWarehouseId()));

        if (!warehouse.isActive()) {
            throw new RuntimeException("Warehouse inactive");
        }

        // Validate items duplicate product check
        Set<Long> productIds = new HashSet<>();

        Order order = new Order();
        order.setWarehouse(warehouse);
        order.setStatus(OrderStatus.PENDING);

        double orderTotal = 0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemReq itemReq : req.getOrderItems()) {

            if (!productIds.add(itemReq.getProId())) {
                throw new RuntimeException("Duplicate productId found in request: " + itemReq.getProId());
            }

            Product product = productRepo
                    .findByIdAndWarehouseId(
                            itemReq.getProId(),
                            req.getWarehouseId()
                    )
                    .orElseThrow(() ->
                            new ProductNotFoundEx("Product not found with id: "+itemReq.getProId()+
                                    " in warehouse with id: "+req.getWarehouseId()));

            if (!product.isActive()) {
                throw new RuntimeException("Product inactive");
            }

            double price = product.getSellingPrice();
            double total = price * itemReq.getQuantity();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(price);
            item.setTotal(total);

            orderTotal += total;

            items.add(item);
        }

        order.setOrderItems(items);
        order.setTotalAmount(orderTotal);
        order = orderRepo.save(order);

        return new OrderAddRes(
                order.getId(),
                order.getStatus(),
                setItemWrapperList(order),
                order.getTotalAmount());
    }
    // Local methode
    public List<OrderItemRes> setItemWrapperList(Order order){

        List<OrderItemRes> itemResList = new ArrayList<>();
        for(OrderItem item : order.getOrderItems()){
            OrderItemRes itemRes = new OrderItemRes(
                    item.getProduct().getId(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getTotal()
            );

            itemResList.add(itemRes);
        }
        return itemResList;
    }
}
