package com.mealPreparation.demomealPreparation.controller;


import com.mealPreparation.demomealPreparation.dto.CreateOrderDto;
import com.mealPreparation.demomealPreparation.dto.OrderDto;
import com.mealPreparation.demomealPreparation.dto.OrderHistoryDto;
import com.mealPreparation.demomealPreparation.service.OrderService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @PostMapping("/{mealId}")
    public ResponseEntity<OrderDto> placeOrder(@PathVariable Long mealId) {
        CreateOrderDto dto = new CreateOrderDto();
        dto.setMealId(mealId);

        OrderDto order = orderService.placeOrder(dto.getMealId());
        return ResponseEntity.ok(order);
    }


    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderHistoryDto>> getMyOrderHistory() {
        List<OrderHistoryDto> orders = orderService.getMyOrderHistory();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}