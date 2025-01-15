package com.example.userorderapi.controller;

import com.example.userorderapi.dto.EntityDTO;
import com.example.userorderapi.entity.Order;
import com.example.userorderapi.entity.User;
import com.example.userorderapi.repository.UserRepository;
import com.example.userorderapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public List<EntityDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityDTO> getUserById(@PathVariable Long id) {
        Optional<EntityDTO> userDTO = userService.getUserById(id);
        return userDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityDTO> createUser(@Valid @RequestBody EntityDTO userDTO) {
        EntityDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public EntityDTO updateUser(@PathVariable Long id, @Valid @RequestBody EntityDTO userDTO) {
        if (userRepository.existsById(id)) {
            User user = convertToEntity(userDTO);
            user.setId(id);
            User savedUser = userRepository.save(user);
            return convertToDTO(savedUser);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private User convertToEntity(EntityDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        List<Order> orders = dto.getOrders().stream()
                .map(orderDTO -> {
                    Order order = new Order();
                    order.setProduct(orderDTO.getProduct());
                    order.setAmount(orderDTO.getAmount());
                    order.setStatus(orderDTO.getStatus());
                    return order;
                })
                .collect(Collectors.toList());

        user.setOrders(orders);
        return user;
    }

    private EntityDTO convertToDTO(User user) {
        EntityDTO dto = new EntityDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        List<EntityDTO> orderDTOs = user.getOrders().stream()
                .map(order -> {
                    EntityDTO orderDTO = new EntityDTO();
                    orderDTO.setProduct(order.getProduct());
                    orderDTO.setAmount(order.getAmount());
                    orderDTO.setStatus(order.getStatus());
                    return orderDTO;
                })
                .collect(Collectors.toList());

        dto.setOrders(orderDTOs);
        return dto;
    }
}
