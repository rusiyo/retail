package com.tenx.ms.retail.store.util;


import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter {

    public StoreEntity convertToStoreEntity(Store store) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setLocation(store.getLocation());
        storeEntity.setManagerName(store.getManagerName());
        storeEntity.setName(store.getName());
        storeEntity.setPhone(store.getPhone());
        return storeEntity;
    }

    public Store convertToStoreDTO(StoreEntity storeEntity) {
        Store store = new Store();
        store.setStoreId(storeEntity.getStoreId());
        store.setName(storeEntity.getName());
        store.setPhone(storeEntity.getPhone());
        store.setManagerName(storeEntity.getManagerName());
        store.setLocation(storeEntity.getLocation());
        return store;
    }
}
