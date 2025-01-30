package com.example.userorderapi.controller;

import com.example.userorderapi.dto.EntityDTO;
import com.example.userorderapi.service.OrderService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    void createOrder_shouldReturnCreatedOrder() throws Exception {
        Long userId = 1L;
        EntityDTO requestDTO = new EntityDTO();
        requestDTO.setProduct("Test Product");
        requestDTO.setAmount(new BigDecimal("100.00"));
        requestDTO.setStatus("NEW");

        EntityDTO responseDTO = new EntityDTO();
        responseDTO.setId(1L);
        responseDTO.setProduct("Test Product");
        responseDTO.setAmount(new BigDecimal("100.00"));
        responseDTO.setStatus("NEW");
        responseDTO.setName("Test User");

        Mockito.when(orderService.createOrder(Mockito.eq(userId), Mockito.any(EntityDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/orders/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.product").value(responseDTO.getProduct()))
                .andExpect(jsonPath("$.status").value(responseDTO.getStatus()))
                .andExpect(jsonPath("$.name").value(responseDTO.getName()));

        Mockito.verify(orderService).createOrder(Mockito.eq(userId), Mockito.any(EntityDTO.class));
    }

    @Test
    void getAllOrders_shouldReturnOrderList() throws Exception {
        EntityDTO order1 = new EntityDTO();
        order1.setId(1L);
        order1.setProduct("Product 1");
        order1.setAmount(new BigDecimal("50.00"));
        order1.setStatus("COMPLETED");
        order1.setName("User 1");

        EntityDTO order2 = new EntityDTO();
        order2.setId(2L);
        order2.setProduct("Product 2");
        order2.setAmount(new BigDecimal("100.00"));
        order2.setStatus("PENDING");
        order2.setName("User 2");

        Mockito.when(orderService.getAllOrders()).thenReturn(List.of(order1, order2));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].product").value("Product 1"))
                .andExpect(jsonPath("$[1].product").value("Product 2"));

        Mockito.verify(orderService).getAllOrders();
    }

    @Test
    void getOrderById_shouldReturnOrder() throws Exception {
        Long orderId = 1L;

        EntityDTO order = new EntityDTO();
        order.setId(orderId);
        order.setProduct("Test Product");
        order.setAmount(new BigDecimal("100.00"));
        order.setStatus("NEW");
        order.setName("Test User");

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.product").value(order.getProduct()));

        Mockito.verify(orderService).getOrderById(orderId);
    }

    @Test
    void updateOrder_shouldReturnUpdatedOrder() throws Exception {
        Long orderId = 1L;

        EntityDTO requestDTO = new EntityDTO();
        requestDTO.setProduct("Updated Product");
        requestDTO.setAmount(new BigDecimal("150.00"));
        requestDTO.setStatus("UPDATED");

        EntityDTO responseDTO = new EntityDTO();
        responseDTO.setId(orderId);
        responseDTO.setProduct("Updated Product");
        responseDTO.setAmount(new BigDecimal("150.00"));
        responseDTO.setStatus("UPDATED");
        responseDTO.setName("Updated User");

        Mockito.when(orderService.updateOrder(Mockito.eq(orderId), Mockito.any(EntityDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.product").value(responseDTO.getProduct()));

        Mockito.verify(orderService).updateOrder(Mockito.eq(orderId), Mockito.any(EntityDTO.class));
    }

    @Test
    void deleteOrder_shouldReturnNoContent() throws Exception {
        Long orderId = 1L;

        Mockito.doNothing().when(orderService).deleteOrder(orderId);

        mockMvc.perform(delete("/orders/{orderId}", orderId))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService).deleteOrder(orderId);
    }
}