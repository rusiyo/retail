package com.tenx.ms.retail.product.util;


import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.rest.dto.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    public ProductEntity convertToProductEntity(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(product.getProductId());
        productEntity.setStoreId(product.getStoreId());
        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setSku(product.getSku());
        return productEntity;
    }

    public Product convertToProductDTO(ProductEntity productEntity) {
        Product product = new Product();
        product.setProductId(productEntity.getProductId());
        product.setStoreId(productEntity.getStoreId());
        product.setName(productEntity.getName());
        product.setDescription(productEntity.getDescription());
        product.setPrice(productEntity.getPrice());
        product.setSku(productEntity.getSku());
        return product;
    }
}
