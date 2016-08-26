package com.tenx.ms.retail.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class TestProductController extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Value("classpath:productTests/create-product-request.json")
    private File createProductRequest;

    @Value("classpath:productTests/bad-create-product-request.json")
    private File badCreateProductRequest;

    private static final String API_VERSION = RestConstants.VERSION_ONE;

    private static final String REQUEST_URI = "%s" + API_VERSION + "/products/";

    private final RestTemplate template = new TestRestTemplate();

    @Test
    public void getProductsInNonExistingStore() {
        ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + "5006", null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void badCreateProduct() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(badCreateProductRequest), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createProduct() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(createProductRequest), HttpMethod.POST);
            String received = response.getBody();
            Long storeId = new Long(1);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            ResourceCreated<Integer> rc = mapper.readValue(received, ResourceCreated.class);
            Integer productId = rc.getId();
            // try to get the inserted product and verify its content
            Product product = getProduct(storeId, productId.longValue());
            assertNotNull(product);
            assertEquals("Product's Name is incorrect", product.getName(), "BOSE Home Theater");
            assertEquals("Product's Description is incorrect", product.getDescription(), "Awesome sound system");
            assertEquals("Product's Sku is incorrect", product.getSku(), "ABC12345");
            assertTrue("Product's Price is incorrect", product.getPrice().compareTo(new BigDecimal("3500.51")) == 0);
            assertEquals("Product's ProductId is incorrect", product.getProductId(), (Long) productId.longValue());
            assertEquals("Product's StoreId is incorrect", product.getStoreId(), storeId);

            List<Product> storeProducts = getProductsByStore(storeId);
            for (Product storeProduct : storeProducts) {
                assertEquals("Product's StoreId is incorrect", storeProduct.getStoreId(), storeId);
            }

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private Product getProduct(Long storeId, Long productId) {
        Product product = null;
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + storeId + "/" + productId, null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            product = mapper.readValue(received, Product.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return product;
    }

    private List<Product> getProductsByStore(Long storeId) {
        List<Product> products = null;
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + storeId, null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            products = mapper.readValue(received,  new TypeReference<List<Product>>(){});
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return products;
    }
}
