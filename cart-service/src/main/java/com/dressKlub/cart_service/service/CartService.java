package com.dressKlub.cart_service.service;

import com.dressKlub.cart_service.dto.CartItemResponse;
import com.dressKlub.cart_service.dto.CartRequest;
import com.dressKlub.cart_service.feign.ProductClient;
import com.dressKlub.cart_service.model.Cart;
import com.dressKlub.cart_service.model.CartItem;
import com.dressKlub.cart_service.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;

    public CartService(CartRepository cartRepository, ProductClient productClient) {
        this.cartRepository = cartRepository;
        this.productClient = productClient;
    }
    public void addToCart(Long userId, CartRequest request) {
    // 1. Validate quantity
    if (request.getQuantity() == null || request.getQuantity() <= 0) {
        throw new IllegalArgumentException("Quantity must be greater than 0.");
    }

    // 2. Check if product exists using ProductService
    ProductResponse product;
    try {
        product = productClient.getProductById(request.getProductId());
    } catch (Exception e) {
        throw new RuntimeException("Product not found in Product Service.");
    }

    // 3. Get or create user's cart
    Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setItems(new ArrayList<>());
        return cartRepository.save(newCart);
    });

    // 4. Check if product already in cart
    CartItem existingItem = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(request.getProductId()))
            .findFirst()
            .orElse(null);

    if (existingItem != null) {
        // Product already in cart – increase quantity
        existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
    } else {
        // Add new CartItem to cart
        CartItem newItem = new CartItem();
        newItem.setProductId(request.getProductId());
        newItem.setQuantity(request.getQuantity());
        newItem.setCart(cart); // set reference back to parent cart
        cart.getItems().add(newItem);
    }

    // 5. Save cart
    cartRepository.save(cart);

    public List<CartItemResponse> getUserCart(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream().map(item -> {
            ProductResponse product = productClient.getProductById(item.getProductId());
            return new CartItemResponse(item.getProductId(), item.getQuantity(), product);
        }).toList();
    }

    public void removeFromCart(Long userId, Long productId) {
        cartRepository.findByUserIdAndProductId(userId, productId)
                .ifPresent(cartRepository::delete);
    }

    public void updateQuantity(Long userId, Long productId, int quantity) {
        CartItem item = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setQuantity(quantity);
        cartRepository.save(item);
    }
}
