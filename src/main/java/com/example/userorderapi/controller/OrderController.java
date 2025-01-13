package com.example.userorderapi.controller;

import com.example.userorderapi.entity.Order;
import com.example.userorderapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long userId, @RequestBody Order order) {
        Order createdOrder = orderService.createOrder(userId, order);
        return createdOrder != null ? new ResponseEntity<>(createdOrder, HttpStatus.CREATED)
                : ResponseEntity.badRequest().build();
    }
}
