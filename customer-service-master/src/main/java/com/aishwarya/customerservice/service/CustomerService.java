package com.aishwarya.customerservice.service;

import com.aishwarya.customerservice.dto.RequestDTO;
import com.aishwarya.customerservice.exception.CustomerException;
import com.aishwarya.customerservice.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface CustomerService {

    String saveCustomer(RequestDTO requestDTO) throws CustomerException, IOException, JSONException;

    String updateCustomer(RequestDTO requestDTO, Integer id) throws CustomerException, IOException;

    String getAllCustomerData(String fileName) throws NoSuchAlgorithmException;
}
