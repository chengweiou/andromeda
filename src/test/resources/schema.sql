
CREATE TABLE `account` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `username` VARCHAR(45) NOT NULL,
   `password` VARCHAR(500) NOT NULL,
   `personId` VARCHAR(45) NOT NULL,
   `active` TINYINT NOT NULL,
   `extra` VARCHAR(1000) NOT NULL,
   `createAt` DATETIME NOT NULL,
   `updateAt` DATETIME NOT NULL,
   PRIMARY KEY (`id`)
);

CREATE TABLE `loginRecord` (
   `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
   `accountId` INT NOT NULL,
   `personId` VARCHAR(45) NOT NULL,
   `ip` VARCHAR(45) NOT NULL,
   `platform` VARCHAR(45) NOT NULL,
   `loginTime` VARCHAR(45) NOT NULL,
   `logoutTime` VARCHAR(45) NOT NULL,
   `createAt` DATETIME NOT NULL,
   `updateAt` DATETIME NOT NULL,
   PRIMARY KEY (`id`)
);