package com.tenx.ms.retail.stock.service;

import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.util.StockConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Optional<Stock> findOneByStoreIdAndProductId(Long storeId, Long productId) {
        return stockRepository.findOneByStoreIdAndProductId(storeId, productId).map(product -> stockConverter.convertToStockDTO(product));
    }


}

