package com.gtechsolutions.ordersmanagementservice.controller;

import com.gtechsolutions.ordersmanagementservice.dtos.CartItemRequest;
import com.gtechsolutions.ordersmanagementservice.entity.CartItem;
import com.gtechsolutions.ordersmanagementservice.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart/")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;

    @PostMapping("")
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest cartItemRequest) {
        return Optional.ofNullable(cartItemService.addToCart(userId, cartItemRequest))
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @DeleteMapping("item/{productId}/remove")
    public ResponseEntity<String> removeFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable Long productId) {
        boolean isRemoveFromCart = cartItemService.removeFromCart(userId, productId);
        if (isRemoveFromCart) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("user/cart")
    public ResponseEntity<List<CartItem>> getUserCart(@RequestHeader("X-User-ID") String userId){
        List<CartItem> userCartItems = cartItemService.fetchUserCart(userId);
        return ResponseEntity.ok().body(userCartItems);

    }
}
