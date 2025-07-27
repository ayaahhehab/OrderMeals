package com.mealPreparation.demomealPreparation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = true)
    private Meal meal;

    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    
    @Column(name = "price")
    private double price;

    // @Column(name="meal_name")
    private String nameOfMeal;
    
}
