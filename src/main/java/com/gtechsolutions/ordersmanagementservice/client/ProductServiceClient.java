package com.gtechsolutions.ordersmanagementservice.client;

import com.gtechsolutions.ordersmanagementservice.dtos.ProductDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductServiceClient {

    @GetExchange("/api/products/{productId}")
    ProductDTO searchProduct(@PathVariable Long productId);
}
