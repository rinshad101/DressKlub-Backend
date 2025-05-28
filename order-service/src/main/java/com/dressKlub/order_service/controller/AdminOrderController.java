package com.dressKlub.order_service.controller;

import com.dressKlub.order_service.dto.OrderDTO;
import com.dressKlub.order_service.repository.OrderRepository;
import com.dressKlub.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public AdminOrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PutMapping("/{orderId}")
    public void updateOrderStatus(@PathVariable Long orderId, @RequestParam String status){
        orderService.updateOrderStatus(orderId, status);
    }

    @PutMapping("/{orderId}/update-status/{productId}")
    public void updateDeliveryStatus(@PathVariable Long orderId, @PathVariable Long productId, @RequestParam String status) {
        orderService.updateDeliveryStatus(orderId, productId, status);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(){
        List<OrderDTO> orderDTOS = orderService.GetALlOrders();
        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteEmptyOrders() {
        orderRepository.deleteByOrderItemsIsEmpty();
    }
}
