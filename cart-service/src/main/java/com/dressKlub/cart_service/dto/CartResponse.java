package com.dressKlub.cart_service.dto;

import java.util.List;

public class CartResponse {

    private Long cartId;
    private Long userId;
    private List<CartItemResponse> items;
}
