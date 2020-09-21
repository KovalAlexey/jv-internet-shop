package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectDataController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private UserService userService = (UserService) injector.getInstance(UserService.class);
    private ProductService productService =
            (ProductService) injector.getInstance(ProductService.class);
    private ShoppingCartService cartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User admin = new User("admin");
        admin.setLogin("admin");
        admin.setPassword("123");
        admin.setRoles(Set.of(Role.of("ADMIN")));
        userService.create(admin);

        User bob = new User("Bob");
        bob.setLogin("bob");
        bob.setPassword("258");
        bob.setRoles(Set.of(Role.of("USER")));
        userService.create(bob);
        cartService.create(new ShoppingCart(bob.getId()));

        User alice = new User("Alice");
        alice.setLogin("alice");
        alice.setPassword("456");
        alice.setRoles(Set.of(Role.of("USER")));
        userService.create(alice);
        cartService.create((new ShoppingCart(alice.getId())));

//        productService.create(new Product("iPhone", 1200));
//        productService.create(new Product("MacBook", 2000));
//        productService.create(new Product("Apple Watch 6", 999));
//        productService.create(new Product("new iPad", 800));
        req.getRequestDispatcher("/WEB-INF/views/injectData.jsp").forward(req, resp);
    }
}
