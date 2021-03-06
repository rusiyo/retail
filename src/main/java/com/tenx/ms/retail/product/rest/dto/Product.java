package com.tenx.ms.retail.product.rest.dto;

import com.tenx.ms.commons.validation.constraints.DollarAmount;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class Product {

    @ApiModelProperty(value = "The Product ID", readOnly = true)
    private Long productId;

    @ApiModelProperty(value = "The Store where the product is registered", readOnly = true)
    @NotNull
    private Long storeId;

    @ApiModelProperty("The name of the Product")
    @NotNull
    private String name;

    @ApiModelProperty("Product description")
    @NotNull
    private String description;

    @ApiModelProperty("Product price")
    @DollarAmount
    @NotNull
    private BigDecimal price;

    @ApiModelProperty("The phone to contact the store")
    @Size(min = 5, max = 10)
    @Pattern(regexp = "[a-zA-Z0-9]*$")
    @NotNull
    private String sku;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String toString() {
        return "Store #" + storeId.toString() + " " + name;
    }
}
