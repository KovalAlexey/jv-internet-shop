CREATE SCHEMA `online_store` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `internet-shop`.`products` (
  `product_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `product_name` VARCHAR(225) NOT NULL,
  `product_price` DOUBLE NOT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`product_id`));
