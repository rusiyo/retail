package com.tenx.ms.retail.product.repository;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {
    Optional<ProductEntity> findOneByStoreIdAndProductId(final Long storeId, final Long productId);

    Optional<ProductEntity> findOneByName(final String name);

    Page<ProductEntity> findAllByStoreId(final Long storeId, Pageable pageable);
}
