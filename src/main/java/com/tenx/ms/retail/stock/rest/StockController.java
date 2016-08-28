package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.rest.AbstractController;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.exceptions.UpdateViolationException;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
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

@Api(value = "stock", description = "This provides the ability to create, update and delete new Stocks.")
@RestController("stockControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stock")
public class StockController extends AbstractController{
    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Create/Update Stock")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful creation/update of stock"),
        @ApiResponse(code = 412, message = "Validation failure."),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST)
    public void createStock(@ApiParam(name = "stock", value = "The stock entity", required = true) @Validated @RequestBody Stock stock){
        LOGGER.debug("Creating stock {}", stock);
        stockService.createOrUpdateStock(stock);
    }

    @ApiOperation(value = "")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of product in store"),
        @ApiResponse(code = 404, message = "Product not found in Store"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(value = {"/{storeId:\\d+}/{productId:\\d+}"}, method = RequestMethod.GET)
    public Object getProductById(@ApiParam(value = "storeId", required = false) @PathVariable long storeId, @ApiParam(name = "productId", value = "The id of the product to fetch") @PathVariable long productId) {
        LOGGER.debug("Fetching Product by Id {}", productId);
        return stockService.findOneByStoreIdAndProductId(storeId, productId).get();
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(UpdateViolationException.class)
    protected void handleUpdateViolationException(UpdateViolationException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage());
    }
}
