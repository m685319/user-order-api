package com.example.userorderapi.controller;

import com.example.userorderapi.dto.EntityDTO;
import com.example.userorderapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import java.util.Optional;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    void getAllUsers() throws Exception {
        EntityDTO user1 = new EntityDTO();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@example.com");

        EntityDTO user2 = new EntityDTO();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@example.com");

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));

        Mockito.verify(userService).getAllUsers();
    }

    @Test
    void getUserById() throws Exception {
        Long userId = 1L;

        EntityDTO user = new EntityDTO();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("testuser@example.com");

        Mockito.when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        Mockito.verify(userService).getUserById(userId);
    }

    @Test
    void createUser() throws Exception {
        EntityDTO requestDTO = new EntityDTO();
        requestDTO.setName("New User");
        requestDTO.setEmail("newuser@example.com");

        EntityDTO responseDTO = new EntityDTO();
        responseDTO.setId(1L);
        responseDTO.setName("New User");
        responseDTO.setEmail("newuser@example.com");

        Mockito.when(userService.createUser(Mockito.any(EntityDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.name").value(responseDTO.getName()))
                .andExpect(jsonPath("$.email").value(responseDTO.getEmail()));

        Mockito.verify(userService).createUser(Mockito.any(EntityDTO.class));
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;

        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser(userId);
    }
}
