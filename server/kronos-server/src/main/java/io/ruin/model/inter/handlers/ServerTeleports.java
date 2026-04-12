package io.ruin.model.inter.handlers;

import io.ruin.cache.ItemID;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import lombok.Getter;

public enum ServerTeleports {
	GWD("Godwars Dungeon", new Position(2882, 5311, 2),
		new Item[]{
			(new Item(11826)), (new Item(11828)), (new Item(11830)),
			(new Item(11832)), (new Item(11834)),
			(new Item(11810)), (new Item(11812)), (new Item(11814)), (new Item(11816)),
			(new Item(11791)), (new Item(11824)), (new Item(11785))
		},
		"An ancient temple of the last <br>remaining battlefields of the gods.<br>", "Hard", "No requirements.", 27661, 4318),
	VENENATIS("Venenatis", new Position(3320, 3795, 0),
		new Item[]{(new Item(12605)), (new Item(11920)), (new Item(30562))},
		"A multi-target attacking poisonous <br>spider of the wilderness.", "Hard", "No requirements.", 28294, 5318),
	CALLISTO("Callisto", new Position(3293, 3848, 0),
		new Item[]{(new Item(12603)), (new Item(11920)), (new Item(30563))},
		"An enormous bear empowered by <br>the corruption of the wilderness.", "Hard", "No requirements.", 28298, 4919),
	VETION("Vet'ion", new Position(3223, 3789, 0),
		new Item[]{(new Item(12601)), (new Item(11920)), (new Item(30561))},
		"A Zamorakian skeletal champion <br>north of the Bone Yard.", "Hard", "No requirements.", 28299, 5505),
	CHAOS_ELE("Chaos elemental", new Position(3283, 3914, 0),
		new Item[]{(new Item(11920))},
		"A pUre dArk cLoUd of cHaOs.", "Hard", "nO reQuirEmenTs.", 11216, 3144),
	VORKATH("Vorkath", new Position(2272, 4051, 0),
		new Item[]{(new Item(21907)), (new Item(22111)), (new Item(11286)), (new Item(22006))},
		"A draconic subject held beneath <br>the dragonkin fortress. Beware.", "Hard", "No requirements.", 35023, 7948
	),
	ARAXXOR("Araxxor", new Position(3659, 9816, 0),
		new Item[]{(new Item(29836)), (new Item(29786)), (new Item(29799)), (new Item(29794)), (new Item(29790)), (new Item(29792)),
			(new Item(29788))},
		"A huge venemous spider lurking within a cave.", "Hard", "92 Slayer.", 0, 0
	),

	ISLE_OF_SOULS("Isle of souls", new Position(2314, 2919, 0),
		new Item[] {
			new Item(995, 250_000),
			new Item(ItemID.DRACONIC_VISAGE)
		},
		"A dungeon of lost souls.",
		"Medium",
		"None.",
		24577,
		90
	),
	GIANTS_DEN("Giant's den", new Position(1417, 3590, 0),
		new Item[] {
			new Item(995, 250_000),
			new Item(ItemID.HILL_GIANT_CLUB)
		},
		"A dungeon of giants.",
		"Medium",
		"None.",
		42534,
		4650
	),
	SOURHOG("Sourhog", new Position(3159, 9712, 0),
		new Item[] {
			new Item(995, 7_333),
		},
		"A dungeon of hogs.",
		"Easy",
		"None.",
		40899,
		8767
	),

	CERBERUS("Cerberus", new Position(1310, 1252, 0),
		new Item[]{(new Item(13231)), (new Item(13229)), (new Item(13227)), (new Item(13233))},
		"A three-headed hound guardian <br>of the river of souls.", "Hard", "91 Slayer.", 29270, 4484),
	BALANCE_ELEMENTAL("Balance Elemental", new Position(0, 0, 0),
		new Item[]{(new Item(33005)), (new Item(21649)), (new Item(6721))},
		"A Elemental that uses all styles.", "Master", "None.", 53290, 11352),
	KRAKEN("Kraken", new Position(2292, 10008, 0),
		new Item[]{(new Item(11905)), (new Item(12004))},
		"A mythical slayer boss of <br>the seas.", "Easy", "87 Slayer.", 28231, 3989),
	ZULRAH("Zulrah", new Position(2198, 3057, 0),
		new Item[]{(new Item(12922)), (new Item(12932)), (new Item(12927)), (new Item(6571))},
		"A serpent monarch lurking in the <br>shadows of poisonous waste.", "Hard", "No requirements.", 10415, 1721),
	GIANT_MOLE("Giant mole", new Position(1764, 5179, 0),
		new Item[]{(new Item(24253))},
		"Sir Tiffy, care to explain?", "Medium", "No requirements.", 42012, 3309
	),
	KBD("King black dragon", new Position(3069, 10255, 0),
		new Item[]{(new Item(11920)), (new Item(11286)), (new Item(30554))},
		"A three-headed dragon isolated in <br>a lair deep inside the wilderness.", "Medium", "No requirements.", 0, 0
	),
	KQ("Kalphite queen", new Position(3509, 9487, 2),
		new Item[]{(new Item(24253))},
		"Half beetle, half wasp. The <br>strongest of the Kalphites. ", "Medium", "No requirements.", 24594, 6239
	),
	SMOKE_DEVIL("Therm. smoke devil", new Position(2412, 3060, 0),
		new Item[]{(new Item(12002)), (new Item(11998))},
		"An atomic smoke devil boss <br>bound to slayer tasks.", "Medium", "93 Slayer.", 36012, 1829
	),
	HYDRA("Alchemical hydra", new Position(1347, 10242, 0),
		new Item[]{(new Item(22966)), (new Item(22983)), (new Item(22973)), (new Item(22969))
			, (new Item(22971)), (new Item(22988))},
		"The abandoned creation of <br>Karuulm, the dragonkin.", "Medium", "95 Slayer.", 36185, 8232
	),
	GALVEK("Galvek", new Position(1630, 5720, 2),
		new Item[]{(new Item(30593)), (new Item(30592)), (new Item(30591))},
		"The dragonkins plan to destroy <br>humanity. Good luck.", "Grand Master", "No requirements.", 35014, 7902
	),
	ARGENTAVIS("Argentavis", new Position(2143, 5526, 3),
		new Item[]{(new Item(30595)), (new Item(30594)), (new Item(30513)), (new Item(30552))},
		"A menace of the sky.", "Master", "No requirements.", 59368, 5029
	),
	NEX("Nex", new Position(2905, 5204, 0),
		new Item[]{(new Item(26376)), (new Item(26378)), (new Item(26380))
			, (new Item(26370)), (new Item(26372)), (new Item(26235)), (new Item(30545))},
		"A Zarosian abomination.", "Grand Master", "No requirements.", 43209, 9177
	),
	NIGHTMARE("Nightmare", new Position(3808, 9746, 1),
		new Item[]{(new Item(24419)),
			(new Item(24420)), (new Item(24421)), (new Item(24511)), (new Item(24514)),
			(new Item(24517)), (new Item(24422)), (new Item(24417))},
		"The creature of your dreams.", "Master", "No requirements.", 39190, 8574
	),
	SARACHNIS("Sarachnis", new Position(1842, 9913, 0),
		new Item[]{(new Item(ItemID.SARACHNIS_CUDGEL))},
		"The mother of temple spiders.", "Hard", "No requirements.", 37292, 8320
	),
	OPHIDIA("Ophidia", new Position(3149, 4658, 0),
		new Item[]{(new Item(30617)), new Item(30618), new Item(30619), new Item(30620), new Item(30622)},
		"A Strange creature abandoned in the ruins of an ancient temple.", "Master", "No requirements.", 21379, 5616
	),
	GROTESQUE_GUARDIANS("Grotesque Guardians", new Position(3149, 4658, 0),
		new Item[]{(new Item(21748)), new Item(4153), new Item(21736), new Item(21739), new Item(21745), new Item(21730)},
		"The gargoyle defenders of the slayer tower.", "Hard", "75 Slayer.", 34185, 7797
	),
	SCURRIUS("Scurrius", new Position(3275, 9870, 0),
		new Item[]{(new Item(28801)), new Item(28798)},
		"The rat king.", "Medium", "No requirements.", 0, 0
	),
	PHANTOM_MUSPAH("Phantom Muspah", new Position(3149, 4658, 0),
		new Item[]{(new Item(27610)), new Item(27627), new Item(27590)},
		"What even is that thing?", "Hard", "No requirements.", 0, 0
	),
	LEVIATHAN("Leviathan", new Position(3149, 4658, 0),
		new Item[]{(new Item(30585)), new Item(28325), new Item(30521), new Item(30522), new Item(30523), new Item(28252)},
		"The king of desert worms.", "Master", "No requirements.", 0, 0
	),
	SOL_HEREDIT("Sol Heredit", new Position(3149, 4658, 0),
		new Item[]{(new Item(28951)), new Item(30461), new Item(28933), new Item(28936), new Item(28939), new Item(28805)},
		"The final fight of the colosseum", "Master", "No requirements.", 0, 0
	),
	DUKE_SUCELLUS("Duke Sucellus", new Position(3149, 4658, 0),
		new Item[]{(new Item(30583)), new Item(28321), new Item(30521), new Item(30522), new Item(30523), new Item(28250)},
		"Some sort of blob.", "Master", "No requirements.", 0, 0
	),
	VARDORVIS("Vardorvis", new Position(3149, 4658, 0),
		new Item[]{(new Item(30584)), new Item(28319), new Item(30521), new Item(30522), new Item(30523), new Item(28248)},
		"A vampyric executioner infected and controlled by the Strangler parasite.", "Master", "No requirements.", 0, 0
	),
	WHISPERER("The Whisperer", new Position(3149, 4658, 0),
		new Item[]{(new Item(30586)), new Item(28323), new Item(30521), new Item(30522), new Item(30523), new Item(28246)},
		"A siren corrupted by the shadows.", "Master", "No requirements.", 0, 0
	),

	MALAKAR("Malakar", DonationBossHandler.malakarTeleportPosition,
		new Item[]{},
		"A powerful titan, suggested group size: 5", "Master", "No requirements.", 0, 0
	),
	SIRE("Abyssal sire", new Position(3030, 4771, 0),
		new Item[]{(new Item(13263)), (new Item(13265))},
		"An engineer of the Abyss from <br>a time before the God Wars.", "Master", "85 Slayer.", 29477, 4527
	),
	DKS("Dagannoth Kings", new Position(1917, 4363, 0),
		new Item[]{(new Item(6737)), (new Item(6733)), (new Item(6735)), (new Item(6731))},
		"A trio of dagannoth bosses <br>of each combat style.", "Medium", "No requirements.", 9941, 2850
	),
	SKOTIZO("Skotizo", new Position(1665, 10047, 0),
		new Item[]{(new Item(6571))},
		"A demonic boss isolated beneath <br>the Catacombs of Kourend.", "Medium", "No requirements.", 31653, 6935
	),
	CORP("Corporeal beast", new Position(2964, 4382, 2),
		new Item[]{(new Item(12819)), (new Item(12827)), (new Item(12823)), (new Item(30187)), (new Item(30551))},
		"A soul-devouring supernatural <br>horror indentured to spirits.", "Hard", "No requirements.", 11056, 1678
	),
	SEERS("Seers Village", new Position(2726, 3491, 0), null, "Various trees to chop down, large <br>amounts of maples." +
		" near a bank.", "", "No requirements.", 0, 0),
	HARDWOOD_GROVE("Hardwood Grove", new Position(2818, 3083, 0), null, "Mahogany and Teak trees can <br>be chopped here.",
		"", "35 Woodcutting for Teak trees and 50 for Mahogany.", 0, 0),
	PLEASANT_PARK("Pleasant Park", new Position(3110, 3512, 0), null, "A park filled with numerous <br>tree types.",
		"", "No requirements.", 0, 0),
	FARMING_GUILD("Farming Guild", new Position(1249, 3725, 0), null, "The Farming Guild, complete some <br>farming contracts here!",
		"", "45 Farming.", 0, 0),
	KOUREND_ALLOTMENT("Kourend Patches", new Position(1737, 3549, 0), null, "The allotment, herb & flower <br>patches in Kourend.",
		"", "No requirements.", 0, 0),
	ARDOUGNE_ALLOTMENT("Ardougne Patches", new Position(2673, 3373, 0), null, "The allotment, herb & flower <br>patches in Ardougne.",
		"", "No requirements.", 0, 0),
	CATHERBY_ALLOTMENT("Catherby Patches", new Position(2806, 3463, 0), null, "The allotment, herb & flower <br>patches in Catherby.",
		"", "No requirements.", 0, 0),
	FALADOR_ALLOTMENT("Falador Patches", new Position(3056, 3310, 0), null, "The allotment, herb & flower <br>patches in Falador.",
		"", "No requirements.", 0, 0),
	PHASMATYS_ALLOTMENT("Phasmatys Patches", new Position(3603, 3531, 0), null, "The allotment, herb & flower <br>patches west of" +
		" Port Phasmatys.",
		"", "No requirements.", 0, 0),
	CHAMPIONS_GUILD_BUSH("Champion guild bush", new Position(3183, 3361, 0), null, "The bush patch next to the <br>Champions' guild.",
		"", "No requirements.", 0, 0),
	RIMMINGTON_BUSH("Rimmington bush", new Position(2940, 3224, 0), null, "The bush patch in Rimmington.",
		"", "No requirements.", 0, 0),
	ARDOUGNE_BUSH("Ardougne bush", new Position(2615, 3224, 0), null, "The bush patch in Ardougne.",
		"", "No requirements.", 0, 0),
	ETCETERIA_BUSH("Etceteria bush", new Position(2589, 3862, 0), null, "The bush patch in Etceteria.",
		"", "No requirements.", 0, 0),
	LUMBRIDGE_HOPS("Lumbridge hops patch", new Position(3229, 3311, 0), null, "Hops patch north of Lumbridge.",
		"", "No requirements.", 0, 0),
	SEERS_HOPS("Seers hops patch", new Position(2671, 3523, 0), null, "Hops patch north of Seers' Village.",
		"", "No requirements.", 0, 0),
	YANILLE_HOPS("Yanille hops patch", new Position(2575, 3101, 0), null, "Hops patch in Yanille.",
		"", "No requirements.", 0, 0),
	ENTRANA_HOPS("Entrana hops patch", new Position(2812, 3333, 0), null, "Hops patch in Entrana.",
		"", "No requirements.", 0, 0),
	LUMBRIDGE_TREE_PATCH("Lumbridge tree patch", new Position(3196, 3231, 0), null, "Tree patch in Lumbridge.",
		"", "No requirements.", 0, 0),
	VARROCK_TREE_PATCH("Varrock tree patch", new Position(3229, 3456, 0), null, "Tree patch in Varrock.",
		"", "No requirements.", 0, 0),
	FALADOR_TREE_PATCH("Falador park tree patch", new Position(3001, 3373, 0), null, "Tree patch in Falador park.",
		"", "No requirements.", 0, 0),
	TAVERLEY_TREE_PATCH("Taverley tree patch", new Position(2936, 3441, 0), null, "Tree patch in Taverley.",
		"", "No requirements.", 0, 0),
	GNOME_STRONGHOLD_TREE_PATCH("Gnome Stronghold tree patch", new Position(2438, 3415, 0), null, "Tree patch in the Gnome" +
		" Stronghold.", "", "No requirements.", 0, 0),
	GNOME_STRONGHOLD_FRUIT_TREE_PATCH("Gnome Stronghold fruit tree patch", new Position(2473, 3447, 0), null, "Fruit tree patch in" +
		" the Gnome Stronghold.", "", "No requirements.", 0, 0),
	CATHERBY_FRUIT_TREE_PATCH("Catherby fruit tree patch", new Position(2858, 3431, 0), null, "Fruit tree " +
		"patch in Catherby", "", "No requirements.", 0, 0),
	TREE_GNOME_MAZE_FRUIT_TREE_PATCH("Tree gnome maze fruit tree patch", new Position(2489, 3177, 0), null, "Fruit tree " +
		"patch west of the <br>tree gnome village maze.", "", "No requirements.", 0, 0),
	BRIMHAVEN_FRUIT_TREE_PATCH("Brimhaven fruit tree patch", new Position(2767, 3215, 0), null, "Fruit tree " +
		"patch in Brimhaven.", "", "No requirements.", 0, 0),
	LLETYA_FRUIT_TREE_PATCH("Lletya fruit tree patch", new Position(2345, 3164, 0), null, "Fruit tree " +
		"patch in LLetya.", "", "No requirements.", 0, 0),
	ETCETERIA_SPIRIT_TREE_PATCH("Etceteria spirit tree patch", new Position(2613, 3855, 0), null, "Spirit tree patch" +
		" in Etceteria.", "", "No requirements.", 0, 0),
	PORT_SARIM_SPIRIT_TREE_PATCH("Port sarim spirit tree patch", new Position(3060, 3261, 0), null, "Spirit tree patch" +
		" in Port sarim.", "", "No requirements.", 0, 0),
	BRIMHAVEN_SPIRIT_TREE_PATCH("Brimhaven spirit tree patch", new Position(2805, 3202, 0), null, "Spirit tree patch" +
		" in Brimhaven.", "", "No requirements.", 0, 0),
	HOSIDIUS_SPIRIT_TREE_PATCH("Hosidius spirit tree patch", new Position(1696, 3542, 0), null, "Spirit tree patch" +
		" in Hosidius.", "", "No requirements.", 0, 0),
	HOSIDIUS_VINERY("Hosidius Vinery", new Position(1807, 3555, 0), null, "You can plant grape seeds here.",
		"", "36 Farming.", 0, 0),
	MUSHROOM_PATCH("Mushroom patch", new Position(3451, 3470, 0), null, "You can plant mushroom seeds.", "", "53 Farming.", 0, 0),
	SEAWEED_PATCH("Giant seaweed patch", new Position(3733, 10270, 1), null, "You can plant your seaweed <br>seeds here.",
		"", "23 Farming.", 0, 0),
	HARDWOOD_TREE_PATCHES("Hardwood Tree patches", new Position(3708, 3836, 0), null, "You can plant your hardwood <br>tree seeds here.",
		"", "35 Farming for teak trees and 55 for Mahogany.", 0, 0),
	AL_KHARID_CACTUS_PATCH("Al kharid cactus patch", new Position(3315, 3200, 0), null, "You can plant your cactus seeds.",
		"", "55 Farming for cactus seeds and 64 for Potato cactus seeds.", 0, 0),
	CATHTERBY("Catherby Fishing", new Position(2845, 3434, 0), null, "Numerous fishing locations on <br>catherby beach.", "", "Minimum 5 fishing.", 0, 0),
	KARAMBWAN_SPOT("Karambwan spot", new Position(2899, 3116, 0), null, "You can fish karambwans here <br>with your karambwan vessel.",
		"", "65 fishing.", 0, 0),
	ANGLERFISH_SPOT("Anglerfish", new Position(1829, 3774, 0), null, "You can fish anglerfish here.", "", "82 fishing, sandworm bait.", 0, 0),
	PISCATORIS("Piscatoris Fishing colony", new Position(2341, 3697, 0), null, "Sharks or monkfish can be caught.",
		"", "76 fishing for sharks and 62 for monkfish.", 0, 0),
	FISHING_GUILD("Fishing guild", new Position(2594, 3415, 0), null, "The fishing guild, numerous spots <br>to fish here.",
		"", "68 fishing", 0, 0),
	OTTOS_GROTTO("Otto's Grotto", new Position(2505, 3495, 0), null, "Barbarian fishing spots",
		"", "48 fishing, 15 strength & 15 agility and a barbarian fishing rod.", 0, 0),
	ABYSS("The Abyss", new Position(3040, 4842, 0), null, "Enter various rifts that send you <br>to specific altars.", "", "No requirements.", 0, 0),
	ZMI("Ourania Altar", new Position(3016, 5626, 0), null, "Runecraft various runes at once <br>at this altar", "", "No requirements.", 0, 0),
	WRATH_ALTAR("Wrath altar", new Position(2335, 4826, 0), null, "Craft wrath runes at this altar", "", "95 Runecrafting.", 0, 0),
	ARCEUUS_ESSENCE_MINE("Arceuus essence mine", new Position(1762, 3851, 0), null, "Mine dense essence blocks for <br>blood and soul runecrafting.",
		"", "38 mining and 38 crafting.", 0, 0),
	SOUL_ALTAR("Soul altar", new Position(1818, 3854, 0), null, "Craft soul runes here with dark <br>essence fragments", "", "90 Runecrafting & dark essence fragments.", 0, 0),
	BLOOD_ALTAR("Blood altar", new Position(1720, 3827, 0), null, "Craft blood runes here with dark <br>essence fragments", "", "77 Runecrafting & dark essence fragments.", 0, 0),
	MOTHERLOAD_MINE("Motherload mine", new Position(3758, 5666, 0), null, "Mine paydirt and exchange it <br>for ore.", "", "30 mining.", 0, 0),
	AMETHYST_MINE("Amethyst mine", new Position(3024, 9710, 0), null, "Mine amethyst here.", "", "92 Mining.", 0, 0),
	AL_KHARID_MINE("Al-kharid mine", new Position(3297, 3279, 0), null, "A variety of rocks to mine here", "", "None.", 0, 0),
	VARROCK_EAST_MINE("Varrock east mine", new Position(3287, 3371, 0), null, "A variety of rocks to mine here", "", "None.", 0, 0),
	VARROCK_WEST_MINE("Varrock west mine", new Position(3184, 3372, 0), null, "A variety of rocks to mine here", "", "None.", 0, 0),
	MINING_GUILD("Mining guild", new Position(3043, 9740, 0), null, "The mining guild.", "", "60 Mining.", 0, 0),
	WOODCUTTING_GUILD("Woodcutting guild", new Position(1590, 3481, 0), null, "The woodcutting guild", "", "60 Woodcutting.", 0, 0),
	CRIMSON_SWIFTS("Crimson swifts", new Position(2607, 2925, 0), null, "Catch Crimson swifts here.", "", "No requirements.", 0, 0),
	TROPICAL_WAGTAIL("Tropical wagtail", new Position(2502, 2889, 0), null, "Catch tropical wagtails here.", "", "19 Hunter.", 0, 0),
	RED_CHINCHOMPAS("Red Chinchompas", new Position(2526, 9293, 0), null, "Catch red chinchompas here.", "", "63 Hunter.", 0, 0),
	BLACK_SALAMANDER("Black salamander", new Position(3297, 3669, 0), null, "Warning: Level 19 Wilderness, <br>catch black salamanders.", "", "67 Hunter.", 0, 0),
	BLACK_CHINCHOMPA("Black chinchompa", new Position(3143, 3774, 0), null, "Warning: Level 32 Wilderness, <br>catch black chinchompas.", "", "73 Hunter.", 0, 0),
	RED_SALAMANDER("Red salamander", new Position(2452, 3221, 0), null, "Catch red salamanders here.", "", "59 Hunter", 0, 0),
	DESERT_HUNTING_GROUND("Desert hunting area", new Position(3397, 3102, 0), null, "Catch orange salamanders and <br>golden warblers here.",
		"", "48 Hunter for orange salamanders & 5 hunter for golden warblers.", 0, 0),
	SWAMP_LIZARDS("Swamp lizards", new Position(3549, 3439, 0), null, "Catch swamp lizards here,", "", "29 Hunter.", 0, 0),
	PURO_PURO("Puro Puro", new Position(2591, 4319, 0), null, "Catch various implings here.", "", "17 Hunter.", 0, 0),
	FALCONRY("Falconry", new Position(2372, 3611, 0), null, "Use a falcon to catch different <br>kebbits here.", "", "43 Hunter.", 0, 0),
	BIRDHOUSES("Birdhouses", new Position(3681, 3815, 0), null, "Place your birdhouses here.", "", "5 Hunter.", 0, 0),
	GNOME_STRONGHOLD_COURSE("Stronghold course", new Position(2470, 3435, 0), null, "Start your agility training here.",
		"", "No requirements.", 0, 0),
	DRAYNOR_ROOFTOP_COURSE("Draynor rooftop", new Position(3104, 3279, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "10 Agility.", 0, 0),
	AL_KHARID_ROOFTOP_COURSE("Al kharid rooftop", new Position(3273, 3197, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "20 Agility.", 0, 0),
	VARROCK_ROOFTOP_COURSE("Varrock rooftop", new Position(3222, 3414, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "30 Agility.", 0, 0),
	CANIFIS_ROOFTOP_COURSE("Canifis rooftop", new Position(3505, 3486, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "40 Agility.", 0, 0),
	FALADOR_ROOFTOP_COURSE("Falador rooftop", new Position(3033, 3341, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "50 Agility.", 0, 0),
	SEERS_ROOFTOP_COURSE("Seers' rooftop", new Position(2728, 3486, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "60 Agility.", 0, 0),
	POLLNIVNEACH_ROOFTOP_COURSE("Pollnivneach rooftop", new Position(3351, 2960, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "70 Agility.", 0, 0),
	RELLEKA_ROOFTOP_COURSE("Rellekka Rooftop", new Position(2626, 3678, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "80 Agility.", 0, 0),
	ARDOUGNE_ROOFTOP_COURSE("Ardougne rooftop", new Position(2673, 3296, 0), null, "Train agility whilst obtaining <br>marks of grace.",
		"", "90 Agility.", 0, 0),
	WILDERNESS_AGILITY_COURSE("Wilderness course", new Position(2997, 3914, 0), null, "Warning: Level 50 Wilderness.", "", "52 Agility.", 0, 0),
	EDGEVILLE_MARKETPLACE("Edgeville marketplace", new Position(3083, 3468, 0), null, "Various npcs to pickpocket and <br>stalls to thieve from.", "", "No requirements.", 0, 0),
	TAVERLEY_DUNGEON("Taverley dungeon", new Position(2884, 9798, 0), null, "Various slayer creatures lurk <br>within this dungeon", "", "No requirements.", 0, 0),
	LUMMY_DUNGEON("Lumbridge Dungeon", new Position(3235, 9570, 0), null, "Various slayer creatures lurk <br>within this dungeon", "", "No requirements.", 0, 0),
	ARDY_DUNGEON("Ardougne dungeon", new Position(2586, 9657, 0), null, "Various slayer creatures lurk <br>within this dungeon", "", "No requirements.", 0, 0),
	LITHKREN("Lithkren Vault", new Position(3550, 10455, 0), null, "Rune dragons & Adamant dragons lurk <br>within this dungeon", "", "No requirements.", 0, 0),
	ANCIENT_CAVERN("Ancient cavern", new Position(1764, 5366, 1), null, "Various slayer creatures lurk <br>within this cavern", "", "No requirements.", 0, 0),
	SKELETAL_WYVERN("Skeletal wyverns", new Position(3056, 9562, 0), null, "Fight skeletal wyverns here.", "", "72 Slayer", 0, 0),
	FREMENNIK_SLAYER_CAVE("Frem. slayer cave", new Position(2801, 9998, 0), null, "Various slayer creatures lurk <br>within this cave.", "", "Requirements depend on the slayer creature.", 0, 0),
	MOUNT_KARUULM("Mount karuulm", new Position(1309, 3792, 0), null, "Karuulm dungeon is accessed here.", "", "No requirements to access the dungeon.", 0, 0),
	FOSSIL_ISLAND_WYVERN_CAVE("Fossil island wyverns", new Position(3603, 10229, 0), null, "Fight fossil island wyverns here.", "", "No requirements.", 0, 0),
	BRIMHAVEN_DUNGEON("Brimhaven dungeon", new Position(2709, 9564, 0), null, "Fight various npcs that lurk within <br>this dungeon.", "", "No requirements.", 0, 0),
	SLAYER_TOWER("Slayer tower", new Position(3428, 3538, 0), null, "Fight various slayer creatures <br>within this tower.", "", "Requirements are dependant on the creature.", 0, 0),
	MOS_LE_HARMLESS("Mos Le Harmless", new Position(3676, 2981, 0), null, "A few different slayer creatures <br>lurk this island.", "", "Requirements depend on the creature.", 0, 0),
	DARK_BEAST("Dark beasts", new Position(2028, 4650, 0), null, "Fight dark beasts here.", "", "90 Slayer.", 0, 0),
	STRONGHOLD_CAVE("Stronghold cave", new Position(2429, 9824, 0), null, "There are various slayer monsters <br>that lurk within this cave," +
		" but you must be on task!", "", "Must be on slayer task to fight these creatures.", 0, 0),
	DEMONIC_GORILLA("Demonic gorillas", new Position(2128, 5647, 0), new Item[]{(new Item(19529)), (new Item(19589)), (new Item(19586)), (new Item(19610)), (new Item(19592)), (new Item(19601)), (new Item(30546)), (new Item(30547))}, "Fight tortured or demonic gorillas <br>within this cave.", "", "No requirements.", 31241, 7230),
	KOUREND_CATACOMBS("Kourend Catacombs", new Position(1664, 10045, 0), null, "Fight creatures and obtain ancient <br>shards within these catacombs.", "", "No requirements.", 0, 0),
	SMOKE_DEVILS_CAVE("Smoke devils", new Position(2404, 9415, 0), null, "Smoke devil dungeon.", "", "93 Slayer & to be on slayer task.", 0, 0),
	EDGEVILLE("Edgeville", new Position(3088, 3499, 0), null, "Edgeville Teleport.", "", "", 0, 0),
	VARROCK("Varrock", new Position(3211, 3424, 0), null, "Varrock Teleport.", "", "", 0, 0),
	LUMBRIDGE("Lumbridge", new Position(3222, 3218, 0), null, "Lumbridge Teleport.", "", "", 0, 0),
	FALADOR("Falador", new Position(2964, 3378, 0), null, "Falador Teleport.", "", "", 0, 0),
	ARDOUGNE("Ardougne", new Position(2661, 3307, 0), null, "Ardougne Teleport.", "", "", 0, 0),
	YANILLE("Yanille", new Position(2605, 3092, 0), null, "Yanille Teleport.", "", "", 0, 0),
	BRIMHAVEN("Brimhaven", new Position(2806, 3185, 0), null, "Brimhaven Teleport.", "", "", 0, 0),
	PRIFDDINAS("Prifddinas", new Position(3248, 6106, 0), null, "Prifddinas.", "", "", 0, 0),
	APE_ATOLL("Ape Atoll", new Position(2762, 2785, 0), null, "Ape Atoll.", "", "", 0, 0),
	GNOME_STRONGHOLD("Gnome Stronghold", new Position(2465, 3488, 0), null, "Grand Tree Teleport.", "", "", 0, 0),
	PORT_SARIM("Port Sarim", new Position(3016, 3218, 0), null, "Port Sarim Teleport.", "", "", 0, 0),
	AL_KHARID("Al Kharid", new Position(3293, 3173, 0), null, "Al Kharid Teleport.", "", "", 0, 0),
	CANIFIS("Canifis", new Position(3510, 3480, 0), null, "Canifis Teleport.", "", "", 0, 0),
	BURTHORPE("Burthorpe", new Position(2898, 3556, 0), null, "Burthorpe Teleport.", "", "", 0, 0),
	DRAYNOR("Draynor Village", new Position(3104, 3249, 0), null, "Draynor Village Teleport.", "", "", 0, 0),
	RELLEKA("Rellekka", new Position(2643, 3678, 0), null, "Rellekka Teleport.", "", "", 0, 0),
	SHILO_VILLAGE("Shilo Village", new Position(2852, 2957, 0), null, "Shilo Village Teleport.", "", "", 0, 0),
	POLLNIVNEACH("Pollnivneach", new Position(3359, 2969, 0), null, "Pollnivneach Teleport.", "", "", 0, 0),
	LUNAR_ISLE("Lunar Isle", new Position(2099, 3914, 0), null, "Lunar Isle Teleport.", "", "", 0, 0),
	ROCK_CRABS("Rock crabs", new Position(2674, 3710, 0), new Item[]{new Item(995, 500000)}, "Low level training location.", "", "", 4400, 130),
	SAND_CRABS("Sand crabs", new Position(1719, 3465, 0), new Item[]{new Item(995, 500000)}, "Low-mid level training location.", "", "", 31184, 1310),
	AMMONITE_CRABS("Ammonite crabs", new Position(3720, 3873, 0), new Item[]{new Item(995, 500000)}, "Mid-high level training location.", "", "", 33281, 1310),
	YAKS("Yaks", new Position(2321, 3795, 0), new Item[]{new Item(995, 500000)}, "Mid level training location.", "", "", 22165, 5785),
	CHICKENS("Chickens", new Position(3233, 3295, 0), new Item[]{new Item(995, 500000)}, "Low level training location.", "", "", 23905, 5386),
	CHAOS_DRUID("Taverley Dungeon", new Position(2884, 9800, 0), new Item[]{new Item(995, 500000)}, "Low level training location.", "", "", 0, 0),
	EXPERIMENTS("Experiments", new Position(3556, 9947, 0), new Item[]{new Item(995, 500000)}, "Mid level training location.", "", "", 5143, 1614),
	CATALYSTS("Catalysts", new Position(1168, 4125, 0), new Item[]{new Item(30315), new Item(30312), new Item(30309), new Item(ItemID.PERK_POINT_SCROLL), new Item(25590), new Item(ItemID.SMALL_EXP_LAMP), new Item(ItemID.DULL_MINERALS), new Item(ItemID.SHINY_MINERALS)}, "Mid-high level training location.", "", "", 35027, 7922),
	VYREWATCH_SENTINELS("Vyrewatch Sentinels", new Position(3605, 3362, 0), new Item[]{new Item(24777, 1)}, "High level training location.", "", "", 17952, 4689),
	BASILISK_KNIGHTS("Basilisk Knights", new Position(2461, 10415, 0), new Item[]{new Item(24268, 1)}, "An area riddled with basilisks.", "", "", 38864, 8497),
	ARMOURED_ZOMBIES("Armoured Zombies", new Position(3558, 9778, 0), new Item[]{new Item(28810, 1)}, "Aggressive armoured zombies inside.", "", "", 0, 0),
	SHAMANS("Lizardman shamans", new Position(1293, 10063, 0), new Item[]{new Item(13576)}, "Fight shamans in a single way <br>combat location.", "Medium", "No requirements.", 36345, 7191),
	CHAMBERS_OF_XERIC("Chambers of Xeric", new Position(1254, 3567, 0), new Item[]{new Item(20997),
		new Item(21003), new Item(21043), new Item(21018), new Item(21021), new Item(21024), new Item(21012),
		new Item(21000), new Item(13652), new Item(21015), new Item(21079), new Item(21034)},
		"Fight numerous high level monsters <br>within these raids.<br>",
		"Grand Master", "No requirements, high stats recommended.", 32697, 7396),
	THEATRE_OF_BLOOD("Theatre of Blood", new Position(3669, 3219, 0), new Item[]{new Item(ItemID.SCYTHE_OF_VITUR_UNCHARGED),
		new Item(ItemID.GHRAZI_RAPIER), new Item(ItemID.SANGUINESTI_STAFF), new Item(ItemID.AVERNIC_DEFENDER_HILT), new Item(ItemID.JUSTICIAR_FACEGUARD),
		new Item(ItemID.JUSTICIAR_CHESTGUARD), new Item(ItemID.JUSTICIAR_LEGGUARDS), new Item(30545)},
		"Fight numerous high level monsters <br>within these raids.<br>",
		"Grand Master", "No requirements, high stats recommended.", 35381, 8120),
	TOMBS_OF_AMASCUT("Tombs of Amascut", new Position(3355, 9119, 0), new Item[]{new Item(27386),
		new Item(27277), new Item(27226), new Item(27229), new Item(27232), new Item(25985), new Item(26219),
		new Item(25975)},
		"Fight numerous high level monsters <br>within these raids.<br>",
		"Grand Master", "No requirements, high stats recommended.", -1, -1),
	THE_GAUNTLET("The Gauntlet", new Position(3031, 6121, 1), new Item[]{new Item(23995), new Item(25865), new Item(23971), new Item(23975), new Item(23979)},
		"Gather various resources and<br>supplies by skilling and defeating monsters<br>all in preparation to defeat the Hunllef",
		"Grand Master", "No requirements, high stats recommended.", 38595, 8417),
	WARRIORS_GUILD("Warriors guild", new Position(2870, 3546, 0), new Item[]{new Item(12954),
		new Item(8850), new Item(8849), new Item(8848), new Item(8847), new Item(8846), new Item(8845),
		new Item(8844)}, "Obtain warrior tokens and kill the <br>cyclopes to obtain defenders.",
		"Medium", "Your attack and strength level combined must equal at least 130.", 0, 0),
	BARROWS("Barrows", new Position(3565, 3310, 0), null,
		"Slay all 6 brothers and open the <br>chest to be rewarded.",
		"Medium", "No requirements.", 0, 0),
	FIGHT_CAVES("Fight Caves", new Position(2439, 5169, 0), new Item[]{new Item(6570)},
		"Complete all the waves and kill <br>Jad to receive a fire cape!",
		"Hard", "No requirements.", 9319, 2650),
	INFERNO("The Inferno", new Position(2495, 5113, 0), new Item[]{new Item(21295)},
		"Complete all the waves and kill <br>Zuk to receive an infernal cape!",
		"Grand Master", "You must sacrifice a fire cape.", 34586, 7975),
	PEST_CONTROL("Pest control", new Position(2658, 2660, 0), new Item[]{new Item(11665),
		new Item(11664), new Item(11663), new Item(8842), new Item(8840), new Item(8839)},
		"Protect the void knight and kill the <br>portals to win games for rewards.",
		"Easy", "40 combat.", 14549, 3906),
	DOMINION_OF_ECHOES("Dominion of Echoes", new Position(1781, 3105, 0), new Item[] {
		new Item(ItemID.ASGARNIA_ECHO_ORB),
		new Item(ItemID.DESERT_ECHO_ORB),
		new Item(ItemID.FREMENNIK_ECHO_ORB),
		new Item(ItemID.WILDERNESS_ECHO_ORB),
		new Item(ItemID.KANDARIN_ECHO_ORB),
		new Item(ItemID.MORYTANIA_ECHO_ORB),
		new Item(ItemID.VARLAMORE_ECHO_ORB)
	},
		"Try your luck against the strongest raid in history.",
		"Grand Master", "No requirements.", 0, 0),
	WINTERTODT("Wintertodt", new Position(1633, 3944, 0), new Item[]{new Item(20714)},
		"Keep the braziers lit to help <br>destroy the wintertodt.",
		"Medium", "50 firemaking.", 26852, 6809),

	TEMPOROSS("Tempoross", new Position(3153, 2839, 0), new Item[]{new Item(25574), new Item(25582)},
		"The keeper of the ocean.",
		"Medium", "50 fishing.", 41812, 8895),
	DUEL_ARENA("Duel arena", new Position(3365, 3265, 0), null,
		"Stake other players or have <br>friendly fights here.",
		"", "No requirements.", 0, 0),
	EAST_DRAGONS("East dragons", new Position(3112, 3710, 0), null, "You will be teleported to level 24 <br>wilderness.", "Medium", "No requirements.", 0, 0),
	CHAOS_ALTAR("Chaos Altar (Bones)", new Position(2960, 3818, 0), null, "You will be teleported to level 40+ <br>wilderness.", "Easy", "No requirements.", 0, 0),
	WEST_DRAGONS("West dragons", new Position(2980, 3631, 0), null, "You will be teleported to level 14 <br>wilderness.", "Medium", "No requirements.", 0, 0),
	MAGE_BANK("Mage bank", new Position(2539, 4715, 0), null, "You will be teleported to the <br>mage bank (safe).", "", "No requirements.", 0, 0),
	REVENANTS("Revenants", new Position(3255, 10197, 0), new Item[]{
		new Item(23615), new Item(22628), new Item(22631), new Item(22616), new Item(22619),
		new Item(22641), new Item(22644), new Item(22653), new Item(22656), new Item(22550), new Item(22305)}
		, "You will be teleported to level <br>35 wilderness.", "Hard", "No requirements.", 34154, 4318),
	SCORPIA("Scorpia", new Position(3242, 3948, 0), new Item[]{new Item(11930),
		new Item(11933)}, "You will be teleported to level <br>54 wilderness.", "Hard", "No requirements.", 28293, 6252),
	CHAOS_FANATIC("Chaos fanatic", new Position(2993, 3846, 0), new Item[]{new Item(11928),
		new Item(11931)}, "You will be teleported to level <br>41 wilderness.", "Hard", "No requirements.", 0, 0),
	CRAZY_ARCH("Crazy archeologist", new Position(2983, 3687, 0), new Item[]{new Item(11929),
		new Item(11932)}, "You will be teleported to level <br>21 wilderness.", "Hard", "No requirements.", 0, 0),
	WILDY_RESOURCE_AREA("Resource area", new Position(3184, 3946, 0), null, "Train various skills in this area, <br>lvl 54 wilderness (Dangereous).", "", "200k coins to enter.", 0, 0),
	CAVE_HORRORS("Cave horrors", new Position(3746, 9374, 0), new Item[]{new Item(8901)}, "Kill cave horrors in this cave to <br>obtain a black mask.", "Medium", "58 slayer.", 0, 0),
	MANIACAL_MONKEYS("Maniacal Monkeys", new Position(2437, 9154, 1), null, "Great place to chin or burst/barrage.",
		"Easy", "No requirements.", 0, 0);
	final String name;
	@Getter
	Position teleportPos;
	final Item[] notableDrops;
	final String description;
	final String difficulty;
	final String requirements;
	final int modelId;
	final int animId;

	ServerTeleports(String name, Position teleportPos, Item[] notableDrops, String description, String difficulty, String requirements, int modelId, int animId) {
		this.name = name;
		this.teleportPos = teleportPos;
		this.notableDrops = notableDrops;
		this.description = description;
		this.difficulty = difficulty;
		this.requirements = requirements;
		this.modelId = modelId;
		this.animId = animId;
	}

	public static final ServerTeleports[] VALUES = values();

	public Item[] getItems() {
		return notableDrops;
	}
}
