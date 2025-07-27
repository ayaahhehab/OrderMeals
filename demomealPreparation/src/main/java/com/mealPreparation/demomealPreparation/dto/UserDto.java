package com.mealPreparation.demomealPreparation.dto;

import com.mealPreparation.demomealPreparation.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private Role role;
}
