package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.OrderAddReq;
import com.infotact.warehouse_management_system.DTO.Request.PickRequest;
import com.infotact.warehouse_management_system.DTO.Response.OrderAddRes;
import com.infotact.warehouse_management_system.DTO.Response.OrderFulfillRes;
import com.infotact.warehouse_management_system.DTO.Response.OrderPickingRes;
import com.infotact.warehouse_management_system.DTO.Response.PickItemRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.BinInfo;
import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemReq;
import com.infotact.warehouse_management_system.DTO.Wrapper.OrderItemRes;
import com.infotact.warehouse_management_system.DTO.Wrapper.ProductPickInfo;
import com.infotact.warehouse_management_system.Enum.OrderStatus;
import com.infotact.warehouse_management_system.Enum.PickStatus;
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

    @Autowired
    OrderPickItemRepo orderPickItemRepo;

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
            item.setRemainingQua(itemReq.getQuantity());
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
    public OrderFulfillRes orderPacked(Long orderId){

        Order order = orderRepo.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundEx("Order not found with id: "+orderId));

        OrderStatus current = order.getStatus();
        OrderStatus next = OrderStatus.PACKED;

        if(current.equals(next)){
            throw new RuntimeException("Order already in status -> "+next);
        }

        if(!isValidFlow(current, next)){
            throw new InvalidOrderFlowEx(
                    "Invalid flow: " + current + " -> " + next);
        }

        // Stock deduct
        deductStockFromPicked(order);

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
            case PICKING -> next == OrderStatus.PICKED;
            case PICKED -> next == OrderStatus.PACKED;
            case PACKED -> next == OrderStatus.SHIPPED;
            default -> false;
        };
    }
    // Local methode
    private void deductStockFromPicked(Order order){

        boolean allPicked = order.getOrderItems().stream()
                .allMatch(item -> item.getRemainingQua() == 0);

        if(!allPicked){
            throw new RuntimeException("All items are not fully picked");
        }

        List<OrderPickItem> picks = orderPickItemRepo.findByOrderId(order.getId());

        if(picks.isEmpty()){
            throw new RuntimeException("No items picked for this order");
        }

        for (OrderPickItem pick : picks) {

            Long productId = pick.getProduct().getId();
            Long binId = pick.getBin().getId();
            int pickedQty = pick.getPickedQty();

            // Inventory from same bin
            Inventory inventory = inventoryRepo
                    .findByProductIdAndBinId(productId, binId)
                    .orElseThrow(() -> new InventoryNotFoundEx(
                            "Inventory not found for product " + productId + " in bin " + binId
                    ));

            // Inventory Capacity Check
            if(inventory.getQuantity() < pick.getPickedQty()){
                throw new RuntimeException("Cannot deduct more than available stock with inventory Id: "+inventory.getId());
            }
            inventory.setQuantity(
                    inventory.getQuantity() - pick.getPickedQty()
            );
            if(inventory.getQuantity() < 0){
                throw new RuntimeException("Negative stock not allowed with Inventory Id: "+inventory.getId());
            }

            StorageBin bin = inventory.getBin();

            // Bin Capacity Check
            if(bin.getUsedCapacity() < pick.getPickedQty()){
                throw new RuntimeException("Invalid bin capacity deduction with Bin Id: "+binId);
            }
            bin.setUsedCapacity(
                    bin.getUsedCapacity() - pick.getPickedQty()
            );
            if(bin.getUsedCapacity() < 0){
                throw new RuntimeException("Negative bin capacity not allowed with bin Id: "+binId);
            }

            inventoryRepo.save(inventory);
            binRepo.save(bin);

            pick.setStatus(PickStatus.PACKED);
            orderPickItemRepo.save(pick);
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

    @Transactional
    public OrderPickingRes orderPicking(Long orderId){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundEx("Order not found with id: "+orderId));

        OrderStatus current = order.getStatus();
        OrderStatus next = OrderStatus.PICKING;

        if(current.equals(next)){
            throw new RuntimeException("Order already in status -> "+next);
        }
        if(!isValidFlow(current, next)){
            throw new InvalidOrderFlowEx(
                    "Invalid flow: " + current + " -> " + next);
        }

        // Response
        OrderPickingRes res = new OrderPickingRes();
        res.setOrderId(orderId);

        // set product info in ProductPickInfo DTO
        List<ProductPickInfo> productInfoList = new ArrayList<>();

        Long orderWarehouseId = order.getWarehouse().getId();

        for (OrderItem item : order.getOrderItems()){

            isProductExists(item);
            isProductActive(item);

            Long productId = item.getProduct().getId();

            ProductPickInfo productInfo = new ProductPickInfo();
            productInfo.setProId(productId);
            productInfo.setProName(item.getProduct().getName());
            productInfo.setRequiredQty(item.getQuantity());

            // set product bins
            List<Inventory> inventories =
                    inventoryRepo.findByProductId(item.getProduct().getId());

            List<BinInfo> binInfoList = new ArrayList<>();
            for(Inventory inv : inventories){

                StorageBin bin = inv.getBin();
                Long binWarehouseId = bin.getAisle()
                        .getZone()
                        .getWarehouse()
                        .getId();

                // Only same warehouse
                if (!binWarehouseId.equals(orderWarehouseId)) {
                    continue;
                }

                BinInfo binInfo = new BinInfo();
                binInfo.setWarehouse(item.getProduct().getWarehouse().getName());
                binInfo.setZone(bin.getAisle().getZone().getName());
                binInfo.setAisle(bin.getAisle().getName());
                binInfo.setBinId(bin.getId());
                binInfo.setAvailableQty(bin.getUsedCapacity());

                binInfoList.add(binInfo);
            }
            productInfo.setBins(binInfoList);
            productInfoList.add(productInfo);
        }
        res.setItems(productInfoList);
        order.setStatus(OrderStatus.PICKING);
        return res;
    }
    @Transactional
    public PickItemRes orderPicked(Long orderId, PickRequest req){

        Order order = orderRepo.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundEx("Order not found with id: "+orderId));

        OrderStatus current = order.getStatus(); // PICKING
        OrderStatus next = OrderStatus.PICKED; // When after all item picked

        if(current.equals(next)){
            throw new RuntimeException("Order already in status -> "+current);
        }
        if(!isValidFlow(current,next)){
            throw new InvalidOrderFlowEx("Invalid order flow: "+
                    current + " -> "+next);
        }
        Product product = productRepo.findByBarcode(req.getBarcode())
                .orElseThrow(()-> new ProductNotFoundEx("Invalid barcode"));

        OrderItem item = orderItemRepo.findByOrderIdAndProductId(orderId, product.getId())
                .orElseThrow(()-> new OrderItemNotFoundEx("Product not in order"));

        if(item.getRemainingQua() <= 0){
            throw new RuntimeException("Item already picked");
        }

        Inventory inventory = inventoryRepo.
                findByProductIdAndBinId(product.getId(), req.getBinId())
                .orElseThrow(()-> new InventoryNotFoundEx("Inventory not found with product ID: " +
                        product.getId() + " and " + "Bin ID: " + req.getBinId()));

        StorageBin bin = inventory.getBin();

        if(inventory.getQuantity() <= 0){
            throw new RuntimeException("No stock in this bin with Id: "+ bin.getId());
        }

        // check product stock
        if(inventory.getQuantity() < req.getPickedQty()){
            throw new InsufficientStockEx(
                    "Insufficient stock for this product: " + product.getId()
                    +" during item picked"
            );
        }
        // Over-picking validation (TOTAL picked from all bins)
        Integer totalPicked = orderPickItemRepo
                .sumPickedQty(orderId, product.getId());

        if(totalPicked == null){
            totalPicked = 0;
        }

        if(totalPicked + req.getPickedQty() > item.getQuantity()){
            throw new RuntimeException("Over picking not allowed");
        }
        // Save / Update OrderPickItem
        OrderPickItem pickItem = orderPickItemRepo
                .findByOrderIdAndProductIdAndBinId(
                        orderId,
                        product.getId(),
                        req.getBinId()
                )
                .orElse(null);

        if(pickItem == null){
            pickItem = new OrderPickItem();
            pickItem.setOrderId(orderId);
            pickItem.setOrderItemId(item.getId());
            pickItem.setProduct(product);
            pickItem.setBin(bin);
            pickItem.setPickedQty(req.getPickedQty());
            pickItem.setStatus(PickStatus.PICKED);
        } else {
            pickItem.setPickedQty(pickItem.getPickedQty() + req.getPickedQty());
        }

        orderPickItemRepo.save(pickItem);

        // Update remaining quantity
        item.setRemainingQua(item.getQuantity() - (totalPicked + req.getPickedQty()));
        orderItemRepo.save(item);

        // If all items picked -> order status update
        boolean allPicked = order.getOrderItems().stream()
                .allMatch(i -> i.getRemainingQua() == 0);

        if(allPicked){
            order.setStatus(OrderStatus.PICKED);
            orderRepo.save(order);
        }

        // Response
        PickItemRes res = new PickItemRes();

        res.setOrderId(order.getId());
        res.setOrderItemId(item.getId());
        res.setProductName(product.getName());
        res.setRequiredQty(item.getQuantity());
        res.setPickedQty(req.getPickedQty());
        int remaining = item.getQuantity() - (totalPicked + req.getPickedQty());
        res.setRemainingQty(remaining);

        return res;
    }
    @Transactional
    public OrderFulfillRes orderShipped(Long orderId){

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundEx("Order not found with id: " + orderId));

        OrderStatus current = order.getStatus();
        OrderStatus next = OrderStatus.SHIPPED;

        if(current.equals(next)){
            throw new RuntimeException("Order already in status -> " + next);
        }

        if(!isValidFlow(current, next)){
            throw new InvalidOrderFlowEx(
                    "Invalid flow: " + current + " -> " + next);
        }
        order.setStatus(next);
        return new OrderFulfillRes(
                order.getId(),
                current,
                next,
                "Order successfully SHIPPED"
        );
    }
}
