package com.mealPreparation.demomealPreparation.service;

import com.mealPreparation.demomealPreparation.dto.MealDto;
import com.mealPreparation.demomealPreparation.entity.Meal;
import com.mealPreparation.demomealPreparation.repository.MealRepo;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MealService {
    private final MealRepo mealRepository;

    public MealService(MealRepo mealRepository) {
        this.mealRepository = mealRepository;
    }

    public List<MealDto> getAllMeals() {
        return mealRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MealDto> getMealsByDay(String day) {
        return mealRepository.findByDay(day)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MealDto getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found"));
        return toDto(meal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MealDto addMeal(MealDto mealDto) {
        Meal meal = toEntity(mealDto);
        Meal savedMeal = mealRepository.save(meal);
        return toDto(savedMeal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MealDto updateMeal(Long id, MealDto mealDto) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found"));
        meal.setName(mealDto.getName());
        meal.setDescription(mealDto.getDescription());
        meal.setPrice(mealDto.getPrice());
        meal.setDay(mealDto.getDay());
        meal.setImagePath(mealDto.getImagePath());
        Meal updatedMeal = mealRepository.save(meal);
        return toDto(updatedMeal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    
    public void deleteMeal(Long mealId) {
        if (!mealRepository.existsById(mealId)) {
            throw new RuntimeException("Meal not found");
        }
        mealRepository.deleteById(mealId);
    }

    public List<MealDto> getTodayMeals() {
        String today = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return getMealsByDay(today);
    }

    private MealDto toDto(Meal meal) {
        MealDto dto = new MealDto();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setDescription(meal.getDescription());
        dto.setPrice(meal.getPrice());
        dto.setDay(meal.getDay());
        dto.setImagePath(meal.getImagePath());
        return dto;
    }

    private Meal toEntity(MealDto dto) {
        Meal meal = new Meal();
        if (dto.getId() != null) {
            meal.setId(dto.getId());
        }
        meal.setName(dto.getName());
        meal.setDescription(dto.getDescription());
        meal.setPrice(dto.getPrice());
        meal.setDay(dto.getDay());
        meal.setImagePath(dto.getImagePath());
        return meal;
    }
}