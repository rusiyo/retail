package com.tenx.ms.retail.order.util;


import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.domain.OrderProductEntity;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderProduct;
import com.tenx.ms.retail.order.rest.dto.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderConverter {

    public OrderEntity convertToOrderEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity(order.getOrderId(), order.getStoreId());
        orderEntity.setPhone(order.getPhone());
        orderEntity.setEmail(order.getEmail());
        orderEntity.setFirstName(order.getFirstName());
        orderEntity.setLastName(order.getLastName());
        orderEntity.setOrderDate(order.getOrderDate());
        orderEntity.setStatusId(order.getStatus().getId());
        orderEntity.setProducts(order.getProducts().stream().map(e -> convertToOrderProductEntity(e, orderEntity)).collect(Collectors.toList()));
        return orderEntity;
    }

    public Order convertToOrderDTO(OrderEntity orderEntity) {
        Order order = new Order(orderEntity.getOrderId(), orderEntity.getStoreId());
        order.setPhone(orderEntity.getPhone());
        order.setEmail(orderEntity.getEmail());
        order.setFirstName(orderEntity.getFirstName());
        order.setLastName(orderEntity.getLastName());
        order.setOrderDate(orderEntity.getOrderDate());
        order.setStatus(OrderStatus.getOrderStatusById(orderEntity.getStatusId()));
        order.setProducts(orderEntity.getProducts().stream().map(e -> convertToOrderProductDTO(e)).collect(Collectors.toList()));
        return order;
    }

    public OrderProduct convertToOrderProductDTO(OrderProductEntity product) {
        return new OrderProduct(product.getOrder().getOrderId(), product.getProductId(), product.getCount());
    }

    public OrderProductEntity convertToOrderProductEntity(OrderProduct product, OrderEntity order) {
        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setOrder(order);
        orderProductEntity.setProductId(product.getProductId());
        orderProductEntity.setCount(product.getCount());
        return orderProductEntity;
    }
}
