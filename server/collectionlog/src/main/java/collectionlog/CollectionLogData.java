package collectionlog;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;

public enum CollectionLogData {
	ABYSSAL_SIRE(CollectionLogUpdated.Categories.BOSSES, "Abyssal Sire",
			new Item[] { (new Item(13262)), (new Item(13273)), (new Item(7979)), (new Item(13274)),
					(new Item(13275)), (new Item(13276)), (new Item(13277)), (new Item(13265)), (new Item(4151)) },
			// completion rewards
			new Item[] {
					new Item(ItemID.UNSIRED),
					new Item(30580),
					new Item(ItemID.ABYSSAL_WHIP, 2),
					new Item(30575)
			}),
	ARAXXOR(CollectionLogUpdated.Categories.BOSSES, "Araxxor", new Item[] {
			new Item(29836), new Item(29786), new Item(29790),
			new Item(29792), new Item(29794), new Item(29799), new Item(29788),

	},
			// completion rewards
			new Item[] {
					new Item(25430, 15),
					new Item(30542, 5),
					new Item(4810, 1),
					new Item(30570, 5),
			}),
	ARGENTAVIS(CollectionLogUpdated.Categories.BOSSES, "Argentavis", new Item[] {
			new Item(30600), new Item(30513), new Item(30595), new Item(30594), new Item(30552)

	},
			// completion rewards
			new Item[] {
					new Item(7478, 15),
					new Item(30461, 1),
					new Item(30525, 1),
					new Item(30576, 1),
			}),
	DAGANNOTH_KINGS(CollectionLogUpdated.Categories.BOSSES, "Dagannoth Kings",
			new Item[] { (new Item(12644)), (new Item(12643)), (new Item(12645)), (new Item(6737)),
					(new Item(6733)), (new Item(6731)), (new Item(6735)), (new Item(6739)), (new Item(6724)), (new Item(6562)) },
			// completion rewards
			new Item[] {
					new Item(30461),
					new Item(30581),
					new Item(7478, 8),
					new Item(30460)
			}),
	ALCHEMICAL_HYDRA(CollectionLogUpdated.Categories.BOSSES, "Alchemical Hydra", new Item[] {
			(new Item(22971)/* Hydra Fang */), (new Item(22804)/* Dragon knife */),
			(new Item(23077)/* Alchemical hydra heads */), (new Item(22988)/* Hydra tail */),
			(new Item(22746)/* Ikkle hydra */), (new Item(22983)/* Hydra leather */), (new Item(22969)/* Hydra Heart */),
			(new Item(23064)/* Jar of chemicals */), (new Item(22973)/* Hydra eye */),
			(new Item(20849)/* Dragon thrownaxe */),
			(new Item(22966)/* Hydra claw */) },
			// completion rewards
			new Item[] {
					new Item(30458, 25),
					new Item(30460),
					new Item(30542),
					new Item(30452, 10)
			}),
	BALANCE_ELEMENTAL(CollectionLogUpdated.Categories.BOSSES, "Balance Elemental",
			new Item[] { (new Item(33006)), new Item(21649), new Item(6721) },
			// completion rewards
			new Item[] {
					new Item(30461),
					new Item(30575),
					new Item(7478, 10),
					new Item(30541, 1)
			}),
	BARROWS_CHESTS(CollectionLogUpdated.Categories.MINIGAMES, "Barrows Chests", new Item[] {
			(new Item(4732)/* Karil's coif */), (new Item(4736)/* Karil's leathertop */),
			(new Item(4738)/* Karil's leatherskirt */), (new Item(4734)/* Karil's crossbow */),
			(new Item(4708)/* Ahrim's hood */), (new Item(4712)/* Ahrim's robetop */),
			(new Item(4714)/* Ahrim's robeskirt */), (new Item(4710)/* Ahrim's staff */), (new Item(4716)/* Dharok's helm */),
			(new Item(4720)/* Dharok's platebody */), (new Item(4722)/* Dharok's platelegs */),
			(new Item(4718)/* Dharok's greataxe */), (new Item(4724)/* Guthan's helm */), (new Item(4728)/*
																																																		 * Guthan's
																																																		 * platebody
																																																		 */),
			(new Item(4730)/* Guthan's chainskirt */), (new Item(4726)/* Guthan's warspear */), (new Item(4745)/*
																																																					 * Torag's
																																																					 * helm
																																																					 */),
			(new Item(4749)/* Torag's platebody */), (new Item(4751)/* Torag's platelegs */), (new Item(4747)/*
																																																				 * Torag's
																																																				 * hammers
																																																				 */),
			(new Item(4753)/* Verac's helm */), (new Item(4757)/* Verac's brassard */), (new Item(4759)/*
																																																	 * Verac's plateskirt
																																																	 */),
			(new Item(4755)/* Verac's flail */), (new Item(4740)/* Bolt rack */) },
			// completion rewards
			new Item[] {
					new Item(7478, 10),
					new Item(30461, 1),
					new Item(30581, 1),
					new Item(30459, 2),
			}),
	BRYOPHYTA(CollectionLogUpdated.Categories.BOSSES, "Bryophyta",
			new Item[] { (new Item(22372)/* Bryophyta's essence */) },
			// completion rewards
			new Item[] {
					new Item(30579),
					new Item(ItemID.NATURE_RUNE, 15000),
					new Item(7478, 2),
					new Item(995, 15000000)
			}),
	CALLISTO(CollectionLogUpdated.Categories.BOSSES, "Callisto",
			new Item[] { (new Item(13178)/* Callisto cub */), (new Item(12603)/* Tyrannical ring */),
					(new Item(11920)/* Dragon pickaxe */), (new Item(7158)/* Dragon 2h sword */) },
			// completion rewards
			new Item[] {
					new Item(30460),
					new Item(7478, 3),
					new Item(30578),
					new Item(30579)
			}),
	CERBERUS(CollectionLogUpdated.Categories.BOSSES, "Cerberus",
			new Item[] { (new Item(13247)/* Hellpuppy */), (new Item(13227)/* Eternal crystal */),
					(new Item(13229)/* Pegasian crystal */), (new Item(13231)/* Primordial crystal */), (new Item(13245)/*
																																																							 * Jar of
																																																							 * souls
																																																							 */),
					(new Item(13233)/* Smouldering stone */), (new Item(13249)/* Key master teleport */) },
			// completion rewards
			new Item[] {
					new Item(30452, 8),
					new Item(30542),
					new Item(ItemID.SMOULDERING_STONE, 2),
					new Item(30458, 10),
			}),
	CHAOS_ELEMENTAL(CollectionLogUpdated.Categories.BOSSES, "Chaos Elemental", new Item[] { (new Item(11995)/*
																																																					 * Pet chaos
																																																					 * elemental
																																																					 */),
			(new Item(11920)/* Dragon pickaxe */), (new Item(7158)/* Dragon 2h sword */) },
			// completion rewards
			new Item[] {
					new Item(7478, 3),
					new Item(30577, 5),
					new Item(30460),
					new Item(30459),
			}),
	CHAOS_FANATIC(CollectionLogUpdated.Categories.BOSSES, "Chaos Fanatic",
			new Item[] { (new Item(11995)/* Pet chaos elemental */), (new Item(11928)/* Odium shard 1 */),
					(new Item(11931)/* Malediction shard 1 */) },
			// completion rewards
			new Item[] {
					new Item(7478, 2),
					new Item(30577),
					new Item(7968, 5),
					new Item(30578, 1),
			}),
	COMMANDER_ZILYANA(CollectionLogUpdated.Categories.BOSSES, "Commander Zilyana",
			new Item[] { (new Item(12651)/* Pet zilyana */), (new Item(11785)/* ACB */),
					(new Item(11814)/* Sara hilt */), (new Item(11838)/* Sara sword */), (new Item(13256)/* Sara light */),
					(new Item(11818)/* Gs shard 1 */),
					(new Item(11820)/* Gs shard 2 */), (new Item(11822)/* Gs shard 3 */) },
			// completion rewards
			new Item[] {
					new Item(30582, 1),
					new Item(7478, 5),
					new Item(30581, 1),
					new Item(30577, 1),
			}),
	CORPOREAL_BEAST(
			CollectionLogUpdated.Categories.BOSSES, "Corporeal Beast", new Item[] { (new Item(12816)/* Pet dark core */),
					(new Item(30187)/* Ely sigil */), (new Item(12819)/* Ely sigil */),
					(new Item(12823)/* Spec sigil */), (new Item(12827)/* Arcane sigil */), (new Item(12833)/* Holy elixir */),
					(new Item(12829)/* Spirit shield */) },
			// completion rewards
			new Item[] {
					new Item(7478, 10),
					new Item(30581, 2),
					new Item(30572, 1),
					new Item(30459, 2),
			}),
	CRAZY_ARCHAEOLOGIST(CollectionLogUpdated.Categories.BOSSES, "Crazy Archaeologist", new Item[] { (new Item(11929)/*
																																																									 * Odium
																																																									 * shard
																																																									 * 2
																																																									 */),
			(new Item(11932)/* Malediction shard 2 */), (new Item(11990)/* Fedora */) },
			// completion rewards
			new Item[] {
					new Item(7478, 2),
					new Item(30577, 1),
					new Item(7968, 5),
					new Item(30578, 1),
			}),
	FIGHT_CAVES(CollectionLogUpdated.Categories.MINIGAMES, "The Fight Caves", new Item[] { (new Item(13225)/* Pet jad */),
			(new Item(6570)/* Fire cape */) },
			// completion rewards
			new Item[] {
					new Item(ItemID.TOKKUL, 1000000),
					new Item(30581, 1),
					new Item(30461, 1),
					new Item(7478, 10),
			}),
	GALVEK(CollectionLogUpdated.Categories.BOSSES, "Galvek", new Item[] {
			new Item(30417), new Item(30593), new Item(30592),
			new Item(30591), new Item(ItemID.DRAGON_PLATEBODY), new Item(ItemID.DRAGON_KITESHIELD)

	},
			// completion rewards
			new Item[] {
					new Item(7478, 15),
					new Item(30461, 2),
					new Item(30527, 1),
					new Item(30576, 1),
			}),
	GAUNTLET(CollectionLogUpdated.Categories.MINIGAMES, "The Gauntlet",
			new Item[] { (new Item(23757)/* Youngllef */), (new Item(23956)/* Crystal armour seed */),
					(new Item(4207)/* Crystal weapon seed */), (new Item(25859)/* Gaunlet cape */) },
			// completion rewards
			new Item[] {
					new Item(30528, 1),
					new Item(30581, 1),
					new Item(30575, 1),
					new Item(7478, 15),
			}),
	GENERAL_GRAARDOR(CollectionLogUpdated.Categories.BOSSES, "General Graardor", new Item[] { (new Item(12650)/*
																																																						 * Pet
																																																						 * general
																																																						 * graardor
																																																						 */),
			(new Item(11832)/* Bandos chestplate */), (new Item(11834)/* Bandos tassets */), (new Item(11836)/*
																																																				 * Bandos boots
																																																				 */),
			(new Item(11812)/* Bandos hilt */), (new Item(11818)/* Gs shard 1 */), (new Item(11820)/* Gs shard 2 */),
			(new Item(11822)/* Gs shard 3 */) },
			// completion rewards
			new Item[] {
					new Item(30582),
					new Item(7478, 5),
					new Item(30581, 1),
					new Item(30577, 1),
			}),
	GIANT_MOLE(CollectionLogUpdated.Categories.BOSSES, "Giant Mole",
			new Item[] { (new Item(12646)/* Baby mole */), (new Item(7418)/* Mole skin */),
					(new Item(7416)/* Mole claw */) },
			// completion rewards
			new Item[] {
					new Item(30579, 1),
					new Item(7478, 5),
					new Item(30455, 5),
					new Item(30577, 2),
			}),

	GROTESQUE_GUARDIANS(CollectionLogUpdated.Categories.BOSSES, "Grotesque Guardians",
			new Item[] { (new Item(21748)), (new Item(4153)), (new Item(21736)), (new Item(21739)), (new Item(21730)),
					(new Item(21745)) },
			// completion rewards
			new Item[] {
					new Item(30577, 5),
					new Item(989, 50),
					new Item(4810, 1),
					new Item(2528)
			}),

	HESPORI(CollectionLogUpdated.Categories.BOSSES, "Hespori", new Item[] { (new Item(22994)/*
																																													 * Bottomless compost bucket
																																													 */), (new Item(22883)/*
																																																								 * Iasor
																																																								 * seed
																																																								 */),
			(new Item(22885)/* Kronos seed */), (new Item(22881)/* Attas seed */) },
			// completion rewards
			new Item[] {
					new Item(ItemID.ULTRACOMPOST + 1, 750),
					new Item(7478, 3),
					new Item(ItemID.HESPORI_SEED, 25),
					new Item(30577, 1),
			}),
	THE_INFERNO(CollectionLogUpdated.Categories.MINIGAMES, "The Inferno", new Item[] { (new Item(22319)/* Pet */),
			(new Item(21295)/* Infernal cape */) },
			// completion rewards
			new Item[] {
					new Item(ItemID.TOKKUL, 2500000),
					new Item(30581, 3),
					new Item(4810, 2),
					new Item(30576, 1),
			}),
	PEST_CONTROL(CollectionLogUpdated.Categories.MINIGAMES, "Pest Control",
			new Item[] { (new Item(ItemID.VOID_RANGER_HELM)),
					(new Item(ItemID.VOID_MAGE_HELM)), (new Item(ItemID.VOID_MELEE_HELM)), (new Item(ItemID.VOID_KNIGHT_GLOVES)),
					(new Item(ItemID.VOID_KNIGHT_TOP)), (new Item(ItemID.VOID_KNIGHT_ROBE)),
					(new Item(ItemID.ELITE_VOID_TOP)), (new Item(ItemID.ELITE_VOID_ROBE)) },
			// completion rewards
			new Item[] {
					new Item(30580, 2),
					new Item(30461, 1),
			}),
	KALPHITE_QUEEN(CollectionLogUpdated.Categories.BOSSES, "Kalphite Queen",
			new Item[] { (new Item(12647)/* Kalphite princess */), (new Item(7981)/* Kq head */),
					(new Item(12885)/* Jar of sand */), (new Item(7158)/* D2h */), (new Item(3140)/* D chain */) },
			// completion rewards
			new Item[] {
					new Item(7478, 8),
					new Item(30460, 1),
					new Item(30581, 2),
					new Item(7968, 5),
			}),
	KING_BLACK_DRAGON(CollectionLogUpdated.Categories.BOSSES, "King Black Dragon",
			new Item[] { (new Item(12653)/* Prince black dragon */), (new Item(7980)/* Kbd heads */),
					(new Item(11920)/* D pickaxe */), (new Item(11286)/* Visage */) },
			// completion rewards
			new Item[] {
					new Item(7478, 8),
					new Item(30460, 1),
					new Item(30581, 2),
					new Item(7968, 5),
			}),
	KRAKEN(CollectionLogUpdated.Categories.BOSSES, "Kraken",
			new Item[] { (new Item(12655)/* Pet kraken */), (new Item(12004)/* Kraken tentacle */),
					(new Item(11905)/* Trident of the seas */), (new Item(12007)/* Jar of dirt */) },
			// completion rewards
			new Item[] {
					new Item(30452, 10),
					new Item(30542, 1),
					new Item(7478, 5),
					new Item(30458, 10),
			}),
	KREE_ARRA(CollectionLogUpdated.Categories.BOSSES, "Kree'arra",
			new Item[] { (new Item(12649)/* Pet kree */), (new Item(11826)/* Arma helm */),
					(new Item(11828)/* Arma chestplate */), (new Item(11830)/* Arma chainskirt */),
					(new Item(11810)/* Arma hilt */), (new Item(11818)/* Gs shard 1 */), (new Item(11820)/* Gs shard 2 */),
					(new Item(11822)/* Gs shard 3 */) },
			// completion rewards
			new Item[] {
					new Item(30582, 1),
					new Item(7478, 5),
					new Item(30581, 1),
					new Item(30577, 1),
			}),
	KRIL_TSUTSAROTH(CollectionLogUpdated.Categories.BOSSES, "K'ril Tsutsaroth",
			new Item[] { (new Item(12652)/* Pet kril */), (new Item(11791)/* Sotd */),
					(new Item(11824)/* Zammy spear */), (new Item(11787)/* Steam battlestaff */),
					(new Item(11816)/* Zammy hilt */),
					(new Item(11818)/* Gs shard 1 */), (new Item(11820)/* Gs shard 2 */), (new Item(11822)/* Gs shard 3 */) },
			// completion rewards
			new Item[] {
					new Item(30582, 1),
					new Item(7478, 5),
					new Item(30581, 1),
					new Item(30577, 1),
			}),
	THE_NIGHTMARE(CollectionLogUpdated.Categories.BOSSES, "The Nightmare", new Item[] {
			(new Item(24491)/* Little Nightmare */), (new Item(24417)/* Inq mace */), (new Item(24419)/* Inq great helm */),
			(new Item(24420)/* Inq hauberk */), (new Item(24421)/* Inq plateskirt */), (new Item(24422)/* Nightmare staff */),
			(new Item(24514)/* Volatile orb */), (new Item(24511)/* Harm orb */), (new Item(24517)/* Eld orb */) },
			// completion rewards
			new Item[] {
					new Item(30526, 1),
					new Item(7478, 10),
					new Item(7968, 5),
					new Item(30581, 2),
			}),
	LEVIATHAN(CollectionLogUpdated.Categories.BOSSES, "Leviathan", new Item[] {
			(new Item(28276)/* Inq mace */), (new Item(30585)/* Inq great helm */),
			(new Item(28325)/* Inq hauberk */), (new Item(30521)/* Inq plateskirt */), (new Item(30522)/* Nightmare staff */),
			(new Item(30523)/* Volatile orb */), (new Item(28252)/* Eld orb */) },
			// completion rewards
			new Item[] {
					new Item(4810, 1),
					new Item(7478, 10),
					new Item(7968, 5),
					new Item(30581, 2),
			}),
	DUKE_SUCELLUS(CollectionLogUpdated.Categories.BOSSES, "Duke Sucellus", new Item[] {
			(new Item(28276)/* Inq mace */), (new Item(30583)/* Inq great helm */),
			(new Item(28321)/* Inq hauberk */), (new Item(30521)/* Inq plateskirt */), (new Item(30522)/* Nightmare staff */),
			(new Item(30523)/* Volatile orb */), (new Item(28250)/* Eld orb */) },
			// completion rewards
			new Item[] {
					new Item(4810, 1),
					new Item(7478, 10),
					new Item(7968, 5),
					new Item(30581, 2),
			}),
	VARDORVIS(CollectionLogUpdated.Categories.BOSSES, "Vardorvis", new Item[] {
			(new Item(28276)/* Inq mace */), (new Item(30584)/* Inq great helm */),
			(new Item(28319)/* Inq hauberk */), (new Item(30521)/* Inq plateskirt */), (new Item(30522)/* Nightmare staff */),
			(new Item(30523)/* Volatile orb */), (new Item(28248)/* Eld orb */) },
			// completion rewards
			new Item[] {
					new Item(4810, 1),
					new Item(7478, 10),
					new Item(7968, 5),
					new Item(30581, 2),
			}),
	SOL_HEREDIT(CollectionLogUpdated.Categories.BOSSES, "Sol Heredit", new Item[] {
			(new Item(28947)/* Inq mace */), (new Item(28933)/* Inq great helm */),
			(new Item(28936)/* Inq hauberk */), (new Item(28939)/* Inq plateskirt */), (new Item(28805)/* Nightmare staff */),
			(new Item(28960)/* Volatile orb */) },
			// completion rewards
			new Item[] {
					new Item(4810, 1),
					new Item(7478, 10),
					new Item(21535, 50),
					new Item(30581, 2),
			}),
	WHISPERER(CollectionLogUpdated.Categories.BOSSES, "The Whisperer", new Item[] {
			(new Item(28276)/* Inq mace */), (new Item(30586)/* Inq great helm */),
			(new Item(28323)/* Inq hauberk */), (new Item(30521)/* Inq plateskirt */), (new Item(30522)/* Nightmare staff */),
			(new Item(30523)/* Volatile orb */), (new Item(28246)/* Eld orb */) },
			// completion rewards
			new Item[] {
					new Item(4810, 1),
					new Item(7478, 10),
					new Item(7968, 5),
					new Item(30581, 2),
			}),
	NEX(CollectionLogUpdated.Categories.BOSSES, "Nex", new Item[] {
			new Item(26348), new Item(26376), new Item(26378), new Item(26380),
			new Item(26372), new Item(26235), new Item(26370),

	},
			// completion rewards,
			new Item[] {
					new Item(30524, 1),
					new Item(30575, 1),
					new Item(7478, 20),
					new Item(30581, 1),
			}),
	OBOR(CollectionLogUpdated.Categories.BOSSES, "Obor", new Item[] { (new Item(20756)/* Hill giant club */) },
			// completion rewards
			new Item[] {
					new Item(30577, 1),
					new Item(7968, 2),
					new Item(7478, 2),
					new Item(30579, 1),
			}),
	OPHIDIA(CollectionLogUpdated.Categories.BOSSES, "Ophidia", new Item[] { (new Item(30617)/* Hill giant club */),
			new Item(30618), new Item(30619), new Item(30620), new Item(30622) },
			// completion rewards
			new Item[] {
					new Item(2528, 2),
					new Item(4810, 2),
					new Item(30456, 2),
					new Item(7478, 12),
			}),
	PHANTOM_MUSPAH(CollectionLogUpdated.Categories.BOSSES, "Phantom Muspah", new Item[] {
			(new Item(27616)/* Hill giant club */),
			new Item(27627), new Item(27614), new Item(27590) },
			// completion rewards
			new Item[] {
					new Item(2528, 2),
					new Item(4810, 2),
					new Item(30456, 2),
					new Item(7478, 12),
			}),
	SARACHNIS(CollectionLogUpdated.Categories.BOSSES, "Sarachnis",
			new Item[] { (new Item(23495)/* Sraracha */), (new Item(23525)/* Jar of eyes */),
					(new Item(23517)/* Giant egg sac */), (new Item(23528)/* Sarachnis cudgel */) },
			// completion rewards
			new Item[] {
					new Item(7478, 6),
					new Item(30459, 1),
					new Item(30580, 2),
					new Item(7968, 3),
			}),
	SCORPIA(CollectionLogUpdated.Categories.BOSSES, "Scorpia",
			new Item[] { (new Item(13181)/* Scorpia's offspring */), (new Item(11930)/* Odium shard 3 */),
					(new Item(11933)/* Mal shard 3 */) },
			// completion rewards
			new Item[] {
					new Item(7478, 2),
					new Item(30577, 1),
					new Item(7968, 5),
					new Item(30578, 1),
			}),
	SKOTIZO(CollectionLogUpdated.Categories.BOSSES, "Skotizo",
			new Item[] { (new Item(21273)/* Skotos */), (new Item(19701)/* Jar of darkness */),
					(new Item(21275)/* Dark claw */), (new Item(19685)/* Dark totem */), (new Item(6571)/* Uncut onyx */),
					(new Item(19677)/* Ancient shard */) },
			// completion rewards
			new Item[] {
					new Item(30452, 2),
					new Item(30461, 1),
					new Item(30581, 3),
					new Item(7478, 12),
			}),
	SCURRIUS(CollectionLogUpdated.Categories.BOSSES, "Scurrius",
			new Item[] { (new Item(28801)), (new Item(28798)) },
			// completion rewards
			new Item[] {
					new Item(30451, 5),
					new Item(30461, 1),
					new Item(30581, 2),
					new Item(7478, 6),
			}),
	THERMONUCLEAR_SMOKE_DEVIL(CollectionLogUpdated.Categories.BOSSES, "Thermonuclear Smoke Devil",
			new Item[] { (new Item(12648)/* Pet smoke devil */),
					(new Item(12002)/* Occult necklace */), (new Item(11998)/* Smoke battlestaff */),
					(new Item(3140)/* D chain */) },
			// completion rewards
			new Item[] {
					new Item(30452, 10),
					new Item(30542, 1),
					new Item(7478, 5),
					new Item(30458, 10),
			}),
	TORMENTED_DEMON(CollectionLogUpdated.Categories.BOSSES, "Tormented Demon",
			new Item[] { (new Item(33031)/* Pet */),
					(new Item(29580)/* Synapse */), (new Item(29574)/* claw */) },
			// completion rewards
			new Item[] {
					new Item(30462, 1),
					new Item(30459, 10),
					new Item(2528, 1),
					new Item(33020, 1)
			}),
	VENENATIS(CollectionLogUpdated.Categories.BOSSES, "Venenatis",
			new Item[] { (new Item(13177)/* Pet ven */), (new Item(12605)/* T ring */),
					(new Item(11920)/* D pickaxe */), (new Item(7158)/* D2h */) },
			// completion rewards
			new Item[] {
					new Item(30459, 1),
					new Item(7478, 5),
					new Item(30578, 1),
					new Item(30579, 1),
			}),
	VETION(CollectionLogUpdated.Categories.BOSSES, "Vet'ion",
			new Item[] { (new Item(13179)/* Vet jr */), (new Item(12601)/* Ring of the gods */),
					(new Item(11920)/* D pickaxe */), (new Item(7158)/* D2h */) },
			// completion rewards
			new Item[] {
					new Item(30459, 1),
					new Item(7478, 5),
					new Item(30578, 1),
					new Item(30579, 1),
			}),
	VORKATH(CollectionLogUpdated.Categories.BOSSES, "Vorkath", new Item[] { (new Item(21992)/* Vorki */),
			(new Item(2425)/* Vork head */),
			(new Item(11286)/* Draconic visage */), (new Item(22006)/* Skele visage */), (new Item(22106)/* Jar of decay */),
			(new Item(22111)/* Dragonbone necklace */) },
			// completion rewards
			new Item[] {
					new Item(7478, 12),
					new Item(30581, 2),
					new Item(ItemID.DRAGON_BOLTS, 5000),
					new Item(30460, 1),
			}),

	WINTERTODT(CollectionLogUpdated.Categories.MINIGAMES, "Wintertodt",
			new Item[] { (new Item(20693)/* Phoenix */), (new Item(20716)/* Tome of fire(empty) */),
					(new Item(20718)/* Burnt page */), (new Item(20704)/* Pyro top */), (new Item(20708)/* Pyro hood */),
					(new Item(20706)/* Pyro legs */), (new Item(6739)/* D axe */), (new Item(20710)/* Pyro boots */),
					(new Item(20712)/* Warm gloves */), (new Item(20720)/* Bruma torch */) },
			// completion rewards
			new Item[] {
					new Item(ItemID.BURNT_PAGE, 2500),
					new Item(30580, 1),
					new Item(7478, 9),
					new Item(30460, 1),
			}),
	YAMA(CollectionLogUpdated.Categories.BOSSES, "Yama",
			new Item[] { (new Item(30888)/* Yami */), (new Item(30759)/* Soulflame horn */),
					(new Item(30750)/* Oathplate helm */), (new Item(30753)/* Oathplate body */), (new Item(30756)/*
																																																				 * Oathplate
																																																				 * legs
																																																				 */) },
			// completion rewards
			new Item[] {
					new Item(30446),
					new Item(2528, 2),
					new Item(30575, 2),
					new Item(30456, 10),
			}),
	// TEMPOROSS(CollectionLogUpdated.Categories.MINIGAMES, "Tempoross", new Item[]{
	// new Item(25588), new Item(25578), new Item(25582),
	// new Item(25580), new Item(25559), new Item(25576),
	// new Item(ItemID.DRAGON_HARPOON), new Item(25602)
	// },
	// //completion rewards
	// new Item[]{
	// new Item(25578, 2500),
	// new Item(30580, 1),
	// new Item(7478, 9),
	// new Item(30460, 1),
	// }
	// ),
	// ZALCANO(CollectionLogUpdated.Categories.BOSSES, "Zalcano", new Item[]{(new
	// Item(23760)/*Smolcano*/), (new Item(23953)/*Crystal tool seed*/),
	// (new Item(23908)/*Zalcano shard*/), (new Item(6571)/*Uncut onyx*/)},
	// //completion rewards
	// new Item[]{
	// new Item(30581, 2),
	// new Item(7478, 6),
	// new Item(30577, 2),
	// new Item(7968, 3),
	// }
	// ),
	ZULRAH(CollectionLogUpdated.Categories.BOSSES, "Zulrah",
			new Item[] { (new Item(12921)/* Pet snake */), (new Item(13200)/* Tanz mutagen */),
					(new Item(13201)/* Magma mutagen */), (new Item(12936)/* Jar of swamp */), (new Item(12932)/* Magic fang */),
					(new Item(12927)/* Serp visage */), (new Item(12922)/* Tanz fang */), (new Item(12938)/* Zul-andra tele */),
					(new Item(6571)/* Uncut onyx */), (new Item(12934)/* Zulrah's scales */) },
			// completion rewards
			new Item[] {
					new Item(7478, 12),
					new Item(30581, 2),
					new Item(30461, 1),
					new Item(30460, 1),
			}),
	CHAMBERS_OF_XERIC(CollectionLogUpdated.Categories.RAIDS, "Chambers of Xeric", new Item[] { (new Item(20851)/*
																																																							 * Olmlet
																																																							 */),
			(new Item(20997)/* Twisted bow */), (new Item(21003)/* Elder maul */),
			(new Item(21043)/* Kodai insignia */), (new Item(13652)/* D claws */), (new Item(21018)/* Ancest hat */),
			(new Item(21021)/* Ancest robe top */), (new Item(21024)/* Ancest robe bottom */), (new Item(21015)/*
																																																					 * Dinh's
																																																					 * bulwark
																																																					 */),
			(new Item(21034)/* Dex */), (new Item(21079)/* Arc pray */), (new Item(21012)/* D hunter cbow */),
			(new Item(21000)/* Twisted buckler */), (new Item(21047)/* Torn prayer */), (new Item(21027)/* Dark relic */) },
			// completion rewards
			new Item[] {
					new Item(30530, 1),
					new Item(30573, 1),
					new Item(30461, 1),
					new Item(30570, 12),
			}),
	THEATRE_OF_BLOOD(CollectionLogUpdated.Categories.RAIDS, "Theatre of Blood", new Item[] { (new Item(22473)/*
																																																						 * Lil' zik
																																																						 */),
			(new Item(22486)/* Scythe of vitur(u) */), (new Item(22324)/* Ghrazi rapier */),
			(new Item(ItemID.SANGUINESTI_STAFF_UNCHARGED)/* Sang staff(u) */),
			(new Item(22326)/* Just faceguard */), (new Item(22327)/* Just chestguard */), (new Item(22328)/*
																																																			 * Just legguards
																																																			 */),
			(new Item(22477)/* Avernic def hilt */) },
			// completion rewards
			new Item[] {
					new Item(30529, 1),
					new Item(30573, 1),
					new Item(30572, 1),
					new Item(30570, 9),
			}),
	TOMBS_OF_AMASCUT(CollectionLogUpdated.Categories.RAIDS, "Tombs of Amascut", new Item[] {
			(new Item(Pet.TUMEKENS_GUARDIAN.itemId)/* Lil' zik */),
			(new Item(27277)/* Scythe of vitur(u) */), (new Item(27226)/* Ghrazi rapier */), (new Item(27229)/*
																																																				 * Sang staff(u)
																																																				 */),
			(new Item(27232)/* Just faceguard */), (new Item(25985)/* Just chestguard */), (new Item(26219)/*
																																																			 * Just legguards
																																																			 */),
			(new Item(25975)/* Avernic def hilt */) },
			// completion rewards
			new Item[] {
					new Item(30449, 1),
					new Item(30573, 1),
					new Item(30572, 1),
					new Item(30570, 6),
			}),
	DOMINION_OF_ECHOS(CollectionLogUpdated.Categories.RAIDS,
			"Dominion of Echoes",
			new Item[] {
					new Item(ItemID.KEEPER_OF_ECHO_PET),
					new Item(ItemID.ASGARNIA_ECHO_ORB),
					new Item(ItemID.DESERT_ECHO_ORB),
					new Item(ItemID.FREMENNIK_ECHO_ORB),
					new Item(ItemID.WILDERNESS_ECHO_ORB),
					new Item(ItemID.KANDARIN_ECHO_ORB),
					new Item(ItemID.MORYTANIA_ECHO_ORB),
					new Item(ItemID.VARLAMORE_ECHO_ORB)
			},
			// completion rewards
			new Item[] {
					new Item(30572, 1), // 5% Drop Rate Scroll
					new Item(30574, 1), // $100 Dono Scroll
					new Item(33017, 1), // Echo orb box
					new Item(30449, 1)// Advanced Dono Mystery Chest
			}),

	ALL_PETS(CollectionLogUpdated.Categories.OTHER, "All Pets", new Item[] { (new Item(13262)/* Abyssal orphan */),
			(new Item(22746)/* Ikkle hydra */), (new Item(13178)/* Callisto cub */), (new Item(13247)/* Hellpuppy */),
			(new Item(11995)/* Pet chaos ele */), (new Item(12651)/* Pet zilyana */), (new Item(12816)/* Pet dark core */),
			(new Item(12644)/* Pet dag prime */), (new Item(12643)/* Pet dag supreme */), (new Item(12645)/* Pet dag rex */),
			(new Item(13225)/* Pet jad */), (new Item(12650)/* Pet graardor */), (new Item(12646)/* Baby mole */),
			(new Item(22319)/* Jal-nib-rek */), (new Item(12647)/* Kalphite princess */),
			(new Item(12653)/* Princess black dragon */), (new Item(12655)/* Pet kraken */), (new Item(12649)/* Pet kree */),
			(new Item(12652)/* Pet kril */), (new Item(13181)/* Scorp offspring */), (new Item(21273)/* Skotos */),
			(new Item(12648)/* Pet smoke devil */), (new Item(13177)/* Ven pet */), (new Item(13179)/* Vet pet */),
			(new Item(21992)/* Vorki */), (new Item(20693)/* Phoenix */), (new Item(12921)/* Pet snakeling */),
			(new Item(20851)/* Olmlet */), (new Item(22473)/* Lil' zik */), (new Item(Pet.TUMEKENS_GUARDIAN.itemId)),
			(new Item(19730)/* Bloodhound */),
			(new Item(13320)/* Heron */), (new Item(13321)/* Rock golem */), (new Item(13071)/* Chompy */),
			(new Item(13322)/* Beaver */), (new Item(13323)/* Baby chin */), (new Item(20659)/* Baby squirrel */),
			(new Item(20661)/* Tangleroot */), (new Item(20663)/* Rocky */), (new Item(20665)/* Rift guardian */),
			(new Item(23757)/* Youngllef */), (new Item(26348)/* Nexling */), (new Item(30600)/* Awwgentavis */),
			(new Item(23495)/* Sraracha */), (new Item(30417)/* Baby Galvek */), (new Item(30617)/* Ophi the brute */),
			(new Item(24491)/* Little Nightmare */), (new Item(28252)), (new Item(28250)), (new Item(28248)),
			(new Item(28246)), (new Item(27590)), new Item(29836), new Item(28801), (new Item(21748)) /* Noon */,
			(new Item(28960)), (new Item(33006)) },
			// completion rewards
			new Item[] {
					new Item(30449, 3),
					new Item(30574, 1),
					new Item(30570, 40),
					new Item(30571, 1),
			}),
	CHAOS_DRUIDS(CollectionLogUpdated.Categories.OTHER, "Chaos Druids",
			new Item[] { (new Item(20517)/* Elder chaos top */),
					(new Item(20520)/* Elder chaos robe */), (new Item(20595)/* Elder chaos hood */) },
			// completion rewards
			new Item[] {
					new Item(30578, 1),
					new Item(7478, 4),
					new Item(30579, 1),
					new Item(7968, 3),
			}),
	CYCLOPES(CollectionLogUpdated.Categories.OTHER, "Cyclopes",
			new Item[] { (new Item(8844)/* Bronze def */), (new Item(8845)/* Iron def */),
					(new Item(8846)/* Steel def */), (new Item(8847)/* Black def */), (new Item(8848)/* Mith def */),
					(new Item(8849)/* Addy def */), (new Item(8850)/* Rune def */), (new Item(12954)/* Dragon def */) },
			// completion rewards
			new Item[] {
					new Item(7478, 4),
					new Item(30579, 2),
					new Item(7968, 3),
					new Item(30460, 1),
			}),
	GLOUGHS_EXPERIMENTS(CollectionLogUpdated.Categories.OTHER, "Glough's Experiments", new Item[] { (new Item(19529)/*
																																																									 * Zenyte
																																																									 * shard
																																																									 */),
			(new Item(19586)/* Light frame */), (new Item(19589)/* Heavy frame */), (new Item(19592)/* Ballista limbs */),
			(new Item(19610)/* Monkey tail */), (new Item(19601)/* Ballista spring */),
			(new Item(30547)/* Ballista spring */), (new Item(30546)/* Ballista spring */) },
			// completion rewards
			new Item[] {
					new Item(7478, 7),
					new Item(30460, 2),
					new Item(30570, 4),
					new Item(30577, 3),
			}),
	MOTHERLOAD_MINE(CollectionLogUpdated.Categories.OTHER, "Motherload Mine",
			new Item[] { (new Item(12019)/* Coal bag */), (new Item(12020)/* Gem bag */),
					(new Item(12013)/* Pros helmet */), (new Item(12014)/* Pros jacket */), (new Item(12015)/* Pros legs */),
					(new Item(12016)/* Pros boots */) },
			// completion rewards
			new Item[] {
					new Item(ItemID.BAG_FULL_OF_GEMS, 10),
					new Item(30580, 1),
					new Item(7968, 4),
					new Item(ItemID.COAL + 1, 5000),
			}),
	REVENANTS(CollectionLogUpdated.Categories.OTHER, "Revenants", new Item[] { (new Item(22542)/* Vig chainmace(u) */),
			(new Item(22547)/* Craw's bow(u) */),
			(new Item(22552)/* Tham's sceptre(u) */), (new Item(22557)/* Amulet of avarice */), (new Item(21817)/*
																																																					 * Brace of
																																																					 * eth(u)
																																																					 */),
			(new Item(21804)/* Ancient crystal */), (new Item(22305)/* Ancient relic */),
			(new Item(22302)/* Ancient effigy */),
			(new Item(22299)/* Ancient medallion */), (new Item(21813)/* Ancient statuette */), (new Item(21810)/*
																																																					 * Ancient
																																																					 * totem
																																																					 */),
			(new Item(21807)/* Ancient emblem */), (new Item(21802)/* Rev cave teleport */),
			(new Item(21820)/* Rev ether */) },
			// completion rewards
			new Item[] {
					new Item(30578, 5),
					new Item(7478, 20),
					new Item(30461, 3),
					new Item(30576, 1),
			}),
	REVENANT_MALEDICTUS(CollectionLogUpdated.Categories.OTHER, "Revenant Maledictus", new Item[] {
			(new Item(ItemID.VIGGORAS_CHAINMACE_U)), (new Item(ItemID.CRAWS_BOW_U)),
			(new Item(ItemID.THAMMARONS_SCEPTRE_U)), (new Item(ItemID.VESTAS_SPEAR)), (new Item(ItemID.VESTAS_LONGSWORD)),
			(new Item(ItemID.VESTAS_CHAINBODY)), (new Item(ItemID.VESTAS_PLATESKIRT)), (new Item(ItemID.STATIUSS_FULL_HELM)),
			(new Item(ItemID.STATIUSS_PLATEBODY)), (new Item(ItemID.STATIUSS_PLATELEGS)), (new Item(ItemID.ZURIELS_STAFF)),
			(new Item(ItemID.ZURIELS_HOOD)), (new Item(ItemID.ZURIELS_ROBE_TOP)), (new Item(ItemID.ZURIELS_ROBE_BOTTOM)),
			(new Item(ItemID.MORRIGANS_COIF)),
			(new Item(ItemID.MORRIGANS_LEATHER_BODY)), (new Item(ItemID.MORRIGANS_LEATHER_CHAPS)),
			(new Item(ItemID.MORRIGANS_THROWING_AXE)) },
			// completion rewards
			new Item[] {
					new Item(21820, 100000),
					new Item(23490, 30),
					new Item(22330, 2),
					new Item(30576, 1),
			}),
	ROOFTOP_AGILITY(CollectionLogUpdated.Categories.OTHER, "Rooftop Agility", new Item[] {
			(new Item(11849)/* Mark of grace */), (new Item(11850)/* Graceful hood */),
			(new Item(11852)/* Graceful cape */), (new Item(11854)/* Graceful top */),
			(new Item(11856)/* Graceful legs */), (new Item(11858)/* Graceful gloves */),
			(new Item(11860)/* Graceful boots */) },
			// completion rewards
			new Item[] {
					new Item(30580, 1),
					new Item(7478, 4),
					new Item(30460, 1),
					new Item(ItemID.MARK_OF_GRACE, 200),
			}),
	SKILLING_PETS(CollectionLogUpdated.Categories.OTHER, "Skilling Pets",
			new Item[] { (new Item(13320)/* Heron */), (new Item(13321)/* Rock golem */),
					(new Item(13322)/* Beaver */), (new Item(13323)/* Baby chin */), (new Item(20659)/* Giant squirrel */),
					(new Item(20661)/* Tangleroot */), (new Item(20663)/* Rocky */), (new Item(20665)/* Rift guardian */),
					(new Item(13071)/* Chompy */) },
			// completion rewards
			new Item[] {
					new Item(30449, 1),
					new Item(30576, 1),
					new Item(30570, 20),
					new Item(30460, 10),
			}),
	SLAYER(CollectionLogUpdated.Categories.OTHER, "Slayer",
			new Item[] { (new Item(ItemID.CRAWLING_HAND_7975)), (new Item(ItemID.COCKATRICE_HEAD)),
					(new Item(ItemID.BASILISK_HEAD)), (new Item(ItemID.KURASK_HEAD)), (new Item(ItemID.ABYSSAL_HEAD)),
					(new Item(ItemID.IMBUED_HEART_MAGIC)), (new Item(ItemID.ETERNAL_GEM)), (new Item(ItemID.DUST_BATTLESTAFF)),
					(new Item(ItemID.MIST_BATTLESTAFF)), (new Item(ItemID.ABYSSAL_WHIP)), (new Item(ItemID.GRANITE_MAUL)),
					(new Item(ItemID.BRINE_SABRE)), (new Item(ItemID.LEAFBLADED_SWORD)), (new Item(ItemID.LEAFBLADED_BATTLEAXE)),
					(new Item(ItemID.BLACK_MASK_10)), (new Item(ItemID.GRANITE_LONGSWORD)), (new Item(ItemID.GRANITE_BOOTS)),
					(new Item(ItemID.GRANITE_LEGS)), (new Item(ItemID.WYVERN_VISAGE)), (new Item(ItemID.DRACONIC_VISAGE)),
					(new Item(ItemID.BRONZE_BOOTS)), (new Item(ItemID.IRON_BOOTS)), (new Item(ItemID.STEEL_BOOTS)),
					(new Item(ItemID.BLACK_BOOTS)), (new Item(ItemID.MITHRIL_BOOTS)),
					(new Item(ItemID.ADAMANT_BOOTS)), (new Item(ItemID.RUNE_BOOTS)), (new Item(ItemID.DRAGON_BOOTS)),
					(new Item(ItemID.ABYSSAL_DAGGER)), (new Item(ItemID.UNCHARGED_TRIDENT)), (new Item(ItemID.KRAKEN_TENTACLE)),
					(new Item(ItemID.DARK_BOW)), (new Item(ItemID.OCCULT_NECKLACE)), (new Item(ItemID.DRAGON_CHAINBODY_3140)),
					(new Item(ItemID.DRAGON_THROWNAXE)), (new Item(ItemID.DRAGON_HARPOON)), (new Item(ItemID.DRAGON_SWORD)),
					(new Item(ItemID.DRAGON_KNIFE)), (new Item(ItemID.BROKEN_DRAGON_HASTA)), (new Item(ItemID.DRAKES_TOOTH)),
					(new Item(ItemID.DRAKES_CLAW)), (new Item(ItemID.HYDRA_TAIL)), (new Item(ItemID.HYDRAS_FANG)),
					(new Item(ItemID.HYDRAS_EYE)), (new Item(ItemID.HYDRAS_HEART)),
					(new Item(ItemID.MYSTIC_HAT_LIGHT)), (new Item(ItemID.MYSTIC_ROBE_TOP_LIGHT)),
					(new Item(ItemID.MYSTIC_ROBE_BOTTOM_LIGHT)), (new Item(ItemID.MYSTIC_GLOVES_LIGHT)),
					(new Item(ItemID.MYSTIC_BOOTS_LIGHT)), (new Item(ItemID.MYSTIC_HAT_DARK)),
					(new Item(ItemID.MYSTIC_ROBE_TOP_DARK)), (new Item(ItemID.MYSTIC_ROBE_BOTTOM_DARK)),
					(new Item(ItemID.MYSTIC_GLOVES_DARK)), (new Item(ItemID.MYSTIC_BOOTS_DARK)),
					(new Item(ItemID.MYSTIC_HAT_DUSK)), (new Item(ItemID.MYSTIC_ROBE_TOP_DUSK)),
					(new Item(ItemID.MYSTIC_ROBE_BOTTOM_DUSK)), (new Item(ItemID.MYSTIC_GLOVES_DUSK)),
					(new Item(ItemID.MYSTIC_BOOTS_DUSK)), (new Item(ItemID.BASILISK_JAW)), (new Item(ItemID.DAGON_HAI_HAT)),
					(new Item(ItemID.DAGON_HAI_ROBE_TOP)), (new Item(ItemID.DAGON_HAI_ROBE_BOTTOM)),
					(new Item(24777)),
			},
			// completion rewards
			new Item[] {
					new Item(30575, 1),
					new Item(30462, 2),
					new Item(30581, 2),
					new Item(30570, 20),
			}),
	TZHAAR(CollectionLogUpdated.Categories.OTHER, "TzHaar", new Item[] { (new Item(6568)/* Obby cape */),
			(new Item(6524)/* Obby shield */), (new Item(6528)/* Obby maul */), (new Item(6523)/* Obby sword */),
			(new Item(6525)/* Obby dagger */), (new Item(6526)/* Obby staff */), (new Item(6522)/* Obby rings */),
			(new Item(21298)/* Obby helm */), (new Item(21301)/* Obby platebody */), (new Item(21304)/* Obby platelegs */) },
			// completion rewards
			new Item[] {
					new Item(ItemID.TOKKUL, 1000000),
					new Item(30461, 1),
					new Item(30581, 1),
					new Item(30570, 5),
			}),
	ENHANCED_CRYSTAL_CHEST(CollectionLogUpdated.Categories.OTHER, "Enhanced Crystal Chest", new Item[] {
			(new Item(24034)/* Dstone full helm */), (new Item(24037)/* Dstone body */), (new Item(24040)/* Dstone legs */),
			(new Item(24043)/* Dstone boots */),
			(new Item(24046)/* Dstone gloves */), (new Item(23962)/* crystal shard */),
			(new Item(23911)/* Crystal crown */) },
			// completion rewards
			new Item[] {
					new Item(ItemID.ENHANCED_CRYSTAL_KEY, 50),
					new Item(30462, 1),
					new Item(30581, 1),
			}),
	BEGINNER_CLUES(CollectionLogUpdated.Categories.CLUES, "Beginner Clue Scroll Rewards", new Item[] {
			new Item(ItemID.BEAR_FEET), new Item(ItemID.MOLE_SLIPPERS), new Item(ItemID.FROG_SLIPPERS),
			new Item(ItemID.DEMON_FEET), new Item(ItemID.SANDWICH_LADY_HAT), new Item(ItemID.SANDWICH_LADY_TOP),
			new Item(ItemID.SANDWICH_LADY_BOTTOM), new Item(ItemID.RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK),
			new Item(ItemID.RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN),
			new Item(ItemID.RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX), new Item(ItemID.MONKS_ROBE_TOP_T),
			new Item(ItemID.MONKS_ROBE_T),
			new Item(ItemID.AMULET_OF_DEFENCE_T), new Item(ItemID.JESTER_CAPE), new Item(ItemID.SHOULDER_PARROT),
	},
			new Item[] {
					new Item(30460, 1),
					new Item(30579, 2),
					new Item(7478, 9),
					new Item(7968, 3),
			}),
	EASY_CLUES(CollectionLogUpdated.Categories.CLUES, "Easy Clue Scroll Rewards", new Item[] {
			new Item(ItemID.BRONZE_FULL_HELM_T), new Item(ItemID.BRONZE_PLATEBODY_T), new Item(ItemID.BRONZE_PLATELEGS_T),
			new Item(ItemID.BRONZE_PLATESKIRT_T), new Item(ItemID.BRONZE_KITESHIELD_T), new Item(ItemID.BRONZE_FULL_HELM_G),
			new Item(ItemID.BRONZE_PLATEBODY_G), new Item(ItemID.BRONZE_PLATELEGS_G), new Item(ItemID.BRONZE_PLATESKIRT_G),
			new Item(ItemID.BRONZE_KITESHIELD_G), new Item(ItemID.IRON_FULL_HELM_T), new Item(ItemID.IRON_PLATEBODY_T),
			new Item(ItemID.IRON_PLATELEGS_T), new Item(ItemID.IRON_PLATESKIRT_T), new Item(ItemID.IRON_KITESHIELD_T),
			new Item(ItemID.IRON_FULL_HELM_G), new Item(ItemID.IRON_PLATEBODY_G), new Item(ItemID.IRON_PLATELEGS_G),
			new Item(ItemID.IRON_PLATESKIRT_G), new Item(ItemID.IRON_KITESHIELD_G), new Item(ItemID.STEEL_FULL_HELM_T),
			new Item(ItemID.STEEL_PLATEBODY_T), new Item(ItemID.STEEL_PLATELEGS_T), new Item(ItemID.STEEL_PLATESKIRT_T),
			new Item(ItemID.STEEL_KITESHIELD_T), new Item(ItemID.STEEL_FULL_HELM_G), new Item(ItemID.STEEL_PLATEBODY_G),
			new Item(ItemID.STEEL_PLATELEGS_G), new Item(ItemID.STEEL_PLATESKIRT_G), new Item(ItemID.STEEL_KITESHIELD_G),
			new Item(ItemID.BLACK_FULL_HELM_T), new Item(ItemID.BLACK_PLATEBODY_T), new Item(ItemID.BLACK_PLATELEGS_T),
			new Item(ItemID.BLACK_PLATESKIRT_T), new Item(ItemID.BLACK_KITESHIELD_T), new Item(ItemID.BLACK_FULL_HELM_G),
			new Item(ItemID.BLACK_PLATEBODY_G), new Item(ItemID.BLACK_PLATELEGS_G), new Item(ItemID.BLACK_PLATESKIRT_G),
			new Item(ItemID.BLACK_KITESHIELD_G), new Item(ItemID.BLACK_BERET), new Item(ItemID.BLUE_BERET),
			new Item(ItemID.WHITE_BERET), new Item(ItemID.RED_BERET), new Item(ItemID.HIGHWAYMAN_MASK),
			new Item(ItemID.BEANIE), new Item(ItemID.BLUE_WIZARD_HAT_T), new Item(ItemID.BLUE_WIZARD_ROBE_T),
			new Item(ItemID.BLUE_SKIRT_T), new Item(ItemID.BLUE_WIZARD_HAT_G), new Item(ItemID.BLUE_WIZARD_ROBE_G),
			new Item(ItemID.BLUE_SKIRT_G), new Item(ItemID.BLACK_WIZARD_HAT_T), new Item(ItemID.BLACK_WIZARD_ROBE_T),
			new Item(ItemID.BLACK_SKIRT_T), new Item(ItemID.BLACK_WIZARD_HAT_G), new Item(ItemID.BLACK_WIZARD_ROBE_G),
			new Item(ItemID.BLACK_SKIRT_G), new Item(ItemID.STUDDED_BODY_T), new Item(ItemID.STUDDED_CHAPS_T),
			new Item(ItemID.STUDDED_BODY_G), new Item(ItemID.STUDDED_CHAPS_G), new Item(ItemID.LEATHER_BODY_G),
			new Item(ItemID.LEATHER_CHAPS_G), new Item(ItemID.BLACK_HELM_H1), new Item(ItemID.BLACK_HELM_H2),
			new Item(ItemID.BLACK_HELM_H3), new Item(ItemID.BLACK_HELM_H4), new Item(ItemID.BLACK_HELM_H5),
			new Item(ItemID.BLACK_PLATEBODY_H1), new Item(ItemID.BLACK_PLATEBODY_H2), new Item(ItemID.BLACK_PLATEBODY_H3),
			new Item(ItemID.BLACK_PLATEBODY_H4), new Item(ItemID.BLACK_PLATEBODY_H5), new Item(ItemID.BLACK_SHIELD_H1),
			new Item(ItemID.BLACK_SHIELD_H2), new Item(ItemID.BLACK_SHIELD_H3), new Item(ItemID.BLACK_SHIELD_H4),
			new Item(ItemID.BLACK_SHIELD_H5), new Item(ItemID.BLUE_ELEGANT_SHIRT), new Item(ItemID.BLUE_ELEGANT_LEGS),
			new Item(ItemID.BLUE_ELEGANT_BLOUSE), new Item(ItemID.BLUE_ELEGANT_SKIRT), new Item(ItemID.GREEN_ELEGANT_SHIRT),
			new Item(ItemID.GREEN_ELEGANT_LEGS), new Item(ItemID.GREEN_ELEGANT_BLOUSE), new Item(ItemID.GREEN_ELEGANT_SKIRT),
			new Item(ItemID.RED_ELEGANT_SHIRT), new Item(ItemID.RED_ELEGANT_LEGS), new Item(ItemID.RED_ELEGANT_BLOUSE),
			new Item(ItemID.RED_ELEGANT_SKIRT), new Item(ItemID.BOBS_RED_SHIRT), new Item(ItemID.BOBS_BLUE_SHIRT),
			new Item(ItemID.BOBS_GREEN_SHIRT), new Item(ItemID.BOBS_BLACK_SHIRT), new Item(ItemID.BOBS_PURPLE_SHIRT),
			new Item(ItemID.STAFF_OF_BOB_THE_CAT), new Item(ItemID.A_POWDERED_WIG), new Item(ItemID.FLARED_TROUSERS),
			new Item(ItemID.PANTALOONS), new Item(ItemID.SLEEPING_CAP), new Item(ItemID.AMULET_OF_MAGIC_T),
			new Item(ItemID.AMULET_OF_POWER_T), new Item(ItemID.RAIN_BOW), new Item(ItemID.HAM_JOINT),
			new Item(ItemID.BLACK_CANE), new Item(ItemID.BLACK_PICKAXE), new Item(ItemID.GUTHIX_ROBE_TOP),
			new Item(ItemID.GUTHIX_ROBE_LEGS), new Item(ItemID.SARADOMIN_ROBE_TOP), new Item(ItemID.SARADOMIN_ROBE_LEGS),
			new Item(ItemID.ZAMORAK_ROBE_TOP), new Item(ItemID.ZAMORAK_ROBE_LEGS), new Item(ItemID.ANCIENT_ROBE_TOP),
			new Item(ItemID.ANCIENT_ROBE_LEGS), new Item(ItemID.BANDOS_ROBE_TOP), new Item(ItemID.BANDOS_ROBE_LEGS),
			new Item(ItemID.ARMADYL_ROBE_TOP), new Item(ItemID.ARMADYL_ROBE_LEGS), new Item(ItemID.IMP_MASK),
			new Item(ItemID.GOBLIN_MASK), new Item(ItemID.TEAM_CAPE_I), new Item(ItemID.TEAM_CAPE_X),
			new Item(ItemID.TEAM_CAPE_ZERO), new Item(ItemID.CAPE_OF_SKULLS), new Item(ItemID.WOODEN_SHIELD_G),
			new Item(ItemID.GOLDEN_CHEFS_HAT), new Item(ItemID.GOLDEN_APRON), new Item(ItemID.MONKS_ROBE_TOP_G),
			new Item(ItemID.MONKS_ROBE_G), new Item(ItemID.LARGE_SPADE)
	},
			new Item[] {
					new Item(30460, 1),
					new Item(30461, 1),
					new Item(7478, 10),
					new Item(30570, 10),
			}),

	MEDIUM_CLUES(CollectionLogUpdated.Categories.CLUES, "Medium Clue Scroll Rewards", new Item[] {
			new Item(ItemID.RANGER_BOOTS), new Item(ItemID.HOLY_SANDALS), new Item(ItemID.SPIKED_MANACLES),
			new Item(ItemID.WIZARD_BOOTS), new Item(ItemID.CLIMBING_BOOTS_G),
			new Item(ItemID.MITHRIL_FULL_HELM_T), new Item(ItemID.MITHRIL_PLATEBODY_T), new Item(ItemID.MITHRIL_PLATELEGS_T),
			new Item(ItemID.MITHRIL_PLATESKIRT_T), new Item(ItemID.MITHRIL_KITESHIELD_T),
			new Item(ItemID.MITHRIL_FULL_HELM_G),
			new Item(ItemID.MITHRIL_PLATEBODY_G), new Item(ItemID.MITHRIL_PLATELEGS_G), new Item(ItemID.MITHRIL_PLATESKIRT_G),
			new Item(ItemID.MITHRIL_KITESHIELD_G), new Item(ItemID.ADAMANT_FULL_HELM_T), new Item(ItemID.ADAMANT_PLATEBODY_T),
			new Item(ItemID.ADAMANT_PLATELEGS_T), new Item(ItemID.ADAMANT_PLATESKIRT_T),
			new Item(ItemID.ADAMANT_KITESHIELD_T),
			new Item(ItemID.ADAMANT_FULL_HELM_G), new Item(ItemID.ADAMANT_PLATEBODY_G), new Item(ItemID.ADAMANT_PLATELEGS_G),
			new Item(ItemID.ADAMANT_PLATESKIRT_G), new Item(ItemID.ADAMANT_KITESHIELD_G),
			new Item(ItemID.BLACK_HEADBAND), new Item(ItemID.RED_HEADBAND),
			new Item(ItemID.BROWN_HEADBAND), new Item(ItemID.PINK_HEADBAND), new Item(ItemID.GREEN_HEADBAND),
			new Item(ItemID.BLUE_HEADBAND), new Item(ItemID.WHITE_HEADBAND), new Item(ItemID.GOLD_HEADBAND),
			new Item(ItemID.RED_BOATER), new Item(ItemID.ORANGE_BOATER), new Item(ItemID.GREEN_BOATER),
			new Item(ItemID.BLUE_BOATER), new Item(ItemID.BLACK_BOATER), new Item(ItemID.PINK_BOATER),
			new Item(ItemID.PURPLE_BOATER), new Item(ItemID.WHITE_BOATER), new Item(ItemID.GREEN_DHIDE_BODY_T),
			new Item(ItemID.GREEN_DHIDE_CHAPS_T), new Item(ItemID.GREEN_DHIDE_BODY_G), new Item(ItemID.GREEN_DHIDE_CHAPS_G),
			new Item(ItemID.ADAMANT_HELM_H1), new Item(ItemID.ADAMANT_HELM_H2), new Item(ItemID.ADAMANT_HELM_H3),
			new Item(ItemID.ADAMANT_HELM_H4), new Item(ItemID.ADAMANT_HELM_H5), new Item(ItemID.ADAMANT_PLATEBODY_H1),
			new Item(ItemID.ADAMANT_PLATEBODY_H2), new Item(ItemID.ADAMANT_PLATEBODY_H3),
			new Item(ItemID.ADAMANT_PLATEBODY_H4),
			new Item(ItemID.ADAMANT_PLATEBODY_H5), new Item(ItemID.ADAMANT_SHIELD_H1), new Item(ItemID.ADAMANT_SHIELD_H2),
			new Item(ItemID.ADAMANT_SHIELD_H3), new Item(ItemID.ADAMANT_SHIELD_H4), new Item(ItemID.ADAMANT_SHIELD_H5),
			new Item(ItemID.BLACK_ELEGANT_SHIRT), new Item(ItemID.BLACK_ELEGANT_LEGS), new Item(ItemID.WHITE_ELEGANT_BLOUSE),
			new Item(ItemID.WHITE_ELEGANT_SKIRT), new Item(ItemID.PURPLE_ELEGANT_SHIRT), new Item(ItemID.PURPLE_ELEGANT_LEGS),
			new Item(ItemID.PURPLE_ELEGANT_BLOUSE), new Item(ItemID.PURPLE_ELEGANT_SKIRT),
			new Item(ItemID.PINK_ELEGANT_SHIRT),
			new Item(ItemID.PINK_ELEGANT_LEGS), new Item(ItemID.PINK_ELEGANT_BLOUSE), new Item(ItemID.PINK_ELEGANT_SKIRT),
			new Item(ItemID.GOLD_ELEGANT_SHIRT), new Item(ItemID.GOLD_ELEGANT_LEGS), new Item(ItemID.GOLD_ELEGANT_BLOUSE),
			new Item(ItemID.GOLD_ELEGANT_SKIRT), new Item(ItemID.WOLF_CLOAK), new Item(ItemID.WOLF_MASK),
			new Item(ItemID.STRENGTH_AMULET_T), new Item(ItemID.ADAMANT_CANE), new Item(ItemID.GUTHIX_MITRE),
			new Item(ItemID.SARADOMIN_MITRE), new Item(ItemID.ZAMORAK_MITRE), new Item(ItemID.BANDOS_MITRE),
			new Item(ItemID.ARMADYL_MITRE), new Item(ItemID.GUTHIX_CLOAK), new Item(ItemID.SARADOMIN_CLOAK),
			new Item(ItemID.ZAMORAK_CLOAK), new Item(ItemID.BANDOS_CLOAK), new Item(ItemID.ARMADYL_CLOAK),
			new Item(ItemID.ANCIENT_STOLE), new Item(ItemID.ARMADYL_STOLE), new Item(ItemID.BANDOS_STOLE),
			new Item(ItemID.ANCIENT_CROZIER), new Item(ItemID.ARMADYL_CROZIER), new Item(ItemID.BANDOS_CROZIER),
			new Item(ItemID.CAT_MASK), new Item(ItemID.PENGUIN_MASK), new Item(ItemID.GNOMISH_FIRELIGHTER),
			new Item(ItemID.CRIER_HAT), new Item(ItemID.CRIER_BELL), new Item(ItemID.CRIER_COAT),
			new Item(ItemID.LEPRECHAUN_HAT), new Item(ItemID.BLACK_LEPRECHAUN_HAT), new Item(ItemID.BLACK_UNICORN_MASK),
			new Item(ItemID.WHITE_UNICORN_MASK), new Item(ItemID.ARCEUUS_BANNER), new Item(ItemID.PISCARILIUS_BANNER),
			new Item(ItemID.LOVAKENGJ_BANNER), new Item(ItemID.SHAYZIEN_BANNER), new Item(ItemID.HOSIDIUS_BANNER),
			new Item(ItemID.CABBAGE_ROUND_SHIELD), new Item(ItemID.CLUELESS_SCROLL),
	},
			new Item[] {
					new Item(20545, 25),
					new Item(7478, 15),
					new Item(30570, 10),
					new Item(30461, 1),
			}),

	HARD_CLUES(CollectionLogUpdated.Categories.CLUES, "Hard Clue Scroll Rewards", new Item[] {
			new Item(ItemID.RUNE_FULL_HELM_T), new Item(ItemID.RUNE_PLATEBODY_T), new Item(ItemID.RUNE_PLATELEGS_T),
			new Item(ItemID.RUNE_PLATESKIRT_T), new Item(ItemID.RUNE_KITESHIELD_T), new Item(ItemID.RUNE_FULL_HELM_G),
			new Item(ItemID.RUNE_PLATEBODY_G), new Item(ItemID.RUNE_PLATELEGS_G), new Item(ItemID.RUNE_PLATESKIRT_G),
			new Item(ItemID.RUNE_KITESHIELD_G), new Item(ItemID.GUTHIX_FULL_HELM), new Item(ItemID.GUTHIX_PLATEBODY),
			new Item(ItemID.GUTHIX_PLATELEGS), new Item(ItemID.GUTHIX_PLATESKIRT), new Item(ItemID.GUTHIX_KITESHIELD),
			new Item(ItemID.SARADOMIN_FULL_HELM), new Item(ItemID.SARADOMIN_PLATEBODY), new Item(ItemID.SARADOMIN_PLATELEGS),
			new Item(ItemID.SARADOMIN_PLATESKIRT), new Item(ItemID.SARADOMIN_KITESHIELD), new Item(ItemID.ZAMORAK_FULL_HELM),
			new Item(ItemID.ZAMORAK_PLATEBODY), new Item(ItemID.ZAMORAK_PLATELEGS), new Item(ItemID.ZAMORAK_PLATESKIRT),
			new Item(ItemID.ZAMORAK_KITESHIELD), new Item(ItemID.ANCIENT_FULL_HELM), new Item(ItemID.ANCIENT_PLATEBODY),
			new Item(ItemID.ANCIENT_PLATELEGS), new Item(ItemID.ANCIENT_PLATESKIRT), new Item(ItemID.ANCIENT_KITESHIELD),
			new Item(ItemID.BANDOS_FULL_HELM), new Item(ItemID.BANDOS_PLATEBODY), new Item(ItemID.BANDOS_PLATELEGS),
			new Item(ItemID.BANDOS_PLATESKIRT), new Item(ItemID.BANDOS_KITESHIELD), new Item(ItemID.ARMADYL_FULL_HELM),
			new Item(ItemID.ARMADYL_PLATEBODY), new Item(ItemID.ARMADYL_PLATELEGS), new Item(ItemID.ARMADYL_PLATESKIRT),
			new Item(ItemID.ARMADYL_KITESHIELD), new Item(ItemID.RUNE_HELM_H1), new Item(ItemID.RUNE_HELM_H2),
			new Item(ItemID.RUNE_HELM_H3), new Item(ItemID.RUNE_HELM_H4), new Item(ItemID.RUNE_HELM_H5),
			new Item(ItemID.RUNE_PLATEBODY_H1), new Item(ItemID.RUNE_PLATEBODY_H2), new Item(ItemID.RUNE_PLATEBODY_H3),
			new Item(ItemID.RUNE_PLATEBODY_H4), new Item(ItemID.RUNE_PLATEBODY_H5), new Item(ItemID.RUNE_SHIELD_H1),
			new Item(ItemID.RUNE_SHIELD_H2), new Item(ItemID.RUNE_SHIELD_H3), new Item(ItemID.RUNE_SHIELD_H4),
			new Item(ItemID.RUNE_SHIELD_H5), new Item(ItemID.BLUE_DHIDE_BODY_T), new Item(ItemID.BLUE_DHIDE_CHAPS_T),
			new Item(ItemID.BLUE_DHIDE_BODY_G), new Item(ItemID.BLUE_DHIDE_CHAPS_G), new Item(ItemID.RED_DHIDE_BODY_T),
			new Item(ItemID.RED_DHIDE_CHAPS_T), new Item(ItemID.RED_DHIDE_BODY_G), new Item(ItemID.RED_DHIDE_CHAPS_G),
			new Item(ItemID.ENCHANTED_HAT), new Item(ItemID.ENCHANTED_TOP), new Item(ItemID.ENCHANTED_ROBE),
			new Item(ItemID.ROBIN_HOOD_HAT), new Item(ItemID.TAN_CAVALIER), new Item(ItemID.DARK_CAVALIER),
			new Item(ItemID.BLACK_CAVALIER), new Item(ItemID.WHITE_CAVALIER), new Item(ItemID.RED_CAVALIER),
			new Item(ItemID.PIRATE_HAT), new Item(ItemID.AMULET_OF_GLORY_T), new Item(ItemID.GUTHIX_COIF),
			new Item(ItemID.GUTHIX_DHIDE), new Item(ItemID.GUTHIX_CHAPS), new Item(ItemID.GUTHIX_BRACERS),
			new Item(ItemID.GUTHIX_DHIDE_BOOTS), new Item(ItemID.GUTHIX_DHIDE_SHIELD), new Item(ItemID.SARADOMIN_COIF),
			new Item(ItemID.SARADOMIN_DHIDE), new Item(ItemID.SARADOMIN_CHAPS), new Item(ItemID.SARADOMIN_BRACERS),
			new Item(ItemID.SARADOMIN_DHIDE_BOOTS), new Item(ItemID.SARADOMIN_DHIDE_SHIELD), new Item(ItemID.ZAMORAK_COIF),
			new Item(ItemID.ZAMORAK_DHIDE), new Item(ItemID.ZAMORAK_CHAPS), new Item(ItemID.ZAMORAK_BRACERS),
			new Item(ItemID.ZAMORAK_DHIDE_BOOTS), new Item(ItemID.ZAMORAK_DHIDE_SHIELD), new Item(ItemID.ARMADYL_COIF),
			new Item(ItemID.ARMADYL_DHIDE), new Item(ItemID.ARMADYL_CHAPS), new Item(ItemID.ARMADYL_BRACERS),
			new Item(ItemID.ARMADYL_DHIDE_BOOTS), new Item(ItemID.ARMADYL_DHIDE_SHIELD), new Item(ItemID.ANCIENT_COIF),
			new Item(ItemID.ANCIENT_DHIDE), new Item(ItemID.ANCIENT_CHAPS), new Item(ItemID.ANCIENT_BRACERS),
			new Item(ItemID.ANCIENT_DHIDE_BOOTS), new Item(ItemID.ANCIENT_DHIDE_SHIELD), new Item(ItemID.BANDOS_COIF),
			new Item(ItemID.BANDOS_DHIDE), new Item(ItemID.BANDOS_CHAPS), new Item(ItemID.BANDOS_BRACERS),
			new Item(ItemID.BANDOS_DHIDE_BOOTS), new Item(ItemID.BANDOS_DHIDE_SHIELD), new Item(ItemID.GUTHIX_STOLE),
			new Item(ItemID.SARADOMIN_STOLE), new Item(ItemID.ZAMORAK_STOLE), new Item(ItemID.GUTHIX_CROZIER),
			new Item(ItemID.SARADOMIN_CROZIER), new Item(ItemID.ZAMORAK_CROZIER), new Item(ItemID.GREEN_DRAGON_MASK),
			new Item(ItemID.BLUE_DRAGON_MASK), new Item(ItemID.RED_DRAGON_MASK), new Item(ItemID.BLACK_DRAGON_MASK),
			new Item(ItemID.PITH_HELMET), new Item(ItemID.EXPLORER_BACKPACK), new Item(ItemID.RUNE_CANE),
			new Item(ItemID.ZOMBIE_HEAD_19912), new Item(ItemID.CYCLOPS_HEAD), new Item(ItemID.NUNCHAKU),
			new Item(ItemID.DUAL_SAI), new Item(ItemID.THIEVING_BAG), new Item(ItemID.TZHAARKETOM_ORNAMENT_KIT),
			new Item(ItemID.BERSERKER_NECKLACE_ORNAMENT_KIT), new Item(ItemID.DRAGON_BOOTS_ORNAMENT_KIT),
			new Item(ItemID.RUNE_DEFENDER_ORNAMENT_KIT),

	},
			new Item[] {
					new Item(20544, 25),
					new Item(30573, 1),
					new Item(4810, 1),
					new Item(30570, 10),
			}),

	ELITE_CLUES(CollectionLogUpdated.Categories.CLUES, "Elite Clue Scroll Rewards", new Item[] {
			new Item(ItemID.DRAGON_FULL_HELM_ORNAMENT_KIT), new Item(ItemID.DRAGON_CHAINBODY_ORNAMENT_KIT),
			new Item(ItemID.DRAGON_LEGSSKIRT_ORNAMENT_KIT),
			new Item(ItemID.DRAGON_SQ_SHIELD_ORNAMENT_KIT), new Item(ItemID.DRAGON_SCIMITAR_ORNAMENT_KIT),
			new Item(ItemID.FURY_ORNAMENT_KIT),
			new Item(ItemID.LIGHT_INFINITY_COLOUR_KIT), new Item(ItemID.DARK_INFINITY_COLOUR_KIT),
			new Item(ItemID.ROYAL_CROWN),
			new Item(ItemID.ROYAL_GOWN_TOP), new Item(ItemID.ROYAL_GOWN_BOTTOM), new Item(ItemID.ROYAL_SCEPTRE),
			new Item(ItemID.MUSKETEER_HAT), new Item(ItemID.MUSKETEER_TABARD), new Item(ItemID.MUSKETEER_PANTS),
			new Item(ItemID.BLACK_DHIDE_BODY_T), new Item(ItemID.BLACK_DHIDE_CHAPS_T), new Item(ItemID.BLACK_DHIDE_BODY_G),
			new Item(ItemID.BLACK_DHIDE_CHAPS_G), new Item(ItemID.RANGERS_TUNIC), new Item(ItemID.RANGER_GLOVES),
			new Item(ItemID.HOLY_WRAPS), new Item(ItemID.BRONZE_DRAGON_MASK), new Item(ItemID.IRON_DRAGON_MASK),
			new Item(ItemID.STEEL_DRAGON_MASK), new Item(ItemID.MITHRIL_DRAGON_MASK), new Item(ItemID.ADAMANT_DRAGON_MASK),
			new Item(ItemID.RUNE_DRAGON_MASK), new Item(ItemID.ARCEUUS_SCARF), new Item(ItemID.HOSIDIUS_SCARF),
			new Item(ItemID.LOVAKENGJ_SCARF), new Item(ItemID.PISCARILIUS_SCARF), new Item(ItemID.SHAYZIEN_SCARF),
			new Item(ItemID.KATANA), new Item(ItemID.DRAGON_CANE), new Item(ItemID.BUCKET_HELM),
			new Item(ItemID.BLACKSMITHS_HELM), new Item(ItemID.DEERSTALKER), new Item(ItemID.AFRO),
			new Item(ItemID.BIG_PIRATE_HAT), new Item(ItemID.TOP_HAT), new Item(ItemID.MONOCLE),
			new Item(ItemID.BRIEFCASE), new Item(ItemID.SAGACIOUS_SPECTACLES), new Item(ItemID.RANGERS_TIGHTS),
			new Item(ItemID.URIS_HAT), new Item(ItemID.GIANT_BOOT), new Item(ItemID.FREMENNIK_KILT),
			new Item(ItemID.DARK_BOW_TIE), new Item(ItemID.DARK_TUXEDO_JACKET), new Item(ItemID.DARK_TUXEDO_CUFFS),
			new Item(ItemID.DARK_TROUSERS), new Item(ItemID.DARK_TUXEDO_SHOES), new Item(ItemID.LIGHT_BOW_TIE),
			new Item(ItemID.LIGHT_TUXEDO_JACKET), new Item(ItemID.LIGHT_TUXEDO_CUFFS), new Item(ItemID.LIGHT_TROUSERS),
			new Item(ItemID.LIGHT_TUXEDO_SHOES)

	},
			new Item[] {
					new Item(20543, 25),
					new Item(30573, 1),
					new Item(4810, 2),
					new Item(30570, 10),
			}),

	MASTER_CLUES(CollectionLogUpdated.Categories.CLUES, "Master Clue Scroll Rewards", new Item[] {
			new Item(ItemID.BLOODHOUND), new Item(ItemID.OCCULT_ORNAMENT_KIT), new Item(ItemID.TORTURE_ORNAMENT_KIT),
			new Item(ItemID.ANGUISH_ORNAMENT_KIT),
			new Item(ItemID.TORMENTED_ORNAMENT_KIT), new Item(ItemID.DRAGON_DEFENDER_ORNAMENT_KIT),
			new Item(ItemID.HOOD_OF_DARKNESS),
			new Item(ItemID.ROBE_TOP_OF_DARKNESS), new Item(ItemID.GLOVES_OF_DARKNESS),
			new Item(ItemID.ROBE_BOTTOM_OF_DARKNESS),
			new Item(ItemID.BOOTS_OF_DARKNESS), new Item(ItemID.SAMURAI_KASA), new Item(ItemID.SAMURAI_SHIRT),
			new Item(ItemID.SAMURAI_GLOVES), new Item(ItemID.SAMURAI_GREAVES), new Item(ItemID.SAMURAI_BOOTS),
			new Item(ItemID.ARCEUUS_HOOD), new Item(ItemID.HOSIDIUS_HOOD), new Item(ItemID.LOVAKENGJ_HOOD),
			new Item(ItemID.PISCARILIUS_HOOD), new Item(ItemID.SHAYZIEN_HOOD), new Item(ItemID.OLD_DEMON_MASK),
			new Item(ItemID.LESSER_DEMON_MASK), new Item(ItemID.GREATER_DEMON_MASK), new Item(ItemID.BLACK_DEMON_MASK),
			new Item(ItemID.JUNGLE_DEMON_MASK), new Item(ItemID.LEFT_EYE_PATCH), new Item(ItemID.BOWL_WIG),
			new Item(ItemID.ALE_OF_THE_GODS), new Item(ItemID.OBSIDIAN_CAPE_R), new Item(ItemID.FANCY_TIARA),
			new Item(ItemID.HALF_MOON_SPECTACLES), new Item(ItemID.ARMADYL_GODSWORD_ORNAMENT_KIT),
			new Item(ItemID.BANDOS_GODSWORD_ORNAMENT_KIT),
			new Item(ItemID.SARADOMIN_GODSWORD_ORNAMENT_KIT), new Item(ItemID.ZAMORAK_GODSWORD_ORNAMENT_KIT),
			new Item(ItemID.DRAGON_PLATEBODY_ORNAMENT_KIT),
			new Item(ItemID.ANKOU_MASK), new Item(ItemID.ANKOU_TOP), new Item(ItemID.ANKOUS_LEGGINGS),
			new Item(ItemID.ANKOU_SOCKS), new Item(ItemID.ANKOU_GLOVES), new Item(ItemID.MUMMYS_HEAD),
			new Item(ItemID.MUMMYS_BODY), new Item(ItemID.MUMMYS_LEGS), new Item(ItemID.MUMMYS_FEET),
			new Item(ItemID.MUMMYS_HANDS), new Item(ItemID.DRAGON_KITESHIELD_ORNAMENT_KIT)

	},
			new Item[] {
					new Item(19836, 25),
					new Item(30573, 1),
					new Item(30446, 2),
					new Item(30570, 10),
			}),

	CLUE_MEGA_RARE(CollectionLogUpdated.Categories.CLUES, "Clue Scroll Mega Rare Rewards", new Item[] {
			new Item(ItemID.BUCKET_HELM_G), new Item(ItemID.GILDED_FULL_HELM), new Item(ItemID.GILDED_PLATEBODY),
			new Item(ItemID.GILDED_PLATELEGS), new Item(ItemID.GILDED_PLATESKIRT), new Item(ItemID.GILDED_KITESHIELD),
			new Item(ItemID.GILDED_BOOTS), new Item(ItemID.GILDED_MED_HELM), new Item(ItemID.GILDED_SQ_SHIELD),
			new Item(ItemID.GILDED_CHAINBODY), new Item(ItemID.GILDED_SCIMITAR),
			new Item(ItemID.GILDED_2H_SWORD), new Item(ItemID.GILDED_HASTA), new Item(ItemID.GILDED_SPEAR),
			new Item(ItemID.GILDED_AXE), new Item(ItemID.GILDED_PICKAXE), new Item(ItemID.GILDED_SPADE),
			new Item(ItemID.GILDED_COIF), new Item(ItemID.GILDED_DHIDE_BODY), new Item(ItemID.GILDED_DHIDE_CHAPS),
			new Item(ItemID.GILDED_DHIDE_VAMBS), new Item(ItemID._3RD_AGE_FULL_HELMET), new Item(ItemID._3RD_AGE_PLATEBODY),
			new Item(ItemID._3RD_AGE_PLATELEGS), new Item(ItemID._3RD_AGE_PLATESKIRT), new Item(ItemID._3RD_AGE_KITESHIELD),
			new Item(ItemID._3RD_AGE_LONGSWORD), new Item(ItemID._3RD_AGE_MAGE_HAT), new Item(ItemID._3RD_AGE_ROBE_TOP),
			new Item(ItemID._3RD_AGE_ROBE), new Item(ItemID._3RD_AGE_AMULET), new Item(ItemID._3RD_AGE_WAND),
			new Item(ItemID._3RD_AGE_RANGE_COIF), new Item(ItemID._3RD_AGE_RANGE_TOP), new Item(ItemID._3RD_AGE_RANGE_LEGS),
			new Item(ItemID._3RD_AGE_VAMBRACES), new Item(ItemID._3RD_AGE_BOW), new Item(ItemID._3RD_AGE_DRUIDIC_ROBE_TOP),
			new Item(ItemID._3RD_AGE_DRUIDIC_ROBE_BOTTOMS), new Item(ItemID._3RD_AGE_DRUIDIC_CLOAK),
			new Item(ItemID._3RD_AGE_DRUIDIC_STAFF),
			new Item(ItemID._3RD_AGE_AXE), new Item(ItemID._3RD_AGE_PICKAXE), new Item(ItemID._3RD_AGE_CLOAK),
			new Item(ItemID.RING_OF_3RD_AGE)

	},
			new Item[] {
					new Item(30497, 1),
					new Item(30574, 1),
					new Item(30449, 2),
					new Item(19687, 1),
			}),

	;

	final Item[] uniqueItems;
	final String name;
	final CollectionLogUpdated.Categories categories;
	final Item[] completionRewards;

	CollectionLogData(CollectionLogUpdated.Categories categories, String name, Item[] uniqueItems,
			Item[] completionRewards) {
		this.uniqueItems = uniqueItems;
		this.name = name;
		this.categories = categories;
		this.completionRewards = completionRewards;
	}

	public static final CollectionLogData[] VALUES = values();

	public Item[] getUniqueItems() {
		return uniqueItems;
	}

	public static boolean isCollectionLogSlotItem(Item item) {
		for (CollectionLogData data : CollectionLogData.values()) {
			for (Item uniqueItem : data.getUniqueItems()) {
				if (uniqueItem.getId() == item.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	public CollectionLogUpdated.Categories getCategories() {
		return categories;
	}
}
