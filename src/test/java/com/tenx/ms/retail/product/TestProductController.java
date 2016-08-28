package com.tenx.ms.retail.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.product.rest.dto.Product;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
public class TestProductController extends AbstractIntegrationTest {

    private static final String API_VERSION = RestConstants.VERSION_ONE;
    private static final String REQUEST_URI = "%s" + API_VERSION + "/products/";
    private final RestTemplate template = new TestRestTemplate();
    @Autowired
    private ObjectMapper mapper;
    @Value("classpath:productTests/success/create-product-request.json")
    private File goodRequest1;
    @Value("classpath:productTests/error/sku.json")
    private File badRequest1;
    @Value("classpath:productTests/error/price.json")
    private File badRequest2;
    @Value("classpath:productTests/error/sku-short.json")
    private File badRequest3;
    @Value("classpath:productTests/error/missing-description.json")
    private File badRequest4;
    @Value("classpath:productTests/error/missing-name.json")
    private File badRequest5;
    @Value("classpath:productTests/error/missing-price.json")
    private File badRequest6;
    @Value("classpath:productTests/error/missing-sku.json")
    private File badRequest7;
    @Value("classpath:productTests/error/missing-store.json")
    private File badRequest8;

    @Test
    @FlywayTest
    public void getProductsInNonExistingStore() {
        ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + "5006", null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void badCreateProduct412() {
        List<File> validationFiles = Arrays.asList(badRequest1, badRequest3, badRequest4, badRequest5, badRequest6, badRequest7, badRequest8);
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
    public void badCreateProduct400() {
        List<File> validationFiles = Arrays.asList(badRequest2);
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
    public void testCreateProduct() {
        Long storeId = Long.valueOf("1");
        Integer productId = createProduct(goodRequest1);
        // try to get the inserted product and verify its content
        Product product = getProduct(storeId, productId.longValue());
        assertNotNull("Product cannot be null", product);
        validateDescription(product, "Awesome sound system");
        validateName(product, "BOSE Home Theater");
        validatePrice(product, new BigDecimal("3500.51"));
        validateProductId(product, productId.longValue());
        validateSKU(product, "ABC12345");
        validateStoreId(product, storeId);
        validateStoreProducts(storeId);
    }

    private void validateStoreProducts(Long storeId) {
        List<Product> storeProducts = getProductsByStore(storeId);
        for (Product storeProduct : storeProducts) {
            assertEquals("Product's StoreId is incorrect", storeProduct.getStoreId(), storeId);
        }
    }

    private void validateName(Product product, String expected) {
        assertEquals("Product's Name is incorrect", product.getName(), expected);
    }

    private void validateDescription(Product product, String expected) {
        assertEquals("Product's Description is incorrect", product.getDescription(), expected);
    }

    private void validateSKU(Product product, String expected) {
        assertEquals("Product's Sku is incorrect", product.getSku(), expected);
    }

    private void validatePrice(Product product, BigDecimal expected) {
        if (expected == null) {
            assertNull("Product's Price is incorrect", product.getPrice());
        } else {
            assertSame("Product's Price is incorrect", product.getPrice().compareTo(expected), 0);
        }
    }

    private void validateProductId(Product product, Long expected) {
        assertEquals("Product's ProductId is incorrect", product.getProductId(), expected);
    }

    private void validateStoreId(Product product, Long expected) {
        assertEquals("Product's StoreId is incorrect", product.getStoreId(), expected);
    }

    private Integer createProduct(File file) {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(file), HttpMethod.POST);
            String received = response.getBody();
            ResourceCreated<Integer> rc = mapper.readValue(received, ResourceCreated.class);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            return rc.getId();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
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
            products = mapper.readValue(received, new TypeReference<List<Product>>() {
            });
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return products;
    }
}
