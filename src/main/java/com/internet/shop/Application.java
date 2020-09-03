package com.internet.shop;

import com.internet.shop.db.Storage;
import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;
import java.util.List;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);

        Product phone = new Product("iPhoneX", 1000);
        Product notebook = new Product("MacBook", 2000);
        Product desktop = new Product("iMac", 1800);

        productService.create(phone);
        productService.create(notebook);
        productService.create(desktop);

        List<Product> productList = Storage.getAllProducts();

        productService.delete(phone.getId());

        System.out.println(productList);
    }
}
