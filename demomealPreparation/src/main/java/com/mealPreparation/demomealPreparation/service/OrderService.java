package com.mealPreparation.demomealPreparation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mealPreparation.demomealPreparation.dto.OrderDto;
import com.mealPreparation.demomealPreparation.dto.OrderHistoryDto;
import com.mealPreparation.demomealPreparation.entity.Meal;
import com.mealPreparation.demomealPreparation.entity.Order;
import com.mealPreparation.demomealPreparation.entity.User;
import com.mealPreparation.demomealPreparation.repository.MealRepo;
import com.mealPreparation.demomealPreparation.repository.OrderRepo;
import com.mealPreparation.demomealPreparation.repository.UserRepo;


@Service
public class OrderService {
    private final OrderRepo orderRepository;
    private final MealRepo mealRepository;
    private final UserRepo userRepository;
    public OrderService(OrderRepo orderRepository, MealRepo mealRepository, UserRepo userRepository) {
        this.orderRepository = orderRepository;
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    public OrderDto placeOrder(Long mealId) {
        Long currentUserId = getCurrentUserId();
        
        if (orderRepository.findTodayOrderByUserId(currentUserId).isPresent()) {
            throw new RuntimeException("You already ordered a meal today");
        }

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        String today = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        if (!meal.getDay().equalsIgnoreCase(today)) {
            throw new RuntimeException("You can only order meals for today (" + today + ")");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setMeal(meal);
        order.setOrderDate(LocalDateTime.now());
        order.setPrice(meal.getPrice());
        order.setNameOfMeal(meal.getName());

        Order savedOrder = orderRepository.save(order);
        return toOrderDto(savedOrder);
    }

    public List<OrderHistoryDto> getMyOrderHistory() {
        Long userId = getCurrentUserId();
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        return orders.stream()
                .map(this::toOrderHistoryDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

    public void deleteOrder(Long orderId) {
        Long currentUserId = getCurrentUserId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("You can only delete your own orders");
        }

        orderRepository.delete(order);
    }

    private OrderDto toOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setUserName(order.getUser().getName());
        dto.setMealId(order.getMeal().getId());
        dto.setNameOfMeal(order.getMeal().getName());
        dto.setMealDescription(order.getMeal().getDescription());
        dto.setMealDay(order.getMeal().getDay());
        dto.setPrice(order.getPrice());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }

    private OrderHistoryDto toOrderHistoryDto(Order order) {
        OrderHistoryDto dto = new OrderHistoryDto();
        dto.setId(order.getId());
        dto.setNameOfMeal(order.getNameOfMeal());
        dto.setMealDay(order.getMeal().getDay());
        dto.setPrice(order.getPrice());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }

    private Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("No authenticated user found");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Long.parseLong(userDetails.getUsername());
    }
}
