package com.DressKlub.product_service.service;

import com.DressKlub.product_service.model.Cart;
import com.DressKlub.product_service.model.CartItems;
import com.DressKlub.product_service.model.Product;
import com.DressKlub.product_service.repository.CartItemRepository;
import com.DressKlub.product_service.repository.CartRepository;
import com.DressKlub.product_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItems> getCartItemsByUserId(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        return optionalCart.map(Cart::getCartItems).orElse(Collections.emptyList());
    }

    public Cart addToCart(Long userId, Long productId, int quantity) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

        Cart cart = optionalCart.orElseGet(() ->{
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()){
            throw new RuntimeException("Product Not Found !!!");
        }
        Product product = optionalProduct.get();

        Optional<CartItems> optionalCartItems = cartItemRepository.findByCartIdAndProductId(cart.getId(),productId);
        if (optionalCartItems.isPresent()){
            CartItems cartItems = optionalCartItems.get();
            cartItems.setQuantity(cartItems.getQuantity() + quantity);
            cartItemRepository.save(cartItems);
        } else {
            CartItems cartItems = new CartItems();
            cartItems.setCart(cart);
            cartItems.setQuantity(quantity);
            cartItems.setProduct(product);
            cartItemRepository.save(cartItems);
        }
        return cartRepository.save(cart);
    }

    public Cart removeFromCart(Long userId, Long productId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isPresent()){
            Cart cart = optionalCart.get();
            cart.getCartItems().removeIf(cartItems -> cartItems.getProduct().getId().equals(productId));
            return cartRepository.save(cart);
        }
        return null;
    }

    public CartItems updateQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()-> new RuntimeException("cart not found for user id"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        Optional<CartItems> optionalCartItems = cartItemRepository.findByCartIdAndProductId(cart.getId(),productId);

        CartItems cartItems;
        if (optionalCartItems.isPresent()){
            cartItems = optionalCartItems.get();
            cartItems.setQuantity(cartItems.getQuantity()+quantity);
        } else {
            cartItems = new CartItems(cart, product, quantity);
        }
        return cartItemRepository.save(cartItems);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("cart not found"));
        cartItemRepository.deleteByCartId(cart.getId());
    }
}
