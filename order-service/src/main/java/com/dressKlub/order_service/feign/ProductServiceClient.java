package com.dressKlub.order_service.feign;

import com.dressKlub.order_service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {

    @DeleteMapping("/api/products/cart/clear/{userId}")
    void clearCart(@PathVariable Long userId);

    @GetMapping("/api/products/{productId}")
    ProductResponse getProductById(@PathVariable Long productId);
}
