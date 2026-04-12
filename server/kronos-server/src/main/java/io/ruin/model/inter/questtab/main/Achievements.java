package io.ruin.model.inter.questtab.main;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;
import io.ruin.model.stat.StatType;

public enum Achievements {
/*
    Easy achievements

     */

	ARE_YOU_SHORE_ABOUT_THIS(new Item[]{(new Item(995, 1000000)), (new Item(4587))}, "Are you shore about this?",
		2500, 1, 0, 5000, 849, "Kill 30 rock/sand crab variants.", 30, 82, "strength", StatType.Strength, AchievementTypes.EASY, 30, 56),
	MIND_GAMES(new Item[]{(new Item(995, 1000000)), (new Item(562, 2500))}, "Mind Games", 2500,
		1, 0, 5000, 849, "Cast 50 Strike spells.", 50, 83, "magic", StatType.Magic, AchievementTypes.EASY, 31, 57),
	DIVINE_SENSES_I(new Item[]{(new Item(995, 500000)), (new Item(533, 100))}, "Divine Senses I", 2000, 1, 0,
		2500, 849, "Bury or sacrifice 25 bones of any kind.", 25, 84, "prayer", StatType.Prayer, AchievementTypes.EASY, 32, 58),
	THE_POWER_WITHIN(new Item[]{(new Item(995, 500000)), (new Item(5509)), (new Item(5510))}, "The Power Within",
		1500, 1, 0, 2500, 849, "Runecraft 50 runes of any kind.", 50, 85, "runecrafting", StatType.Runecrafting, AchievementTypes.EASY, 33, 59),
	SKIN_IS_THE_GAME(new Item[]{(new Item(995, 500000))}, "Skin is the game", 1500, 1,
		0, 2000, 849, "Craft 20 leather items.", 20, 86, "crafting", StatType.Crafting, AchievementTypes.EASY, 34, 60),
	IRON_INTELLECT(new Item[]{(new Item(2352, 100))}, "Iron Intellect", 2500,
		1, 0, 2000, 849, "Smelt 20 iron bars.", 20, 87, "smithing", StatType.Smithing, AchievementTypes.EASY, 35, 61),
	SOMETHING_IS_FISHY_HERE_I(new Item[]{(new Item(995, 500000))}, "Something's fishy here I", 2000,
		1, 0, 5000, 849, "Fly fish 50 fish.", 50, 88, "fishing", StatType.Fishing, AchievementTypes.EASY, 36, 62),
	JUST_FOR_THE_HALIBUT_I(new Item[]{(new Item(995, 500000))}, "Just for the halibut I",
		1000, 1, 0, 5000, 849, "Successfully cook 50 fish.", 50, 89, "cooking", StatType.Cooking, AchievementTypes.EASY, 37, 63),
	LUMBERJACK_I(new Item[]{(new Item(995, 250000))}, "Lumberjack I", 1000,
		1, 0, 1000, 849, "Chop 25 oak logs.", 25, 90, "woodcutting", StatType.Woodcutting, AchievementTypes.EASY, 38, 64),
	PYROMANCER_I(new Item[]{(new Item(995, 250000))}, "Pyromancer I", 1000,
		1, 0, 1000, 849, "Burn 25 oak logs.", 25, 91, "firemaking", StatType.Firemaking, AchievementTypes.EASY, 39, 65),
	THESE_SHOULDNT_TAKE_LONG_I(new Item[]{(new Item(995, 500000))}, "These shouldn't take long I",
		1500, 1, 0, 2000, 849, "String 25 shortbows of any kind.", 25, 92, "fletching", StatType.Fletching, AchievementTypes.EASY, 40, 66),
	ANOTHER_ONE_BITES_THE_DUST(new Item[]{(new Item(995, 250000)), (new Item(11849, 10))}, "Another run bites the dust",
		2000, 1, 0, 2500, 849, "Run 20 laps of any agility course.", 20, 93, "agility", StatType.Agility, AchievementTypes.EASY, 41, 67),
	CANT_KEEP_MY_HANDS_TO_MYSELF_I(new Item[]{(new Item(995, 250000))}, "Can't keep my hands to myself I", 1000,
		1, 0, 1500, 849, "Pickpocket a man 25 times.", 25, 94, "thieving", StatType.Thieving, AchievementTypes.EASY, 42, 68),
	RAISING_THE_BAR(new Item[]{(new Item(30470))}, "Raising the bar", 1000, 1,
		0, 2500, 849, "Complete a slayer task.", 1, 95, "slayer", StatType.Slayer, AchievementTypes.EASY, 43, 69),
	THE_GRASS_IS_ALWAYS_GREENER(new Item[]{(new Item(995, 250000))}, "The grass is always greener", 1000,
		1, 0, 3000, 849, "Rake a herb patch.", 1, 96, "farming", StatType.Farming, AchievementTypes.EASY, 44, 70),
	FIRST_TIME_HOME_OWNER(new Item[]{(new Item(995, 300000))}, "First time home owner", 1000,
		1, 0, 1000, 849, "Purchase a house.", 1, 97, "construction", StatType.Construction, AchievementTypes.EASY, 45, 71),
	THE_HUNT_IS_ON_I(new Item[]{(new Item(995, 300000))}, "The hunt is on I", 1500, 1,
		0, 5000, 849, "Catch 20 copper longtails.", 20, 98, "hunter", StatType.Hunter, AchievementTypes.EASY, 46, 72),
	DONT_BE_SO_GRIMY(new Item[]{(new Item(995, 200000))}, "Don't be so grimy", 1000, 1,
		0, 2500, 849, "Clean 10 guam herbs.", 10, 99, "herblore", StatType.Herblore, AchievementTypes.EASY, 47, 73),
	THANK_GOD_FOR_THAT(new Item[]{(new Item(995, 1000000))}, "Thank god for that", 2500, 1,
		25, 5000, 849, "Obtain a god magic cape.", 1, 100, "magic", StatType.Magic, AchievementTypes.EASY, 48, 74),
	JUST_A_LITTLE_BOOST(new Item[]{(new Item(995, 100000))}, "Just a little boost", 500, 1,
		0, 0, 849, "Use the home teleport. (This will replenish your health, prayer & stats)", 1, 101, "", StatType.Magic, AchievementTypes.EASY, 49, 75),
	NEXWORKING(new Item[]{(new Item(995, 200000))}, "Nexworking", 500, 1,
		0, 0, 849, "Teleport using the portal nexus at home.", 1, 102, "", StatType.Magic, AchievementTypes.EASY, 50, 76),
	OH_CUL(new Item[]{(new Item(995, 200000))}, "Oh cul", 500, 1, 0,
		0, 849, "Switch spellbooks with the Occult Altar at home.", 1, 103, "", StatType.Magic, AchievementTypes.EASY, 51, 77),
	OPEN_YOUR_MINE(new Item[]{(new Item(995, 250000)), (new Item(1275))}, "Open your mine", 1000,
		1, 0, 1000, 849, "Enter the motherload mine via the cave.", 1, 104, "mining", StatType.Mining, AchievementTypes.EASY, 52, 78),
	QUITE_PUZZLED(new Item[]{(new Item(30470))}, "Quite puzzled", 1000, 1, 0, 0, 849,
		"Open an easy clue casket.", 1, 105, "", StatType.Magic, AchievementTypes.EASY, 53, 79),
	THIRD_PART_CANDIDATE(new Item[]{(new Item(995, 1000000))}, "Third party candidate", 1000, 1, 0,
		0, 849, "Claim a vote reward.", 1, 106, "", StatType.Magic, AchievementTypes.EASY, 54, 80),
	FEELING_LIKE_A_NEW_MAN(new Item[]{(new Item(995, 500000))}, "Feeling like a new man", 1000, 1, 0,
		0, 849, "Use the makeover mage to change your outfit.", 1, 107, "", StatType.Magic, AchievementTypes.EASY, 55, 81),

    /*
    Med Achievements
     */

	IBANT_BELIEVE_THIS(new Item[]{(new Item(995, 500000)), (new Item(560, 250))}, "Ibant believe this",
		2000, 2, 0, 10000, 849, "Cast Iban's blast 150 times.", 150, 82, "magic", StatType.Magic, AchievementTypes.MEDIUM, 30, 56),
	ITS_MAGIC_YOU_KNOW(new Item[]{(new Item(892, 1000))}, "It's magic you know", 2000,
		2, 0, 5000, 849, "Equip a magic shortbow and perform 150 attacks with it.", 150, 83, "ranged", StatType.Ranged, AchievementTypes.MEDIUM, 31, 57),
	PASSED_THE_BARR(new Item[]{(new Item(995, 1000000))}, "Passed the barr", 2000,
		3, 0, 5000, 849, "Complete a barrows chest run killing all 6 brothers.", 1, 84, "magic", StatType.Magic, AchievementTypes.MEDIUM, 32, 58),
	DIVINE_SENSES_II(new Item[]{(new Item(537, 50))}, "Divine Senses II", 1500,
		2, 0, 5000, 849, "Bury or Sacrifice 50 dragon bones.", 50, 85, "prayer", StatType.Prayer, AchievementTypes.MEDIUM, 33, 59),
	ASTRALWORLD(new Item[]{(new Item(9075, 500))}, "Astralworld", 1500,
		2, 0, 5000, 849, "Runecraft 150 astral runes.", 150, 86, "runecrafting", StatType.Runecrafting, AchievementTypes.MEDIUM, 34, 60),
	HOW_MANY_RINGS_DO_YOU_NEED(new Item[]{(new Item(1618, 100))}, "How many rings do you need?", 2000,
		2, 0, 5000, 849, "Cut 150 diamonds.", 150, 87, "crafting", StatType.Crafting, AchievementTypes.MEDIUM, 35, 61),
	DID_YOU_MITH_ONE(new Item[]{(new Item(995, 500000)), (new Item(2360, 100))}, "Did you mith one?",
		2500, 2, 0, 5000, 849, "Smelt 100 mithril bars.", 100, 88, "smithing", StatType.Smithing, AchievementTypes.MEDIUM, 36, 62),
	SOMETHING_IS_FISHY_HERE_II(new Item[]{(new Item(995, 250000)), (new Item(372, 100))}, "Something's fishy here II",
		2000, 2, 0, 5000, 849, "Fish 100 swordfish.", 100, 89, "fishing", StatType.Fishing, AchievementTypes.MEDIUM, 37, 63),
	JUST_FOR_THE_HALIBUT_II(new Item[]{(new Item(995, 250000)), (new Item(374, 100))}, "Just for the halibut II",
		2000, 2, 0, 5000, 849, "Successfully cook 100 swordfish.", 100, 90, "cooking", StatType.Cooking, AchievementTypes.MEDIUM, 38, 64),
	PYROMANCER_II(new Item[]{(new Item(995, 500000)), (new Item(30470, 1))}, "Pyromancer II", 3000,
		2, 0, 5000, 849, "Complete 10 runs of Wintertodt.", 10, 91, "firemaking", StatType.Firemaking, AchievementTypes.MEDIUM, 39, 65),
	LUMBERJACK_II(new Item[]{(new Item(995, 500000))}, "Lumberjack II", 1500,
		2, 0, 3000, 849, "Chop 100 yew logs.", 100, 92, "woodcutting", StatType.Woodcutting, AchievementTypes.MEDIUM, 40, 66),
	NOW_THAT_IS_JUST_UNNCECESSARY(new Item[]{(new Item(995, 250000)), (new Item(8783, 100))},
		"Now that's just unnecessary", 2500, 2, 0, 5000, 849, "Construct 50 mahogany armchairs.", 50, 93, "construction", StatType.Construction, AchievementTypes.MEDIUM, 41, 67),
	LIMBS_TO_LIMBS(new Item[]{(new Item(995, 250000)), (new Item(9144, 1000))},
		"Limbs to limbs", 2500, 2, 0, 5000, 849, "String a runite crossbow.", 1, 94, "fletching", StatType.Fletching, AchievementTypes.MEDIUM, 42, 68),
	CANT_KEEP_MY_HANDS_TO_MYSELF_II(new Item[]{(new Item(995, 500000))}, "Can't keep my hands to myself II", 2000,
		2, 0, 5000, 849, "Pickpocket 100 knights successfully.", 100, 95, "thieving", StatType.Thieving, AchievementTypes.MEDIUM, 43, 69),
	DONT_MIND_IF_I_DO(new Item[]{(new Item(995, 250000))}, "Don't mind if I do", 1000,
		2, 0, 3000, 849, "Extend or block a slayer task.", 1, 96, "slayer", StatType.Slayer, AchievementTypes.MEDIUM, 44, 70),
	IN_BLOOM(new Item[]{(new Item(995, 500000))}, "In Bloom", 1500, 2, 0,
		5000, 849, "Grow a tree of any kind using the farming skill.", 1, 97, "farming", StatType.Farming, AchievementTypes.MEDIUM, 45, 71),
	IS_THIS_EVEN_SAFE(new Item[]{(new Item(10034, 100))}, "Is this even safe?", 2500, 2,
		0, 5000, 849, "Boxtrap catch 100 chinchompas of any variant.", 100, 98, "hunter", StatType.Hunter, AchievementTypes.MEDIUM, 46, 72),
	MIGHT_NEED_THESE_LATER(new Item[]{(new Item(3025, 25))}, "Might need these later", 5000,
		2, 0, 5000, 849, "Mix 100 super restore potions using the herblore skill.", 100, 99, "herblore", StatType.Herblore, AchievementTypes.MEDIUM, 47, 73),
	GONE_IN_THE_BLINK_OF_AN_EYE(new Item[]{(new Item(995, 250000))}, "Gone in the blink of an eye",
		1000, 1, 0, 0, 849, "Use the fairy ring at home to travel to a new location.", 1, 100, "", StatType.Magic, AchievementTypes.MEDIUM, 48, 74),
	WHAT_DO_WE_HAVE_HERE_I(new Item[]{(new Item(995, 500000)), (new Item(23083, 2))}, "What do we have here I",
		2000, 2, 0, 2500, 849, "Unlock the brimstone chest 5 times.", 5, 101, "slayer", StatType.Slayer, AchievementTypes.MEDIUM, 49, 75),
	MIGHT_NEED_A_JAR_OR_TWO(new Item[]{(new Item(11259))}, "Might need a jar or two", 2000,
		2, 0, 2500, 849, "Travel to Puro Puro and enter the wheat fields.", 1, 102, "hunter", StatType.Hunter, AchievementTypes.MEDIUM, 50, 76),
	RELAX_YOURE_ALMOST_DONE(new Item[]{(new Item(995, 1000000))}, "What am I looking at?", 2000,
		3, 50, 0, 849, "Kill the chaos elemental.", 1, 103, "", StatType.Magic, AchievementTypes.MEDIUM, 51, 77),
	GROWING_THE_GREEN(new Item[]{(new Item(995, 500000)), (new Item(210, 25))}, "Growing the greens",
		2000, 2, 0, 2500, 849, "Successfully grow an irit leaf using the farming skill.", 1, 104, "farming", StatType.Farming, AchievementTypes.MEDIUM, 52, 78),
	OVERNIGHT_MUSCLES(new Item[]{(new Item(30470, 1))}, "Overnight muscles", 2000,
		2, 0, 0, 849, "Equip a fighter's torso.", 1, 105, "", StatType.Magic, AchievementTypes.MEDIUM, 53, 79),
	THIS_SEEMS_A_LITTLE_DANGEROUS(new Item[]{(new Item(995, 500000)), (new Item(11849, 25))}, "This seems a little dangerous",
		3000, 2, 10, 5000, 849, "Run 25 laps of the wilderness agility course.", 25, 106, "agility", StatType.Agility, AchievementTypes.MEDIUM, 54, 80),
	THINK_IM_GETTING_THE_HANG_OF_THIS(new Item[]{(new Item(995, 1000000))}, "Think I'm getting the hang of this", 5000,
		2, 0, 0, 849, "Open 15 medium clue scroll caskets.", 15, 107, "", StatType.Magic, AchievementTypes.MEDIUM, 55, 81),

    /*
    Hard Achievements

     */

	PROTECTIVE_HEADGEAR(new Item[]{(new Item(30470)), (new Item(30470))}, "Protective headgear", 3000,
		3, 0, 5000, 849, "Unlock and create a slayer helm.", 1, 82, "slayer", StatType.Slayer, AchievementTypes.HARD, 30, 56),
	WATCH_YOUR_SURROUNDINGS(new Item[]{(new Item(995, 1500000))}, "Watch your surroundings", 4000,
		3, 25, 0, 849, "Kill 100 lava dragons.", 100, 83, "", StatType.Magic, AchievementTypes.HARD, 31, 57),
	DIVINE_SENSES_III(new Item[]{(new Item(ItemID.LAVA_DRAGON_BONES + 1, 50))}, "Divine Senses III", 3000, 3,
		0, 5000, 849, "Bury or sacrifice 75 lava dragon bones.", 75, 84, "prayer", StatType.Prayer, AchievementTypes.HARD, 32, 58),
	ALL_THIS_FROM_A_SEED(new Item[]{(new Item(24125))}, "All this from a seed?", 4000, 3,
		0, 0, 849, "Equip a crystal bow or crystal shield.", 1, 85, "", StatType.Magic, AchievementTypes.HARD, 33, 59),
	PRAYERS_ANSWERED(new Item[]{(new Item(565, 1000))}, "Prayers? Answered", 5000, 3, 50,
		5000, 849, "Purchase a god staff and power it with charge.", 1, 86, "magic", StatType.Magic, AchievementTypes.HARD, 34, 60),
	WHAT_DO_WE_HAVE_HERE_II(new Item[]{(new Item(995, 1500000)), (new Item(23490, 2))}, "What do we have here II",
		4000, 3, 30, 5000, 849, "Unlock the Larran's chest 5 times.", 5, 87, "slayer", StatType.Slayer, AchievementTypes.HARD, 35, 61),
	THESE_SHOULDNT_TAKE_LONG_II(new Item[]{(new Item(995, 2500000))}, "These shouldn't take long II", 2500,
		3, 0, 5000, 849, "String 50 magic shortbows.", 50, 88, "fletching", StatType.Fletching, AchievementTypes.HARD, 36, 62),
	PRAY_TO_RNJESUS(new Item[]{(new Item(995, 10000000))}, "Pray to the RN-Jesus", 4000,
		3, 75, 0, 849, "Kill 50 revenants of any kind.", 50, 89, "", StatType.Magic, AchievementTypes.HARD, 37, 63),
	CANT_KEEP_MY_HANDS_TO_MYSELF_III(new Item[]{(new Item(995, 5000000))}, "Can't keep my hands to myself III", 5000,
		3, 0, 10000, 849, "Pickpocket a Hero 100 times.", 100, 90, "thieving", StatType.Thieving, AchievementTypes.HARD, 38, 64),
	LUMBERJACK_III(new Item[]{(new Item(995, 3000000)), (new Item(30470))}, "Lumberjack III",
		5000, 3, 0, 10000, 849, "Chop 150 magic logs.", 150, 91, "woodcutting", StatType.Woodcutting, AchievementTypes.HARD, 39, 65),
	PYROMANCER_III(new Item[]{(new Item(995, 4000000)), (new Item(1514, 250))}, "Pyromancer III", 5000,
		3, 0, 10000, 849, "Burn 150 magic logs.", 150, 92, "firemaking", StatType.Firemaking, AchievementTypes.HARD, 40, 66),
	THIS_MIGHT_TAKE_AWHILE(new Item[]{(new Item(995, 2500000)), (new Item(30470))}, "This might take awhile",
		3000, 3, 0, 10000, 849, "Mine 150 adamant ore.", 150, 93, "mining", StatType.Mining, AchievementTypes.HARD, 41, 67),
	ADAMANT_ABOUT_IT(new Item[]{(new Item(2362, 100))}, "Adamant about it", 4000, 3,
		0, 10000, 849, "Smelt 100 adamant bars.", 100, 94, "smithing", StatType.Smithing, AchievementTypes.HARD, 42, 68),
	SOMETHING_IS_FISHY_HERE_III(new Item[]{(new Item(384, 100)), (new Item(13440, 100))}, "Something's fishy here III",
		3000, 3, 0, 10000, 849, "Fish 150 sharks or anglerfish.", 150, 95, "fishing", StatType.Fishing, AchievementTypes.HARD, 43, 69),
	JUST_FOR_THE_HALIBUT_III(new Item[]{(new Item(386, 100)), (new Item(13442, 100))}, "Just for the halibut III",
		2500, 3, 0, 10000, 849, "Cook 150 sharks or anglerfish.", 150, 96, "cooking", StatType.Cooking, AchievementTypes.HARD, 44, 70),
	REAPING_THE_BENEFITS(new Item[]{(new Item(565, 500))}, "Reaping the benefits", 5000, 3,
		0, 10000, 849, "Runecraft 150 blood runes.", 150, 97, "runecrafting", StatType.Runecrafting, AchievementTypes.HARD, 45, 71),
	NOT_SO_HARD_AFTER_ALL(new Item[]{(new Item(20544, 3))}, "Not so hard after all", 5000,
		3, 0, 0, 849, "Open 10 hard clue scroll caskets.", 10, 98, "", StatType.Magic, AchievementTypes.HARD, 46, 72),
	WAS_I_THAT_SMALL_ALL_THIS_TIME(new Item[]{(new Item(12012, 75))}, "Was I that small all this time?", 3000, 3,
		0, 8000, 849, "Climb to the upper motherload mine area.", 1, 99, "mining", StatType.Mining, AchievementTypes.HARD, 47, 73),
	TRAVELLERS_LUCK(new Item[]{(new Item(995, 5000000))}, "Traveller's luck", 4000, 3,
		0, 8000, 849, "Smith a dragon sq shield.", 1, 100, "smithing", StatType.Smithing, AchievementTypes.HARD, 48, 74),
	ARMED_NOT_LOADED(new Item[]{(new Item(810, 2500))}, "Armed, not loaded", 6000,
		3, 0, 7000, 849, "Fletch a toxic blowpipe.", 1, 101, "fletching", StatType.Fletching, AchievementTypes.HARD, 49, 75),
	I_PROBABLY_SHOULDNT_BE_DOING_THIS(new Item[]{(new Item(995, 8000000))}, "I probably shouldn't be doing this", 5000,
		3, 50, 7000, 849, "Steal from Rogue's chest 100 times.", 100, 102, "thieving", StatType.Thieving, AchievementTypes.HARD, 50, 76),
	SACRELIGIOUS(new Item[]{(new Item(995, 5000000)), (new Item(11818))}, "Sacreligious",
		7500, 5, 0, 0, 849, "Kill any of the God Wars bosses 5 times.", 5, 103, "", StatType.Magic, AchievementTypes.HARD, 51, 77),
	FROM_THE_KRAK_OF_DAWN(new Item[]{(new Item(995, 3000000)), (new Item(30470))}, "From the Krak of dawn",
		5000, 4, 0, 10000, 849, "Kill 25 Kraken.", 25, 104, "slayer", StatType.Slayer, AchievementTypes.HARD, 52, 78),
	EMPOWERED(new Item[]{(new Item(995, 15000000))}, "Empowered", 5000, 4,
		0, 0, 849, "Equip a full set of void knight armour.", 1, 105, "", StatType.Magic, AchievementTypes.HARD, 53, 79),
	GOT_ANY_CHANGE(new Item[]{(new Item(995, 7000000))}, "Got any change?", 5000, 3, 0,
		5000, 849, "Get assigned a slayer task from every slayer master once.", 6, 106, "slayer", StatType.Slayer, AchievementTypes.HARD, 54, 80),
	ALL_MINE(new Item[]{(new Item(2362, 150))}, "All mine", 3000, 3,
		0, 5000, 849, "Unlock and wear the full prospectors set.", 1, 107, "mining", StatType.Mining, AchievementTypes.HARD, 55, 81),

    /*
    Elite achievements
    */

	LUMBERJACK_IV(new Item[]{(new Item(6739, 1))}, "Lumberjack IV", 7500, 5,
		0, 50000, 849, "Chop 250 redwood logs.", 250, 82, "woodcutting", StatType.Woodcutting, AchievementTypes.ELITE, 30, 56),
	PYROMANCER_IV(new Item[]{(new Item(19670, 350))}, "Pyromancer IV", 7500,
		5, 0, 50000, 849, "Burn 250 redwood logs.", 250, 83, "firemaking", StatType.Firemaking, AchievementTypes.ELITE, 31, 57),
	END_OF_THE_MINE(new Item[]{(new Item(21348, 150))}, "End of the mine", 8000,
		5, 0, 50000, 849, "Mine 150 amethyst.", 150, 84, "mining", StatType.Mining, AchievementTypes.ELITE, 32, 58),
	THIS_WILL_TEACH_THEM(new Item[]{(new Item(995, 3500000))}, "This'll teach them", 10000, 5, 0,
		50000, 849, "Create a dragonfire shield.", 1, 85, "smithing", StatType.Smithing, AchievementTypes.ELITE, 33, 59),
	SOMETHING_IS_FISHY_HERE_IV(new Item[]{(new Item(11935, 250))}, "Something's fishy here IV",
		10000, 5, 100, 50000, 849, "Fish 250 dark crabs.", 250, 86, "fishing", StatType.Fishing, AchievementTypes.ELITE, 34, 60),
	JUST_FOR_THE_HALIBUT_IV(new Item[]{(new Item(11937, 250))}, "Just for the halibut IV", 7000,
		5, 0, 50000, 849, "Cook 250 dark crabs.", 250, 87, "cooking", StatType.Cooking, AchievementTypes.ELITE, 35, 61),
	SMALL_BUT_DEADLY(new Item[]{(new Item(995, 5000000)), (new Item(11232, 150))}, "Small but deadly",
		6000, 5, 0, 50000, 849, "Fletch 250 dragon darts.", 250, 88, "fletching", StatType.Fletching, AchievementTypes.ELITE, 36, 62),
	COLOURFUL_TIPS(new Item[]{(new Item(995, 6000000)), (new Item(21326, 500))}, "Colourful tips",
		6500, 5, 0, 30000, 849, "Craft 150 amethyst arrow tips.", 150, 89, "crafting", StatType.Crafting, AchievementTypes.ELITE, 37, 63),
	CANT_KEEP_MY_HANDS_TO_MYSELF_IV(new Item[]{(new Item(6529, 250000)), (new Item(30470))}, "Can't keep my hands to myself IV",
		5000, 5, 0, 25000, 849, "Steal from 150 TzHaar members.", 150, 90, "thieving", StatType.Thieving, AchievementTypes.ELITE, 38, 64),
	SLAYERS_GETTING_WILD(new Item[]{(new Item(995, 8000000)), (new Item(23490, 10))},
		"Slayer's getting wild!", 5000, 5, 50, 25000, 849, "Complete 20 wilderness slayer tasks.", 20, 91, "slayer", StatType.Slayer, AchievementTypes.ELITE, 39, 65),
	CHAMBER_OF_SECRETS(new Item[]{(new Item(995, 20000000)), (new Item(6686, 100)), (new Item(3025, 100))},
		"Chamber of secrets", 15000, 7, 0, 0, 849, "Complete 20 Chambers of Xeric Raids.", 20, 92, "", StatType.Magic, AchievementTypes.ELITE, 40, 66),
	PULL_THE_VORK_OUT(new Item[]{(new Item(995, 10000000)), (new Item(22109))}, "Pull the Vork out",
		10000, 6, 0, 0, 849, "Kill Vorkath 50 times.", 50, 93, "", StatType.Magic, AchievementTypes.ELITE, 41, 67),
	FACING_YOUR_FEARS(new Item[]{(new Item(995, 20000000)), (new Item(30470)), (new Item(30470))},
		"Facing your fears", 15000, 7, 0, 0, 849, "Kill Galvek 5 times.", 5, 94, "", StatType.Magic, AchievementTypes.ELITE, 42, 68),
	IT_HAS_HOW_MANY_HEADS(new Item[]{(new Item(995, 10000000)), (new Item(23083, 7))},
		"It has how many heads?", 10000, 7, 0, 50000, 849, "Kill Alchemical Hydra 25 times.", 25, 95, "slayer", StatType.Slayer, AchievementTypes.ELITE, 43, 69),
	IVE_MASTERED_IT(new Item[]{(new Item(19836, 3))}, "I've mastered it", 10000, 5,
		0, 0, 849, "Open 10 master clue caskets.", 10, 96, "", StatType.Magic, AchievementTypes.ELITE, 44, 70),
	CUT_THE_HEAD_OFF_THE_SNAKE(new Item[]{(new Item(995, 10000000)), (new Item(12934, 10000))}, "Cut the head off the snake",
		10000, 5, 0, 0, 849, "Kill Zulrah 50 times.", 50, 97, "", StatType.Magic, AchievementTypes.ELITE, 45, 71),
	FEEL_MY_WRATH(new Item[]{(new Item(21880, 1000)), (new Item(30470))}, "Feel my wrath",
		6000, 5, 0, 50000, 849, "Runecraft 250 wrath runes.", 250, 98, "runecrafting", StatType.Runecrafting, AchievementTypes.ELITE, 46, 72),
	THANKFULLY_NOT_ALL_AT_ONCE(new Item[]{(new Item(995, 5000000)), (new Item(537, 250))}, "Thankfully not all at once",
		5000, 4, 0, 25000, 849, "Reanimate 25 dragons.", 25, 99, "prayer", StatType.Prayer, AchievementTypes.ELITE, 47, 73),
	KING_OF_THE_UNDERGROUND(new Item[]{(new Item(995, 10000000))}, "King of the underground", 5000, 4,
		0, 25000, 849, "Craft a demonic throne and sit on it.", 1, 100, "construction", StatType.Construction, AchievementTypes.ELITE, 48, 74),
	NO_NET_NEEDED(new Item[]{(new Item(19732)), (new Item(11256))}, "No net needed", 7500,
		4, 0, 50000, 849, "Barehand catch 5 dragon or lucky implings.", 5, 101, "hunter", StatType.Hunter, AchievementTypes.ELITE, 49, 75),
	THIS_WAS_TORTURE(new Item[]{(new Item(995, 10000000)), (new Item(6571))}, "This was torture",
		7500, 4, 0, 50000, 849, "Craft a zenyte amulet and enchant it.", 1, 102, "crafting", StatType.Crafting, AchievementTypes.ELITE, 50, 76),
	LITTLE_BIT_OF_DIVINITY_IN_THE_MIX(new Item[]{(new Item(270, 100)), (new Item(23686, 10))}, "Little bit of divinity in the mix",
		7500, 4, 0, 50000, 849, "Mix 10 super combat potions.", 10, 103, "herblore", StatType.Herblore, AchievementTypes.ELITE, 51, 77),
	ALLDOUGNE(new Item[]{(new Item(995, 25000000)), (new Item(11849, 100))}, "Alldougne", 10000,
		4, 0, 50000, 849, "Complete 50 laps of the Ardougne rooftop course.", 50, 104, "agility", StatType.Agility, AchievementTypes.ELITE, 52, 78),
	WOOX_WOULD_BE_PROUD(new Item[]{(new Item(995, 50000000)), (new Item(21295))}, "Woox would be proud",
		15000, 4, 0, 0, 849, "Complete the inferno once.", 1, 105, "", StatType.Magic, AchievementTypes.ELITE, 53, 79),
	THERES_LAYERS_TO_IT(new Item[]{(new Item(995, 5000000))}, "There's layers to it",
		5000, 3, 0, 15000, 849, "Create an infernal pickaxe.", 1, 106, "mining", StatType.Mining, AchievementTypes.ELITE, 54, 80),
	TIS_THE_SPIRIT(new Item[]{(new Item(995, 10000000)), (new Item(30470))}, "Tis the spirit",
		5000, 4, 0, 20000, 849, "Teleport via a self-planted spirit tree in your POH.", 1, 107, "construction", StatType.Construction, AchievementTypes.ELITE, 55, 81),
	;


	String achievementName;
	int reasonPointReward;
	int achievementPointReward;
	int pkpReward;
	int experienceReward;
	int interfaceID;
	public String achievementDesc;
	Item[] items;
	public int completionAmount;
	int componentID;
	String skill;
	StatType stat;
	AchievementTypes achievementTypes;
	int buttonID;
	int hiddenButtonID;


	Achievements(Item[] items, String achievementName, int reasonPointReward, int achievementPointReward, int pkpReward, int experienceReward, int interfaceID,
	             String achievementDesc, int completionAmount, int componentID, String skill, StatType stat, AchievementTypes achievementTypes, int buttonID, int hiddenButtonID) {
		this.achievementName = achievementName;
		this.reasonPointReward = reasonPointReward;
		this.achievementPointReward = achievementPointReward;
		this.pkpReward = pkpReward;
		this.experienceReward = experienceReward;
		this.interfaceID = interfaceID;
		this.achievementDesc = achievementDesc;
		this.items = items;
		this.completionAmount = completionAmount;
		this.componentID = componentID;
		this.skill = skill;
		this.stat = stat;
		this.achievementTypes = achievementTypes;
		this.buttonID = buttonID;
		this.hiddenButtonID = hiddenButtonID;
	}

	public static final Achievements[] VALUES = values();

	public enum AchievementTypes {
		EASY,
		MEDIUM,
		HARD,
		ELITE;

		public static final AchievementTypes[] VALUES = values();
	}

	public String getAchievementName() {
		return achievementName;
	}

	public int getReasonPointReward() {
		return reasonPointReward;
	}

	public int getAchievementPointReward() {
		return achievementPointReward;
	}

	public int getPKPointReward() {
		return pkpReward;
	}

	public int getExperienceReward() {
		return experienceReward;
	}

	public int getInterfaceID() {
		return interfaceID;
	}

	public String getAchievementDesc() {
		return achievementDesc;
	}

	public Item[] getItems() {
		return items;
	}

	public int getCompletionAmount() {
		return completionAmount;
	}

	public int getComponentID() {
		return componentID;
	}

	public String getSkillName() {
		return skill;
	}

	public StatType getStatType() {
		return stat;
	}

	public boolean isInterfaceAndComponentID(int compID, int intID) {
		return componentID == compID && intID == interfaceID;
	}

	public AchievementTypes getAchievementType() {
		return achievementTypes;
	}

	public int getButtonID() {
		return buttonID;
	}

	public int getHiddenButtonID() {
		return hiddenButtonID;
	}
}


