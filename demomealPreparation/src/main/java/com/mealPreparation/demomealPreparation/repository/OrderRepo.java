package com.mealPreparation.demomealPreparation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mealPreparation.demomealPreparation.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
    
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
    
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND DATE(o.orderDate) = CURRENT_DATE")
    Optional<Order> findTodayOrderByUserId(@Param("userId") Long userId);


    // not usedd
    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByMealIdOrderByOrderDateDesc(Long mealId);
    
    // 34an el orderd meal myt8yr4 b3d el update
    @Query("SELECT o FROM Order o WHERE o.meal.id = :mealId")
    List<Order> findByMealId(@Param("mealId") Long mealId);
    
}