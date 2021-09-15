package com.aishwarya.customerservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customer")
public class Customer {
    @Id
    private Integer customerNumber;
    private String customerName;
    private String address;
    private String countryCode;
}



