package com.gtechsolutions.ordersmanagementservice.client;

import com.gtechsolutions.ordersmanagementservice.dtos.UserDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;


@HttpExchange
public interface UserServiceClient {

   @GetExchange("/api/users/{userId}")
   UserDTO searchUser(@PathVariable String userId);
}
