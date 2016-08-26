package com.tenx.ms.retail.product.service;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.util.ProductConverter;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.rest.dto.Store;
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
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductConverter converter;

    public List<Product> getProductsByStoreId(Long storeId, Pageable pageable) {
        return productRepository.findAllByStoreId(storeId, pageable).getContent().stream().map(product -> converter.convertToProductDTO(product)).collect(Collectors.toList());
    }

    public Optional<Product> getProductByName(String name) {
        return productRepository.findOneByName(name).map(product -> converter.convertToProductDTO(product));
    }

    public Optional<Product> getProductById(Long storeId, Long productId) {
        return productRepository.findOneByStoreIdAndProductId(storeId, productId).map(product -> converter.convertToProductDTO(product));
    }

    @Transactional
    public Long createProduct(Product product) {
        LOGGER.debug("Create new {}", product);
        ProductEntity productEntity = converter.convertToProductEntity(product);
        productEntity = productRepository.save(productEntity);
        return productEntity.getProductId();
    }
}

