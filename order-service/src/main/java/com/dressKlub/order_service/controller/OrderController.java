package com.dressKlub.order_service.controller;

import com.dressKlub.order_service.dto.OrderDTO;
import com.dressKlub.order_service.model.OrderItem;
import com.dressKlub.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place/{userId}")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallBackResponse")
    public Map<String, Object>placeOrder(@PathVariable Long userId, @RequestBody List<OrderItem> orderItems){
        return orderService.placeOrder(userId, orderItems);
    }

    public OrderDTO getOrderById(@PathVariable Long orderId){
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackResponse")
    public List<OrderDTO> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    public ResponseEntity<String> fallBackResponse(String orderId,Throwable t){
        return ResponseEntity.ok("Product Service is unavailable. Please try later!");
    }

    @DeleteMapping("/{orderId}/items/{productId}")
    public ResponseEntity<Map<String ,Object>> cancelOrder(@PathVariable Long orderId,@PathVariable Long productId) {

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", orderService.cancelOrder(orderId, productId));
        return ResponseEntity.ok(response);
    }
}
