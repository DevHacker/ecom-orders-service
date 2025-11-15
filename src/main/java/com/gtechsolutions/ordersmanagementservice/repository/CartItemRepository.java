package com.gtechsolutions.ordersmanagementservice.repository;

import com.gtechsolutions.ordersmanagementservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemByUserIdAndProductId(String userId, Long productId);

    List<CartItem> findCartItemsByUserId(String userId);

    boolean deleteCartItemsByUserId(String userId);
}
