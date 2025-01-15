package com.example.userorderapi.service;

import com.example.userorderapi.dto.EntityDTO;
import com.example.userorderapi.entity.Order;
import com.example.userorderapi.entity.User;
import com.example.userorderapi.repository.OrderRepository;
import com.example.userorderapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    public EntityDTO createOrder(Long userId, EntityDTO orderDTO) {
        Order order = convertToOrderEntity(orderDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);
        Order savedOrder = orderRepository.save(order);
        return convertOrderToDTO(savedOrder);
    }

    public List<EntityDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertOrderToDTO)
                .toList();
    }

    public EntityDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertOrderToDTO(order);
    }

    public EntityDTO updateOrder(Long orderId, EntityDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existingOrder.setProduct(orderDTO.getProduct());
        existingOrder.setAmount(orderDTO.getAmount());
        existingOrder.setStatus(orderDTO.getStatus());

        Order updatedOrder = orderRepository.save(existingOrder);
        return convertOrderToDTO(updatedOrder);
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(orderId);
    }

    private EntityDTO convertOrderToDTO(Order order) {
        EntityDTO dto = new EntityDTO();
        dto.setId(order.getId());
        dto.setProduct(order.getProduct());
        dto.setAmount(order.getAmount());
        dto.setStatus(order.getStatus());
        dto.setName(order.getUser().getName());
        return dto;
    }

    private Order convertToOrderEntity(EntityDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setProduct(dto.getProduct());
        order.setAmount(dto.getAmount());
        order.setStatus(dto.getStatus());
        return order;
    }
}
