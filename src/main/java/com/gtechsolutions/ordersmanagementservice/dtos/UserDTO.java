package com.gtechsolutions.ordersmanagementservice.dtos;

import com.gtechsolutions.ordersmanagementservice.entity.UserRole;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddressDTO address;
}
