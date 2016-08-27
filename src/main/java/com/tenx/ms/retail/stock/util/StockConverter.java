package com.tenx.ms.retail.stock.util;


import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockConverter {

    public StockEntity convertToStockEntity(Stock stock) {
        return new StockEntity(stock.getStoreId(), stock.getProductId(), stock.getCount());
    }

    public Stock convertToStockDTO(StockEntity stockEntity) {
        return new Stock(stockEntity.getStoreId(), stockEntity.getProductId(), stockEntity.getCount());
    }
}
