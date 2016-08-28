package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by javier on 8/28/16.
 */
public class OrderProduct {

    @ApiModelProperty(value = "The Id of the order", readOnly = true)
    private Long orderId;
    @ApiModelProperty(value = "The Id of the product", readOnly = true)
    @NotNull
    private Long productId;
    @ApiModelProperty(value = "The quantity of product that the order includes")
    @NotNull
    private Integer count;

    public OrderProduct() {
    }

    public OrderProduct(Long orderId, Long productId, Integer count) {
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getCount() {
        return count;
    }
}
