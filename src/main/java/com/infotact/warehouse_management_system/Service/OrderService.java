package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.OrderAddReq;
import com.infotact.warehouse_management_system.DTO.Request.OrderFulfillReq;
import com.infotact.warehouse_management_system.DTO.Response.OrderAddRes;
import com.infotact.warehouse_management_system.DTO.Response.OrderFulfillRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemReq;
import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemRes;
import com.infotact.warehouse_management_system.Enum.OrderStatus;
import com.infotact.warehouse_management_system.Exception.*;
import com.infotact.warehouse_management_system.Model.*;
import com.infotact.warehouse_management_system.Repository.*;
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

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    StorageBinRepo binRepo;

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
    private List<OrderItemRes> setItemWrapperList(Order order){

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

    @Transactional
    public OrderFulfillRes updateOrderStatus(Long orderId, OrderFulfillReq req){

        Order order = orderRepo.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundEx("Order not found with id: "+orderId));

        OrderStatus current = order.getStatus();
        OrderStatus next = req.getStatus();

        if(current.equals(next)){
            throw new RuntimeException("Order already in status -> "+next);
        }

        if(!isValidFlow(current, next)){
            throw new InvalidOrderFlowEx(
                    "Invalid flow: " + current + " -> " + next);
        }

        // Stock deduct only status is--> PACKED
        if(next == OrderStatus.PACKED){
            deductStock(order);
        }

        order.setStatus(next);
        orderRepo.save(order);

        return new OrderFulfillRes(
                order.getId(),
                current,
                next,
                "Order successfully moved from " + current + " to " + next
        );
    }
    // Local methode
    private boolean isValidFlow(OrderStatus current, OrderStatus next){

        return switch (current){
            case PENDING -> next == OrderStatus.PICKING;
            case PICKING -> next == OrderStatus.PACKED;
            case PACKED -> next == OrderStatus.SHIPPED;
            default -> false;
        };
    }
    // Local methode
    private void deductStock(Order order){

        Long orderWarehouseId = order.getWarehouse().getId();

        for (OrderItem item : order.getOrderItems()) {

            isProductExists(item);
            isProductActive(item);

            Long productId = item.getProduct().getId();
            int remaining = item.getQuantity();

            List<Inventory> inventories =
                    inventoryRepo.findByProductId(productId);

            for (Inventory inv : inventories) {

                StorageBin bin = inv.getBin();
                Long binWarehouseId = bin.getAisle()
                        .getZone()
                        .getWarehouse()
                        .getId();

                // Only same warehouse
                if (!binWarehouseId.equals(orderWarehouseId)) {
                    continue;
                }

                int available = inv.getQuantity();

                // skip empty inventory
                if (available <= 0) {
                    continue;
                }

                // Full deduction
                if (available >= remaining) {

                    inv.setQuantity(available - remaining);
                    bin.setUsedCapacity(bin.getUsedCapacity() - remaining);

                    inventoryRepo.save(inv);
                    binRepo.save(bin);

                    remaining = 0;
                    break;
                }

                // Partial deduction
                else {

                    inv.setQuantity(0);
                    bin.setUsedCapacity(bin.getUsedCapacity() - available);

                    remaining -= available;

                    inventoryRepo.save(inv);
                    binRepo.save(bin);
                }
            }

            // Not enough stock
            if (remaining > 0) {
                throw new InsufficientStockEx(
                        "Insufficient stock for product id: " + productId
                );
            }
        }
    }
    // Local methode
    private void isProductExists(OrderItem item){
        if(!productRepo.existsById(item.getProduct().getId())){
            throw new ProductNotFoundEx("Product not found with id: "+item.getProduct().getId());
        }
    }
    // Local methode
    private void isProductActive(OrderItem item){
        if(!item.getProduct().isActive()){
            throw new RuntimeException("Product inactive with id: "+item.getProduct().getId());
        }
    }
}
