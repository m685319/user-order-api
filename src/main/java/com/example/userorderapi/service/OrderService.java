package com.example.userorderapi.service;

import com.example.userorderapi.entity.Order;
import com.example.userorderapi.entity.User;
import com.example.userorderapi.repository.OrderRepository;
import com.example.userorderapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Long userId, Order order) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            order.setUser(user);
            return orderRepository.save(order);
        }
        return null;
    }
}
