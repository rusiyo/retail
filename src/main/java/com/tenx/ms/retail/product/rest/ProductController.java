package com.tenx.ms.retail.product.rest;

import com.tenx.ms.commons.rest.AbstractController;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.exceptions.UpdateViolationException;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.store.service.StoreService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value = "product", description = "This provides the ability to create, update and delete new Products.")
@RestController("productControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/products")
public class ProductController extends AbstractController{
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of product in store"),
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
    @RequestMapping(method = RequestMethod.POST)
    public ResourceCreated<Long> createProduct(@ApiParam(name = "product", value = "The product entity", required = true) @Validated @RequestBody Product product){
        LOGGER.debug("Creating product {}", product);
        return new ResourceCreated<Long>(productService.createProduct(product));
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(UpdateViolationException.class)
    protected void handleUpdateViolationException(UpdateViolationException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage());
    }
}
