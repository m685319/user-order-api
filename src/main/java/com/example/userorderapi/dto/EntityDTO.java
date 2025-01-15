package com.example.userorderapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityDTO {

    public interface Views {
        interface UserSummary {}
        interface UserDetails extends UserSummary {}
        interface OrderDetails {}
    }

    @JsonView(Views.UserSummary.class)
    private Long id;

    @JsonView(Views.UserSummary.class)
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @JsonView(Views.UserDetails.class)
    @Email(message = "Email should be valid")
    private String email;

    @JsonView(Views.UserDetails.class)
    private List<EntityDTO> orders;

    @JsonView(Views.OrderDetails.class)
    @NotBlank(message = "Product name is required for orders")
    private String product;

    @JsonView(Views.OrderDetails.class)
    @NotNull(message = "Amount is required for orders")
    private Double amount;

    @JsonView(Views.OrderDetails.class)
    @NotBlank(message = "Status is required for orders")
    private String status;
}
