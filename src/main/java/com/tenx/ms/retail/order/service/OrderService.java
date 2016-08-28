package com.tenx.ms.retail.order.service;

import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.repository.OrderRepository;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.util.OrderConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderConverter orderConverter;

    @Transactional
    public void createOrder(Order order) {
        LOGGER.debug("Create new {}", order);
        OrderEntity orderEntity = orderConverter.convertToOrderEntity(order);
        orderRepository.save(orderEntity);
    }
}

