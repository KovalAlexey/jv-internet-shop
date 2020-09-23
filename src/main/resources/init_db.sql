CREATE SCHEMA `internet-shop` DEFAULT CHARACTER SET utf8;

CREATE TABLE `internet-shop`.`products`
(
    `product_id`    BIGINT          NOT NULL AUTO_INCREMENT,
    `product_name`  VARCHAR(256)    NOT NULL,
    `product_price` DOUBLE ZEROFILL NOT NULL,
    `is_deleted`    TINYINT         NULL DEFAULT 0,
    PRIMARY KEY (`product_id`)
);

/*
 users table
 */

CREATE TABLE `internet-shop`.`users`
(
    `user_id`    BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `user_name`  VARCHAR(256) NOT NULL,
    `user_login` VARCHAR(256) NOT NULL,
    `password`   VARCHAR(256) NOT NULL,
    `is_deleted` TINYINT      NULL DEFAULT 0,
    PRIMARY KEY (`user_id`)
);

/*
 shopping cart table
 */

CREATE TABLE `internet-shop`.`shopping_carts`
(
    `cart_id`    BIGINT(11) NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT(11) NOT NULL,
    `is_deleted` TINYINT    NULL DEFAULT 0,
    PRIMARY KEY (`cart_id`),
    CONSTRAINT `user_cart`
        FOREIGN KEY (`user_id`)
            REFERENCES `internet-shop`.`users` (`user_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

/*
 Shopping cart products relation table
 */

CREATE TABLE `internet-shop`.`shopping_carts_products`
(
    `cart_id`    BIGINT(11) NOT NULL,
    `product_id` BIGINT(11) NOT NULL,
    CONSTRAINT `cart`
        FOREIGN KEY (`cart_id`)
            REFERENCES `internet-shop`.`shopping_carts` (`cart_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `product`
        FOREIGN KEY (`product_id`)
            REFERENCES `internet-shop`.`products` (`product_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

/*
 Orders table
 */

CREATE TABLE `internet-shop`.`orders`
(
    `order_id`   BIGINT(11) NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT(11) NOT NULL,
    `is_deleted` TINYINT    NULL DEFAULT 0,
    PRIMARY KEY (`order_id`),
    CONSTRAINT `user_order`
        FOREIGN KEY (`user_id`)
            REFERENCES `internet-shop`.`users` (`user_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

/*
 Orders product relation table
 */

CREATE TABLE `internet-shop`.`orders_products`
(
    `order_id`   BIGINT(11) NOT NULL,
    `product_id` BIGINT(11) NOT NULL,
    CONSTRAINT `order`
        FOREIGN KEY (`order_id`)
            REFERENCES `internet-shop`.`orders` (`order_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `product`
        FOREIGN KEY (`product_id`)
            REFERENCES `internet-shop`.`products` (`product_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

/*
 Role table
 */

CREATE TABLE `internet-shop`.`roles`
(
    `role_id`   BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `role_name` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`role_id`)
);

INSERT INTO `roles`(role_name)
VALUES ('ADMIN');
INSERT INTO `roles`(role_name)
VALUES ('USER');

/*
 users_role relation table
 */

CREATE TABLE `internet-shop`.`users_roles`
(
    `user_id` BIGINT(11) NOT NULL,
    `role_id` BIGINT(11) NOT NULL,
    CONSTRAINT `user_roles`
        FOREIGN KEY (`user_id`)
            REFERENCES `internet-shop`.`users` (`user_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `role_roles`
        FOREIGN KEY (`role_id`)
            REFERENCES `internet-shop`.`roles` (`role_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
