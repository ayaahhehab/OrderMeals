package com.mealPreparation.demomealPreparation.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private double price;
    private String day;
    private String imagePath;

    // 34an el ordered meal myt8yr4 b3d el update
    @OneToMany(mappedBy = "meal")
    List<Order>orders;
}
