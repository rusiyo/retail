package com.tenx.ms.retail.product.rest.dto;

import com.tenx.ms.commons.validation.constraints.DollarAmount;
import com.tenx.ms.retail.store.rest.dto.Store;
import io.swagger.annotations.ApiModelProperty;
import org.apache.avro.reflect.Nullable;
import org.apache.avro.specific.FixedSize;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class Product {

    @ApiModelProperty(value = "The Product ID", readOnly = true)
    private Long productId;

    @ApiModelProperty(value = "The Store where the product is registered", readOnly = true)
    private Long storeId;

    @ApiModelProperty("The name of the Product")
    @Nullable
    private String name;

    @ApiModelProperty("Product description")
    @Nullable
    private String description;

    @ApiModelProperty("Product price")
    @DollarAmount
    @Nullable
    private BigDecimal price;

    @ApiModelProperty("The phone to contact the store")
    @Size(min = 5, max = 10)
    @Pattern(regexp = "[a-zA-Z0-9]*$")
    @Nullable
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
