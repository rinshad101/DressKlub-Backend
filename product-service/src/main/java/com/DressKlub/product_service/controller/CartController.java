package com.DressKlub.product_service.controller;

import com.DressKlub.product_service.model.Cart;
import com.DressKlub.product_service.model.CartItems;
import com.DressKlub.product_service.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItems>> getCartItems(@RequestParam Long userId){
        return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestParam Long userId, @RequestParam Long productId){
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<CartItems> updateQuantity(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.updateQuantity(userId,productId,quantity));
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Map<String, String>> clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cart cleared successfully"));
    }
}
