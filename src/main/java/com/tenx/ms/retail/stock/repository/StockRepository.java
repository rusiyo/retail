package com.tenx.ms.retail.stock.repository;

import com.tenx.ms.retail.stock.domain.StockEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StockRepository extends PagingAndSortingRepository<StockEntity, Long> {

    Optional<StockEntity> findOneByStoreIdAndProductId(final Long storeId, final Long productId);

}
