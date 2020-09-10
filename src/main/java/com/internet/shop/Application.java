package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.util.List;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        final ProductService productService =
                (ProductService) injector.getInstance(ProductService.class);
        final OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final UserService userService = (UserService) injector.getInstance(UserService.class);

        /*
        Product testing

        * create products
        * get by id's
        * update price
        * delete
        * sout final list
         */

        Product phone = new Product("iPhoneX", 1000);
        Product notebook = new Product("MacBook", 2000);
        Product desktop = new Product("iMac", 1800);
        Product printer = new Product("Canon", 450);
        Product camera = new Product("GoPro", 800);

        List<Product> productList = List.of(phone, notebook, desktop, camera, printer);
        for (Product product : productList) {
            productService.create(product);
        }

        Product newDesktop = productService.get(desktop.getId());
        newDesktop.setPrice(1500);
        productService.update(newDesktop);

        productService.delete(phone.getId());

        productService.getAll().forEach(System.out::println);

        /*
        User testing

        * create users
        * get by id's
        * update password
        * delete
        * sout final list
         */

        User vasia = new User("Vasia", "vasia", "123");
        User petia = new User("Petia", "petia", "123");
        User masha = new User("Masha", "masha", "123");
        User dasha = new User("Dasha", "dasha", "123");
        User sasha = new User("Sasha", "sasha", "233");

        List<User> userList = List.of(vasia, petia, dasha, masha, sasha);
        for (User user : userList) {
            userService.create(user);
        }

        User newPassword = userService.get(vasia.getId());
        newPassword.setPassword("qwerty");
        userService.update(newPassword);

        userService.delete(sasha.getId());

        userService.getAll().forEach(System.out::println);

        /*
        ShoppingCart testing

        * create few shopping carts for users
        * add products to them
        * delete product from cart
        * delete whole cart
        * clean cart
        * sout current carts
         */

        Long vasiaId = vasia.getId();
        Long mashaId = masha.getId();

        ShoppingCart firstCart = new ShoppingCart(vasiaId);
        ShoppingCart secondCart = new ShoppingCart(mashaId);

        shoppingCartService.create(firstCart);
        shoppingCartService.create(secondCart); // would be cleared

        shoppingCartService.addProduct(firstCart, desktop);
        shoppingCartService.addProduct(firstCart, notebook);

        shoppingCartService.addProduct(secondCart, camera);
        shoppingCartService.addProduct(secondCart, printer);

        shoppingCartService.deleteProduct(secondCart, camera);

        shoppingCartService.clear(secondCart);

        shoppingCartService.getAll().forEach(System.out::println);

        /*
        Order testing

        * complete orders with previous carts
        * get order id
        * delete one of orders
        * sout current orders
         */

        Order firstOrder = orderService.completeOrder(firstCart);
        Order secondOrder = orderService.completeOrder(secondCart);

        Long secondOrderId = secondOrder.getId();

        orderService.delete(secondOrderId);

        orderService.getAll().forEach(System.out::println);
    }
}
