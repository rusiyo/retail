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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class TestStoreController extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Value("classpath:storeTests/create-store-request.json")
    private File createStoreRequest;

    @Value("classpath:storeTests/bad-store-request.json")
    private File badStoreRequest;

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
    public void badCreateStore() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(badStoreRequest), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createStore() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(createStoreRequest), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            ResourceCreated<Integer> rc = mapper.readValue(received, ResourceCreated.class);
            Integer storeId = rc.getId();
            // try to get the inserted store and verify its content
            Store store = getStore(storeId.longValue());
            assertNotNull(store);
            assertEquals("Store's Location is incorrect", store.getLocation(), "11253 Pines Blvd, Hollywood, FL 33026");
            assertEquals("Store's Name is incorrect", store.getName(), "Target");
            assertEquals("Store's Manage Name is incorrect", store.getManagerName(), "John Target");
            assertEquals("Store's Phone is incorrect", store.getPhone(), "786-955-4232");
        } catch (IOException e) {
            fail(e.getMessage());
        }
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
