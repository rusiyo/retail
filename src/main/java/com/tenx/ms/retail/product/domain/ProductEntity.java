package com.tenx.ms.retail.product.domain;

import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.rest.dto.Store;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
public class ProductEntity {

    public ProductEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "sku")
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

    public void setStoreId(Long store) {
        this.storeId = store;
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
}
