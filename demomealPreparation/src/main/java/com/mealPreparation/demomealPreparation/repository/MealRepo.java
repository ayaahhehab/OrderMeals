package com.mealPreparation.demomealPreparation.repository;

import com.mealPreparation.demomealPreparation.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepo extends JpaRepository<Meal,Long> {
    List<Meal> findByDay(String day);

}
