package com.gtechsolutions.ordersmanagementservice.dtos;
import com.gtechsolutions.ordersmanagementservice.enums.OrderStatus;
import com.gtechsolutions.ordersmanagementservice.enums.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDTO {
    private String id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemsDTO> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private PaymentType paymentType;

}
