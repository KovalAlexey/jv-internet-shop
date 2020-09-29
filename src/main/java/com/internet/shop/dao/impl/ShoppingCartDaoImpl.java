package com.internet.shop.dao.impl;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.db.Storage;
import com.internet.shop.model.ShoppingCart;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        Storage.addShoppingCart(shoppingCart);
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> get(Long itemId) {
        return Storage.carts.stream()
                .filter(cart -> cart.getId().equals(itemId))
                .findFirst();
    }

    @Override
    public Optional<ShoppingCart> getByUserId(Long id) {
        return Storage.carts.stream()
                .filter(cart -> cart.getUserId().equals(id))
                .findFirst();
    }

    @Override
    public List<ShoppingCart> getAll() {
        return Storage.carts;
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        IntStream.range(0, Storage.carts.size())
                .filter(index -> Storage.carts.get(index).getId().equals(shoppingCart.getId()))
                .forEach(index -> Storage.carts.set(index, shoppingCart));
        return shoppingCart;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.carts.removeIf(cart -> cart.getId().equals(id));
    }
}
