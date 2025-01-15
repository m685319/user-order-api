package com.example.userorderapi.service;

import com.example.userorderapi.dto.EntityDTO;
import com.example.userorderapi.entity.Order;
import com.example.userorderapi.entity.User;
import com.example.userorderapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<EntityDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EntityDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    public EntityDTO createUser(EntityDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private EntityDTO convertToDTO(User user) {
        EntityDTO dto = new EntityDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setOrders(user.getOrders().stream()
                .map(this::convertOrderToDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private User convertToEntity(EntityDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    private EntityDTO convertOrderToDTO(Order order) {
        EntityDTO orderDTO = new EntityDTO();
        orderDTO.setProduct(order.getProduct());
        orderDTO.setAmount(order.getAmount());
        orderDTO.setStatus(order.getStatus());
        return orderDTO;
    }
}
