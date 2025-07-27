package com.mealPreparation.demomealPreparation.dto;

import lombok.Data;

@Data
public class MealHistoryDto  {
  private Long id;
  private String name;
    private String day;
    private double price;
}
