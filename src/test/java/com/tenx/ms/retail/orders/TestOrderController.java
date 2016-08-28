package com.tenx.ms.retail.orders;

import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import org.apache.commons.io.FileUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class TestOrderController extends AbstractIntegrationTest {

    @Value("classpath:orderTests/errors/no_date.json")
    private File badRequest1;

    @Value("classpath:orderTests/errors/no_email.json")
    private File badRequest2;

    @Value("classpath:orderTests/errors/no_first_name.json")
    private File badRequest3;

    @Value("classpath:orderTests/errors/no_last_name.json")
    private File badRequest4;

    @Value("classpath:orderTests/errors/no_phone.json")
    private File badRequest5;

    @Value("classpath:orderTests/errors/no_products.json")
    private File badRequest6;

    @Value("classpath:orderTests/errors/no_status.json")
    private File badRequest7;

    @Value("classpath:orderTests/errors/no_store.json")
    private File badRequest8;

    @Value("classpath:orderTests/errors/invalid.json")
    private File invalidRequest1;

    @Value("classpath:orderTests/success/success.json")
    private File goodRequest1;

    private static final String API_VERSION = RestConstants.VERSION_ONE;

    private static final String REQUEST_URI = "%s" + API_VERSION + "/orders/";

    private final RestTemplate template = new TestRestTemplate();

    @Test
    @FlywayTest
    public void testValidationOnAllFieldsForCreateOrder() {
        List<File> validationFiles = Arrays.asList(badRequest1, badRequest2, badRequest3, badRequest4, badRequest5, badRequest6, badRequest7, badRequest8);
        for (File file : validationFiles) {
            try {
                ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(file), HttpMethod.POST);
                assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    @FlywayTest
    public void testInvalidCreateOrder() {
        List<File> validationFiles = Arrays.asList(invalidRequest1);
        for (File file : validationFiles) {
            try {
                ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(file), HttpMethod.POST);
                assertEquals("HTTP Status code incorrect", HttpStatus.BAD_REQUEST, response.getStatusCode());
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    @FlywayTest
    public void testCreateOrder() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(goodRequest1), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            assertEquals("Body is not empty", received, null);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
