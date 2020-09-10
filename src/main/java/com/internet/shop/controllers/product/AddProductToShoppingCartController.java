package com.internet.shop.controllers.product;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductToShoppingCartController extends HttpServlet {
    private static final Long USER_ID = 1L;
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private ShoppingCartService cartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private ProductService productService =
            (ProductService) injector.getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long productId = Long.valueOf(req.getParameter("id"));

        Product product = productService.get(productId);
        ShoppingCart cart = cartService.getByUserId(USER_ID);
        cartService.addProduct(cart, product);

        resp.sendRedirect(req.getContextPath() + "/products/all");
    }
}
