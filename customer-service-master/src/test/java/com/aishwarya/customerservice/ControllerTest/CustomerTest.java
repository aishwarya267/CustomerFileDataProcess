package com.aishwarya.customerservice.ControllerTest;

import com.aishwarya.customerservice.CustomerServiceApplication;
import com.aishwarya.customerservice.dto.FileDataDTO;
import com.aishwarya.customerservice.model.Customer;
import com.aishwarya.customerservice.repository.CustomerRepository;
import com.aishwarya.util.AbstractTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CustomerServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class CustomerTest extends AbstractTestConfig {

    private final Logger logger = LoggerFactory.getLogger(CustomerTest.class);
    private final String error_message = "Error occured while testing..!";


    MvcResult mvcResult;

    MockHttpServletResponse result;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @After
    public void tearDown() {
        final File file = new File(System.getProperty("user.dir") + "//data//test.mv");
        file.delete();
    }
    

    @Test
    public void testSaveCustomerSuccess() {
        final String url = "/fileData/fileDataSave";
        String inputJson;
        try {
            inputJson = mapToJson(createCustomerDTO());

            mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        } catch (final Exception e) {
            logger.error(error_message, e.getMessage());
        }
        result = mvcResult.getResponse();
        assertEquals(200, result.getStatus());
    }


    @Test
    public void testSaveCustomerFail() {
        final String url = "/fileData/fileDataSave";
        String inputJson;
        try {
            inputJson = mapToJson(createCustomerDTO1());
            mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        } catch (final Exception e) {
            logger.error(error_message, e.getMessage());
        }
        result = mvcResult.getResponse();
        assertEquals(400, result.getStatus());
    }


    @Test
    public void testUpdateCustomerSuccess() {
        final String url = "/fileData/fileDataUpdate/2";
        String inputJson;
        try {
            inputJson = mapToJson(createCustomerDTO());
            mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        } catch (final Exception e) {
            logger.error(error_message, e.getMessage());
        }
        result = mvcResult.getResponse();
        assertEquals(200, result.getStatus());
    }

    @Test
    public void testUpdateCustomerFail() {
        final String url = "/fileData/fileDataUpdate/3";
        String inputJson;
        try {
            inputJson = mapToJson(createCustomerDTO());
            mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        } catch (final Exception e) {
            logger.error(error_message, e.getMessage());
        }
        result = mvcResult.getResponse();
        assertEquals(404, result.getStatus());
    }

    @Test
    public void testFetchAllCustomer() {
        final String url = "/fileData/fileDataFetch?fileName=xyz.csv";

        try {
            String inputJson = mapToJson(createCustomerDTO1());
            mvcResult = mvc.perform(
                    MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_VALUE).header("accept-language", "en"))
                    .andReturn();

        } catch (final Exception e) {
            logger.error(error_message, e.getMessage());
        }
        result = mvcResult.getResponse();
        assertEquals(200, result.getStatus());
    }

    @Test
    public void testFetchAllCustomerNotFound() {
        final String url = "/fileData/fileDataFetch?fileName=abc.xml";

        try {
            mvcResult = mvc.perform(
                    MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_VALUE).header("accept-language", "en"))
                    .andReturn();

        } catch (final Exception e) {
            logger.error(error_message, e.getMessage());
        }
        result = mvcResult.getResponse();
        assertEquals(404, result.getStatus());
    }


    private FileDataDTO createCustomerDTO() {
        final FileDataDTO fileDataDTO = new FileDataDTO();
        fileDataDTO.setCustomerId("2");
        fileDataDTO.setAddress("Bangalore");
        fileDataDTO.setName("ABC");
        fileDataDTO.setSalary((double) 10000);
        fileDataDTO.setAge(26);
        return fileDataDTO;
    }

    private FileDataDTO createCustomerDTO1() {
        final FileDataDTO fileDataDTO = new FileDataDTO();
        fileDataDTO.setCustomerId("3");
        fileDataDTO.setAddress("Mumbai");
        fileDataDTO.setName("XYZ");
        fileDataDTO.setSalary((double) 25000);
        fileDataDTO.setAge(35);
        return fileDataDTO;
    }

}
