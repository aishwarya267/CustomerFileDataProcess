package com.aishwarya.customerservice.dto;

import lombok.Data;

@Data
public class FileDataDTO {
    private String customerId;
    private String name;
    private String address;
    private Double salary;
    private Integer age;
}
