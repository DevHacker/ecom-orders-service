package com.gtechsolutions.ordersmanagementservice.service;

import com.gtechsolutions.ordersmanagementservice.dtos.OrderDTO;

import java.util.Optional;

public interface OrderService {
    Optional<OrderDTO> createOrder(String userId);
    Optional<OrderDTO> validateOrder(String orderId);
   void deleteOrder(String orderId);
}
