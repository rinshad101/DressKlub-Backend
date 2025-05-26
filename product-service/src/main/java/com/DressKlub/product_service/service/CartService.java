package com.DressKlub.product_service.service;

import com.DressKlub.product_service.model.Cart;
import com.DressKlub.product_service.model.CartItems;
import com.DressKlub.product_service.model.Product;
import com.DressKlub.product_service.repository.CartRepository;
import com.DressKlub.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
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

        Optional<CartItems> optionalCartItems = cartRepository.find
    }
}
