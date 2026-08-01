package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        orderService.addOrderToPriorityQueue(order);
        return ResponseEntity.ok("✅ Order " + order.getOrderId() + " placed successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllPendingOrders() {
        return ResponseEntity.ok(orderService.getPendingOrdersSorted());
    }

    @PostMapping("/dispatch")
    public ResponseEntity<Object> dispatchNextOrder() {
        Order processedOrder = orderService.dispatchHighestPriorityOrder();
        if (processedOrder == null) {
            return ResponseEntity.badRequest().body("⚠️ No orders available in queue!");
        }
        return ResponseEntity.ok(processedOrder);
    }
}
