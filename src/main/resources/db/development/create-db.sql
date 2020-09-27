-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema alaajo_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `alaajo_db`;

-- ------------- Schema mydb

-- -----------------------------------------------------------------------------------------------
-- Schema alaajo_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `alaajo_db` DEFAULT CHARACTER SET latin1 ;
USE `alaajo_db` ;

-- -----------------------------------------------------
-- Table `alaajo_db`.`application_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`application_user` (
  `id` INT(11) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(150) NOT NULL,
  `role` VARCHAR(128) NOT NULL,
  `is_active` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`admin` (
  `admin_id` INT(11) NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(15) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `date_joined` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `password` VARCHAR(200) NOT NULL,
  `unique_account_id` VARCHAR(45) NOT NULL,
  `image_url` VARCHAR(120) NULL DEFAULT NULL,
  `bvn` VARCHAR(45) NULL DEFAULT NULL,
  `token` VARCHAR(100) NULL DEFAULT NULL,
  `application_user_id` INT(11) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC),
  INDEX `fk_saver_application_user1_idx` (`application_user_id` ASC),
  CONSTRAINT `fk_admin_application_user1`
    FOREIGN KEY (`application_user_id`)
    REFERENCES `alaajo_db`.`application_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`saver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`saver` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(15) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `date_joined` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `password` VARCHAR(200) NOT NULL,
  `unique_account_id` VARCHAR(45) NOT NULL,
  `image_url` VARCHAR(120) NULL DEFAULT NULL,
  `bvn` VARCHAR(45) NULL DEFAULT NULL,
  `token` VARCHAR(100) NULL DEFAULT NULL,
  `application_user_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC),
  INDEX `fk_saver_application_user1_idx` (`application_user_id` ASC),
  CONSTRAINT `fk_saver_application_user1`
    FOREIGN KEY (`application_user_id`)
    REFERENCES `alaajo_db`.`application_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 147
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`bank_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`bank_details` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `code` VARCHAR(11) NULL,
  `account_number` VARCHAR(20) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `recipient_code` VARCHAR(70) NULL DEFAULT NULL,
  `saver_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_bank_details_saver1_idx` (`saver_id` ASC),
  UNIQUE INDEX `account_number_UNIQUE` (`account_number` ASC),
  UNIQUE INDEX `recipient_code_UNIQUE` (`recipient_code` ASC),
CONSTRAINT `fk_bank_details_saver1`
    FOREIGN KEY (`saver_id`)
    REFERENCES `alaajo_db`.`saver` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`investment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`investment` (
  `invest_id` INT(11) NOT NULL AUTO_INCREMENT,
  `image_url` VARCHAR(120) NULL DEFAULT NULL,
  `invest_title` VARCHAR(45) NOT NULL,
  `minimum_amount` DOUBLE NOT NULL,
  `maximum_amount` DOUBLE NOT NULL,
  `units` INT(11) NOT NULL,
  `duration` INT(11) NOT NULL,
  `category` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `expected_return` DOUBLE NOT NULL,
  `description` text(2000) NOT NULL,
  PRIMARY KEY (`invest_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`password_reset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`password_reset` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(100) NOT NULL,
  `application_user_id` INT(11) NOT NULL,
  `expiry_date` BIGINT(20) NOT NULL,
  `is_password_reset` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `password_reset_id` (`id` ASC),
  INDEX `fk_password_reset_application_user_idx` (`application_user_id` ASC),
  CONSTRAINT `fk_password_reset_application_user1`
    FOREIGN KEY (`application_user_id`)
    REFERENCES `alaajo_db`.`application_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`saving_target`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`saving_target` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `amount` DOUBLE NOT NULL,
  `frequency` VARCHAR(45) NOT NULL,
  `start_date` DATE NOT NULL,
  `withdrawal_date` DATE NOT NULL,
  `image_url` VARCHAR(145) NULL DEFAULT NULL,
  `title` VARCHAR(45) NULL DEFAULT NULL,
  `saver_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_saving_target_saver1_idx` (`saver_id` ASC),
  CONSTRAINT `fk_saving_target_saver1`
    FOREIGN KEY (`saver_id`)
    REFERENCES `alaajo_db`.`saver` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `alaajo_db`.`saving_plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`saving_plan` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `amount` VARCHAR(45) NOT NULL,
  `plan_interval` VARCHAR(45) NULL DEFAULT NULL,
  `name` VARCHAR(150) NULL DEFAULT NULL,
  `plan_code` VARCHAR(150) NOT NULL,
  `wallet` VARCHAR(145) NULL DEFAULT NULL,
  `subscription_code` VARCHAR(145) NULL DEFAULT NULL,
  `saver_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_saving_target_saver1_plan_idx` (`saver_id` ASC),
  UNIQUE INDEX `plan_code_UNIQUE` (`plan_code` ASC),
  CONSTRAINT `fk_saving_target_saver_plan`
    FOREIGN KEY (`saver_id`)
    REFERENCES `alaajo_db`.`saver` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`transaction` (
  `transaction_id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  `customer_code` VARCHAR(45) NULL DEFAULT NULL,
  `origin_id` INT(11) NULL DEFAULT NULL,
  `domain` VARCHAR(45) NULL DEFAULT NULL,
  `gateway_response` VARCHAR(45) NULL DEFAULT NULL,
  `to_wallet` VARCHAR(45) NULL DEFAULT NULL,
  `from_wallet` VARCHAR(45) NULL DEFAULT NULL,
  `reference` VARCHAR(45) NOT NULL,
  `paid_at` VARCHAR(45) NULL DEFAULT NULL,
  `created_at` VARCHAR(45) NOT NULL,
  `description` TEXT(2000) NULL DEFAULT NULL,
  `currency` VARCHAR(100) NULL DEFAULT NULL,
  `channel` VARCHAR(100) NULL DEFAULT NULL,
  `status` VARCHAR(45) NOT NULL,
  `amount` VARCHAR(45) NOT NULL,
  `transfer_code` VARCHAR(100) NULL DEFAULT NULL,
  `saver_id` INT(11) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  INDEX `fk_transaction_user1_idx` (`saver_id` ASC),
  UNIQUE INDEX `reference_code_UNIQUE` (`reference` ASC),
  UNIQUE INDEX `transfer_code_UNIQUE` (`transfer_code` ASC),
  CONSTRAINT `fk_transaction_user1`
    FOREIGN KEY (`saver_id`)
    REFERENCES `alaajo_db`.`saver` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 135
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`user_activity_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`user_activity_log` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `last_login` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_activity_log_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_activity_log_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `alaajo_db`.`application_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`user_investment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`user_investment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `investment_id_invest_id` INT(11) NOT NULL,
  `user_id_id` INT(11) NOT NULL,
  `start_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_investment_investment1_idx` (`investment_id_invest_id` ASC),
  INDEX `fk_user_investment_useadmin_idadmin_idadmin_idr1_idx` (`user_id_id` ASC),
  CONSTRAINT `fk_user_investment_investment1`
    FOREIGN KEY (`investment_id_invest_id`)
    REFERENCES `alaajo_db`.`investment` (`invest_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_investment_user1`
    FOREIGN KEY (`user_id_id`)
    REFERENCES `alaajo_db`.`saver` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`wallet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`wallet` (
  `wallet_id` INT(11) NOT NULL,
  `balance` DOUBLE NOT NULL,
  `currency` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `saver_id` INT(11) NOT NULL,
  PRIMARY KEY (`wallet_id`),
  INDEX `fk_wallet_user1_idx` (`saver_id` ASC),
  CONSTRAINT `fk_wallet_user1`
    FOREIGN KEY (`saver_id`)
    REFERENCES `alaajo_db`.`saver` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`table_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`table_group` (
 `id` INT NOT NULL AUTO_INCREMENT,
 `name` VARCHAR(45) NOT NULL,
 `description` TEXT(2000) NULL DEFAULT NULL,
 `frequency` VARCHAR(45) NULL DEFAULT NULL,
 `min_contribution` DOUBLE NULL DEFAULT NULL,
 `group_is_public` TINYINT(1) NULL DEFAULT '0',
 `start_date` DATETIME NOT NULL,
 `withdrawal_date` DATETIME NULL DEFAULT NULL,
 `image` VARCHAR(112) NULL DEFAULT NULL,
 `owner_id` INT NOT NULL,
 PRIMARY KEY (`id`),
 INDEX `owner_id_idx` (`owner_id` ASC) VISIBLE,
 CONSTRAINT `owner_id`
     FOREIGN KEY (`owner_id`)
         REFERENCES `alaajo_db`.`saver` (`id`)
         ON DELETE NO ACTION
         ON UPDATE NO ACTION)
    ENGINE = InnoDB
    AUTO_INCREMENT = 22
    DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `alaajo_db`.`group_has_saver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`group_has_saver` (
 `group_id` INT NOT NULL,
 `saver_id` INT NOT NULL,
 PRIMARY KEY (`group_id`, `saver_id`),
 INDEX `fk_group_has_saver_saver1_idx` (`saver_id` ASC) VISIBLE,
 INDEX `fk_group_has_saver_group1_idx` (`group_id` ASC) VISIBLE,
 CONSTRAINT `fk_group_has_saver_group1`
     FOREIGN KEY (`group_id`)
         REFERENCES `alaajo_db`.`table_group` (`id`),
 CONSTRAINT `fk_group_has_saver_saver1`
     FOREIGN KEY (`saver_id`)
         REFERENCES `alaajo_db`.`saver` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `alaajo_db`.`group_invitation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alaajo_db`.`group_invitation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `inviter_email` VARCHAR(120) NOT NULL,
  `group_id` INT NOT NULL,
  `token` VARCHAR(120) NOT NULL,
  `is_invitation_accepted`boolean not null,
  `date_created` DATETIME NOT NULL,
  `saver_id` INT NOT NULL,
  `expiry_date` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `group_id_idx` (`group_id` ASC) INVISIBLE,
  INDEX `saver_id_idx` (`saver_id` ASC) VISIBLE,
  CONSTRAINT `group_id`
      FOREIGN KEY (`group_id`)
          REFERENCES `alaajo_db`.`table_group` (`id`)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT `saver_id`
      FOREIGN KEY (`saver_id`)
          REFERENCES `alaajo_db`.`saver` (`id`)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
