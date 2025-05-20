package com.dressKlub.cart_service.controller;

import com.dressKlub.cart_service.model.Cart;
import com.dressKlub.cart_service.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId,@RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addProductToCart(userId,productId,quantity));
    }
}
