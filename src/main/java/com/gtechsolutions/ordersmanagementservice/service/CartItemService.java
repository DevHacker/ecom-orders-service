package com.gtechsolutions.ordersmanagementservice.service;

import com.gtechsolutions.ordersmanagementservice.dtos.CartItemRequest;
import com.gtechsolutions.ordersmanagementservice.entity.CartItem;

import java.util.List;

public interface CartItemService {
    String addToCart(String userId, CartItemRequest cartItemRequest);
    Boolean removeFromCart(String userId, Long productId);
    List<CartItem> fetchUserCart(String userId);
    void  deleteCart(String userId);
}
