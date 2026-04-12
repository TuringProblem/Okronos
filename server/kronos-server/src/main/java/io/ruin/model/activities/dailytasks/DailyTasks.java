package io.ruin.model.activities.dailytasks;


import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.cache.NpcID;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.skills.construction.Buildable;
import io.ruin.model.skills.thieving.PickPocket;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Misc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DailyTasks {
	KILL_CORPOREAL_BEAST("Kill the Corporeal Beast", "You're tasked with slaying the Corporeal Beast", Difficulty.ELITE, 4, 10, NpcID.CORPOREAL_BEAST, null, TaskType.PVM, 115),
	KILL_THE_GAUNTLET("Completing The Gauntlet", "You're tasked with completing The Gauntlet", Difficulty.ELITE, 1, 4, "gauntlet", null, TaskType.PVM, 100),
	KILL_THE_CORRUPTED_GAUNTLET("Complete the corrupted gauntlet", "You're tasked with completing The Corrupted Gauntlet", Difficulty.ELITE, 1, 3, "corruptedGauntlet", null, TaskType.PVM, 115),
	KILL_THEATRE_OF_BLOOD("Complete the theatre of blood", "You're tasked with completing The Theatre of Blood", Difficulty.ELITE, 1, 3, "tob", null, TaskType.PVM, 120),
	KILL_COX("Complete the chambers of xeric", "You're tasked with completing the chambers of  xeric", Difficulty.ELITE, 2, 4, "cox", null, TaskType.PVM, 120),
	KILL_GIANT_MOLE("Kill Giant Mole", "You're tasked with defeating the Giant Mole", Difficulty.HARD, 4, 10, NpcID.GIANT_MOLE, null, TaskType.PVM, 75),
	KILL_ARGENTAVIS("Kill the Argentavis", "You're tasked with defeating the Argentavis", Difficulty.ELITE, 4, 10, 763, null, TaskType.PVM, 115),
	KILL_NEX("Kill the Nex", "You're tasked with defeating the Nex", Difficulty.ELITE, 3, 10, "nex", null, TaskType.PVM, 115),
	KILL_TZTOK_JAD("Kill TzTok-Jad", "You're tasked with defeating TzTok-Jad at the Fight Caves", Difficulty.HARD, 1, 1, NpcID.TZTOKJAD, null, TaskType.PVM, 70),
	KILL_DAGANNOTH_PRIME("Kill Dagannoth Prime", "You're tasked with defeating Dagannoth Prime", Difficulty.HARD, 3, 7, NpcID.DAGANNOTH_PRIME, null, TaskType.PVM, 85),
	KILL_DAGANNOTH_REX("Kill Dagannoth Rex", "You're tasked with defeating Dagannoth Rex", Difficulty.HARD, 3, 7, NpcID.DAGANNOTH_REX, null, TaskType.PVM, 85),
	KILL_DAGANNOTH_SUPREME("Kill Dagannoth Supreme", "You're tasked with defeating Dagannoth Supreme", Difficulty.HARD, 3, 7, NpcID.DAGANNOTH_SUPREME, null, TaskType.PVM, 85),
	KILL_CHAOS_ELEMENTAL("Kill the Chaos Elemental", "You're tasked with defeating the Chaos Elemental", Difficulty.HARD, 4, 10, NpcID.CHAOS_ELEMENTAL, null, TaskType.PVM, 85),
	KILL_GALVEK("Kill Galvek", "You must slay the Galvek", Difficulty.ELITE, 3, 7, NpcID.GALVEK_8096, null, TaskType.PVM, 120),
	KILL_ZULRAH("Kill Zulrah", "You're tasked with defeating Zulrah", Difficulty.HARD, 4, 10, "zulrah", null, TaskType.PVM, 75),
	KILL_KING_BLACK_DRAGON("Kill the King Black Dragon", "You're tasked with slaying the King Black Dragon", Difficulty.HARD, 4, 12, NpcID.KING_BLACK_DRAGON, null, TaskType.PVM, 70),
	KILL_COMMANDER_ZILYANA("Kill Commander Zilyana", "You must defeat Commander Zilyana in the God Wars Dungeon", Difficulty.ELITE, 4, 10, NpcID.COMMANDER_ZILYANA, null, TaskType.PVM, 100),
	KILL_KRIL_TSUTSAROTH("Kill K'ril Tsutsaroth", "You must defeat K'ril Tsutsaroth in the God Wars Dungeon", Difficulty.ELITE, 4, 10, NpcID.KRIL_TSUTSAROTH, null, TaskType.PVM, 105),
	KILL_GENERAL_GRAARDOR("Kill General Graardor", "You must defeat General Graardor in the God Wars Dungeon", Difficulty.ELITE, 4, 10, NpcID.GENERAL_GRAARDOR, null, TaskType.PVM, 100),
	KILL_KREE_ARRA("Kill Kree'Arra", "You must defeat Kree'Arra in the God Wars Dungeon", Difficulty.ELITE, 4, 10, NpcID.KREEARRA, null, TaskType.PVM, 110),
	HARVEST_BIRD_HOUSE("Loot birdhouses", "You're tasked with harvesting full birdhouses", Difficulty.EASY, 3, 6, "birdhouses", StatType.Hunter, TaskType.SKILLING, 10),
	BARROWS("Complete barrows", "You're tasked with completing the barrows minigame", Difficulty.HARD, 4, 16, "barrows", null, TaskType.PVM, 75),
	MIX_PRAYER("Mix prayer potions", "You're tasked with mixing prayer potions", Difficulty.EASY, 10, 28, "mixprayerpots", StatType.Herblore, TaskType.SKILLING, 38),
	MIX_SUPER_ATTACK("Mix super attack potions", "You're tasked with mixing super attack potions", Difficulty.MEDIUM, 20, 40, "mixsuperattack", StatType.Herblore, TaskType.SKILLING, 45),
	MIX_SUPER_DEFENCE("Mix super defence potions", "You're tasked with mixing super defence potions", Difficulty.HARD, 20, 40, "mixsuperdefence", StatType.Herblore, TaskType.SKILLING, 66),
	MIX_SUPER_STRENGTH("Mix super strength potions", "You're tasked with mixing super strength potions", Difficulty.HARD, 20, 40, "mixsuperstrength", StatType.Herblore, TaskType.SKILLING, 55),
	MIX_ATTACK_POTION("Mix attack potions", "You're tasked with mixing attack potions", Difficulty.EASY, 20, 40, "mixattack", StatType.Herblore, TaskType.SKILLING, 1),
	MIX_STRENGTH_POTION("Mix strength potions", "You're tasked with mixing strength potions", Difficulty.EASY, 20, 40, "mixstrength", StatType.Herblore, TaskType.SKILLING, 7),
	MIX_SUPER_RESTORE("Mix sup. restores", "You're tasked with mixing super restore potions", Difficulty.HARD, 20, 40, "mixsuperrestore", StatType.Herblore, TaskType.SKILLING, 63),
	MIX_SARA_BREW("Mix sara. brews", "You're tasked with mixing saradomin brews", Difficulty.ELITE, 20, 40, "mixsarabrews", StatType.Herblore, TaskType.SKILLING, 81),
	CHOP_LOGS("Chop regular trees", "You're tasked with chopping regular trees", Difficulty.EASY, 20, 40, ItemID.LOGS, StatType.Woodcutting, TaskType.SKILLING, 1),
	CHOP_OAK_LOGS("Chop oak trees", "You're tasked with chopping oak trees", Difficulty.EASY, 20, 40, ItemID.OAK_LOGS, StatType.Woodcutting, TaskType.SKILLING, 15),
	CHOP_WILLOW_LOGS("Chop willow trees", "You're tasked with chopping willow trees", Difficulty.MEDIUM, 20, 40, ItemID.WILLOW_LOGS, StatType.Woodcutting, TaskType.SKILLING, 30),
	CHOP_TEAK_LOGS("Chop teak trees", "You're tasked with chopping teak trees", Difficulty.MEDIUM, 20, 40, ItemID.TEAK_LOGS, StatType.Woodcutting, TaskType.SKILLING, 35),
	CHOP_MAPLE_LOGS("Chop maple trees", "You're tasked with chopping maple trees", Difficulty.MEDIUM, 20, 40, ItemID.MAPLE_LOGS, StatType.Woodcutting, TaskType.SKILLING, 45),
	CHOP_MAHOGANY_LOGS("Chop mahogany trees", "You're tasked with chopping mahogany trees", Difficulty.MEDIUM, 20, 40, ItemID.MAHOGANY_LOGS, StatType.Woodcutting, TaskType.SKILLING, 50),
	CHOP_YEW_LOGS("Chop yew trees", "You're tasked with chopping yew trees", Difficulty.HARD, 20, 40, ItemID.YEW_LOGS, StatType.Woodcutting, TaskType.SKILLING, 60),
	CHOP_MAGIC_LOGS("Chop magic trees", "You're tasked with chopping magic trees", Difficulty.HARD, 20, 40, ItemID.MAGIC_LOGS, StatType.Woodcutting, TaskType.SKILLING, 75),
	CHOP_REDWOOD_LOGS("Chop redwood trees", "You're tasked with chopping redwood trees", Difficulty.ELITE, 20, 40, ItemID.REDWOOD_LOGS, StatType.Woodcutting, TaskType.SKILLING, 90),
	BURN_LOGS("Burn regular logs", "You're tasked with burning regular logs", Difficulty.EASY, 20, 40, ItemID.LOGS, StatType.Firemaking, TaskType.SKILLING, 1),
	BURN_OAK_LOGS("Burn oak logs", "You're tasked with burning oak logs", Difficulty.EASY, 20, 40, ItemID.OAK_LOGS, StatType.Firemaking, TaskType.SKILLING, 15),
	BURN_WILLOW_LOGS("Burn willow logs", "You're tasked with burning willow logs", Difficulty.MEDIUM, 20, 40, ItemID.WILLOW_LOGS, StatType.Firemaking, TaskType.SKILLING, 30),
	BURN_TEAK_LOGS("Burn teak logs", "You're tasked with burning teak logs", Difficulty.MEDIUM, 20, 40, ItemID.TEAK_LOGS, StatType.Firemaking, TaskType.SKILLING, 35),
	BURN_MAPLE_LOGS("Burn maple logs", "You're tasked with burning maple logs", Difficulty.MEDIUM, 20, 40, ItemID.MAPLE_LOGS, StatType.Firemaking, TaskType.SKILLING, 45),
	BURN_MAHOGANY_LOGS("Burn mahogany logs", "You're tasked with burning mahogany logs", Difficulty.MEDIUM, 20, 40, ItemID.MAHOGANY_LOGS, StatType.Firemaking, TaskType.SKILLING, 50),
	BURN_YEW_LOGS("Burn yew logs", "You're tasked with burning yew logs", Difficulty.HARD, 20, 40, ItemID.YEW_LOGS, StatType.Firemaking, TaskType.SKILLING, 60),
	BURN_MAGIC_LOGS("Burn magic logs", "You're tasked with burning magic logs", Difficulty.HARD, 20, 40, ItemID.MAGIC_LOGS, StatType.Firemaking, TaskType.SKILLING, 75),
	BURN_REDWOOD_LOGS("Burn redwood logs", "You're tasked with burning redwood logs", Difficulty.ELITE, 20, 40, ItemID.REDWOOD_LOGS, StatType.Firemaking, TaskType.SKILLING, 90),
	FISH_SHRIMPS("Fish shrimps", "You're tasked with fishing shrimps", Difficulty.EASY, 10, 30, ItemID.RAW_SHRIMPS, StatType.Fishing, TaskType.SKILLING, 1),
	FISH_TROUT("Fish trouts", "You're tasked with fishing trouts", Difficulty.EASY, 15, 30, ItemID.RAW_TROUT, StatType.Fishing, TaskType.SKILLING, 20),
	FISH_SALMON("Fish salmon", "You're tasked with fishing salmon", Difficulty.MEDIUM, 15, 30, ItemID.RAW_SALMON, StatType.Fishing, TaskType.SKILLING, 30),
	FISH_TUNA("Fish tuna", "You're tasked with fishing tunas", Difficulty.MEDIUM, 20, 40, ItemID.RAW_TUNA, StatType.Fishing, TaskType.SKILLING, 35),
	FISH_LOBSTER("Fish lobsters", "You're tasked with fishing lobsters", Difficulty.MEDIUM, 20, 40, ItemID.RAW_LOBSTER, StatType.Fishing, TaskType.SKILLING, 40),
	FISH_SWORDFISH("Fish swordfish", "You're tasked with fishing swordfish", Difficulty.MEDIUM, 20, 40, ItemID.RAW_SWORDFISH, StatType.Fishing, TaskType.SKILLING, 50),
	FISH_MONKFISH("Fish monkfish", "You're tasked with fishing monkfish", Difficulty.HARD, 20, 40, ItemID.RAW_MONKFISH, StatType.Fishing, TaskType.SKILLING, 62),
	FISH_SHARK("Fish sharks", "You're tasked with fishing sharks", Difficulty.ELITE, 20, 40, ItemID.RAW_SHARK, StatType.Fishing, TaskType.SKILLING, 76),
	FISH_ANGLER("Fish anglerfish", "You're tasked with fishing anglerfish", Difficulty.ELITE, 20, 40, ItemID.RAW_ANGLERFISH, StatType.Fishing, TaskType.SKILLING, 82),
	COOK_SHRIMPS("Cook shrimps", "You're tasked with cooking shrimps", Difficulty.EASY, 20, 40, ItemID.SHRIMPS, StatType.Cooking, TaskType.SKILLING, 1),
	COOK_TROUT("Cook trouts", "You're tasked with cooking trouts", Difficulty.EASY, 20, 40, ItemID.TROUT, StatType.Cooking, TaskType.SKILLING, 15),
	COOK_SALMON("Cook salmon", "You're tasked with cooking salmon", Difficulty.MEDIUM, 20, 40, ItemID.SALMON, StatType.Cooking, TaskType.SKILLING, 25),
	COOK_TUNA("Cook tuna", "You're tasked with cooking tunas", Difficulty.MEDIUM, 20, 40, ItemID.TUNA, StatType.Cooking, TaskType.SKILLING, 30),
	COOK_LOBSTER("Cook lobsters", "You're tasked with cooking lobsters", Difficulty.MEDIUM, 20, 40, ItemID.LOBSTER, StatType.Cooking, TaskType.SKILLING, 40),
	COOK_SWORDFISH("Cook swordfish", "You're tasked with cooking swordfish", Difficulty.MEDIUM, 20, 40, ItemID.SWORDFISH, StatType.Cooking, TaskType.SKILLING, 45),
	COOK_MONKFISH("Cook monkfish", "You're tasked with cooking monkfish", Difficulty.HARD, 20, 40, ItemID.MONKFISH, StatType.Cooking, TaskType.SKILLING, 62),
	COOK_KARAMBWAN("Cook karambwan", "You're tasked with cooking karambwan", Difficulty.MEDIUM, 20, 40, ItemID.COOKED_KARAMBWAN, StatType.Cooking, TaskType.SKILLING, 1),
	COOK_SHARK("Cook sharks", "You're tasked with cooking sharks", Difficulty.ELITE, 20, 40, ItemID.SHARK, StatType.Cooking, TaskType.SKILLING, 80),
	COOK_ANGLER("Cook anglerfish", "You're tasked with cooking anglerfish", Difficulty.ELITE, 20, 40, ItemID.ANGLERFISH, StatType.Cooking, TaskType.SKILLING, 84),
	MINE_COPPER("Mine copper ore", "You're tasked with mining copper ore", Difficulty.EASY, 20, 40, ItemID.COPPER_ORE, StatType.Mining, TaskType.SKILLING, 1),
	MINE_TIN("Mine tin ore", "You're tasked with mining tin ore", Difficulty.EASY, 20, 40, ItemID.TIN_ORE, StatType.Mining, TaskType.SKILLING, 1),
	MINE_SILVER("Mine silver ore", "You're tasked with mining silver ore", Difficulty.EASY, 20, 40, ItemID.SILVER_ORE, StatType.Mining, TaskType.SKILLING, 20),
	MINE_IRON("Mine iron ore", "You're tasked with mining iron ore", Difficulty.EASY, 20, 40, ItemID.IRON_ORE, StatType.Mining, TaskType.SKILLING, 15),
	MINE_COAL("Mine coal", "You're tasked with mining coal", Difficulty.MEDIUM, 20, 40, ItemID.COAL, StatType.Mining, TaskType.SKILLING, 30),
	MINE_GOLD("Mine gold ore", "You're tasked with mining gold ore", Difficulty.MEDIUM, 20, 40, ItemID.GOLD_ORE, StatType.Mining, TaskType.SKILLING, 40),
	MINE_MITHRIL("Mine mithril ore", "You're tasked with mining mithril ore", Difficulty.HARD, 20, 40, ItemID.MITHRIL_ORE, StatType.Mining, TaskType.SKILLING, 55),
	MINE_ADAMANTITE("Mine adamantite ore", "You're tasked with mining adamantite ore", Difficulty.HARD, 20, 40, ItemID.ADAMANTITE_ORE, StatType.Mining, TaskType.SKILLING, 70),
	MINE_RUNITE("Mine runite ore", "You're tasked with mining runite ore", Difficulty.ELITE, 20, 40, ItemID.RUNITE_ORE, StatType.Mining, TaskType.SKILLING, 85),
	SMELT_BRONZE("Smelt bronze bars", "You're tasked with smelting bronze bars", Difficulty.EASY, 10, 30, ItemID.BRONZE_BAR, StatType.Smithing, TaskType.SKILLING, 1),
	SMELT_IRON("Smelt iron bars", "You're tasked with smelting iron bars", Difficulty.EASY, 20, 40, ItemID.IRON_BAR, StatType.Smithing, TaskType.SKILLING, 15),
	SMELT_STEEL("Smelt steel bars", "You're tasked with smelting steel bars", Difficulty.MEDIUM, 20, 40, ItemID.STEEL_BAR, StatType.Smithing, TaskType.SKILLING, 30),
	SMELT_GOLD("Smelt gold bars", "You're tasked with smelting gold bars", Difficulty.MEDIUM, 20, 40, ItemID.GOLD_BAR, StatType.Smithing, TaskType.SKILLING, 40),
	SMELT_MITHRIL("Smelt mithril bars", "You're tasked with smelting mithril bars", Difficulty.HARD, 10, 30, ItemID.MITHRIL_BAR, StatType.Smithing, TaskType.SKILLING, 55),
	SMELT_ADAMANTITE("Smelt adamantite bars", "You're tasked with smelting adamantite bars", Difficulty.HARD, 10, 30, ItemID.ADAMANTITE_BAR, StatType.Smithing, TaskType.SKILLING, 70),
	SMELT_RUNITE("Smelt runite bars", "You're tasked with smelting runite bars", Difficulty.ELITE, 10, 30, ItemID.RUNITE_BAR, StatType.Smithing, TaskType.SKILLING, 85),
	SMITH_BRONZE_PLATEBODY("Smith bronze platebodies", "You're tasked with smithing bronze platebodies", Difficulty.EASY, 5, 10, ItemID.BRONZE_PLATEBODY, StatType.Smithing, TaskType.SKILLING, 18),
	SMITH_BRONZE_HELMET("Smith bronze full helmets", "You're tasked with smithing bronze full helmets", Difficulty.EASY, 10, 20, ItemID.BRONZE_FULL_HELM, StatType.Smithing, TaskType.SKILLING, 7),
	SMITH_BRONZE_PLATELEGS("Smith bronze platelegs", "You're tasked with smithing bronze platelegs", Difficulty.EASY, 7, 20, ItemID.BRONZE_PLATELEGS, StatType.Smithing, TaskType.SKILLING, 16),
	SMITH_BRONZE_KITESHIELD("Smith bronze kiteshields", "You're tasked with smithing bronze kiteshields", Difficulty.EASY, 7, 15, ItemID.BRONZE_KITESHIELD, StatType.Smithing, TaskType.SKILLING, 12),
	SMITH_IRON_HELMET("Smith iron full helmets", "You're tasked with smithing iron full helmets", Difficulty.EASY, 10, 20, ItemID.IRON_FULL_HELM, StatType.Smithing, TaskType.SKILLING, 18),
	SMITH_IRON_PLATEBODY("Smith iron platebodies", "You're tasked with smithing iron platebodies", Difficulty.EASY, 5, 10, ItemID.IRON_PLATEBODY, StatType.Smithing, TaskType.SKILLING, 33),
	SMITH_IRON_PLATELEGS("Smith iron platelegs", "You're tasked with smithing iron platelegs", Difficulty.EASY, 7, 20, ItemID.IRON_PLATELEGS, StatType.Smithing, TaskType.SKILLING, 31),
	SMITH_IRON_KITESHIELD("Smith iron kiteshields", "You're tasked with smithing iron kiteshields", Difficulty.EASY, 7, 15, ItemID.IRON_KITESHIELD, StatType.Smithing, TaskType.SKILLING, 27),
	SMITH_STEEL_HELMET("Smith steel full helmets", "You're tasked with smithing steel full helmets", Difficulty.MEDIUM, 10, 20, ItemID.STEEL_FULL_HELM, StatType.Smithing, TaskType.SKILLING, 33),
	SMITH_STEEL_PLATEBODY("Smith steel platebodies", "You're tasked with smithing steel platebodies", Difficulty.MEDIUM, 5, 10, ItemID.STEEL_PLATEBODY, StatType.Smithing, TaskType.SKILLING, 48),
	SMITH_STEEL_PLATELEGS("Smith steel platelegs", "You're tasked with smithing steel platelegs", Difficulty.MEDIUM, 7, 20, ItemID.STEEL_PLATELEGS, StatType.Smithing, TaskType.SKILLING, 46),
	SMITH_STEEL_KITESHIELD("Smith steel kiteshields", "You're tasked with smithing steel kiteshields", Difficulty.MEDIUM, 7, 15, ItemID.STEEL_KITESHIELD, StatType.Smithing, TaskType.SKILLING, 42),
	SMITH_MITHRIL_HELMET("Smith mithril full helmets", "You're tasked with smithing mithril full helmets", Difficulty.HARD, 10, 20, ItemID.MITHRIL_FULL_HELM, StatType.Smithing, TaskType.SKILLING, 57),
	SMITH_MITHRIL_PLATEBODY("Smith mithril platebodies", "You're tasked with smithing mithril platebodies", Difficulty.HARD, 5, 10, ItemID.MITHRIL_PLATEBODY, StatType.Smithing, TaskType.SKILLING, 78),
	SMITH_MITHRIL_PLATELEGS("Smith mithril platelegs", "You're tasked with smithing mithril platelegs", Difficulty.HARD, 7, 20, ItemID.MITHRIL_PLATELEGS, StatType.Smithing, TaskType.SKILLING, 76),
	SMITH_MITHRIL_KITESHIELD("Smith mithril kiteshields", "You're tasked with smithing mithril kiteshields", Difficulty.HARD, 7, 15, ItemID.MITHRIL_KITESHIELD, StatType.Smithing, TaskType.SKILLING, 72),
	SMITH_ADAMANT_HELMET("Smith adamant full helmets", "You're tasked with smithing adamant full helmets", Difficulty.HARD, 10, 20, ItemID.ADAMANT_FULL_HELM, StatType.Smithing, TaskType.SKILLING, 77),
	SMITH_ADAMANT_PLATEBODY("Smith adamant platebodies", "You're tasked with smithing adamant platebodies", Difficulty.HARD, 5, 10, ItemID.ADAMANT_PLATEBODY, StatType.Smithing, TaskType.SKILLING, 88),
	SMITH_ADAMANT_PLATELEGS("Smith adamant platelegs", "You're tasked with smithing adamant platelegs", Difficulty.HARD, 7, 20, ItemID.ADAMANT_PLATELEGS, StatType.Smithing, TaskType.SKILLING, 86),
	SMITH_ADAMANT_KITESHIELD("Smith adamant kiteshields", "You're tasked with smithing adamant kiteshields", Difficulty.HARD, 7, 15, ItemID.ADAMANT_KITESHIELD, StatType.Smithing, TaskType.SKILLING, 82),
	SMITH_RUNE_HELMET("Smith rune full helmets", "You're tasked with smithing rune full helmets", Difficulty.ELITE, 10, 20, ItemID.RUNE_FULL_HELM, StatType.Smithing, TaskType.SKILLING, 92),
	SMITH_RUNE_PLATEBODY("Smith rune platebodies", "You're tasked with smithing rune platebodies", Difficulty.ELITE, 5, 10, ItemID.RUNE_PLATEBODY, StatType.Smithing, TaskType.SKILLING, 99),
	SMITH_RUNE_PLATELEGS("Smith rune platelegs", "You're tasked with smithing rune platelegs", Difficulty.ELITE, 7, 20, ItemID.RUNE_PLATELEGS, StatType.Smithing, TaskType.SKILLING, 99),
	SMITH_RUNE_KITESHIELD("Smith rune kiteshields", "You're tasked with smithing rune kiteshields", Difficulty.ELITE, 7, 15, ItemID.RUNE_KITESHIELD, StatType.Smithing, TaskType.SKILLING, 97),
	HARVEST_POTATO("Harvest potatoes farming.", "You're tasked with harvest potatoes", Difficulty.EASY, 10, 30, ItemID.POTATO, StatType.Farming, TaskType.SKILLING, 1),
	HARVEST_ONIONS("Harvest onions", "You're tasked with harvesting onions", Difficulty.EASY, 10, 30, ItemID.ONION, StatType.Farming, TaskType.SKILLING, 5),
	HARVEST_CABBAGE("Harvest cabbages", "You're tasked with harvesting cabbages", Difficulty.EASY, 10, 30, ItemID.CABBAGE, StatType.Farming, TaskType.SKILLING, 7),
	HARVEST_TOMATO("Harvest tomatoes", "You're tasked with harvesting tomatoes", Difficulty.EASY, 10, 30, ItemID.TOMATO, StatType.Farming, TaskType.SKILLING, 12),
	HARVEST_SWEETCORN("Harvest sweetcorn", "You're tasked with harvesting sweetcorn", Difficulty.EASY, 20, 40, ItemID.SWEETCORN, StatType.Farming, TaskType.SKILLING, 20),
	HARVEST_STRAWBERRY("Harvest strawberries", "You're tasked with harvesting strawberries", Difficulty.MEDIUM, 20, 40, ItemID.STRAWBERRY, StatType.Farming, TaskType.SKILLING, 31),
	HARVEST_WATERMELON("Harvest watermelons", "You're tasked with harvesting watermelons", Difficulty.MEDIUM, 20, 40, ItemID.WATERMELON, StatType.Farming, TaskType.SKILLING, 47),
	HARVEST_SNAPDRAGON("Harvest snapdragons", "You're tasked with harvesting snapdragons", Difficulty.MEDIUM, 15, 35, ItemID.GRIMY_SNAPDRAGON, StatType.Farming, TaskType.SKILLING, 62),
	HARVEST_MUSHROOM("Harvest mushrooms", "You're tasked with harvesting mushrooms", Difficulty.MEDIUM, 15, 35, ItemID.MUSHROOM, StatType.Farming, TaskType.SKILLING, 53),
	HARVEST_RANARR("Harvest ranarr herbs", "You're tasked with harvesting ranarr herbs", Difficulty.HARD, 15, 35, ItemID.GRIMY_RANARR_WEED, StatType.Farming, TaskType.SKILLING, 32),
	HARVEST_TOADFLAX("Harvest toadflax herbs", "You're tasked with harvesting toadflax herbs", Difficulty.HARD, 15, 35, ItemID.GRIMY_TOADFLAX, StatType.Farming, TaskType.SKILLING, 38),
	HARVEST_AVANTOE("Harvest avantoe herbs", "You're tasked with harvesting avantoe herbs", Difficulty.HARD, 15, 35, ItemID.GRIMY_AVANTOE, StatType.Farming, TaskType.SKILLING, 50),
	HARVEST_CADANTINE("Harvest cadantine herbs", "You're tasked with harvesting cadantine herbs", Difficulty.ELITE, 15, 35, ItemID.GRIMY_CADANTINE, StatType.Farming, TaskType.SKILLING, 67),
	HARVEST_LANTADYME("Harvest lantadyme herbs", "You're tasked with harvesting lantadyme herbs", Difficulty.ELITE, 15, 35, ItemID.GRIMY_LANTADYME, StatType.Farming, TaskType.SKILLING, 73),
	HARVEST_DWARF_WEED("Harvest dwarf weed herbs", "You're tasked with harvesting dwarf weed herbs", Difficulty.ELITE, 15, 35, ItemID.GRIMY_DWARF_WEED, StatType.Farming, TaskType.SKILLING, 79),
	HARVEST_TORSTOL("Harvest torstol herbs", "You're tasked with harvesting torstol herbs", Difficulty.ELITE, 15, 35, ItemID.GRIMY_TORSTOL, StatType.Farming, TaskType.SKILLING, 85),
	PICKPOCKET_MAN("Pickpocket from a man", "You're tasked with pickpocketing men", Difficulty.EASY, 20, 40, PickPocket.MAN, StatType.Thieving, TaskType.SKILLING, 1),
	PICKPOCKET_FARMER("Pickpocket from a farmer", "You're tasked with pickpocketing farmers", Difficulty.EASY, 20, 40, PickPocket.FARMER, StatType.Thieving, TaskType.SKILLING, 10),
	PICKPOCKET_GUARD("Pickpocket from a guard", "You're tasked with pickpocketing guards", Difficulty.MEDIUM, 20, 40, PickPocket.GUARD, StatType.Thieving, TaskType.SKILLING, 40),
	PICKPOCKET_KNIGHT("Pickpocket from a knight", "You're tasked with pickpocketing knights", Difficulty.HARD, 40, 60, PickPocket.KNIGHT, StatType.Thieving, TaskType.SKILLING, 55),
	PICKPOCKET_PALADIN("Pickpocket from a paladin", "You're tasked with pickpocketing paladins", Difficulty.HARD, 40, 80, PickPocket.PALADIN, StatType.Thieving, TaskType.SKILLING, 70),
	PICKPOCKET_HERO("Pickpocket from a hero", "You're tasked with pickpocketing heroes", Difficulty.ELITE, 40, 95, PickPocket.HERO, StatType.Thieving, TaskType.SKILLING, 80),
	//PICKPOCKET_ELF("Pickpocket from an elf", "You're tasked with pickpocketing elves", Difficulty.ELITE, 220, 320, PickPocket.ELF, StatType.Thieving, TaskType.SKILLING, 85),
	PICKPOCKET_TZHAAR("Pickpocket from a TzHaar", "You're tasked with pickpocketing TzHaar creatures", Difficulty.ELITE, 40, 95, PickPocket.TZHAAR_HUR, StatType.Thieving, TaskType.SKILLING, 90),
	//PICKPOCKET_MENAPHITE_THUG("Pickpocket from Menaphite thugs", "You're tasked with pickpocketing Menaphite thugs", Difficulty.HARD, 300, 400, PickPocket.MENAPHITE_THUG, StatType.Thieving, TaskType.SKILLING, 55),
	PICKPOCKET_GNOME("Pickpocket from a gnome", "You're tasked with pickpocketing gnomes", Difficulty.ELITE, 40, 95, PickPocket.GNOME, StatType.Thieving, TaskType.SKILLING, 75),
	PICKPOCKET_MASTER_FARMER("Pickpocket from a master farmer", "You're tasked with pickpocketing master farmers", Difficulty.MEDIUM, 40, 60, PickPocket.MASTER_FARMER, StatType.Thieving, TaskType.SKILLING, 38),
	CRAFT_HARD_LEATHER_BODY("Craft hard leather body", "You're tasked with crafting a hard leather body", Difficulty.EASY, 10, 30, ItemID.HARDLEATHER_BODY, StatType.Crafting, TaskType.SKILLING, 15),
	CRAFT_GOLD_RING("Craft gold ring", "You're tasked with crafting a gold ring", Difficulty.MEDIUM, 10, 20, ItemID.GOLD_RING, StatType.Crafting, TaskType.SKILLING, 20),
	CRAFT_UNPOWERED_ORB("Craft unpowered orb", "You're tasked with crafting an unpowered orb", Difficulty.MEDIUM, 20, 50, ItemID.UNPOWERED_ORB, StatType.Crafting, TaskType.SKILLING, 25),
	CRAFT_STUDDED_LEATHER_BODY("Craft studded leather body", "You're tasked with crafting a studded leather body", Difficulty.MEDIUM, 10, 20, ItemID.STUDDED_BODY, StatType.Crafting, TaskType.SKILLING, 41),
	CRAFT_SAPPHIRE_BRACELET("Craft sapphire bracelet", "You're tasked with crafting a sapphire bracelet", Difficulty.MEDIUM, 10, 20, ItemID.SAPPHIRE_BRACELET, StatType.Crafting, TaskType.SKILLING, 35),
	CRAFT_EMERALD_RING("Craft emerald ring", "You're tasked with crafting an emerald ring", Difficulty.MEDIUM, 10, 20, ItemID.EMERALD_RING, StatType.Crafting, TaskType.SKILLING, 40),
	CRAFT_RUBY_NECKLACE("Craft ruby necklace", "You're tasked with crafting a ruby necklace", Difficulty.HARD, 10, 20, ItemID.RUBY_NECKLACE, StatType.Crafting, TaskType.SKILLING, 50),
	CRAFT_DIAMOND_BRACELET("Craft diamond bracelet", "You're tasked with crafting a diamond bracelet", Difficulty.HARD, 10, 20, ItemID.DIAMOND_BRACELET, StatType.Crafting, TaskType.SKILLING, 55),
	//CRAFT_BEER_GLASS("Craft beer glass", "You're tasked with crafting a beer glass", Difficulty.EASY, 20, 50, ItemID.BEER_GLASS, StatType.Crafting, TaskType.SKILLING, 5),
	//CRAFT_VIAL("Craft vial", "You're tasked with crafting a vial", Difficulty.EASY, 25, 55, ItemID.VIAL, StatType.Crafting, TaskType.SKILLING, 10),
	//CRAFT_EMPTY_FISHBOWL("Craft empty fishbowl", "You're tasked with crafting an empty fishbowl", Difficulty.EASY, 30, 60, ItemID.EMPTY_FISHBOWL, StatType.Crafting, TaskType.SKILLING, 15),
	CRAFT_LIGHT_ORB("Craft light orb", "You're tasked with crafting a light orb", Difficulty.EASY, 20, 60, ItemID.LIGHT_ORB, StatType.Crafting, TaskType.SKILLING, 20),
	CRAFT_LEATHER_CHAPS("Craft leather chaps", "You're tasked with crafting leather chaps", Difficulty.MEDIUM, 10, 30, ItemID.LEATHER_CHAPS, StatType.Crafting, TaskType.SKILLING, 30),
	CRAFT_GREEN_DHIDE_BODY("Craft green dragonhide body", "You're tasked with crafting a green dragonhide body", Difficulty.HARD, 15, 30, ItemID.GREEN_DHIDE_BODY, StatType.Crafting, TaskType.SKILLING, 55),
	CRAFT_BLUE_DHIDE_BODY("Craft blue dragonhide body", "You're tasked with crafting a blue dragonhide body", Difficulty.HARD, 15, 40, ItemID.BLUE_DHIDE_BODY, StatType.Crafting, TaskType.SKILLING, 60),
	CRAFT_RED_DHIDE_BODY("Craft red dragonhide body", "You're tasked with crafting a red dragonhide body", Difficulty.HARD, 15, 40, ItemID.RED_DHIDE_BODY, StatType.Crafting, TaskType.SKILLING, 65),
	CRAFT_BLACK_DHIDE_BODY("Craft black dragonhide body", "You're tasked with crafting a black dragonhide body", Difficulty.HARD, 15, 50, ItemID.BLACK_DHIDE_BODY, StatType.Crafting, TaskType.SKILLING, 70),
	//CRAFT_GLASSBLOWING_VIAL("Craft vial through glassblowing", "You're tasked with crafting a vial through glassblowing", Difficulty.EASY, 20, 50, ItemID.VIAL, StatType.Crafting, TaskType.SKILLING, 5),
	CRAFT_GLASSBLOWING_ORB("Craft unpowered orb through glassblowing", "You're tasked with crafting an unpowered orb through glassblowing", Difficulty.MEDIUM, 20, 80, ItemID.UNPOWERED_ORB, StatType.Crafting, TaskType.SKILLING, 25),
	CRAFT_GLASSBLOWING_LANTERN("Craft lantern lens through glassblowing", "You're tasked with crafting a lantern lens through glassblowing", Difficulty.HARD, 40, 90, ItemID.LANTERN_LENS, StatType.Crafting, TaskType.SKILLING, 50),
	//COMPLETE_ACHIEVEMENTS("Complete achievement diary entries", "You're tasked with completing achievements", Difficulty.ELITE, 15, 25, "achiev", null, TaskType.SKILLING, 1),
	FLETCH_ARROW_SHAFT("Fletch some arrow shafts", "You're tasked with fletching arrow shafts sets", Difficulty.EASY, 20, 100, ItemID.ARROW_SHAFT, StatType.Fletching, TaskType.SKILLING, 1),
	FLETCH_UNSTRUNG_SHORTBOW("Fletch unstrung shortbows", "You're tasked with fletching unstrung shortbows", Difficulty.EASY, 10, 20, ItemID.SHORTBOW_U, StatType.Fletching, TaskType.SKILLING, 5),
	FLETCH_UNSTRUNG_LONGBOW("Fletch unstrung longbows", "You're tasked with fletching unstrung longbows", Difficulty.EASY, 10, 20, ItemID.LONGBOW_U, StatType.Fletching, TaskType.SKILLING, 10),
	FLETCH_UNSTRUNG_OAK_SHORTBOW("Fletch unstrung oak shortbows", "You're tasked with fletching unstrung oak shortbows", Difficulty.MEDIUM, 10, 20, ItemID.OAK_SHORTBOW_U, StatType.Fletching, TaskType.SKILLING, 20),
	FLETCH_UNSTRUNG_OAK_LONGBOW("Fletch unstrung oak longbows", "You're tasked with fletching unstrung oak longbows", Difficulty.MEDIUM, 10, 20, ItemID.OAK_LONGBOW_U, StatType.Fletching, TaskType.SKILLING, 25),
	FLETCH_UNSTRUNG_WILLOW_SHORTBOW("Fletch unstrung willow shortbows", "You're tasked with fletching unstrung willow shortbows", Difficulty.MEDIUM, 20, 40, ItemID.WILLOW_SHORTBOW_U, StatType.Fletching, TaskType.SKILLING, 35),
	FLETCH_UNSTRUNG_WILLOW_LONGBOW("Fletch unstrung willow longbows", "You're tasked with fletching unstrung willow longbows", Difficulty.MEDIUM, 20, 40, ItemID.WILLOW_LONGBOW_U, StatType.Fletching, TaskType.SKILLING, 40),
	FLETCH_UNSTRUNG_MAPLE_SHORTBOW("Fletch unstrung maple shortbows", "You're tasked with fletching unstrung maple shortbows", Difficulty.MEDIUM, 20, 40, ItemID.MAPLE_SHORTBOW_U, StatType.Fletching, TaskType.SKILLING, 50),
	FLETCH_UNSTRUNG_MAPLE_LONGBOW("Fletch unstrung maple longbows", "You're tasked with fletching unstrung maple longbows", Difficulty.MEDIUM, 20, 40, ItemID.MAPLE_LONGBOW_U, StatType.Fletching, TaskType.SKILLING, 55),
	FLETCH_UNSTRUNG_YEW_SHORTBOW("Fletch unstrung yew shortbows", "You're tasked with fletching unstrung yew shortbows", Difficulty.ELITE, 20, 50, ItemID.YEW_SHORTBOW_U, StatType.Fletching, TaskType.SKILLING, 65),
	FLETCH_UNSTRUNG_YEW_LONGBOW("Fletch unstrung yew longbows", "You're tasked with fletching unstrung yew longbows", Difficulty.ELITE, 20, 50, ItemID.YEW_LONGBOW_U, StatType.Fletching, TaskType.SKILLING, 70),
	FLETCH_UNSTRUNG_MAGIC_SHORTBOW("Fletch unstrung magic shortbows", "You're tasked with fletching unstrung magic shortbows", Difficulty.ELITE, 30, 50, ItemID.MAGIC_SHORTBOW_U, StatType.Fletching, TaskType.SKILLING, 80),
	FLETCH_UNSTRUNG_MAGIC_LONGBOW("Fletch unstrung magic longbows", "You're tasked with fletching unstrung magic longbows", Difficulty.ELITE, 30, 50, ItemID.MAGIC_LONGBOW_U, StatType.Fletching, TaskType.SKILLING, 85),
	FLETCH_BRONZE_ARROW("Fletch bronze arrows", "You're tasked with fletching sets of bronze arrows", Difficulty.EASY, 10, 20, ItemID.BRONZE_ARROW, StatType.Fletching, TaskType.SKILLING, 1),
	FLETCH_IRON_ARROW("Fletch iron arrows", "You're tasked with fletching sets of iron arrows", Difficulty.MEDIUM, 20, 30, ItemID.IRON_ARROW, StatType.Fletching, TaskType.SKILLING, 15),
	FLETCH_STEEL_ARROW("Fletch steel arrows", "You're tasked with fletching sets of steel arrows", Difficulty.MEDIUM, 20, 40, ItemID.STEEL_ARROW, StatType.Fletching, TaskType.SKILLING, 30),
	FLETCH_MITHRIL_ARROW("Fletch mithril arrows", "You're tasked with fletching sets of mithril arrows", Difficulty.HARD, 40, 60, ItemID.MITHRIL_ARROW, StatType.Fletching, TaskType.SKILLING, 45),
	FLETCH_ADAMANT_ARROW("Fletch adamant arrows", "You're tasked with fletching sets of adamant arrows", Difficulty.HARD, 60, 80, ItemID.ADAMANT_ARROW, StatType.Fletching, TaskType.SKILLING, 60),
	FLETCH_RUNE_ARROW("Fletch rune arrows", "You're tasked with fletching sets of rune arrows", Difficulty.ELITE, 80, 100, ItemID.RUNE_ARROW, StatType.Fletching, TaskType.SKILLING, 75),
	RUNECRAFT_AIR_RUNE("Runecraft air runes", "You're tasked with runecrafting air runes", Difficulty.EASY, 60, 120, ItemID.AIR_RUNE, StatType.Runecrafting, TaskType.SKILLING, 1),
	RUNECRAFT_MIND_RUNE("Runecraft mind runes", "You're tasked with runecrafting mind runes", Difficulty.EASY, 60, 120, ItemID.MIND_RUNE, StatType.Runecrafting, TaskType.SKILLING, 2),
	RUNECRAFT_WATER_RUNE("Runecraft water runes", "You're tasked with runecrafting water runes", Difficulty.EASY, 60, 120, ItemID.WATER_RUNE, StatType.Runecrafting, TaskType.SKILLING, 5),
	RUNECRAFT_EARTH_RUNE("Runecraft earth runes", "You're tasked with runecrafting earth runes", Difficulty.MEDIUM, 90, 180, ItemID.EARTH_RUNE, StatType.Runecrafting, TaskType.SKILLING, 9),
	RUNECRAFT_FIRE_RUNE("Runecraft fire runes", "You're tasked with runecrafting fire runes", Difficulty.MEDIUM, 90, 180, ItemID.FIRE_RUNE, StatType.Runecrafting, TaskType.SKILLING, 14),
	RUNECRAFT_BODY_RUNE("Runecraft body runes", "You're tasked with runecrafting body runes", Difficulty.MEDIUM, 90, 180, ItemID.BODY_RUNE, StatType.Runecrafting, TaskType.SKILLING, 20),
	RUNECRAFT_COSMIC_RUNE("Runecraft cosmic runes", "You're tasked with runecrafting cosmic runes", Difficulty.MEDIUM, 90, 180, ItemID.COSMIC_RUNE, StatType.Runecrafting, TaskType.SKILLING, 27),
	RUNECRAFT_CHAOS_RUNE("Runecraft chaos runes", "You're tasked with runecrafting chaos runes", Difficulty.HARD, 120, 240, ItemID.CHAOS_RUNE, StatType.Runecrafting, TaskType.SKILLING, 35),
	RUNECRAFT_NATURE_RUNE("Runecraft nature runes", "You're tasked with runecrafting nature runes", Difficulty.HARD, 120, 240, ItemID.NATURE_RUNE, StatType.Runecrafting, TaskType.SKILLING, 44),
	RUNECRAFT_LAW_RUNE("Runecraft law runes", "You're tasked with runecrafting law runes", Difficulty.HARD, 120, 240, ItemID.LAW_RUNE, StatType.Runecrafting, TaskType.SKILLING, 54),
	RUNECRAFT_DEATH_RUNE("Runecraft death runes", "You're tasked with runecrafting death runes", Difficulty.ELITE, 160, 300, ItemID.DEATH_RUNE, StatType.Runecrafting, TaskType.SKILLING, 65),
	RUNECRAFT_ASTRAL_RUNE("Runecraft astral runes", "You're tasked with runecrafting astral runes", Difficulty.MEDIUM, 160, 300, ItemID.ASTRAL_RUNE, StatType.Runecrafting, TaskType.SKILLING, 40),
	RUNECRAFT_SOUL_RUNE("Runecraft soul runes", "You're tasked with runecrafting soul runes", Difficulty.ELITE, 160, 300, ItemID.SOUL_RUNE, StatType.Runecrafting, TaskType.SKILLING, 90),
	RUNECRAFT_BLOOD_RUNE("Runecraft blood runes", "You're tasked with runecrafting blood runes", Difficulty.ELITE, 160, 300, ItemID.BLOOD_RUNE, StatType.Runecrafting, TaskType.SKILLING, 77),
	RUNECRAFT_WRATH_RUNE("Runecraft wrath runes", "You're tasked with runecrafting wrath runes", Difficulty.ELITE, 160, 300, ItemID.WRATH_RUNE, StatType.Runecrafting, TaskType.SKILLING, 95),
	BURY_LAVA_DRAGON_BONES("Bury lava dragon bones at lava isle", "You're tasked with burying lava dragon bones at lava isle", Difficulty.ELITE, 10, 40, "lavadragisle", StatType.Prayer, TaskType.SKILLING, 1),
	BUILD_CRUDE_CHAIR("Build crude wooden chairs in your POH", "You're tasked with building crude wooden chairs in your POH", Difficulty.EASY, 5, 15, Buildable.CRUDE_WOODEN_CHAIR, StatType.Construction, TaskType.SKILLING, 1),
	BUILD_WOODEN_CHAIR("Build wooden chairs in your POH", "You're tasked with building wooden chairs in your POH", Difficulty.EASY, 5, 15, Buildable.WOODEN_CHAIR, StatType.Construction, TaskType.SKILLING, Buildable.WOODEN_CHAIR.getLevelReq()),
	BUILD_OAK_CHAIR("Build oak chairs in your POH", "You're tasked with building oak chairs in your POH", Difficulty.EASY, 5, 15, Buildable.OAK_CHAIR, StatType.Construction, TaskType.SKILLING, Buildable.OAK_CHAIR.getLevelReq()),
	BUILD_GILDED_ALTAR("Build a gilded altars in your POH", "You're tasked with building a gilded altar in your POH", Difficulty.HARD, 1, 3, Buildable.GILDED_ALTAR, StatType.Construction, TaskType.SKILLING, Buildable.GILDED_ALTAR.getLevelReq()),
	BUILD_OAK_LARDER("Build oak larders in your POH", "You're tasked with building oak larders in your POH", Difficulty.MEDIUM, 10, 20, Buildable.OAK_LARDER, StatType.Construction, TaskType.SKILLING, Buildable.OAK_LARDER.getLevelReq()),
	BUILD_TEAK_TABLE("Build teak dining tables in your POH", "You're tasked with building teak dining tables in your POH", Difficulty.HARD, 10, 20, Buildable.TEAK_DINING_TABLE, StatType.Construction, TaskType.SKILLING, Buildable.TEAK_DINING_TABLE.getLevelReq()),
	BUILD_MAHOGANY_TABLE("Build mahogany dining tables in your POH", "You're tasked with building mahogany dining tables in your POH", Difficulty.HARD, 15, 40, Buildable.MAHOGANY_DINING_TABLE, StatType.Construction, TaskType.SKILLING, Buildable.MAHOGANY_DINING_TABLE.getLevelReq()),
	BUILD_OAK_THRONE("Build oak thrones in your POH", "You're tasked with building oak thrones in your POH", Difficulty.HARD, 15, 30, Buildable.OAK_THRONE, StatType.Construction, TaskType.SKILLING, Buildable.OAK_THRONE.getLevelReq()),
	KILL_WINTERTODT("Subdue the wintertodt", "You're tasked with subduing the wintertodt", Difficulty.HARD, 2, 5, "wintertodt", StatType.Firemaking, TaskType.SKILLING, 50),
	OBTAIN_PERK_POINTS("Obtain perk points", "You're tasked with obtaining perk points through the perk master", Difficulty.ELITE, 2, 4, "perkPoints", null, TaskType.SKILLING, 1),
	OBTAIN_REASON_POINTS("Obtain Reason points", "You are tasked with obtaining", Difficulty.ELITE, 5000, 15000, "reasonPoints", null, TaskType.SKILLING, 1),
	CATCH_ECLECTRIC("Catch eclectic implings", "You're tasked with catching eclectric implings", Difficulty.MEDIUM, 5, 10, "eclectic", StatType.Hunter, TaskType.SKILLING, 50),
	CATCH_GOURMET_IMPLINGS("Catch gourmet implings", "You're tasked with catching gourmet implings", Difficulty.MEDIUM, 5, 20, "gourmet", StatType.Hunter, TaskType.SKILLING, 28),
	CATCH_NATURE_IMPLINGS("Catch nature implings", "You're tasked with catching nature implings", Difficulty.HARD, 7, 25, "nature", StatType.Hunter, TaskType.SKILLING, 58),
	CATCH_MAGPIE_IMPLINGS("Catch magpie implings", "You're tasked with catching magpie implings", Difficulty.HARD, 7, 30, "magpie", StatType.Hunter, TaskType.SKILLING, 65),
	//KILL_MAGE_CATALYST("Kill Catalyst Magers", "You're tasked with killing Catalyst Magers", Difficulty.MEDIUM, 7, 40, 8066, StatType.Prayer, TaskType.PVM, 37),
	KILL_MELEE_CATALYST("Kill Catalyst Brutes", "You're tasked with killing Catalyst Brutes", Difficulty.MEDIUM, 20, 40, 8064, StatType.Prayer, TaskType.PVM, 43),
	KILL_LIZARDMAN_SHAMANS("Kill Lizardman Shamans", "You're tasked with killing Lizardman Shamans in the Molch dungeon", Difficulty.MEDIUM, 20, 40, 8565, null, TaskType.PVM, 80),
	KILL_VYRE_WATCH("Kill Vyrewatch sentinels", "You're tasked with killing Vyrewatch sentinels", Difficulty.HARD, 20, 40, "vyrewatch sentinel", null, TaskType.PVM, 90),
	;

	String name;
	String description;
	Difficulty difficulty;
	int mimumumAmount;
	int maximumAmount;
	StatType statType;
	int level;

	Object identifier;
	TaskType taskType;

	DailyTasks(String name, String description, Difficulty difficulty, int mimumumAmount, int maximumAmount,
	           Object identifier, StatType statType, TaskType taskType, int level) {
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.maximumAmount = maximumAmount;
		this.mimumumAmount = mimumumAmount;
		this.identifier = identifier;
		this.statType = statType;
		this.level = level;
		this.taskType = taskType;
	}

	public static final DailyTasks[] VALUES = values();

	enum Difficulty {
		EASY,
		MEDIUM,
		HARD,
		ELITE;

		public static final Difficulty[] VALUES = values();
	}

	enum TaskType {
		SKILLING,
		PVM,
		MISC;

		public static final TaskType[] VALUES = values();
	}

	private static DailyTasks getTaskByTier(Player player, Difficulty difficulty) {
		List<DailyTasks> filteredTasks = Arrays.stream(DailyTasks.VALUES)
			.filter(task -> task.difficulty == difficulty)
			.filter(dailyTasks -> {
				if (dailyTasks.statType == null) {
					return player.getCombat().getLevel() >= dailyTasks.level;
				} else {
					return player.getStats().get(dailyTasks.statType).fixedLevel >= dailyTasks.level;
				}
			})
			.filter(dailyTasks -> {
				if (player.getAchievement().getCompletionAmount() > 10) {
					return !dailyTasks.name().contains("achiev");
				}
				return true;
			})
			.collect(Collectors.toList());

		// If no tasks found for the specified difficulty, try assigning a task with a lower tier
		if (filteredTasks.isEmpty()) {
			int lowerDifficultyOrdinal = difficulty.ordinal() - 1;
			if (lowerDifficultyOrdinal >= 0) {
				Difficulty lowerDifficulty = Difficulty.values()[lowerDifficultyOrdinal];
				// Recursively call the method with the lower difficulty tier
				return getTaskByTier(player, lowerDifficulty);
			}
			// If the difficulty is already at the lowest tier, return null or handle it as needed
			return null;
		}

		// Generate a random index within the range of the filtered tasks
		int randomIndex = Random.get(filteredTasks.size());

		// Return the randomly selected task
		return filteredTasks.get(randomIndex);
	}


	public static void handleTaskKill(Player player, int npcId) {
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			if (player.currentDailyTasks[i].taskType == TaskType.PVM) {
				if (player.currentDailyTasks[i].identifier instanceof Integer) {
					if ((int) player.currentDailyTasks[i].identifier == npcId) {
						if (player.currentDailyTaskAmounts[i] > 0) {
							player.currentDailyTaskAmounts[i]--;
							if (player.currentDailyTaskAmounts[i] <= 0) {
								player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
							}
						}
					}
				}
			}
		}
	}


	public static void handleTaskProgression(Player player, Object identifier) {
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			if (player.currentDailyTasks[i].identifier == identifier) {
				if (player.currentDailyTaskAmounts[i] > 0) {
					player.currentDailyTaskAmounts[i]--;
					if (player.currentDailyTaskAmounts[i] <= 0) {
						player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
					}
				}
			}
		}
	}

	public static void handleItemObtained(Player player, int itemId, StatType statType) {
		//System.out.println("Item obtained: " + itemId + " statType: " + statType);
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			//  System.out.println("Checking task: " + player.currentDailyTasks[i].name());
			// System.out.println("Checking identifier: " + player.currentDailyTasks[i].identifier);
			if (player.currentDailyTasks[i].identifier instanceof Integer) {
				//  System.out.println("Identifier is integer");
				if ((int) player.currentDailyTasks[i].identifier == itemId) {
					if (player.currentDailyTasks[i].statType == statType) {
						if (player.currentDailyTaskAmounts[i] > 0) {
							player.currentDailyTaskAmounts[i]--;
							if (player.currentDailyTaskAmounts[i] <= 0) {
								player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
							}
						}
					}
				}
			}
		}
	}

	public static void handleItemObtained(Player player, int itemId, StatType statType, int makeAmount) {
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			if (player.currentDailyTasks[i].identifier instanceof Integer) {
				if ((int) player.currentDailyTasks[i].identifier == itemId) {
					if (player.currentDailyTasks[i].statType == statType) {
						if (player.currentDailyTaskAmounts[i] > 0) {
							player.currentDailyTaskAmounts[i] -= makeAmount;
							if (player.currentDailyTaskAmounts[i] <= 0) {
								player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
							}
						}
					}
				}
			}
		}
	}

	private static DailyTasks getRandomTaskByTier(Player player, Difficulty difficulty, List<DailyTasks> tasks, List<DailyTasks> fallbackTasks, List<Integer> assignedTasks) {
		int maxLevelDifference = 25; // Maximum allowed level difference between fixedLevel and dailyTasks.level

		// Filter tasks based on the specified difficulty, other criteria, and assignedTasks list
		List<DailyTasks> filteredTasks = tasks.stream()
			.filter(task -> task.difficulty == difficulty)
			.filter(dailyTasks -> {
				if (!assignedTasks.contains(dailyTasks.ordinal())) { // Check if ordinal is not in assignedTasks
					if (dailyTasks.statType == null) {
						return player.getCombat().getLevel() >= dailyTasks.level;
					} else {
						int fixedLevel = player.getStats().get(dailyTasks.statType).fixedLevel;
						return fixedLevel >= dailyTasks.level && fixedLevel <= dailyTasks.level + maxLevelDifference;
					}
				}
				return false;
			})
			.collect(Collectors.toList());

		// If no tasks available for the specified difficulty, use the fallback tasks
		if (filteredTasks.isEmpty()) {
			filteredTasks = fallbackTasks.stream()
				.filter(task -> task.difficulty == difficulty)
				.filter(dailyTasks -> {
					if (!assignedTasks.contains(dailyTasks.ordinal())) { // Check if ordinal is not in assignedTasks
						if (dailyTasks.statType == null) {
							return player.getCombat().getLevel() >= dailyTasks.level;
						} else {
							int fixedLevel = player.getStats().get(dailyTasks.statType).fixedLevel;
							return fixedLevel >= dailyTasks.level && fixedLevel <= dailyTasks.level + maxLevelDifference;
						}
					}
					return false;
				})
				.collect(Collectors.toList());
		}

		if (filteredTasks.isEmpty()) {
			List<Difficulty> difficulties = new ArrayList<>();
			difficulties.addAll(Arrays.asList(Difficulty.values()));
			difficulties.remove(difficulty);
			return getRandomTaskByTier(player, Random.get(difficulties), tasks, fallbackTasks, assignedTasks);
		}

		// Generate a random index within the range of the filtered tasks
		int randomIndex = Random.get(filteredTasks.size() - 1);

		// Return the randomly selected task
		return filteredTasks.get(randomIndex);
	}


	public static void claimReward(Player player, int index) {
		if (player.currentDailyTaskAmounts[index] < 1) {

		} else {
			player.sendMessage("You must complete the task before claiming the reward!");
		}
	}

	private static void setRewards(Player player) {
		for (int i = 0; i < 5; i++) {
			if (player.currentDailyTasks[i].difficulty == Difficulty.EASY)
				player.currentDailyRewards[i] = player.getGameMode().isIronMan() ? ironEasyRewards.rollItem() : easyRewards.rollItem();
			else if (player.currentDailyTasks[i].difficulty == Difficulty.MEDIUM)
				player.currentDailyRewards[i] = player.getGameMode().isIronMan() ? ironMediumRewards.rollItem() : mediumRewards.rollItem();
			else if (player.currentDailyTasks[i].difficulty == Difficulty.HARD)
				player.currentDailyRewards[i] = player.getGameMode().isIronMan() ? ironHardRewards.rollItem() : hardRewards.rollItem();
			else if (player.currentDailyTasks[i].difficulty == Difficulty.ELITE)
				player.currentDailyRewards[i] = player.getGameMode().isIronMan() ? ironEliteRewards.rollItem() : eliteRewards.rollItem();
			player.dailyTasksCompletionReward = player.getGameMode().isIronMan() ? ironCompletionReward.rollItem() : completionReward.rollItem();
			player.currentDailyRewardsClaimed[i] = false;
		}
		for (int i = 0; i < 5; i++)
			player.currentDailyRewards[i].setAmount(player.currentDailyRewards[i].getAmount());
		player.dailyTaskCompletionRewardClaimed = false;
	}

	public static void handleTaskDecrement(Player player, String identifier) {
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			if (player.currentDailyTasks[i].identifier instanceof String) {
				if (((String) player.currentDailyTasks[i].identifier).equalsIgnoreCase(identifier)) {
					if (player.currentDailyTaskAmounts[i] > 0) {
						player.currentDailyTaskAmounts[i]--;
						if (player.currentDailyTaskAmounts[i] <= 0) {
							player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
						}
					}
				}
			}
		}
	}

	public static void handleTaskDecrement(Player player, int npcId) {
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			if (player.currentDailyTasks[i].identifier instanceof Integer) {
				if ((int) player.currentDailyTasks[i].identifier == npcId) {
					if (player.currentDailyTaskAmounts[i] > 0) {
						player.currentDailyTaskAmounts[i]--;
						if (player.currentDailyTaskAmounts[i] <= 0) {
							player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
						}
					}
				}
			}
		}
	}

	public static void handleTaskDecrementItemId(Player player, int itemId) {
		// System.out.println("Item id: " + itemId);
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			if (player.currentDailyTasks[i].identifier instanceof Integer) {
				// System.out.println("Identifier is integer of: " + player.currentDailyTasks[i].identifier);
				if ((int) player.currentDailyTasks[i].identifier == itemId) {
					// System.out.println("Identifier matches item id");
					if (player.currentDailyTaskAmounts[i] > 0) {
						//System.out.println("Amount is greater than 0");
						player.currentDailyTaskAmounts[i]--;
						if (player.currentDailyTaskAmounts[i] <= 0) {
							player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
						}
					}
				}
			}
		}
	}

	public static void handleTaskDecrement(Player player, String identifier, int amount) {
		for (int i = 0; i < player.currentDailyTasks.length; i++) {
			if (player.currentDailyTasks[i].identifier instanceof String) {
				if (((String) player.currentDailyTasks[i].identifier).equalsIgnoreCase(identifier)) {
					if (player.currentDailyTaskAmounts[i] > 0) {
						player.currentDailyTaskAmounts[i] -= amount;
						if (player.currentDailyTaskAmounts[i] <= 0) {
							player.sendMessage("You have completed one of your " + player.currentDailyTasks[i].difficulty.toString().toLowerCase() + " tasks.");
						}
					}
				}
			}
		}
	}

	public static void assignDailyTasks(Player player, boolean scroll) {
		List<DailyTasks> pvmTasks = new ArrayList<>();
		List<DailyTasks> skillingTasks = new ArrayList<>();
		List<DailyTasks> miscTasks = new ArrayList<>();
		List<Integer> assignedTasks = new ArrayList<>();

		// Split tasks into PvM, skilling, and misc lists
		for (DailyTasks task : DailyTasks.VALUES) {
			if (task.taskType == TaskType.PVM) {
				pvmTasks.add(task);
			} else if (task.taskType == TaskType.SKILLING) {
				skillingTasks.add(task);
			} else if (task.taskType == TaskType.MISC) {
				miscTasks.add(task);
			}
		}

		// Randomly select PvM tasks
		for (int i = 0; i < 2; i++) {
			DailyTasks task = getRandomTaskByTier(player, Difficulty.HARD, Misc.random(1) == 0 ? skillingTasks : pvmTasks, skillingTasks, assignedTasks);
			assignedTasks.add(task.ordinal());
			int amount = Random.get(task.mimumumAmount, task.maximumAmount);
			player.currentDailyTaskAmounts[i] = amount;
			player.startingDailyTaskAmounts[i] = amount;
			player.currentDailyTasks[i] = task;
		}

		// Randomly select skilling tasks, with misc tasks as fallback
		DailyTasks dailyTasks2 = getRandomTaskByTier(player, Difficulty.HARD, Misc.random(1) == 0 ? skillingTasks : pvmTasks, skillingTasks, assignedTasks);
		assignedTasks.add(dailyTasks2.ordinal());
		player.currentDailyTasks[2] = dailyTasks2;
		int amount2 = Random.get(dailyTasks2.mimumumAmount, dailyTasks2.maximumAmount);
		player.currentDailyTaskAmounts[2] = amount2;
		player.startingDailyTaskAmounts[2] = amount2;

		DailyTasks dailyTasks3 = getRandomTaskByTier(player, Difficulty.HARD, Misc.random(1) == 0 ? skillingTasks : pvmTasks, skillingTasks, assignedTasks);
		assignedTasks.add(dailyTasks3.ordinal());
		int amount3 = Random.get(dailyTasks3.mimumumAmount, dailyTasks3.maximumAmount);
		player.currentDailyTasks[3] = dailyTasks3;
		player.currentDailyTaskAmounts[3] = amount3;
		player.startingDailyTaskAmounts[3] = amount3;


		DailyTasks dailyTasks4 = getRandomTaskByTier(player, Difficulty.ELITE, Misc.random(1) == 0 ? skillingTasks : pvmTasks, skillingTasks, assignedTasks);
		assignedTasks.add(dailyTasks4.ordinal());
		int amount4 = Random.get(dailyTasks4.mimumumAmount, dailyTasks4.maximumAmount);
		player.currentDailyTasks[4] = dailyTasks4;
		player.currentDailyTaskAmounts[4] = amount4;
		player.startingDailyTaskAmounts[4] = amount4;

		setRewards(player);

		int slayerSkips = 0;
		switch (player.getSecondaryGroup()) {
			case SUPREME_DONATOR:
			case LEGENDARY_DONATOR:
				slayerSkips = 5;
				break;
			case PLATINUM_DONATOR:
			case GOLD_DONATOR:
				slayerSkips = 4;
				break;
			case NOBLE_DONATOR:
				slayerSkips = 3;
				break;
			case SUPER_DONATOR:
			case ELITE_DONATOR:
				slayerSkips = 2;
				break;
			case DONATOR:
				slayerSkips = 1;
				break;

		}
		if (!scroll) {
			if (slayerSkips > 0) {
				if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
					player.getInventory().addOrDrop(30458, slayerSkips);
					player.getInventory().addOrDrop(30629, slayerSkips);
					player.sendMessage(slayerSkips + " of each slayer scroll was added to your inventory for being a " +
						player.getSecondaryGroup().name().replace("_", " ").toLowerCase() + ".");
				} else {
					player.getBank().add(30458, slayerSkips);
					player.getBank().add(30629, slayerSkips);
					player.sendMessage(slayerSkips + " of each slayer scroll were added to your bank for being a " +
						player.getSecondaryGroup().name().replace("_", " ").toLowerCase() + ".");
				}
			}
		}
		Instant now = Instant.now();
		player.lastDailyTaskResetInEpoch = now.getEpochSecond();
		player.sendMessage("Your new daily tasks have been assigned!");
	}

	static LootTable easyRewards = new LootTable().addTable(1,
		new LootItem(ItemID.OAK_LOGS + 1, 50, 100, 5),
		new LootItem(ItemID.WILLOW_LOGS + 1, 50, 100, 4),
		new LootItem(ItemID.GRIMY_GUAM_LEAF + 1, 20, 30, 5),
		new LootItem(ItemID.GRIMY_MARRENTILL + 1, 20, 30, 5),
		new LootItem(ItemID.GRIMY_TARROMIN + 1, 20, 30, 4),
		new LootItem(ItemID.OAK_PLANK + 1, 20, 40, 5),
		new LootItem(ItemID.GUAM_SEED, 3, 6, 5),
		new LootItem(ItemID.TARROMIN_SEED, 3, 6, 5),
		new LootItem(ItemID.MARRENTILL_SEED, 3, 6, 4),
		new LootItem(ItemID.IRON_ORE + 1, 20, 40, 5),
		new LootItem(ItemID.COAL + 1, 40, 70, 5),
		new LootItem(ItemID.SOFT_CLAY + 1, 20, 40, 5),
		new LootItem(ItemID.BRONZE_BAR + 1, 20, 40, 5),
		new LootItem(ItemID.IRON_BAR + 1, 20, 40, 4),
		new LootItem(ItemID.RAW_LOBSTER + 1, 20, 60, 3),
		new LootItem(ItemID.RAW_TUNA + 1, 20, 60, 4),
		new LootItem(ItemID.ZOGRE_BONES + 1, 30, 60, 4),
		new LootItem(ItemID.MOLTEN_GLASS + 1, 40, 60, 4),
		new LootItem(ItemID.RUNE_ARROWP_5627, 75, 2),
		new LootItem(ItemID.BOW_STRING + 1, 75, 200, 3),
		new LootItem(ItemID.LEATHER + 1, 20, 50, 5),
		new LootItem(ItemID.HARD_LEATHER + 1, 20, 50, 4),
		new LootItem(ItemID.RED_SPIDERS_EGGS + 1, 30, 50, 4),
		new LootItem(ItemID.WHITE_BERRIES + 1, 30, 50, 4),
		new LootItem(ItemID.UNCUT_SAPPHIRE + 1, 30, 50, 4),
		new LootItem(ItemID.UNCUT_EMERALD + 1, 30, 50, 3),
		new LootItem(ItemID.GREEN_DRAGON_LEATHER + 1, 20, 40, 2),
		new LootItem(ItemID.UNCUT_RUBY + 1, 30, 50, 2),
		new LootItem(ItemID.BABYDRAGON_BONES + 1, 30, 50, 2)
	);
	static LootTable mediumRewards = new LootTable().addTable(1,
		new LootItem(ItemID.GRIMY_HARRALANDER + 1, 25, 40, 5),
		new LootItem(ItemID.GRIMY_TOADFLAX + 1, 25, 40, 4),
		new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 20, 30, 3),
		new LootItem(ItemID.TEAK_PLANK + 1, 30, 50, 3),
		new LootItem(ItemID.MAPLE_LOGS + 1, 40, 60, 4),
		new LootItem(ItemID.CRUSHED_NEST + 1, 30, 40, 3),
		new LootItem(ItemID.CHAOS_RUNE, 100, 250, 5),
		new LootItem(ItemID.DEATH_RUNE, 100, 250, 4),
		new LootItem(ItemID.HARRALANDER_SEED, 4, 8, 5),
		new LootItem(ItemID.TOADFLAX_SEED, 3, 7, 4),
		new LootItem(ItemID.RANARR_SEED, 3, 6, 3),
		new LootItem(ItemID.GOAT_HORN_DUST + 1, 40, 60, 4),
		new LootItem(ItemID.SNAPE_GRASS + 1, 40, 70, 5),
		new LootItem(ItemID.POTATO_CACTUS + 1, 40, 60, 4),
		new LootItem(ItemID.RAW_SWORDFISH + 1, 40, 80, 4),
		new LootItem(ItemID.DRAGON_BONES + 1, 50, 100, 2),
		new LootItem(ItemID.WYRM_BONES + 1, 60, 120, 3),
		new LootItem(ItemID.BLUE_DRAGON_LEATHER + 1, 40, 80, 4),
		new LootItem(ItemID.RED_DRAGON_LEATHER + 1, 40, 80, 3),
		new LootItem(ItemID.RAW_LOBSTER + 1, 50, 100, 5),
		new LootItem(ItemID.COAL + 1, 90, 200, 5),
		new LootItem(ItemID.UNCUT_DIAMOND + 1, 50, 80, 3),
		new LootItem(ItemID.STEEL_BAR + 1, 40, 80, 4),
		new LootItem(ItemID.BATTLESTAFF + 1, 40, 60, 4),
		new LootItem(ItemID.ROYAL_SEED_POD, 1, 1, 1),
		new LootItem(ItemID.CANNONBALL, 150, 300, 3),
		new LootItem(ItemID.BURNT_PAGE, 1, 1, 1),
		new LootItem(25527, 120, 250, 2), //Stardust
		new LootItem(ItemID.MAGIC_SECATEURS, 1, 1, 1),
		new LootItem(ItemID.YEW_LONGBOW_U + 1, 75, 150, 3),
		new LootItem(ItemID.BRIMSTONE_KEY, 1, 1, 3),
		new LootItem(ItemID.CRYSTAL_KEY, 1, 2, 3),
		new LootItem(ItemID.UNCUT_DRAGONSTONE + 1, 30, 60, 3),
		new LootItem(ItemID.AMETHYST + 1, 75, 100, 3),
		new LootItem(ItemID.AMETHYST_ARROWP_21336, 75, 150, 2),
		new LootItem(25853, 150, 250, 2), //Amethyst dart tip
		new LootItem(ItemID.NINJA_IMPLING_JAR + 1, 3, 5, 3),
		new LootItem(ItemID.REWARD_CASKET_MEDIUM, 2, 3, 3),
		new LootItem(30470, 1, 2), //Achievement lamp
		new LootItem(ItemID.CRYSTAL_SAW, 1, 1, 1),
		new LootItem(ItemID.WILLOW_SEED, 3, 6, 4)
	);
	static LootTable hardRewards = new LootTable().addTable(1,
		new LootItem(ItemID.PRAYER_POTION4 + 1, 20, 30, 4),
		new LootItem(ItemID.SUPER_ATTACK4 + 1, 15, 30, 5),
		new LootItem(ItemID.SUPER_STRENGTH4 + 1, 15, 30, 5),
		new LootItem(ItemID.SUPER_DEFENCE4 + 1, 15, 30, 5),
		new LootItem(ItemID.MORT_MYRE_FUNGUS + 1, 60, 80, 4),
		new LootItem(ItemID.SNAPE_GRASS + 1, 60, 80, 4),
		new LootItem(ItemID.LIMPWURT_ROOT + 1, 60, 80, 4),
		new LootItem(ItemID.CRUSHED_NEST + 1, 60, 80, 3),
		new LootItem(ItemID.MAHOGANY_PLANK + 1, 75, 150, 4),
		new LootItem(ItemID.DEATH_RUNE, 300, 500, 5),
		new LootItem(ItemID.WRATH_RUNE, 200, 300, 4),
		new LootItem(ItemID.BLOOD_RUNE, 300, 500, 5),
		new LootItem(ItemID.RANARR_SEED, 6, 8, 5),
		new LootItem(ItemID.KWUARM_SEED, 6, 9, 5),
		new LootItem(ItemID.AVANTOE_SEED, 6, 9, 5),
		new LootItem(ItemID.SNAPDRAGON_SEED, 6, 9, 5),
		new LootItem(ItemID.SNAPE_GRASS_SEED, 8, 12, 5),
		new LootItem(ItemID.LAVA_DRAGON_BONES + 1, 50, 120, 3),
		new LootItem(ItemID.RAURG_BONES + 1, 50, 90, 4),
		new LootItem(ItemID.HYDRA_BONES + 1, 50, 90, 4),
		new LootItem(ItemID.MITHRIL_BAR + 1, 50, 90, 5),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 50, 90, 4),
		new LootItem(ItemID.STEEL_BAR + 1, 60, 120, 5),
		new LootItem(ItemID.UNCUT_DIAMOND + 1, 80, 150, 4),
		new LootItem(ItemID.UNCUT_DRAGONSTONE + 1, 60, 120, 3),
		new LootItem(ItemID.MAGIC_SHORTBOW_U + 1, 90, 180, 4),
		new LootItem(ItemID.BATTLESTAFF + 1, 15, 40, 5),
		new LootItem(ItemID.BLACK_DRAGON_LEATHER + 1, 60, 100, 4),
		new LootItem(ItemID.DRAGON_DART_TIP, 160, 300, 3),
		new LootItem(ItemID.RUNITE_BOLTS_UNF, 140, 210, 3),
		new LootItem(ItemID.BRIMSTONE_KEY, 2, 3, 4),
		new LootItem(ItemID.CRYSTAL_KEY, 3, 5, 4),
		new LootItem(ItemID.ENHANCED_CRYSTAL_KEY, 2, 3, 2),
		new LootItem(ItemID.HERB_BOX, 2, 4, 2),
		new LootItem(ItemID.BAG_FULL_OF_GEMS, 3, 5, 2),
		new LootItem(30458, 2, 3, 3), //Slayer task skip scroll
		new LootItem(ItemID.CANNONBALL, 100, 300, 5),
		new LootItem(ItemID.ANCIENT_SHARD, 1, 3, 2),
		new LootItem(ItemID.REWARD_CASKET_HARD, 2, 4, 3),
		new LootItem(ItemID.MAPLE_SEED, 3, 6, 5),
		new LootItem(ItemID.YEW_SEED, 2, 4, 4),
		new LootItem(ItemID.DRAGON_IMPLING_JAR, 2, 4, 3),
		new LootItem(ItemID.PALM_TREE_SEED, 2, 3, 4),
		new LootItem(ItemID.RAW_MONKFISH + 1, 75, 150, 5),
		new LootItem(ItemID.RAW_SEA_TURTLE + 1, 75, 150, 4),
		new LootItem(ItemID.RAW_KARAMBWAN + 1, 75, 150, 4)
	);
	static LootTable eliteRewards = new LootTable().addTable(1,
		new LootItem(ItemID.SUPER_RESTORE4 + 1, 30, 50, 5),
		new LootItem(ItemID.SUPER_COMBAT_POTION4 + 1, 15, 30, 4),
		new LootItem(ItemID.SUPER_STRENGTH4 + 1, 30, 50, 5),
		new LootItem(ItemID.MAGIC_POTION4 + 1, 30, 50, 5),
		new LootItem(ItemID.WINE_OF_ZAMORAK + 1, 60, 120, 5),
		new LootItem(ItemID.SARADOMIN_BREW4 + 1, 15, 30, 5),
		new LootItem(ItemID.AMYLASE_CRYSTAL, 50, 100, 3),
		new LootItem(ItemID.WRATH_RUNE, 400, 600, 4),
		new LootItem(ItemID.MAGIC_SEED, 2, 5, 4),
		new LootItem(ItemID.YEW_SEED, 3, 5, 5),
		new LootItem(ItemID.DRAGONFRUIT_TREE_SEED, 2, 5, 5),
		new LootItem(ItemID.SNAPDRAGON_SEED, 5, 9, 5),
		new LootItem(ItemID.TORSTOL_SEED, 5, 9, 5),
		new LootItem(7478, 4, 8, 3), //Instance tokens
		new LootItem(ItemID.RUNITE_BAR + 1, 40, 60, 4),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 60, 80, 5),
		new LootItem(ItemID.BATTLESTAFF + 1, 30, 50, 5),
		new LootItem(ItemID.RAW_SHARK + 1, 100, 160, 5),
		new LootItem(ItemID.RAW_ANGLERFISH + 1, 75, 125, 4),
		new LootItem(ItemID.RAW_DARK_CRAB + 1, 75, 125, 3),
		new LootItem(ItemID.MAGIC_LOGS + 1, 50, 120, 4),
		new LootItem(ItemID.REDWOOD_LOGS + 1, 40, 110, 3),
		new LootItem(ItemID.YEW_LOGS + 1, 70, 150, 5),
		new LootItem(ItemID.REWARD_CASKET_ELITE, 2, 4, 3),
		new LootItem(ItemID.DRAGON_DART_TIP, 250, 400, 4),
		new LootItem(ItemID.DRAGON_BOLTS_UNF, 70, 140, 4),
		new LootItem(ItemID.OURG_BONES + 1, 50, 70, 5),
		new LootItem(ItemID.SUPERIOR_DRAGON_BONES + 1, 50, 80, 4),
		new LootItem(ItemID.LUCKY_IMPLING_JAR, 2, 4, 3),
		new LootItem(ItemID.DRAGON_JAVELIN, 100, 200, 3),
		new LootItem(30579, 1, 1), //Small exp lamp
		new LootItem(608, 1, 2), //10% drop rate scroll
		new LootItem(30459, 1, 3, 2), //Double drop scroll
		new LootItem(620, 3, 5, 1), //Vote pt ticket
		new LootItem(30570, 1, 2), //Perk point scroll
		new LootItem(30470, 2, 2), //Achievement lamp
		new LootItem(ItemID.DHAROKS_ARMOUR_SET, 1, 1),
		new LootItem(ItemID.VERACS_ARMOUR_SET, 1, 1),
		new LootItem(ItemID.MAGES_BOOK, 1, 1),
		new LootItem(ItemID.UNCUT_ONYX, 1, 1)
	);
	static LootTable completionReward = new LootTable().addTable(1,
		new LootItem(30461, 1, 2), //Donator mystery box
		new LootItem(30464, 1, 2), //$5 bond
		new LootItem(2528, 1, 1), //Donator lamp
		new LootItem(30580, 1, 4), //Medium exp lamp
		new LootItem(620, 4, 8, 5), //Vote pt ticket
		new LootItem(995, 25000000, 75000000, 4), //25-75m
		new LootItem(30470, 4, 4), //Achievement lamps
		new LootItem(10551, 1, 3), //Fighter torso
		new LootItem(25430, 5, 8, 5), //Slayer key(T4)
		new LootItem(23490, 4, 5, 7), //Larran's key
		new LootItem(30460, 2, 4, 7), //Double exp scroll (30mins)
		new LootItem(25432, 5, 8, 9), //Slayer key(T3)
		new LootItem(30570, 3, 4, 7), //Perk point scroll
		new LootItem(30453, 2, 3), //Prayer drain scroll
		new LootItem(30455, 2, 3, 6), //Brew immunity scroll
		new LootItem(30570, 1, 2, 8), //Damage boost scroll
		new LootItem(26706, 1, 1, 3), //Scroll of imbuing
		new LootItem(22092, 1, 1, 2), //Pet bonus
		new LootItem(30537, 1, 1, 2), //Venom tipped sigil
		new LootItem(30532, 1, 1, 2), //Siphon the dead sigil
		new LootItem(30540, 1, 1, 1), //Special saver sigil
		new LootItem(30541, 1, 1, 2), //Critical hit sigil
		new LootItem(30531, 1, 1, 3), //AoE swipe sigil
		new LootItem(30534, 1, 1, 2), //Double hit sigil
		new LootItem(30538, 1, 1, 3), //Health siphon sigil
		new LootItem(30543, 1, 1, 6), //Armour break sigil
		new LootItem(30539, 1, 1, 4), //Respect the dead sigil
		new LootItem(30535, 1, 1, 7), //Damage for hire low
		new LootItem(30536, 1, 1, 5), //Damage for hire high
		new LootItem(ItemID.SUPERIOR_DRAGON_BONES + 1, 250, 300, 10),
		new LootItem(ItemID.DRAGON_DART_TIP, 500, 750, 10),
		new LootItem(ItemID.REWARD_CASKET_MASTER, 3, 5, 10),
		new LootItem(ItemID.RUBY_DRAGON_BOLTS_E, 100, 200, 10),
		new LootItem(ItemID.DRAGON_BOLTS_UNF, 150, 275, 10),
		new LootItem(ItemID.RUNITE_BAR + 1, 200, 300, 10),
		new LootItem(ItemID.MAGIC_LOGS + 1, 200, 350, 10),
		new LootItem(ItemID.REDWOOD_LOGS + 1, 200, 350, 10),
		new LootItem(25527, 3000, 6000, 8), //Stardust
		new LootItem(ItemID.BERSERKER_RING_I, 1, 4),
		new LootItem(ItemID.ARCHERS_RING_I, 1, 6),
		new LootItem(ItemID.SEERS_RING_I, 1, 6),
		new LootItem(ItemID.ABYSSAL_BLUDGEON, 1, 2),
		new LootItem(ItemID.OCCULT_NECKLACE, 1, 2),
		new LootItem(ItemID.DRAGONFIRE_SHIELD, 1, 1)
	);

	static LootTable ironEasyRewards = new LootTable().addTable(1,
		new LootItem(ItemID.OAK_LOGS + 1, 50, 100, 5),
		new LootItem(ItemID.WILLOW_LOGS + 1, 50, 100, 4),
		new LootItem(ItemID.GRIMY_GUAM_LEAF + 1, 20, 30, 5),
		new LootItem(ItemID.GRIMY_MARRENTILL + 1, 20, 30, 5),
		new LootItem(ItemID.GRIMY_TARROMIN + 1, 20, 30, 4),
		new LootItem(ItemID.OAK_PLANK + 1, 20, 40, 5),
		new LootItem(ItemID.GUAM_SEED, 3, 6, 5),
		new LootItem(ItemID.TARROMIN_SEED, 3, 6, 5),
		new LootItem(ItemID.MARRENTILL_SEED, 3, 6, 4),
		new LootItem(ItemID.IRON_ORE + 1, 20, 40, 5),
		new LootItem(ItemID.COAL + 1, 40, 70, 5),
		new LootItem(ItemID.SOFT_CLAY + 1, 20, 40, 5),
		new LootItem(ItemID.BRONZE_BAR + 1, 20, 40, 5),
		new LootItem(ItemID.IRON_BAR + 1, 20, 40, 4),
		new LootItem(ItemID.RAW_LOBSTER + 1, 20, 60, 3),
		new LootItem(ItemID.RAW_TUNA + 1, 20, 60, 4),
		new LootItem(ItemID.ZOGRE_BONES + 1, 30, 60, 4),
		new LootItem(ItemID.MOLTEN_GLASS + 1, 40, 60, 4),
		new LootItem(ItemID.RUNE_ARROWP_5627, 75, 2),
		new LootItem(ItemID.BOW_STRING + 1, 75, 200, 3),
		new LootItem(ItemID.LEATHER + 1, 20, 50, 5),
		new LootItem(ItemID.HARD_LEATHER + 1, 20, 50, 4),
		new LootItem(ItemID.RED_SPIDERS_EGGS + 1, 30, 50, 4),
		new LootItem(ItemID.WHITE_BERRIES + 1, 30, 50, 4),
		new LootItem(ItemID.UNCUT_SAPPHIRE + 1, 30, 50, 4),
		new LootItem(ItemID.UNCUT_EMERALD + 1, 30, 50, 3),
		new LootItem(ItemID.GREEN_DRAGON_LEATHER + 1, 20, 40, 2),
		new LootItem(ItemID.UNCUT_RUBY + 1, 30, 50, 2),
		new LootItem(ItemID.BABYDRAGON_BONES + 1, 30, 50, 2)
	);
	static LootTable ironMediumRewards = new LootTable().addTable(1,
		new LootItem(ItemID.GRIMY_HARRALANDER + 1, 25, 40, 5),
		new LootItem(ItemID.GRIMY_TOADFLAX + 1, 25, 40, 4),
		new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 20, 30, 3),
		new LootItem(ItemID.TEAK_PLANK + 1, 30, 50, 3),
		new LootItem(ItemID.MAPLE_LOGS + 1, 40, 60, 4),
		new LootItem(ItemID.CRUSHED_NEST + 1, 30, 40, 3),
		new LootItem(ItemID.CHAOS_RUNE, 100, 250, 5),
		new LootItem(ItemID.DEATH_RUNE, 100, 250, 4),
		new LootItem(ItemID.HARRALANDER_SEED, 4, 8, 5),
		new LootItem(ItemID.TOADFLAX_SEED, 3, 7, 4),
		new LootItem(ItemID.RANARR_SEED, 3, 6, 3),
		new LootItem(ItemID.GOAT_HORN_DUST + 1, 40, 60, 4),
		new LootItem(ItemID.SNAPE_GRASS + 1, 40, 70, 5),
		new LootItem(ItemID.POTATO_CACTUS + 1, 40, 60, 4),
		new LootItem(ItemID.RAW_SWORDFISH + 1, 40, 80, 4),
		new LootItem(ItemID.DRAGON_BONES + 1, 50, 100, 2),
		new LootItem(ItemID.WYRM_BONES + 1, 60, 120, 3),
		new LootItem(ItemID.BLUE_DRAGON_LEATHER + 1, 40, 80, 4),
		new LootItem(ItemID.RED_DRAGON_LEATHER + 1, 40, 80, 3),
		new LootItem(ItemID.RAW_LOBSTER + 1, 50, 100, 5),
		new LootItem(ItemID.COAL + 1, 90, 200, 5),
		new LootItem(ItemID.UNCUT_DIAMOND + 1, 50, 80, 3),
		new LootItem(ItemID.STEEL_BAR + 1, 40, 80, 4),
		new LootItem(ItemID.BATTLESTAFF + 1, 40, 60, 4),
		new LootItem(ItemID.ROYAL_SEED_POD, 1, 1, 1),
		new LootItem(ItemID.CANNONBALL, 150, 300, 3),
		new LootItem(ItemID.BURNT_PAGE, 1, 1, 1),
		new LootItem(25527, 120, 250, 2), //Stardust
		new LootItem(ItemID.MAGIC_SECATEURS, 1, 1, 1),
		new LootItem(ItemID.YEW_LONGBOW_U + 1, 75, 150, 3),
		new LootItem(ItemID.BRIMSTONE_KEY, 1, 1, 3),
		new LootItem(ItemID.CRYSTAL_KEY, 1, 2, 3),
		new LootItem(ItemID.UNCUT_DRAGONSTONE + 1, 30, 60, 3),
		new LootItem(ItemID.AMETHYST + 1, 75, 100, 3),
		new LootItem(ItemID.AMETHYST_ARROWP_21336, 75, 150, 2),
		new LootItem(25853, 150, 250, 2), //Amethyst dart tip
		new LootItem(ItemID.NINJA_IMPLING_JAR + 1, 3, 5, 3),
		new LootItem(ItemID.REWARD_CASKET_MEDIUM, 2, 3, 3),
		new LootItem(30470, 1, 2), //Achievement lamp
		new LootItem(ItemID.CRYSTAL_SAW, 1, 1, 1),
		new LootItem(ItemID.WILLOW_SEED, 3, 6, 4)
	);
	static LootTable ironHardRewards = new LootTable().addTable(1,
		new LootItem(ItemID.PRAYER_POTION4 + 1, 20, 30, 4),
		new LootItem(ItemID.SUPER_ATTACK4 + 1, 15, 30, 5),
		new LootItem(ItemID.SUPER_STRENGTH4 + 1, 15, 30, 5),
		new LootItem(ItemID.SUPER_DEFENCE4 + 1, 15, 30, 5),
		new LootItem(ItemID.MORT_MYRE_FUNGUS + 1, 60, 80, 4),
		new LootItem(ItemID.SNAPE_GRASS + 1, 60, 80, 4),
		new LootItem(ItemID.LIMPWURT_ROOT + 1, 60, 80, 4),
		new LootItem(ItemID.CRUSHED_NEST + 1, 60, 80, 3),
		new LootItem(ItemID.MAHOGANY_PLANK + 1, 75, 150, 4),
		new LootItem(ItemID.DEATH_RUNE, 300, 500, 5),
		new LootItem(ItemID.WRATH_RUNE, 200, 300, 4),
		new LootItem(ItemID.BLOOD_RUNE, 300, 500, 5),
		new LootItem(ItemID.RANARR_SEED, 6, 8, 5),
		new LootItem(ItemID.KWUARM_SEED, 6, 9, 5),
		new LootItem(ItemID.AVANTOE_SEED, 6, 9, 5),
		new LootItem(ItemID.SNAPDRAGON_SEED, 6, 9, 5),
		new LootItem(ItemID.SNAPE_GRASS_SEED, 8, 12, 5),
		new LootItem(ItemID.LAVA_DRAGON_BONES + 1, 50, 120, 3),
		new LootItem(ItemID.RAURG_BONES + 1, 50, 90, 4),
		new LootItem(ItemID.HYDRA_BONES + 1, 50, 90, 4),
		new LootItem(ItemID.MITHRIL_BAR + 1, 50, 90, 5),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 50, 90, 4),
		new LootItem(ItemID.STEEL_BAR + 1, 60, 120, 5),
		new LootItem(ItemID.UNCUT_DIAMOND + 1, 80, 150, 4),
		new LootItem(ItemID.UNCUT_DRAGONSTONE + 1, 60, 120, 3),
		new LootItem(ItemID.MAGIC_SHORTBOW_U + 1, 90, 180, 4),
		new LootItem(ItemID.BATTLESTAFF + 1, 15, 40, 5),
		new LootItem(ItemID.BLACK_DRAGON_LEATHER + 1, 60, 100, 4),
		new LootItem(ItemID.DRAGON_DART_TIP, 160, 300, 3),
		new LootItem(ItemID.RUNITE_BOLTS_UNF, 140, 210, 3),
		new LootItem(ItemID.BRIMSTONE_KEY, 2, 3, 4),
		new LootItem(ItemID.CRYSTAL_KEY, 3, 5, 4),
		new LootItem(ItemID.ENHANCED_CRYSTAL_KEY, 2, 3, 2),
		new LootItem(ItemID.HERB_BOX, 2, 4, 2),
		new LootItem(ItemID.BAG_FULL_OF_GEMS, 3, 5, 2),
		new LootItem(30458, 2, 3, 3), //Slayer task skip scroll
		new LootItem(ItemID.CANNONBALL, 100, 300, 5),
		new LootItem(ItemID.ANCIENT_SHARD, 1, 3, 2),
		new LootItem(ItemID.REWARD_CASKET_HARD, 2, 4, 3),
		new LootItem(ItemID.MAPLE_SEED, 3, 6, 5),
		new LootItem(ItemID.YEW_SEED, 2, 4, 4),
		new LootItem(ItemID.DRAGON_IMPLING_JAR, 2, 4, 3),
		new LootItem(ItemID.PALM_TREE_SEED, 2, 3, 4),
		new LootItem(ItemID.RAW_MONKFISH + 1, 75, 150, 5),
		new LootItem(ItemID.RAW_SEA_TURTLE + 1, 75, 150, 4),
		new LootItem(ItemID.RAW_KARAMBWAN + 1, 75, 150, 4)

	);
	static LootTable ironEliteRewards = new LootTable().addTable(1,
		new LootItem(ItemID.SUPER_RESTORE4 + 1, 30, 50, 5),
		new LootItem(ItemID.SUPER_COMBAT_POTION4 + 1, 15, 30, 4),
		new LootItem(ItemID.SUPER_STRENGTH4 + 1, 30, 50, 5),
		new LootItem(ItemID.MAGIC_POTION4 + 1, 30, 50, 5),
		new LootItem(ItemID.WINE_OF_ZAMORAK + 1, 60, 120, 5),
		new LootItem(ItemID.SARADOMIN_BREW4 + 1, 15, 30, 5),
		new LootItem(ItemID.AMYLASE_CRYSTAL, 50, 100, 3),
		new LootItem(ItemID.WRATH_RUNE, 400, 600, 4),
		new LootItem(ItemID.MAGIC_SEED, 2, 5, 4),
		new LootItem(ItemID.YEW_SEED, 3, 5, 5),
		new LootItem(ItemID.DRAGONFRUIT_TREE_SEED, 2, 5, 5),
		new LootItem(ItemID.SNAPDRAGON_SEED, 5, 9, 5),
		new LootItem(ItemID.TORSTOL_SEED, 5, 9, 5),
		new LootItem(7478, 4, 8, 3), //Instance tokens
		new LootItem(ItemID.RUNITE_BAR + 1, 40, 60, 4),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 60, 80, 5),
		new LootItem(ItemID.BATTLESTAFF + 1, 30, 50, 5),
		new LootItem(ItemID.RAW_SHARK + 1, 100, 160, 5),
		new LootItem(ItemID.RAW_ANGLERFISH + 1, 75, 125, 4),
		new LootItem(ItemID.RAW_DARK_CRAB + 1, 75, 125, 3),
		new LootItem(ItemID.MAGIC_LOGS + 1, 50, 120, 4),
		new LootItem(ItemID.REDWOOD_LOGS + 1, 40, 110, 3),
		new LootItem(ItemID.YEW_LOGS + 1, 70, 150, 5),
		new LootItem(ItemID.REWARD_CASKET_ELITE, 2, 4, 3),
		new LootItem(ItemID.DRAGON_DART_TIP, 250, 400, 4),
		new LootItem(ItemID.DRAGON_BOLTS_UNF, 70, 140, 4),
		new LootItem(ItemID.OURG_BONES + 1, 50, 70, 5),
		new LootItem(ItemID.SUPERIOR_DRAGON_BONES + 1, 50, 80, 4),
		new LootItem(ItemID.LUCKY_IMPLING_JAR, 2, 4, 3),
		new LootItem(ItemID.DRAGON_JAVELIN, 100, 200, 3),
		new LootItem(30579, 1, 1), //Small exp lamp
		new LootItem(608, 1, 2), //10% drop rate scroll
		new LootItem(30459, 1, 3, 2), //Double drop scroll
		new LootItem(620, 3, 5, 1), //Vote pt ticket
		new LootItem(30570, 1, 2), //Perk point scroll
		new LootItem(30470, 2, 2) //Achievement lamp
	);
	static LootTable ironCompletionReward = new LootTable().addTable(1,
		new LootItem(30461, 1, 2), //Donator mystery box
		new LootItem(30464, 1, 2), //$5 bond
		new LootItem(2528, 1, 1), //Donator lamp
		new LootItem(30580, 1, 4), //Medium exp lamp
		new LootItem(620, 4, 8, 5), //Vote pt ticket
		new LootItem(995, 10000000, 50000000, 4), //10-50m
		new LootItem(30470, 4, 4), //Achievement lamps
		new LootItem(10551, 1, 3), //Fighter torso
		new LootItem(25430, 5, 8, 5), //Slayer key(T4)
		new LootItem(23490, 4, 5, 7), //Larran's key
		new LootItem(30460, 2, 4, 7), //Double exp scroll (30mins)
		new LootItem(25432, 5, 8, 9), //Slayer key(T3)
		new LootItem(30570, 3, 4, 7), //Perk point scroll
		new LootItem(30453, 2, 3), //Prayer drain scroll
		new LootItem(30455, 2, 3, 6), //Brew immunity scroll
		new LootItem(30570, 1, 2, 8), //Damage boost scroll
		new LootItem(26706, 1, 1, 3), //Scroll of imbuing
		new LootItem(22092, 1, 1, 2), //Pet bonus
		new LootItem(30537, 1, 1, 2), //Venom tipped sigil
		new LootItem(30532, 1, 1, 2), //Siphon the dead sigil
		new LootItem(30540, 1, 1, 1), //Special saver sigil
		new LootItem(30541, 1, 1, 2), //Critical hit sigil
		new LootItem(30531, 1, 1, 3), //AoE swipe sigil
		new LootItem(30534, 1, 1, 2), //Double hit sigil
		new LootItem(30538, 1, 1, 3), //Health siphon sigil
		new LootItem(30543, 1, 1, 6), //Armour break sigil
		new LootItem(30539, 1, 1, 4), //Respect the dead sigil
		new LootItem(30535, 1, 1, 7), //Damage for hire low
		new LootItem(30536, 1, 1, 5), //Damage for hire high
		new LootItem(ItemID.SUPERIOR_DRAGON_BONES + 1, 250, 300, 10),
		new LootItem(ItemID.DRAGON_DART_TIP, 500, 750, 10),
		new LootItem(ItemID.REWARD_CASKET_MASTER, 3, 5, 10),
		new LootItem(ItemID.DRAGON_BOLTS_UNF, 100, 200, 10),
		new LootItem(ItemID.RUBY_DRAGON_BOLTS_E, 100, 200, 10),
		new LootItem(ItemID.RUNITE_BAR + 1, 200, 300, 10),
		new LootItem(ItemID.MAGIC_LOGS + 1, 200, 350, 10),
		new LootItem(ItemID.REDWOOD_LOGS + 1, 200, 350, 10),
		new LootItem(25527, 3000, 6000, 8) //Stardust
	);
}
