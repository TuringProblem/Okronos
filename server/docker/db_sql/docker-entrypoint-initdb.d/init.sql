-- Adminer 5.1.0 MariaDB 11.7.2-MariaDB-ubu2404 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `reason`;
USE `reason`;

CREATE TABLE IF NOT EXISTS `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `zindex` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


CREATE TABLE IF NOT EXISTS `discount_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `percentage` int(11) NOT NULL DEFAULT 0,
  `expires` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


CREATE TABLE IF NOT EXISTS `group_data` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  `leader_name` varchar(255) DEFAULT NULL,
  `hardcore` tinyint(1) DEFAULT NULL,
  `lives_remaining` int(11) DEFAULT NULL,
  `bank_occupied` tinyint(1) DEFAULT NULL,
  `bank_occupier_name` varchar(255) DEFAULT NULL,
  `creation_date` varchar(20) DEFAULT NULL,
  `group_difficulty` int(11) DEFAULT NULL,
  `player1_name` varchar(255) DEFAULT NULL,
  `player2_name` varchar(255) DEFAULT NULL,
  `player3_name` varchar(255) DEFAULT NULL,
  `player4_name` varchar(255) DEFAULT NULL,
  `player5_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_name` (`group_name`),
  UNIQUE KEY `leader_name` (`leader_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `group_items` (
  `item_id` int(11) DEFAULT NULL,
  `item_amount` int(11) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  UNIQUE KEY `unique_item_group` (`item_id`,`group_name`),
  KEY `group_name` (`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `hs_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  `difficulty` int(11) NOT NULL,
  `mode` int(11) NOT NULL,
  `totalLevel` int(11) NOT NULL,
  `totalXp` bigint(20) NOT NULL,
  `attackXp` int(11) NOT NULL,
  `defenceXp` int(11) NOT NULL,
  `strengthXp` int(11) NOT NULL,
  `hitpointsXp` int(11) NOT NULL,
  `rangedXp` int(11) NOT NULL,
  `prayerXp` int(11) NOT NULL,
  `magicXp` int(11) NOT NULL,
  `cookingXp` int(11) NOT NULL,
  `woodcuttingXp` int(11) NOT NULL,
  `fletchingXp` int(11) NOT NULL,
  `fishingXp` int(11) NOT NULL,
  `firemakingXp` int(11) NOT NULL,
  `craftingXp` int(11) NOT NULL,
  `smithingXp` int(11) NOT NULL,
  `miningXp` int(11) NOT NULL,
  `herbloreXp` int(11) NOT NULL,
  `agilityXp` int(11) NOT NULL,
  `thievingXp` int(11) NOT NULL,
  `slayerXp` int(11) NOT NULL,
  `farmingXp` int(11) NOT NULL,
  `runecraftingXp` int(11) NOT NULL,
  `hunterXp` int(11) NOT NULL,
  `constructionXp` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `container` int(11) NOT NULL,
  `slot` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `items` (
  `identifier` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `container` varchar(255) NOT NULL,
  `slot` int(11) NOT NULL,
  PRIMARY KEY (`identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `logs_commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_ip` varchar(255) NOT NULL,
  `cmd_query` varchar(255) NOT NULL,
  `world_id` int(11) NOT NULL,
  `world_stage` varchar(255) NOT NULL,
  `world_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `logs_drop_trades` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taker_id` int(11) NOT NULL,
  `dropper_id` int(11) NOT NULL,
  `taker_ip` varchar(255) NOT NULL,
  `dropper_ip` varchar(255) NOT NULL,
  `taker_name` varchar(255) NOT NULL,
  `dropper_name` varchar(255) NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `value` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `world` int(11) NOT NULL,
  `time_dropped` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `logs_item_drops` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_ip` varchar(255) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_amount` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `world_id` int(11) NOT NULL,
  `world_stage` varchar(255) NOT NULL,
  `world_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `logs_item_pickups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_ip` varchar(255) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_amount` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `world_id` int(11) NOT NULL,
  `world_stage` varchar(255) NOT NULL,
  `world_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `logs_players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `world_stage` varchar(255) NOT NULL,
  `world_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `logs_public_chat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_ip` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  `effects` int(11) NOT NULL,
  `world_id` int(11) NOT NULL,
  `world_stage` varchar(255) NOT NULL,
  `world_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `logs_sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_ip` varchar(255) NOT NULL,
  `world_id` int(11) NOT NULL,
  `world_stage` varchar(255) NOT NULL,
  `world_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `lost_cannons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(12) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `online_characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `world_id` int(11) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `is_helper` tinyint(1) NOT NULL,
  `is_moderator` tinyint(1) NOT NULL,
  `is_administrator` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `online_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `world` int(11) NOT NULL,
  `players` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `payments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(255) NOT NULL,
  `item_number` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `paid` double NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 0,
  `currency` varchar(255) NOT NULL,
  `buyer` varchar(255) DEFAULT NULL,
  `dateline` bigint(20) DEFAULT 0,
  `player_name` varchar(255) DEFAULT NULL,
  `claimed` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


CREATE TABLE IF NOT EXISTS `player_characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `world_id` int(11) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `is_helper` tinyint(1) NOT NULL,
  `is_moderator` tinyint(1) NOT NULL,
  `is_administrator` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(255) NOT NULL,
  `item_id` int(11) NOT NULL DEFAULT -1,
  `category` int(11) NOT NULL DEFAULT 0,
  `price` double NOT NULL,
  `max_qty` int(11) NOT NULL DEFAULT -1,
  `image_url` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `description` longtext DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


CREATE TABLE IF NOT EXISTS `sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `sess_id` varchar(255) NOT NULL,
  `ip_address` varchar(255) NOT NULL,
  `created` bigint(20) NOT NULL,
  `expires` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `tp_collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_name` varchar(12) NOT NULL,
  `amount` bigint(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `tp_containers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `player_name` varchar(255) NOT NULL,
  `slot` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_amount` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `ad_end` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `tp_history` (
  `player_name` varchar(12) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_amount` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `super_admin` tinyint(1) NOT NULL DEFAULT 0,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `last_login` bigint(20) DEFAULT NULL,
  `last_ip` varchar(255) DEFAULT NULL,
  `mfa_secret` varchar(255) DEFAULT NULL,
  `scopes` text DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `users_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `users_sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `access_key` varchar(255) NOT NULL,
  `ip_address` varchar(255) NOT NULL,
  `expires` int(11) NOT NULL DEFAULT -1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `votes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `vote_key` varchar(255) NOT NULL,
  `site_id` int(11) NOT NULL,
  `voted_on` int(11) DEFAULT -1,
  `started_on` int(11) NOT NULL DEFAULT -1,
  `claimed` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE IF NOT EXISTS `vote_links` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `site_id` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;
