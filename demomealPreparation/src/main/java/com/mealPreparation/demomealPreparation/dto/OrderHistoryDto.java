package com.mealPreparation.demomealPreparation.dto;

import java.time.LocalDateTime;


import lombok.Data;
@Data
public class OrderHistoryDto {
    private Long id;
    private String nameOfMeal;
    private String mealDay;
    private double price;
    private LocalDateTime orderDate;
}