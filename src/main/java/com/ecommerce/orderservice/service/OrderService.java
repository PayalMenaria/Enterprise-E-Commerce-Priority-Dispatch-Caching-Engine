package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class OrderService {

    private final PriorityQueue<Order> priorityQueue = new PriorityQueue<>(Comparator
            .comparing(Order::isExpress, Comparator.reverseOrder())
            .thenComparingLong(Order::getTimestamp));

    public void addOrderToPriorityQueue(Order order) {
        if (order.getTimestamp() == 0L) {
            order.setTimestamp(System.currentTimeMillis());
        }
        priorityQueue.offer(order);
    }

    public List<Order> getPendingOrdersSorted() {
        return new ArrayList<>(priorityQueue);
    }

    public Order dispatchHighestPriorityOrder() {
        return priorityQueue.poll();
    }
}
