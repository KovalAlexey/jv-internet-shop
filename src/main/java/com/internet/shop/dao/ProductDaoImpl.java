package com.internet.shop.dao;

import com.internet.shop.db.Storage;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import java.util.List;
import java.util.Optional;

@Dao
public class ProductDaoImpl implements ProductDao {
    @Override
    public Product create(Product product) {
        Storage.addProduct(product);
        return product;
    }

    @Override
    public Optional<Product> getById(Long productId) {
        return Storage.getAllProducts().stream()
                .filter(x -> x.getId().equals(productId))
                .findFirst();
    }

    @Override
    public Product update(Product product) {
        Optional<Product> forUpdate = getById(product.getId());
        if (forUpdate.isPresent()) {
            int index = Storage.getAllProducts().indexOf(forUpdate.get());
            Storage.getAllProducts().set(index, product);
        }
        return product;
    }

    @Override
    public boolean deleteById(Long productId) {
        Optional<Product> forDelete = getById(productId);
        return forDelete.filter(this::deleteItem).isPresent();
    }

    @Override
    public boolean deleteItem(Product product) {
        return Storage.remove(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return Storage.getAllProducts();
    }
}
