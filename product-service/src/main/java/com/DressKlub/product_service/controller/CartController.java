package com.DressKlub.product_service.controller;

import com.DressKlub.product_service.model.Cart;
import com.DressKlub.product_service.model.CartItems;
import com.DressKlub.product_service.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public ResponseEntity<Cart> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }
}
