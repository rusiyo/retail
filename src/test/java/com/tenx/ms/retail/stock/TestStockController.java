package com.tenx.ms.retail.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import org.apache.commons.io.FileUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
public class TestStockController extends AbstractIntegrationTest {

    private static final String API_VERSION = RestConstants.VERSION_ONE;
    private static final String REQUEST_URI = "%s" + API_VERSION + "/stocks/";
    private final RestTemplate template = new TestRestTemplate();
    @Autowired
    private ObjectMapper mapper;
    @Value("classpath:stockTests/errors/no_count.json")
    private File badRequest1;
    @Value("classpath:stockTests/errors/no_product.json")
    private File badRequest2;
    @Value("classpath:stockTests/errors/no_store.json")
    private File badRequest3;
    @Value("classpath:stockTests/success/success.json")
    private File goodRequest1;
    @Value("classpath:stockTests/success/success-second.json")
    private File goodRequest2;

    @Test
    @FlywayTest
    public void testValidationOnAllFieldsForCreateStock() {
        List<File> validationFiles = Arrays.asList(badRequest1, badRequest2, badRequest3);
        for (File file : validationFiles) {
            try {
                ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + "1/1", FileUtils.readFileToString(file), HttpMethod.POST);
                assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    @FlywayTest
    public void testCreateStock() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + "1/1", FileUtils.readFileToString(goodRequest1), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            assertEquals("Body is not empty", received, null);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateAndUpdateStock() {
        // Create
        createOrUpdateStock(goodRequest1);
        // Validate
        Stock stock = getStock(1L, 1L);
        assertEquals("Stock count is not correct", stock.getCount(), (Integer) 1);
        // Update
        createOrUpdateStock(goodRequest2);
        // Validate
        stock = getStock(1L, 1L);
        assertEquals("Stock count is not correct", stock.getCount(), (Integer) 3);
    }

    private void createOrUpdateStock(File file) {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + "1/1", FileUtils.readFileToString(file), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            assertEquals("Body is not empty", received, null);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private Stock getStock(Long storeId, Long productId) {
        Stock product = null;
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + storeId + "/" + productId, null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            product = mapper.readValue(received, Stock.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return product;
    }
}
