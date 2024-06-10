package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.models.OrderItem;
import com.kn.auth.repositories.OrderItemRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem create(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> createMany(List<OrderItem> orders) {
        return orderItemRepository.saveAll(orders);
    }

    public List<OrderItem> getAllByOrderId(int orderId) {
        return orderItemRepository.findAllByOrderId(orderId).get();
    }

    public OrderItem getById(int orderItemId) {
        return orderItemRepository.findById(orderItemId).get();
    }

    public List<OrderItem> updateMany(List<OrderItem> orders) {
        return orderItemRepository.saveAll(orders);
    }
}
