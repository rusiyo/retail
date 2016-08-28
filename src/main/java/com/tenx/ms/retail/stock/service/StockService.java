package com.tenx.ms.retail.stock.service;

import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.util.StockConverter;
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
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockConverter stockConverter;

    @Transactional
    public void createOrUpdateStock(Stock stock) {
        StockEntity stockEntity;
        LOGGER.debug("Create new {}", stock);
        Optional<StockEntity> existingStockOptional = stockRepository.findOneByStoreIdAndProductId(stock.getStoreId(), stock.getProductId());
        if (existingStockOptional.isPresent()) {
            stockEntity = existingStockOptional.get();
            stockEntity.setCount(stock.getCount());
        } else {
            stockEntity = stockConverter.convertToStockEntity(stock);
        }
        stockRepository.save(stockEntity);
    }
}

