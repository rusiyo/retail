package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;


public class Stock {

    @ApiModelProperty(value = "The stock ID", readOnly = true)
    @NotNull
    private Long storeId;
    @ApiModelProperty(value = "The product ID", readOnly = true)
    @NotNull
    private Long productId;
    @ApiModelProperty(value = "The Total amount of product in the store", required = true)
    @NumberFormat
    @NotNull
    private Integer count;

    public Stock() {
    }

    public Stock(Long storeId, Long productId, Integer count) {
        this.storeId = storeId;
        this.productId = productId;
        this.count = count;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {

        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String toString() {
        return "Store #" + storeId + " Product Id: " + productId + " Total: " + count;
    }
}
