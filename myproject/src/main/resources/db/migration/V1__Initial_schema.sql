-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema food_delivery
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema food_delivery
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `food_delivery` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `food_delivery` ;

-- -----------------------------------------------------
-- Table `food_delivery`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `food_delivery`.`users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255) NOT NULL,
  `is_active` BIT(1) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255) NOT NULL,
  `role` ENUM('CUSTOMER', 'RESTAURANT', 'DELIVERY_PARTNER') NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `UK_6dotkott2kjsp8vw4d0m25fb7` (`email` ASC) VISIBLE,
  UNIQUE INDEX `UK_r43af9ap4edm43mmtq01oddj6` (`username` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `food_delivery`.`restaurants`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `food_delivery`.`restaurants` (
  `restaurant_id` BIGINT NOT NULL AUTO_INCREMENT,
  `closing_time` VARCHAR(255) NOT NULL,
  `delivery_radius` INT NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `logo_url` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `opening_time` VARCHAR(255) NOT NULL,
  `user_id` BIGINT NOT NULL,
  `is_active` BIT(1) NOT NULL,
  PRIMARY KEY (`restaurant_id`),
  UNIQUE INDEX `UK_jh0uq0ansrvx9k5me0lvdsvmt` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK18q23rj2s35r80ot3oag4ivo6`
    FOREIGN KEY (`user_id`)
    REFERENCES `food_delivery`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `food_delivery`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `food_delivery`.`orders` (
  `order_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `delivery_address` VARCHAR(255) NOT NULL,
  `status` ENUM('PENDING', 'ACCEPTED', 'PREPARING', 'READY_FOR_DELIVERY', 'ON_THE_WAY', 'DELIVERED', 'CANCELLED') NULL DEFAULT NULL,
  `total_amount` DOUBLE NOT NULL,
  `customer_id` BIGINT NOT NULL,
  `delivery_partner_id` BIGINT NULL DEFAULT NULL,
  `restaurant_id` BIGINT NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `FKsjfs85qf6vmcurlx43cnc16gy` (`customer_id` ASC) VISIBLE,
  INDEX `FKtb0olpml31x7swbu0hleci67x` (`delivery_partner_id` ASC) VISIBLE,
  INDEX `FK2m9qulf12xm537bku3jnrrbup` (`restaurant_id` ASC) VISIBLE,
  CONSTRAINT `FK2m9qulf12xm537bku3jnrrbup`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `food_delivery`.`restaurants` (`restaurant_id`),
  CONSTRAINT `FKsjfs85qf6vmcurlx43cnc16gy`
    FOREIGN KEY (`customer_id`)
    REFERENCES `food_delivery`.`users` (`user_id`),
  CONSTRAINT `FKtb0olpml31x7swbu0hleci67x`
    FOREIGN KEY (`delivery_partner_id`)
    REFERENCES `food_delivery`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `food_delivery`.`delivery_tracking`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `food_delivery`.`delivery_tracking` (
  `tracking_id` BIGINT NOT NULL AUTO_INCREMENT,
  `current_location` VARCHAR(255) NULL DEFAULT NULL,
  `estimated_delivery_time` DATETIME(6) NULL DEFAULT NULL,
  `last_updated` DATETIME(6) NULL DEFAULT NULL,
  `status` ENUM('PICKED_UP', 'IN_TRANSIT', 'DELIVERED') NULL DEFAULT NULL,
  `delivery_partner_id` BIGINT NOT NULL,
  `order_id` BIGINT NOT NULL,
  PRIMARY KEY (`tracking_id`),
  UNIQUE INDEX `UK_8fj62epuoo54oh4er1xjkq7k5` (`order_id` ASC) VISIBLE,
  INDEX `FKllvflf17ij6gmork6wq7aw7bp` (`delivery_partner_id` ASC) VISIBLE,
  CONSTRAINT `FKj94c0yp9a0j4w50d37vdfcup6`
    FOREIGN KEY (`order_id`)
    REFERENCES `food_delivery`.`orders` (`order_id`),
  CONSTRAINT `FKllvflf17ij6gmork6wq7aw7bp`
    FOREIGN KEY (`delivery_partner_id`)
    REFERENCES `food_delivery`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `food_delivery`.`food_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `food_delivery`.`food_items` (
  `food_id` BIGINT NOT NULL AUTO_INCREMENT,
  `category` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `image_url` VARCHAR(255) NULL DEFAULT NULL,
  `is_available` BIT(1) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `price` DOUBLE NOT NULL,
  `restaurant_id` BIGINT NOT NULL,
  PRIMARY KEY (`food_id`),
  INDEX `FK1f3re9f14rtkoyghyuhx3cv9l` (`restaurant_id` ASC) VISIBLE,
  CONSTRAINT `FK1f3re9f14rtkoyghyuhx3cv9l`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `food_delivery`.`restaurants` (`restaurant_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `food_delivery`.`order_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `food_delivery`.`order_items` (
  `order_item_id` BIGINT NOT NULL AUTO_INCREMENT,
  `price_per_unit` DOUBLE NOT NULL,
  `quantity` INT NOT NULL,
  `special_instructions` VARCHAR(255) NULL DEFAULT NULL,
  `food_id` BIGINT NOT NULL,
  `order_id` BIGINT NOT NULL,
  PRIMARY KEY (`order_item_id`),
  INDEX `FKhir9uomm036swc4s00658ioke` (`food_id` ASC) VISIBLE,
  INDEX `FKbioxgbv59vetrxe0ejfubep1w` (`order_id` ASC) VISIBLE,
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w`
    FOREIGN KEY (`order_id`)
    REFERENCES `food_delivery`.`orders` (`order_id`),
  CONSTRAINT `FKhir9uomm036swc4s00658ioke`
    FOREIGN KEY (`food_id`)
    REFERENCES `food_delivery`.`food_items` (`food_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `food_delivery`.`payments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `food_delivery`.`payments` (
  `payment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount` DOUBLE NOT NULL,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `method` ENUM('CARD', 'CASH_ON_DELIVERY', 'PAYPAL') NULL DEFAULT NULL,
  `status` ENUM('PENDING', 'COMPLETED', 'FAILED') NULL DEFAULT NULL,
  `transaction_id` VARCHAR(255) NULL DEFAULT NULL,
  `order_id` BIGINT NOT NULL,
  PRIMARY KEY (`payment_id`),
  UNIQUE INDEX `UK_8vo36cen604as7etdfwmyjsxt` (`order_id` ASC) VISIBLE,
  CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l`
    FOREIGN KEY (`order_id`)
    REFERENCES `food_delivery`.`orders` (`order_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
