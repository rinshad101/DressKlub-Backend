package com.dressKlub.order_service.service;

import com.dressKlub.order_service.dto.OrderDTO;
import com.dressKlub.order_service.dto.OrderItemDTO;
import com.dressKlub.order_service.dto.ProductResponse;
import com.dressKlub.order_service.feign.ProductServiceClient;
import com.dressKlub.order_service.model.Order;
import com.dressKlub.order_service.model.OrderItem;
import com.dressKlub.order_service.repository.OrderItemRepository;
import com.dressKlub.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final OrderItemRepository orderItemRepository;


    public OrderService(OrderRepository orderRepository, ProductServiceClient productServiceClient, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productServiceClient = productServiceClient;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderDTO> GetALlOrders() {
        return orderRepository.findAll()
                .stream()
                .filter(item-> !item.getOrderItems().isEmpty())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> placeOrder(Long userId, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus("Processing");

        List<OrderItem> orderItemList = orderItems.stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setDeliveryStatus("Pending");
            orderItem.setOrder(order);
            return orderItem;
        }).toList();

        order.setOrderItems(orderItemList);
        orderRepository.save(order);

        productServiceClient.clearCart(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("order", convertToDTO(order));

        return response;
    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(order.getId(), order.getUserId(), order.getOrderStatus(), order.getOrderItems().stream().map(item -> {
            ProductResponse product = null;
            try {
                product = productServiceClient.getProductById(item.getProductId());
            } catch (Exception e) {
                System.out.println("Failed to fetch product details for ID " + item.getProductId() + ": " + e.getMessage());
            }
            return new OrderItemDTO(item.getProductId(), item.getQuantity(), item.getDeliveryStatus(), product);
        }).toList());
    }

    public OrderDTO getOrderById(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertToDTO).orElseThrow(() -> new RuntimeException("Order Not Found"));
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Object cancelOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("order not found"));

        OrderItem orderItemToDelete = order.getOrderItems().stream().filter(item ->
                item.getProductId().equals(productId))
                .findFirst().orElseThrow(() -> new RuntimeException("product not found"));
        order.getOrderItems().remove(orderItemToDelete);
        orderItemRepository.delete(orderItemToDelete);

        orderRepository.save(order);
        return convertToDTO(order);
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    public void updateDeliveryStatus(Long orderId, Long productId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        order.getOrderItems().forEach(item -> {
            if (item.getProductId().equals(productId)) {
                item.setDeliveryStatus(status);
            }
        });

        orderRepository.save(order);
    }
}
