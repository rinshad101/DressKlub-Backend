package com.DressKlub.product_service.repository;

import com.DressKlub.product_service.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByCartIdAndProductId(Long id, Long productId);
    void deleteByCartId(Long id);
}
