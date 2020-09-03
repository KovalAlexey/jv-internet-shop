package com.internet.shop.model;

import java.util.List;

public class ShoppingCart {
    private Long id;
    private List<Product> products;
    private Long userId;

    public ShoppingCart(Long id, List<Product> products, Long userId) {
        this.id = id;
        this.products = products;
        this.userId = userId;
    }
}
