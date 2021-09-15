package com.aishwarya.customerservice.controller;

import com.aishwarya.customerservice.dto.RequestDTO;
import com.aishwarya.customerservice.exception.CustomerException;
import com.aishwarya.customerservice.model.Customer;
import com.aishwarya.customerservice.service.CustomerService;
import com.aishwarya.customerservice.service.impl.CustomerServiceImpl;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@CrossOrigin
public class FileDataProcessController {

    @Autowired
    private CustomerService customerService;

    //API to save data to file
    @PostMapping(value = "/fileData/fileDataSave")
    public String saveFileData(@RequestBody RequestDTO requestDTO) throws CustomerException, JSONException, IOException {
        return customerService.saveCustomer(requestDTO);
    }

    //API to update customer data
    @PutMapping(value = "/fileData/fileDataUpdate")
    public String updateCustomerData(@RequestBody RequestDTO requestDTO, @PathVariable Integer customerNumber) throws CustomerException, IOException {
        return customerService.updateCustomer(requestDTO, customerNumber);
    }

    //API to view customer data
    @GetMapping(value = "/fileData/fileDataFetch")
    public String getCustomerData(@RequestParam String fileName) throws NoSuchAlgorithmException {
        return customerService.getAllCustomerData(fileName);
    }

}
