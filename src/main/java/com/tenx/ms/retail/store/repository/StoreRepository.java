package com.tenx.ms.retail.store.repository;

import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StoreRepository extends PagingAndSortingRepository<StoreEntity, Long> {
    Optional<StoreEntity> findOneByStoreId(final Long storeId);

    Page<StoreEntity> findAll(Pageable pageable);
}
