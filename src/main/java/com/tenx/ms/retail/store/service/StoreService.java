package com.tenx.ms.retail.store.service;

import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.util.StoreConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreConverter storeConverter;

    public Optional<Store> getStoreById(Long storeId) {
        return storeRepository.findOneByStoreId(storeId).map(store -> storeConverter.convertToStoreDTO(store));
    }

    public List<Store> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable).getContent().stream().map(store -> storeConverter.convertToStoreDTO(store)).collect(Collectors.toList());
    }

    @Transactional
    public Long createStore(Store store) {
        LOGGER.debug("Create new {}", store);
        StoreEntity storeEntity = storeConverter.convertToStoreEntity(store);
        storeEntity = storeRepository.save(storeEntity);
        return storeEntity.getStoreId();
    }
}

