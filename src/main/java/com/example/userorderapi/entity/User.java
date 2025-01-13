package com.example.userorderapi.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {

    public interface Views {
        interface UserSummary {}
        interface UserDetails extends UserSummary {}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(Views.UserSummary.class)
    private String name;

    @JsonView(Views.UserSummary.class)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.UserDetails.class)
    private List<Order> orders;

}
