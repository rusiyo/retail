package com.tenx.ms.retail.order.repository;

import com.tenx.ms.retail.order.domain.OrderEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long> {

}
