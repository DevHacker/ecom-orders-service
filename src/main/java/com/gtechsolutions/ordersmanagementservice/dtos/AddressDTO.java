package com.gtechsolutions.ordersmanagementservice.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressDTO {
    private String id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
