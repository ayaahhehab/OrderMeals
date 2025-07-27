package com.mealPreparation.demomealPreparation.dto;

import java.time.LocalDateTime;


import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long mealId;
    private String nameOfMeal;
    private String mealDescription;
    private String mealDay;
    private double price;
    private LocalDateTime orderDate;
}



