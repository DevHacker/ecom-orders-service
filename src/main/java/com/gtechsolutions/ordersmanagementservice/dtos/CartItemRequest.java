package com.gtechsolutions.ordersmanagementservice.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private Long prodcutId;
    private Integer quantity;
}
