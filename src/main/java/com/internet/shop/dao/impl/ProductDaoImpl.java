package com.internet.shop.dao.impl;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.db.Storage;
import com.internet.shop.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ProductDaoImpl implements ProductDao {
    @Override
    public Product create(Product product) {
        Storage.addProduct(product);
        return product;
    }

    @Override
    public Optional<Product> get(Long productId) {
        return Storage.products.stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst();
    }

    @Override
    public Product update(Product product) {
        IntStream.range(0, Storage.products.size())
                .filter(index -> Storage.products.get(index).getId().equals(product.getId()))
                .forEach(item -> Storage.products.set(item, product));
        return product;
    }

    @Override
    public boolean delete(Long productId) {
        return Storage.products.removeIf(item -> item.getId().equals(productId));
    }

    @Override
    public List<Product> getAll() {
        return Storage.products;
    }
}
