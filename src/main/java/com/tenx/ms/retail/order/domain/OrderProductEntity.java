package com.tenx.ms.retail.order.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_product")
public class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_product_id")
    private Long orderProductId;

    @ManyToOne
    @JoinColumn(name = "order_id", updatable = false)
    private OrderEntity order;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "count")
    private Integer count;

    public Long getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Long orderProductId) {
        this.orderProductId = orderProductId;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public Long getProductId() {
        return productId;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }
}
