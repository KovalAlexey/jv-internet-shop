package com.internet.shop.model;

public class User {
    private Long id;
    private String name;
    private String login;
    private String password;

    public User(Long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }
}
