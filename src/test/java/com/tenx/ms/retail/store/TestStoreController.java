package com.tenx.ms.retail.orders.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class TestStoreController extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Value("classpath:storeTests/errors/phone.json")
    private File badRequest1;

    @Value("classpath:storeTests/success/create-store-request.json")
    private File goodRequest1;

    @Value("classpath:storeTests/success/minimal.json")
    private File goodRequest2;

    private static final String API_VERSION = RestConstants.VERSION_ONE;

    private static final String REQUEST_URI = "%s" + API_VERSION + "/stores/";

    private final RestTemplate template = new TestRestTemplate();

    @Test
    public void getStores() {
        ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getStore() {
        ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + "5006", null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testValidationOnAllFieldsForCreateStore() {
        List<File> validationFiles = Arrays.asList(badRequest1);
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
    public void testCreateStore() {
        Long storeId = createStore(goodRequest1).longValue();
        Store store = getStore(storeId);
        assertNotNull(store);
        validateLocation(store);
        validateManagerName(store);
        validateName(store);
        validatePhone(store);
    }

    @Test
    public void testCreateStoreMinimal() {
        Long storeId = createStore(goodRequest2).longValue();
        Store store = getStore(storeId);
        assertNotNull(store);
        validateStoreId(store, storeId);
    }

    private void validatePhone(Store store){
        assertEquals("Store's Phone is incorrect", store.getPhone(), "786-955-4232");
    }

    private void validateStoreId(Store store, Long storeId){
        assertEquals("Store's Phone is incorrect", store.getStoreId(), storeId);
    }

    private void validateManagerName(Store store){
        assertEquals("Store's Manage Name is incorrect", store.getManagerName(), "John Target");
    }

    private void validateName(Store store){
        assertEquals("Store's Name is incorrect", store.getName(), "Target");
    }

    private void validateLocation(Store store){
        assertEquals("Store's Location is incorrect", store.getLocation(), "11253 Pines Blvd, Hollywood, FL 33026");
    }

    private Integer createStore(File file) {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(file), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            ResourceCreated<Integer> rc = mapper.readValue(received, ResourceCreated.class);
            return rc.getId();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private Store getStore(Long storeId) {
        Store store = null;
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + storeId, null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            store = mapper.readValue(received, Store.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return store;
    }
}
