package com.mealPreparation.demomealPreparation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MealDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private double price;
    @NotBlank
    private String day;
    private String imagePath;
}
