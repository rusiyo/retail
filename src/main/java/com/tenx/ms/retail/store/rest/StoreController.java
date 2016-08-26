package com.tenx.ms.retail.orders.rest.dto;

import com.tenx.ms.commons.rest.AbstractController;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.exceptions.UpdateViolationException;
import com.tenx.ms.retail.store.rest.dto.Store;
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

@Api(value = "store", description = "This provides the ability to create, update and delete new Stores.")
@RestController("storeControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stores")
public class StoreController extends AbstractController{
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "Get all stores")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of stores"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET)
    public List<Store> getStores(Pageable pageable) {
        LOGGER.debug("Fetching Stores");

        return storeService.getAllStores(pageable);
    }

    @ApiOperation(value = "")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful creation of stores"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.GET)
    public Object getStoreById(@ApiParam(name = "storeId", value = "The id of the store to fetch") @PathVariable long storeId) {
        LOGGER.debug("Fetching Store by Id {}", storeId);
        return storeService.getStoreById(storeId).get();
    }

    @ApiOperation(value = "Create Store")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful creation of store"),
        @ApiResponse(code = 412, message = "Validation failure."),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST)
    public ResourceCreated<Long> createStore(@ApiParam(name = "store", value = "The store entity", required = true) @Validated @RequestBody Store store){
        LOGGER.debug("Creating store {}", store);
        return new ResourceCreated<Long>(storeService.createStore(store));
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(UpdateViolationException.class)
    protected void handleUpdateViolationException(UpdateViolationException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage());
    }
}
