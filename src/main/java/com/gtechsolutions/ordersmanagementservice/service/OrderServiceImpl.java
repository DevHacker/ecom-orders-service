package com.gtechsolutions.ordersmanagementservice.service;
import com.gtechsolutions.ordersmanagementservice.client.UserServiceClient;
import com.gtechsolutions.ordersmanagementservice.dtos.OrderDTO;
import com.gtechsolutions.ordersmanagementservice.dtos.OrderItemsDTO;
import com.gtechsolutions.ordersmanagementservice.dtos.UserDTO;
import com.gtechsolutions.ordersmanagementservice.entity.CartItem;
import com.gtechsolutions.ordersmanagementservice.entity.Order;
import com.gtechsolutions.ordersmanagementservice.entity.OrderItems;
import com.gtechsolutions.ordersmanagementservice.enums.OrderStatus;
import com.gtechsolutions.ordersmanagementservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public Optional<OrderDTO> createOrder(String userId) {
        //Validate user
        UserDTO foundUser = userServiceClient.searchUser(userId);
        if(foundUser == null) {
            log.info("User with the given id is not found");
            return Optional.empty();
        }

        //Validate the existence of user's cart
        List<CartItem> userCartItem = cartItemService.fetchUserCart(userId);
        log.info("userCartItem content :: {}", userCartItem);
        if(userCartItem.isEmpty()) return Optional.empty();

        //Getting order's total amount
        BigDecimal totalAmount = userCartItem.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create a user's order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);

        List<OrderItems> orderItems = userCartItem.stream()
                .map(cartItem -> {
                    OrderItems orderItem = new OrderItems();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setOrder(order);
                    orderItem.setQuantity(cartItem.getQuantity());
                    return orderItem;
                })
                .toList();

        log.info("orderItems content :: {}", orderItems);

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        log.info("saved order content :: {}", savedOrder);

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(savedOrder, orderDTO);
        log.info("orderDTO content :: {}", orderDTO);

        //Transform OrderItem into OrderItemDTO
        List<OrderItemsDTO> orderItemsDTOS = savedOrder.getItems().stream()
                .map(items ->{
                   OrderItemsDTO orderItem = new OrderItemsDTO();
                   orderItem.setId(items.getId());
                   orderItem.setProductId(items.getProductId());
                   orderItem.setQuantity(items.getQuantity());
                   orderItem.setPrice(items.getPrice());
                   orderItem.setSubTotal(items.getPrice().multiply(BigDecimal.valueOf(items.getQuantity())));
                   return  orderItem;
                })
                .toList();
        orderDTO.setItems(orderItemsDTOS);

        //Delete user's cart
        cartItemService.deleteCart(userId);

        return Optional.of(orderDTO);
    }

    @Override
    public Optional<OrderDTO> validateOrder(String orderId) {
        return orderRepository.findOrderById(orderId)
                .map(order -> {
                    order.setStatus(OrderStatus.CONFIRMED);
                    Order updatedOrder = orderRepository.save(order);
                    OrderDTO orderDTO = new OrderDTO();
                    BeanUtils.copyProperties(updatedOrder, orderDTO);
                    return orderDTO;
                });
    }

    @Override
    public void deleteOrder(String orderId) {
        orderRepository.findOrderById(orderId)
                .ifPresent(order -> {
                    order.setStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                });
    }
}
