package com.tenx.ms.retail.order.rest;

import com.tenx.ms.commons.rest.AbstractController;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.exceptions.UpdateViolationException;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.service.OrderService;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "stock", description = "This provides the ability to create, update and delete new Orders.")
@RestController("orderControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/orders")
public class OrderController extends AbstractController{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Create Order")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful creation of order"),
        @ApiResponse(code = 412, message = "Validation failure."),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST)
    public void createOrder(@ApiParam(name = "order", value = "The order entity", required = true) @Validated @RequestBody Order order){
        LOGGER.debug("Creating order {}", order);
        orderService.createOrder(order);
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(UpdateViolationException.class)
    protected void handleUpdateViolationException(UpdateViolationException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage());
    }
}
