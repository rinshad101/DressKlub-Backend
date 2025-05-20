package com.dressKlub.cart_service.model;

import jakarta.persistence.Table;
import jdk.jfr.Enabled;

import java.util.List;

@Enabled
@Table(name = "cart")
public class Cart {

    private Long id;
    private Long userId;
    private List<CartItem> cartItems;
}
