package com.gtechsolutions.ordersmanagementservice.repository;


import com.gtechsolutions.ordersmanagementservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findOrderById(String orderId);
}
