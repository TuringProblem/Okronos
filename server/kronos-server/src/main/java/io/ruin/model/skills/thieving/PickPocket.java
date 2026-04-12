package io.ruin.model.skills.thieving;

import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.cache.NPCType;
import io.ruin.model.World;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.MasterThief;
import io.ruin.model.activities.perktree.perks.TheArtOfMining;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.combat.Hit;

import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.actions.impl.skillcapes.ThievingSkillCape;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.skills.BotPrevention;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Misc;

import static io.ruin.cache.ItemID.COINS_995;
import static io.ruin.cache.ItemID.MENAPHITE_THUG;

public enum PickPocket {

	MAN(1, 8.0, 422, 5, 1, "man's", PlayerCounter.PICKPOCKETED_MAN,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 500, 700, 100)  //Coins
		)),
	FARMER(10, 14.5, 433, 5, 1, "farmer's", PlayerCounter.PICKPOCKETED_FARMER,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 600, 1000, 100), //Coins
			new LootItem(5318, 1, 50) //Potato seed

		)),
	VYRE(82, 306.9, 433, 5, 6, "noctillion lugosi's", PlayerCounter.PICKPOCKETED_VYRE,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 4000, 8000, 5000), //Coins
			new LootItem(ItemID.DEATH_RUNE, 15, 5000),
			new LootItem(24774, 1, 4700),
			new LootItem(1620, 10, 2000),
			new LootItem(ItemID.BLOOD_RUNE, 20, 800),
			new LootItem(ItemID.DIAMOND, 1, 300),
			new LootItem(24785, 1, 300),
			new LootItem(24777, 1, 1)

		)),
	HAM(15, 18.5, 433, 4, 1, "H.A.M member's", PlayerCounter.PICKPOCKETED_HAM_MEMBER,
		new LootTable().addTable(1,
			new LootItem(882, 16, 60), //Coins
			new LootItem(1351, 1, 100), //Coins
			new LootItem(1265, 1, 100), //Coins
			new LootItem(2677, 1, 100), //Clue scroll (easy)
			new LootItem(1349, 1, 100), //Coins
			new LootItem(1267, 1, 100), //Coins
			new LootItem(886, 20, 100), //Coins
			new LootItem(1353, 1, 100), //Coins
			new LootItem(1207, 1, 100), //Coins
			new LootItem(1129, 1, 100), //Coins
			new LootItem(4302, 1, 100), //Coins
			new LootItem(4298, 1, 100), //Coins
			new LootItem(4300, 1, 100), //Coins
			new LootItem(4304, 1, 100), //Coins
			new LootItem(2677, 1, 3), //Clue
			new LootItem(4306, 1, 100), //Coins
			new LootItem(4308, 1, 100), //Coins
			new LootItem(4310, 1, 100), //Coins
			new LootItem(COINS_995, 1200, 100), //Coins
			new LootItem(319, 1, 100), //Coins
			new LootItem(2138, 1, 100), //Coins
			new LootItem(453, 1, 100), //Coins
			new LootItem(440, 1, 100), //Coins
			new LootItem(1739, 1, 100), //Coins
			new LootItem(314, 5, 100), //Coins
			new LootItem(1734, 6, 100), //Coins
			new LootItem(1733, 1, 100), //Coins
			new LootItem(1511, 1, 100), //Coins
			new LootItem(686, 1, 100), //Coins
			new LootItem(697, 1, 100), //Coins
			new LootItem(1625, 1, 100), //Coins
			new LootItem(1627, 1, 100), //Coins
			new LootItem(199, 5, 100), //Coins
			new LootItem(201, 6, 100), //Coins
			new LootItem(203, 1, 100) //Coins

		)),
	WARRIOR(25, 26.0, 386, 5, 2, "warrior's", PlayerCounter.PICKPOCKETED_WARRIOR,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 900, 1200, 1) //Coins
		)),
	ROGUE(32, 35.5, 422, 5, 2, "rogue's", PlayerCounter.PICKPOCKETED_ROGUE,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 2500, 5000, 100), //Coins
			new LootItem(556, 8, 55),  //Air runes
			new LootItem(1933, 1, 20), //Jug of wine
			new LootItem(1219, 1, 20), //Iron dagger(p)
			new LootItem(1523, 1, 20)  //Lockpick

		)),
	MASTER_FARMER(38, 43.0, 386, 5, 3, "master farmer's", PlayerCounter.PICKPOCKETED_MASTER_FARMER,
		new LootTable().addTable(1,
			new LootItem(5318, 1, 4, 100), //Potato seed
			new LootItem(5319, 1, 3, 100), //Onion seed
			new LootItem(5324, 1, 3, 100), //Cabbage seed
			new LootItem(5322, 1, 2, 100), //Tomato seed
			new LootItem(5320, 1, 2, 100), //Sweetcorn seed
			new LootItem(5096, 1, 100), //Marigold seed
			new LootItem(5097, 1, 100), //Rosemary seed
			new LootItem(5098, 1, 100), //Nasturtium seed
			new LootItem(5291, 1, 100), //Guam seed
			new LootItem(5292, 1, 100), //Marrentill seed
			new LootItem(5293, 1, 100), //Tarromin seed
			new LootItem(5294, 1, 100), //Harralander seed
			new LootItem(5323, 1, 100), //Strawberry seed
			new LootItem(5321, 1, 100), //Watermelon seed
			new LootItem(5100, 1, 100), //Limpwurt seed
			new LootItem(5295, 1, 100), //Ranarr seed
			new LootItem(5296, 1, 100), //Toadflax seed
			new LootItem(22879, 1, 100), //snapegrass seed
			new LootItem(5297, 1, 100), //Irit seed
			new LootItem(5298, 1, 100), //Avantoe seed
			new LootItem(5299, 1, 100), //Kwuarm seed
			new LootItem(5300, 1, 100), //Snapdragon seed
			new LootItem(5301, 1, 100), //Cadantine seed
			new LootItem(5302, 1, 100), //Lantadyme seed
			new LootItem(5303, 1, 100), //Dwarf weed seed
			new LootItem(5304, 1, 100)  //Torstol seed

		)),
	MARTIN_THE_MASTER_GARDENER(38, 43.0, 386, 5, 3, "master gardener's", PlayerCounter.PICKPOCKETED_MASTER_FARMER,
		new LootTable().addTable(1,
			new LootItem(5318, 1, 4, 100), //Potato seed
			new LootItem(5319, 1, 3, 100), //Onion seed
			new LootItem(5324, 1, 3, 100), //Cabbage seed
			new LootItem(5322, 1, 2, 100), //Tomato seed
			new LootItem(5320, 1, 2, 100), //Sweetcorn seed
			new LootItem(5096, 1, 100), //Marigold seed
			new LootItem(5097, 1, 100), //Rosemary seed
			new LootItem(5098, 1, 100), //Nasturtium seed
			new LootItem(5291, 1, 100), //Guam seed
			new LootItem(5292, 1, 100), //Marrentill seed
			new LootItem(5293, 1, 100), //Tarromin seed
			new LootItem(5294, 1, 100), //Harralander seed
			new LootItem(5323, 1, 100), //Strawberry seed
			new LootItem(5321, 1, 100), //Watermelon seed
			new LootItem(5100, 1, 100), //Limpwurt seed
			new LootItem(5295, 1, 100), //Ranarr seed
			new LootItem(5296, 1, 100), //Toadflax seed
			new LootItem(5297, 1, 100), //Irit seed
			new LootItem(5298, 1, 100), //Avantoe seed
			new LootItem(5299, 1, 100), //Kwuarm seed
			new LootItem(5300, 1, 100), //Snapdragon seed
			new LootItem(5301, 1, 100), //Cadantine seed
			new LootItem(5302, 1, 100), //Lantadyme seed
			new LootItem(5303, 1, 100), //Dwarf weed seed
			new LootItem(5304, 1, 100)  //Torstol seed

		)),
	GUARD(40, 46.8, 386, 5, 2, "guard's", PlayerCounter.PICKPOCKETED_GUARD,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 1500, 2000, 100) //Coins

		)),
	MENAPHITE_THUG(65, 137.5, 386, 5, 6, "menaphite thug's", PlayerCounter.PICKPOCKETED_GUARD,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 3500, 4000, 100) //Coins

		)),
	BANDIT(53, 79.5, 422, 5, 3, "bandit's", PlayerCounter.PICKPOCKETED_BANDIT,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 2000, 2800, 100), //Coins
			new LootItem(175, 1, 25),  //Antipoison
			new LootItem(1523, 1, 25)  //Lockpick

		)),
	KNIGHT(55, 84.3, 386, 3, 3, "knight's", PlayerCounter.PICKPOCKETED_KNIGHT,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 3000, 4000, 100) //Coins

		)),
	PALADIN(70, 151.8, 386, 3, 3, "paladin's", PlayerCounter.PICKPOCKETED_PALADIN,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 4000, 4500, 20), //Coins
			new LootItem(562, 2, 20)   //Chaos runes

		)),
	GNOME(75, 198.5, 201, 5, 1, "gnome's", PlayerCounter.PICKPOCKETED_GNOME,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 4000, 4500, 16), //Coins
			new LootItem(5321, 3, 20),   //Watermelon seed
			new LootItem(5100, 1, 20),   //Limpwurt seed
			new LootItem(5295, 1, 20),   //Ranarr seed
			new LootItem(5296, 1, 20),   //Toadflax seed
			new LootItem(5297, 1, 20),   //Irit seed
			new LootItem(5298, 1, 20),   //Avantoe seed
			new LootItem(5299, 1, 20),   //Kwuarm seed
			new LootItem(5300, 1, 20),   //Snapdragon seed
			new LootItem(5301, 1, 20),   //Cadantine seed
			new LootItem(5302, 1, 20),   //Lantadyme seed
			new LootItem(5303, 1, 20),   //Dwarf weed seed
			new LootItem(5304, 1, 20),   //Torstol seed
			new LootItem(5312, 1, 20),   //Acorn
			new LootItem(5313, 1, 20),   //Willow seed
			new LootItem(5314, 1, 20),   //Maple seed
			new LootItem(5315, 1, 20),   //Yew seed
			new LootItem(5283, 1, 20),   //Apple tree seed
			new LootItem(5284, 1, 20),   //Banana tree seed
			new LootItem(5285, 1, 20),   //Orange tree seed
			new LootItem(5286, 1, 20),   //Curry tree seed
			new LootItem(5287, 1, 20),   //Pineapple seed
			new LootItem(5288, 1, 20)   //Papaya tree seed

		)),
	HERO(80, 275.0, 386, 3, 4, "hero's", PlayerCounter.PICKPOCKETED_HERO,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 5000, 6000, 16),  //Coins
			new LootItem(565, 1, 20),  //Blood rune
			new LootItem(560, 2, 20),  //Death runes
			new LootItem(1933, 1, 10), //Jug of wine
			new LootItem(569, 1, 10),  //Fire orb
			new LootItem(444, 1, 10),  //Gold ore
			new LootItem(1617, 1, 10)  //Uncut diamond

		)),
	ELF(85, 353.0, 422, 6, 5, "elf's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 5500, 6500, 16), //Coins
			new LootItem(561, 3, 20),  //Nature runes
			new LootItem(560, 2, 20),  //Death runes
			new LootItem(1993, 1, 15), //Jug of wine
			new LootItem(569, 1, 10),  //Fire orb
			new LootItem(444, 1, 10),  //Gold ore
			new LootItem(1617, 1, 10)  //Uncut diamond
		)),
	IDRIL(85, 353.0, 422, 6, 5, "idril's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	MAWRTH(85, 353.0, 422, 6, 5, "mawrth's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	GOREU(85, 353.0, 422, 6, 5, "goreu's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 5, 20) //Crystal Shard
		)),
	ARVEL(85, 353.0, 422, 6, 5, "arvel's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	KELYN(85, 353.0, 422, 6, 5, "kelyn's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ANAIRE(85, 353.0, 422, 6, 5, "anaire's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ARANWE(85, 353.0, 422, 6, 5, "aranwe's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	AREDHEL(85, 353.0, 422, 6, 5, "aredhel's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	CARANTHIR(85, 353.0, 422, 6, 5, "caranthir's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	CELEBRIAN(85, 353.0, 422, 6, 5, "celebrian's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	CELEGORM(85, 353.0, 422, 6, 5, "celegorm's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	CIRDAN(85, 353.0, 422, 6, 5, "cirdan's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	CURUFIN(85, 353.0, 422, 6, 5, "curufin's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	EARWEN(85, 353.0, 422, 6, 5, "earwen's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	EDRAHIL(85, 353.0, 422, 6, 5, "edrahil's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ELENWE(85, 353.0, 422, 6, 5, "elenwe's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ELLADAN(85, 353.0, 422, 6, 5, "elladan's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ENEL(85, 353.0, 422, 6, 5, "enel's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ERESTOR(85, 353.0, 422, 6, 5, "erestor's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ENERDHIL(85, 353.0, 422, 6, 5, "enerdhil's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	ENELYE(85, 353.0, 422, 6, 5, "enelye's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	FEANOR(85, 353.0, 422, 6, 5, "feanor's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	FINDIS(85, 353.0, 422, 6, 5, "findis's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	FINDUILAS(85, 353.0, 422, 6, 5, "finduilas's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	FINGOLFIN(85, 353.0, 422, 6, 5, "fingolfin's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	FINGON(85, 353.0, 422, 6, 5, "fingon's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	GALATHIL(85, 353.0, 422, 6, 5, "galathil's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	GELMIR(85, 353.0, 422, 6, 5, "gelmir's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	GLORFINDEL(85, 353.0, 422, 6, 5, "glorfindel's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	GUILIN(85, 353.0, 422, 6, 5, "guilin's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	HENDOR(85, 353.0, 422, 6, 5, "hendor's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	IMIN(85, 353.0, 422, 6, 5, "imin's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	IMINYE(85, 353.0, 422, 6, 5, "iminye's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	INDIS(85, 353.0, 422, 6, 5, "indis's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	INGWE(85, 353.0, 422, 6, 5, "ingwe's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	INGWION(85, 353.0, 422, 6, 5, "ingwion's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	LENWE(85, 353.0, 422, 6, 5, "lenwe's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	LINDIR(85, 353.0, 422, 6, 5, "lindir's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	MAEGLIN(85, 353.0, 422, 6, 5, "maeglin's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	MAHTAN(85, 353.0, 422, 6, 5, "mahtan's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	MIRIEL(85, 353.0, 422, 6, 5, "miriel's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	MITHRELLAS(85, 353.0, 422, 6, 5, "mithrellas's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	NELLAS(85, 353.0, 422, 6, 5, "nellas's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	NERDANEL(85, 353.0, 422, 6, 5, "nerdanel's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	NIMLOTH(85, 353.0, 422, 6, 5, "nimloth's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	OROPHER(85, 353.0, 422, 6, 5, "oropher's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	OROPHIN(85, 353.0, 422, 6, 5, "orophin's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	SAEROS(85, 353.0, 422, 6, 5, "saeros's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	SALGANT(85, 353.0, 422, 6, 5, "salgant's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	TATIE(85, 353.0, 422, 6, 5, "tatie's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	THINGOL(85, 353.0, 422, 6, 5, "thingol's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	TURGON(85, 353.0, 422, 6, 5, "turgon's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	VAIRE(85, 353.0, 422, 6, 5, "vaire's", PlayerCounter.PICKPOCKETED_ELF,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 6000, 9500, 40), //Coins
			new LootItem(560, 2, 30),  //Death runes
			new LootItem(561, 3, 30),  //Nature runes
			new LootItem(1993, 1, 25), //Jug of wine
			new LootItem(1617, 1, 25),  //Uncut diamond
			new LootItem(569, 1, 25),  //Fire orb
			new LootItem(444, 1, 25),  //Gold ore
			new LootItem(23962, 1, 2, 5)  //Crystal Shard
		)),
	TZHAAR_HUR(90, 456, 2609, 6, 5, "tzhaar-hur's", PlayerCounter.PICKPOCKETED_TZHAAR_HUR,
		new LootTable().addTable(1,
			new LootItem(1755, 1, 20),                    //Chisel
			new LootItem(2347, 1, 20),                    //Hammer
			new LootItem(1935, 1, 20),                    //Jug
			new LootItem(946, 1, 20),                     //Knife
			new LootItem(1931, 1, 20),                    //Pot
			new LootItem(6529, 1, 16, 10),  //Tokkul
			new LootItem(1623, 1, 10),                    //Uncut Sapphire
			new LootItem(1619, 1, 10)                 //Uncut Ruby
		));

	public final int levelReq, stunAnimation, stunSeconds, stunDamage;
	private final String name, identifier;
	public final double exp;
	public final LootTable lootTable;
	public final PlayerCounter counter;

	PickPocket(int levelReq, double exp, int stunAnimation, int stunSeconds, int stunDamage, String identifier, PlayerCounter counter, LootTable lootTable) {
		this.levelReq = levelReq;
		this.exp = exp;
		this.stunAnimation = stunAnimation;
		this.stunSeconds = stunSeconds;
		this.stunDamage = stunDamage;
		this.name = identifier.replace("'s", "");
		this.identifier = identifier;
		this.counter = counter;
		this.lootTable = lootTable;
	}

	public static int[] ThieveOutfit = {5553, 5554, 5555, 5556, 5557};
	private static final long INTERACTION_DELAY = 1500L;

	private static double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	private static void pickpocket(Player player, NPC npc, PickPocket pickpocket) {
		if (!player.getStats().check(StatType.Thieving, pickpocket.levelReq, "pickpocket the " + pickpocket.name + "."))
			return;
		if (player.getInventory().isFull()) {
			player.privateSound(2277);
			player.sendMessage("Your inventory is too full to hold any more loot.");
			return;
		}
		if (player.isStunned()) {
			player.sendMessage("You're stunned!");
			return;
		}

		if (BotPrevention.isBlocked(player)) {
			player.sendMessage("You can't pickpocket an NPC while a guard is watching you.");
			return;
		}
		if (Random.rollDie(125, 1)) {
			player.getInventory().add(ThieveOutfit[Misc.random(ThieveOutfit.length - 1)], 1);
			player.sendMessage("You have found a rogues piece in their pocket stole it.");
		}

		long delay = player.pickpocketDelay;
		if (System.currentTimeMillis() - delay < INTERACTION_DELAY) {
			return;
		}

		player.lock(LockType.FULL_REGULAR_DAMAGE);
		player.sendFilteredMessage("You attempt to pick the " + pickpocket.identifier + " pocket.");
		if (successful(player, pickpocket) && player.getEquipment().get(Equipment.SLOT_CHEST) != null && player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 5553
			&& player.getEquipment().get(Equipment.SLOT_HAT) != null && player.getEquipment().get(Equipment.SLOT_HAT).getId() == 5554 && player.getEquipment().get(Equipment.SLOT_LEGS) != null && player.getEquipment().get(Equipment.SLOT_LEGS).getId() == 5555 &&
			player.getEquipment().get(Equipment.SLOT_HANDS) != null && player.getEquipment().get(Equipment.SLOT_HANDS).getId() == 5556 && player.getEquipment().get(Equipment.SLOT_FEET) != null && player.getEquipment().get(Equipment.SLOT_FEET).getId() == 5557) {
			player.animate(881);
			player.privateSound(2581);
			player.sendFilteredMessage("You pick the " + pickpocket.identifier + " pocket.");
			//TODO Rogue outfit fix
			DailyTasks.handleTaskProgression(player, pickpocket);
			if (pickpocket == MAN) {
				PerkTaskHandler.handleCompleteActivity(player, 13);
				player.manPickpocketCounter++;
				if (player.manPickpocketCounter == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.getAchievementName());
			} else if (pickpocket == FARMER) {
				PerkTaskHandler.handleCompleteActivity(player, 14);
			} else if (pickpocket == MASTER_FARMER)
				PerkTaskHandler.handleCompleteActivity(player, 15);
			else if (pickpocket == GUARD)
				PerkTaskHandler.handleCompleteActivity(player, 16);
			else if (pickpocket == BANDIT)
				PerkTaskHandler.handleCompleteActivity(player, 18);
			else if (pickpocket == MENAPHITE_THUG)
				PerkTaskHandler.handleCompleteActivity(player, 20);
			else if (pickpocket == PALADIN)
				PerkTaskHandler.handleCompleteActivity(player, 21);
			else if (pickpocket == GNOME)
				PerkTaskHandler.handleCompleteActivity(player, 22);
			else if (pickpocket == ELF)
				PerkTaskHandler.handleCompleteActivity(player, 25);
			else if (pickpocket == KNIGHT) {
				PerkTaskHandler.handleCompleteActivity(player, 19);
				player.knightsPickpocketed++;
				if (player.knightsPickpocketed == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.getAchievementName());
			} else if (pickpocket == HERO) {
				PerkTaskHandler.handleCompleteActivity(player, 23);
				player.herosPickpocketed++;
				if (player.herosPickpocketed == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.getAchievementName());
			} else if (pickpocket == TZHAAR_HUR) {
				PerkTaskHandler.handleCompleteActivity(player, 26);
				player.tzhaarPickpocketed++;
				if (player.tzhaarPickpocketed == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.getAchievementName());
			}
			player.getInventory().add(pickpocket.lootTable.rollItem());
			player.getInventory().add(pickpocket.lootTable.rollItem());
			player.getStats().addXp(StatType.Thieving, pickpocket.exp * xpBonus(player), true);
			float petChance = 20000;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				petChance *= c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				petChance *= 0.8F;

			petChance *= getPetDonatorBoost(player);
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				petChance *= 0.85F;
			if (Random.get((int) petChance) == 0)
				Pet.ROCKY.unlock(player, 0);
		} else if (successful(player, pickpocket)) {
			DailyTasks.handleTaskProgression(player, pickpocket);
			player.animate(881);
			player.privateSound(2581);
			player.sendFilteredMessage("You pick the " + pickpocket.identifier + " pocket.");
//                rogueOutfit(player);
			if (pickpocket == MAN) {
				PerkTaskHandler.handleCompleteActivity(player, 13);
				player.manPickpocketCounter++;
				if (player.manPickpocketCounter == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.getAchievementName());
			} else if (pickpocket == FARMER) {
				PerkTaskHandler.handleCompleteActivity(player, 14);
			} else if (pickpocket == MASTER_FARMER)
				PerkTaskHandler.handleCompleteActivity(player, 15);
			else if (pickpocket == GUARD)
				PerkTaskHandler.handleCompleteActivity(player, 16);
			else if (pickpocket == BANDIT)
				PerkTaskHandler.handleCompleteActivity(player, 18);
			else if (pickpocket == MENAPHITE_THUG)
				PerkTaskHandler.handleCompleteActivity(player, 20);
			else if (pickpocket == PALADIN)
				PerkTaskHandler.handleCompleteActivity(player, 21);
			else if (pickpocket == GNOME)
				PerkTaskHandler.handleCompleteActivity(player, 22);
			else if (pickpocket == ELF)
				PerkTaskHandler.handleCompleteActivity(player, 25);
			else if (pickpocket == KNIGHT) {
				PerkTaskHandler.handleCompleteActivity(player, 19);
				player.knightsPickpocketed++;
				if (player.knightsPickpocketed == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.getAchievementName());
			} else if (pickpocket == HERO) {
				PerkTaskHandler.handleCompleteActivity(player, 23);
				player.herosPickpocketed++;
				if (player.herosPickpocketed == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.getAchievementName());
			} else if (pickpocket == TZHAAR_HUR) {
				PerkTaskHandler.handleCompleteActivity(player, 26);
				player.tzhaarPickpocketed++;
				if (player.tzhaarPickpocketed == Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.getAchievementName());
			}
			player.getInventory().add(pickpocket.lootTable.rollItem());
			player.getStats().addXp(StatType.Thieving, pickpocket.exp, true);
			float petChance = 20000;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				petChance *= c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				petChance *= 0.8F;
			petChance *= getPetDonatorBoost(player);
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				petChance *= 0.85F;
			if (Random.get((int) petChance) == 0)
				Pet.ROCKY.unlock(player, 0);
		} else {
			boolean avoidStun = false;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.MASTER_THIEF)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.MASTER_THIEF);
				MasterThief c = (MasterThief) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				if (Random.rollPercent(c.getChanceToAvoidStun())) {
					player.sendFilteredMessage("You avoid being stunned.");
					avoidStun = true;
					if (Random.rollPercent(c.getChanceToSucceed())) {
						player.animate(881);
						player.privateSound(2581);
						player.sendFilteredMessage("You pick the " + pickpocket.identifier + " pocket.");
						player.getInventory().add(pickpocket.lootTable.rollItem());
						player.getStats().addXp(StatType.Thieving, pickpocket.exp * xpBonus(player), true);
					}
				}
			}
			if (!avoidStun) {
				World.startEvent(event -> {
					player.sendFilteredMessage("You fail to pick the " + pickpocket.identifier + " pocket.");
					npc.forceText("What do you think you're doing?");
					npc.faceTemp(player);
					npc.animate(pickpocket.stunAnimation);
					player.hit(new Hit().randDamage(pickpocket.stunDamage));
					event.delay(1);
					player.stun(pickpocket.stunSeconds, true);
				});
			} else player.sendFilteredMessage("You avoid being stunned.");
		}
		BotPrevention.attemptBlock(player);
		player.unlock();
		player.pickpocketDelay = System.currentTimeMillis();
	}

	public static double xpBonus(Player player) {
		double multiplier = 1.0;
		multiplier *= rogueBonus(player);
		return multiplier;
	}

	public static double rogueBonus(Player player) {
		double bonus = 1.0;
		Item helmet = player.getEquipment().get(Equipment.SLOT_HAT);
		Item jacket = player.getEquipment().get(Equipment.SLOT_CHEST);
		Item legs = player.getEquipment().get(Equipment.SLOT_LEGS);
		Item boots = player.getEquipment().get(Equipment.SLOT_FEET);
		Item hands = player.getEquipment().get(Equipment.SLOT_HANDS);

		if (helmet != null && helmet.getId() == 5554)
			bonus += 0.4;
		if (jacket != null && jacket.getId() == 5553)
			bonus += 0.8;
		if (legs != null && legs.getId() == 5555)
			bonus += 0.6;
		if (boots != null && boots.getId() == 5557)
			bonus += 0.2;
		if (hands != null && hands.getId() == 5556)
			bonus += 0.2;

		/* Whole set gives an additional 0.5% exp bonus */
		if (bonus >= 3.0)
			bonus += 0.5;

		return bonus;
	}

	private static boolean successful(Player player, PickPocket pickpocket) {
		return Random.get(100) <= chance(player, pickpocket.levelReq);
	}

	private static final int GLOVES_OF_SILENCE = 10075;
	private static final int[] MAX_CAPES = {13280, 13329, 13331, 13333, 13335, 13337, 13342, 20760};

	//5553 5554 5555 5556 5557
//    public static void rogueOutfit(Player player){
//        if (Random.rollDie(125, 1)) {
//                player.getInventory().add(5553,1);
//        } else if (Random.rollDie(125, 1)) {
//                player.getInventory().add(5554,1);
//        } else if (Random.rollDie(125, 1)) {
//                player.getInventory().add(5555,1);
//        } else if (Random.rollDie(125, 1)) {
//                player.getInventory().add(5556,1);
//        } else if (Random.rollDie(125, 1)) {
//                player.getInventory().add(5557,1);
//        } else {
//            player.getInventory().add(995, 100);
//        }
//    }

	private static int chance(Player player, int levelReq) {
		int slope = 2;
		int chance = 75; //Starts at a 60% chance
		int thievingLevel = player.getStats().get(StatType.Thieving).currentLevel;
		int requiredLevel = levelReq;

		if (player.getEquipment().hasId(GLOVES_OF_SILENCE))
			chance += 5;
		if (player.getEquipment().hasMultiple(MAX_CAPES) || ThievingSkillCape.wearsThievingCape(player))
			chance *= 1.1;
		int gap = thievingLevel - requiredLevel;
		if (gap >= 20)
			return 100;
		if (thievingLevel > levelReq)
			chance += (thievingLevel - requiredLevel) * slope;
		return Math.min(chance, 95); //Caps at 95%
	}

	public static void register() {
		NPCType.forEach(npcDef -> {
			for (PickPocket pickpocket : values()) {
				if (npcDef.name.equalsIgnoreCase(pickpocket.name().replace("_", " ")) ||
					npcDef.name.toLowerCase().contains(pickpocket.name().toLowerCase())) {
					int pickpocketOption = npcDef.getOption("pickpocket");
					if (pickpocketOption == -1)
						return;
					NPCAction.register(npcDef.name, pickpocketOption, (player, npc) -> pickpocket(player, npc, pickpocket));
					NPCAction.register("tzhaar-hur", pickpocketOption, (player, npc) -> pickpocket(player, npc, TZHAAR_HUR));
					final int[] HAM_MEMBERS = {2540, 2541};
					for (int hamMember : HAM_MEMBERS)
						NPCAction.register(hamMember, pickpocketOption, (player, npc) -> pickpocket(player, npc, HAM));
				}
			}
		});
	}
}
