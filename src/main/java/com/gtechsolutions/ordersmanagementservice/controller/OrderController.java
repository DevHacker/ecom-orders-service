package com.gtechsolutions.ordersmanagementservice.controller;

import com.gtechsolutions.ordersmanagementservice.dtos.OrderDTO;
import com.gtechsolutions.ordersmanagementservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/")
@RequiredArgsConstructor
@RefreshScope
public class OrderController {

    private final OrderService orderService;

    @PostMapping("place-order")
    public ResponseEntity<OrderDTO> createOrder(@RequestHeader("X-User-ID") String userId) {
        return orderService.createOrder(userId)
                .map(createdOrder -> new ResponseEntity<>(createdOrder, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("{orderId}/validate")
    public ResponseEntity<OrderDTO> validateOrder(@PathVariable String orderId) {
        return orderService.validateOrder(orderId)
                .map(createdOrder -> new ResponseEntity<>(createdOrder, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/message")
    public ResponseEntity<String> orderMessage() {
        return ResponseEntity.ok("Order Message reached");
    }


}
