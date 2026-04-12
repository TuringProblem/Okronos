SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `reason`;
USE `reason`;

CREATE TABLE IF NOT EXISTS `tradepost_coffer` (
  `username` varchar(16) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `tradepost_collections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_name` varchar(16) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_amount` int(11) DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `tradepost_history` (
  `player_name` varchar(16) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_amount` int(11) DEFAULT NULL,
  `price` varchar(512) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `seller` varchar(12) DEFAULT NULL,
  `age` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `tradepost_offers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_name` varchar(16) DEFAULT NULL,
  `slot` tinyint(4) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_amount` varchar(512) DEFAULT NULL,
  `price` varchar(512) DEFAULT NULL,
  `timestamp` bigint(20) DEFAULT NULL,
  `buy` bit(1) DEFAULT NULL,
  `start_amount` varchar(512) DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

ALTER TABLE tradepost_offers DROP CONSTRAINT IF EXISTS unique_player_slot;
ALTER TABLE tradepost_offers ADD CONSTRAINT unique_player_slot UNIQUE (player_name, slot);
