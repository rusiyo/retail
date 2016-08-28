package com.tenx.ms.retail.stock.domain;

import javax.persistence.*;

@Entity
@Table(name = "stock")
@IdClass(StockEntityPK.class)
public class StockEntity {

    public StockEntity(Long storeId, Long productId, Integer count) {
        this.storeId = storeId;
        this.productId = productId;
        this.count = count;
    }

    public StockEntity() {

    }

    @Id
    @Column(name = "store_id")
    private Long storeId;

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "count")
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getStoreId() {
        return storeId;
    }

}
