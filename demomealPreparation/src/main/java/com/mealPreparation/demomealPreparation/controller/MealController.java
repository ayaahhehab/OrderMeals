package com.mealPreparation.demomealPreparation.controller;

import com.mealPreparation.demomealPreparation.dto.MealDto;
import com.mealPreparation.demomealPreparation.dto.OrderDto;
import com.mealPreparation.demomealPreparation.service.MealService;
import com.mealPreparation.demomealPreparation.service.OrderService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meals")
@AllArgsConstructor
public class MealController {
    private final MealService mealService;
    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<List<MealDto>> getMeals(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            return ResponseEntity.ok(mealService.getAllMeals());
        } else {
            return ResponseEntity.ok(mealService.getTodayMeals());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDto> getMealById(@PathVariable Long id) {
        try {
            MealDto meal = mealService.getMealById(id);
            return ResponseEntity.ok(meal);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // @GetMapping("/day/{day}")
    // public ResponseEntity<List<MealDto>> getMealsByDay(@PathVariable String day) {
    //     List<MealDto> meals = mealService.getMealsByDay(day);
    //     return ResponseEntity.ok(meals);
    // }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MealDto> addMeal(@RequestBody MealDto mealDto) {
        try {
            MealDto createdMeal = mealService.addMeal(mealDto);
            return ResponseEntity.ok(createdMeal);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MealDto> updateMeal(@PathVariable Long id, @RequestBody MealDto mealDto) {
        try {
            MealDto updatedMeal = mealService.updateMeal(id, mealDto);
            return ResponseEntity.ok(updatedMeal);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{mealId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMeal(@PathVariable Long mealId) {
        try {
            mealService.deleteMeal(mealId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //not usedd
    @PostMapping("/order/{mealId}")
    public ResponseEntity<OrderDto> placeOrder(@PathVariable Long mealId) {
        try {
            OrderDto order = orderService.placeOrder(mealId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}