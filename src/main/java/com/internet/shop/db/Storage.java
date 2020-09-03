package com.internet.shop.db;

import com.internet.shop.model.Product;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static Long productId = 0L;
    private static final List<Product> products = new ArrayList<>();

    public static void addProduct(Product product) {
        productId++;
        product.setId(productId);
        products.add(product);
    }

    public static List<Product> getAllProducts() {
        return products;
    }

    public static boolean remove(Product product) {
        if (products.contains(product)) {
            products.remove(product);
            return true;
        }
        return false;
    }
}
