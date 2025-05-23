package com.dressKlub.cart_service.controller;


import com.dressKlub.cart_service.dto.CartItemResponse;
import com.dressKlub.cart_service.dto.CartRequest;
import com.dressKlub.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add to cart
    @PostMapping("/add/{userId}")
    public ResponseEntity<String> addToCart(
            @PathVariable Long userId,
            @RequestBody CartRequest request
    ) {
        cartService.addToCart(userId, request);
        return ResponseEntity.ok("Product added to cart");
    }


    // Get all cart items for a user (with product details)
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(
            @PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    // Update quantity
    @PutMapping("/{userId}/update")
    public ResponseEntity<String> updateQuantity(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        cartService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok("Quantity updated");
    }

    // Remove from cart
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<String> removeFromCart(
            @PathVariable Long userId,
            @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }
}
