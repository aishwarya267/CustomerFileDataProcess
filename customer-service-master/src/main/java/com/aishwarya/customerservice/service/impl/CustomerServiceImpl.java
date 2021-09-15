package com.aishwarya.customerservice.service.impl;

import com.aishwarya.customerservice.constants.ResponseMessages;
import com.aishwarya.customerservice.dto.RequestDTO;
import com.aishwarya.customerservice.exception.CustomerException;
import com.aishwarya.customerservice.repository.CustomerRepository;
import com.aishwarya.customerservice.service.CustomerService;
import com.aishwarya.customerservice.util.EncryptDecryptManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import org.json.*;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public String saveCustomer(RequestDTO requestDTO) throws CustomerException, IOException, JSONException {
        log.info("Started saving Customer Records");
        String fileType = requestDTO.getFileType();
        //key decode
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(requestDTO.getEncodedKey());
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        EncryptDecryptManager decryptionManager = new EncryptDecryptManager();
        String decryptedData = decryptionManager.decrypt(requestDTO.getEncryptedData(), originalKey.toString());

        if (fileType == "csv") {
            JsonNode jsonTree = new ObjectMapper().readTree(decryptedData);
            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
            JsonNode firstObject = jsonTree.elements().next();
            firstObject.fieldNames().forEachRemaining(fieldName -> {
                csvSchemaBuilder.addColumn(fieldName);
            });
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(JsonNode.class)
                    .with(csvSchema)
                    .writeValue(new File("src/main/resources/employeeData.csv"), jsonTree);

        } else if (fileType == "xml") {
            ObjectMapper jsonMapper = new ObjectMapper();
            String xmlFile = System.getProperty("user.dir") + "\\employee.xml";
            JsonNode node = jsonMapper.readValue(decryptedData, JsonNode.class);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
            FileWriter w = new FileWriter(xmlFile);
            xmlMapper.writeValue(w, node);

        } else {
            throw new CustomerException(ResponseMessages.SAVE_FAIL, HttpStatus.BAD_REQUEST);
        }
        return ResponseMessages.SAVE_SUCCESS;
    }



    @Override
    public String updateCustomer(RequestDTO requestDTO, Integer id) throws CustomerException, IOException {
        log.info("Started updating Customer details");

        String workingDir = System.getProperty("user.dir");
        File testfile = new File(workingDir, requestDTO.getFileType());

        if (testfile.exists()) {
            FileWriter myWriter = new FileWriter(testfile);
            myWriter.write(requestDTO.toString());
            myWriter.close();
        } else {
            throw new CustomerException(ResponseMessages.CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ResponseMessages.UPDATE_SUCCESS;
    }



    public String getAllCustomerData(String fileName) throws NoSuchAlgorithmException {
        log.info("Started Fetching all Customer information");
        String data = null;
        try {
            File fileObj = new File(fileName);
            Scanner myReader = new Scanner(fileObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //encrypt data to be sent
        // create new key and get base64 encoded version of the key
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        EncryptDecryptManager encryptionManager = new EncryptDecryptManager();
        String encryptedData = encryptionManager.encrypt(data, encodedKey);
        return encryptedData;
    }

}
