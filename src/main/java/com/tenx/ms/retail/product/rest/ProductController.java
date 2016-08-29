package com.tenx.ms.retail.product.rest;

import com.tenx.ms.commons.rest.AbstractController;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.exceptions.UpdateViolationException;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value = "product", description = "This provides the ability to create, update and delete new Products.")
@RestController("productControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/products")
public class ProductController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.tenx.ms.retail.product.rest.ProductController.class);

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Get all products by store id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of products in store"), @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = {"/{storeId:\\d+}"})
    public List<Product> getProducts(@ApiParam(value = "storeId", required = false) Long storeId, Pageable pageable) {
        LOGGER.debug("Fetching Products");

        return productService.getProductsByStoreId(storeId, pageable);
    }

    @ApiOperation(value = "")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of product in store"),
        @ApiResponse(code = 404, message = "Product not found in Store"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(value = {"/{storeId:\\d+}/{productId:\\d+}"}, method = RequestMethod.GET)
    public Object getProductById(@ApiParam(value = "storeId", required = false) @PathVariable long storeId, @ApiParam(name = "productId", value = "The id of the product to fetch") @PathVariable long productId) {
        LOGGER.debug("Fetching Product by Id {}", productId);
        return productService.getProductById(storeId, productId).get();
    }

    @ApiOperation(value = "Create Product")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful creation of product"),
        @ApiResponse(code = 412, message = "Validation failure."),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST, value = {"/{storeId:\\d+}"})
    public ResourceCreated<Long> createProduct(@ApiParam(value = "storeId", required = false) Long storeId, @ApiParam(name = "product", value = "The product entity", required = true) @Validated @RequestBody Product product) {
        LOGGER.debug("Creating product {}", product);
        product.setStoreId(storeId);
        return new ResourceCreated<>(productService.createProduct(product));
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(UpdateViolationException.class)
    protected void handleUpdateViolationException(UpdateViolationException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage());
    }
}
