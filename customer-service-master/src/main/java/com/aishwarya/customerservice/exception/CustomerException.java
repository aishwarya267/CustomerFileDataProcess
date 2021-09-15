package com.aishwarya.customerservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerException extends RuntimeException {
    private final HttpStatus httpStatus;


    public CustomerException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
}
