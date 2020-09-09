package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private UserService userService = (UserService) injector.getInstance(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User alice = new User("Alice");
        User bob = new User("Bob");

        userService.create(alice);
        userService.create(bob);
        List<User> userList = this.userService.getAll();

        req.setAttribute("users", userList);
        req.getRequestDispatcher("/WEB-INF/views/users/all.jsp").forward(req, resp);

    }
}
