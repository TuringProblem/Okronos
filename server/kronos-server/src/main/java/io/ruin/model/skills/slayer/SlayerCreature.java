package io.ruin.model.skills.slayer;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.skills.slayer.master.Duradel;
import io.ruin.model.skills.slayer.master.Konar;
import io.ruin.model.skills.slayer.master.Nieve;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.TeleportConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
public enum SlayerCreature {
	MONKEYS(1, 1, 1, "monkey", "Shilo Village", TeleportConstants.SLAYERMONKIES),
	GOBLINS(2, 1, 1, "goblin", "Lumbridge Goblins", TeleportConstants.SLAYERGOBLIN),
	RATS(3, 1, 1, "rat", "Misthalin Underground", TeleportConstants.SLAYERRATS),
	SPIDERS(4, 1, 1, "spider", "Stronghold of Security", TeleportConstants.SLAYERSPIDERS),
	BIRDS(5, 1, 1, "bird", "Crash Island", TeleportConstants.SLAYERBIRDS),
	COWS(6, 1, 5, "cow", "Lumbridge Cows", TeleportConstants.SLAYERCOWS),
	SCORPIONS(7, 1, 7, "scorpion", "Dwarven Mines", TeleportConstants.SLAYERSCORPS),
	BATS(8, 1, 5, "bat", "Slayer Tower", TeleportConstants.SLAYERBATS),
	WOLVES(9, 1, 20, "wolf", "Feldip Hills", TeleportConstants.SLAYERWOLF),
	ZOMBIES(10, 1, 10, "zombie", "Graveyard of Shadows", TeleportConstants.SLAYERZOMBIE), // wilderness warning
	SKELETONS(11, 1, 15, "skeleton", "Wilderness Mines", TeleportConstants.SLAYERSKELE), // wilderness warning
	GHOSTS(12, 1, 13, "ghost", "Kourend Underground", TeleportConstants.SLAYERGHOST),
	BEARS(13, 1, 13, "bear", "Legends Guild Mine", TeleportConstants.SLAYERBEARS),
	HILL_GIANTS(14, 1, 25, "hill giant", "Shayzien", TeleportConstants.SLAYERHILLGIANTS),
	ICE_GIANT(15, 1, 50, "ice giant", "Level 47 Wilderness", TeleportConstants.SLAYERICEGIANTS), // wilderness warning
	FIRE_GIANTS(16, 1, 65, "fire giant", "Karuulm Slayer Dungeon", TeleportConstants.SLAYERFIREGIANTS),
	MOSS_GIANTS(17, 1, 40, "moss giant", "Kourend Underground", TeleportConstants.SLAYERMOSSGIANTS),
	TROLLS(18, 1, 60, "troll", "Death Plateau", TeleportConstants.SLAYERTROLL),
	ICE_WARRIOR(19, 1, 45, "ice warrior", "Asgarnia Ice Cave", TeleportConstants.SLAYERICEWARRIOR),
	OGRES(20, 1, 40, "ogre", "Combat Training Camp", TeleportConstants.SLAYEROGRES),
	DOG(22, 1, 15, "dog"),
	GHOUL(23, 1, 25, "ghoul"),
	HOBGOBLIN(21, 1, 20, "hobgoblin", "Level 31 Wilderness", TeleportConstants.SLAYERHOBGOBLINS), // wilderness warning
	GREEN_DRAGONS(24, 1, 52, "green dragon", "Level 23 Wilderness", TeleportConstants.SLAYERGREENDRAGS), // wilderness
																																																				// warning
	BLUE_DRAGONS(25, 1, 65, "blue dragon", "Taverly Dungeon", TeleportConstants.SLAYERBLUEDRAGS),
	RED_DRAGONS(26, 1, 68, "red dragon", "Level 48 Wilderness", TeleportConstants.SLAYER1), // wilderness warning
	BLACK_DRAGONS(27, 77, 80, "black dragon", "Taverley Underground", TeleportConstants.SLAYERBLACKDRAGONS),
	LESSER_DEMONS(28, 1, 60, "lesser demon", "Chasm of fire", TeleportConstants.SLAYERLESSER),
	GREATER_DEMONS(29, 1, 75, "greater demon", "Chasm of fire", TeleportConstants.SLAYERGREATER),
	BLACK_DEMONS(30, 1, 80, "black demon", "Chasm of fire", TeleportConstants.SLAYERBLACKDEMON),
	HELLHOUNDS(31, 1, 75, "hellhound", "Taverly Dungeon", TeleportConstants.SLAYERHELLHOUND),
	SHADOW_WARRIOR(32, 1, 60, "shadow warrior", "", TeleportConstants.SLAYER1), // wilderness warning
	WEREWOLVES(33, 1, 60, "werewolf", "Canifis", TeleportConstants.SLAYER1),
	VAMPYRE(34, 1, 35, "vampyre", "", TeleportConstants.SLAYER1), // wilderness warning
	DAGANNOTH(35, 1, 75, "dagannoth", "Waterbirth Dungeon", TeleportConstants.SLAYER1),
	TUROTH(36, 55, 60, "turoth", "Fremennik Slayer Cave", TeleportConstants.SLAYER1),
	CAVE_CRAWLER(37, 10, 10, "cave crawler", "Fremennik Slayer Cave", TeleportConstants.SLAYER1),
	BANSHEE(38, 15, 20, "banshee", "Slayer Tower", TeleportConstants.SLAYER1),
	CRAWLING_HANDS(39, 5, 10, "crawling hand", "Slayer Tower", TeleportConstants.SLAYER1),
	INFERNAL_MAGE(40, 45, 40, "infernal mage", "Chasm of fire", TeleportConstants.SLAYER1),
	ABERRANT_SPECTRES(41, 60, 65, "aberrant spectre", "Gnome Stronghold", TeleportConstants.SLAYER1),
	ABYSSAL_DEMON(42, 85, 85, "abyssal demon", "Catacombs, Slayer Tower", TeleportConstants.SLAYER1),
	BASILISKS(43, 40, 40, "basilisk",
			(p, s) -> s.getNpcId() == Konar.KONAR || s.getNpcId() == Duradel.DURADEL || s.getNpcId() == Nieve.NIEVE),
	COCKATRICE(44, 25, 25, "cockatrice", "Fremennik Slayer Cave", TeleportConstants.SLAYER1),
	KURASK(45, 70, 65, "kurask", "Fremennik Slayer Cave", TeleportConstants.SLAYER1),
	GARGOYLE(46, 75, 80, "gargoyle", "Slayer Tower", TeleportConstants.SLAYER1),
	PYREFIEND(47, 30, 25, "pyrefiend", "Smoke Dungeon", TeleportConstants.SLAYER1),
	BLOODVELDS(48, 50, 50, "bloodveld", "Slayer Tower", TeleportConstants.SLAYER1),
	DUST_DEVILS(49, 65, 70, "dust devil", "Smoke Dungeon", TeleportConstants.SLAYER1),
	JELLIES(50, 52, 57, "jelly", "Fremennik Slayer Dungeon", TeleportConstants.SLAYER1),
	ROCKSLUG(51, 20, 20, "rockslug", "Fremennik Slayer Dungeon", TeleportConstants.SLAYER1),
	NECHRYAEL(52, 80, 85, "nechryael", "Slayer Dungeon", TeleportConstants.SLAYER1),
	KALPHITE(53, 1, 15, "kalphite", "Kalphite Lair", TeleportConstants.KALPHITE_QUEEN),
	EARTH_WARRIORS(54, 1, 35, "earth warrior", "Misthalin Underground", TeleportConstants.SLAYER1), // wilderness warning
	OTHERWORLDLY_BEINGS(55, 1, 40, "otherworldly being", "Slayer Tower", TeleportConstants.SLAYER1),
	ELVES(56, 1, 70, "elves", "Slayer Tower", TeleportConstants.SLAYERELVES),
	DWARVES(57, 1, 6, "dwarf", "Dwarven Mines", TeleportConstants.SLAYER1),
	BRONZE_DRAGONS(58, 1, 75, "bronze dragon", "Karamja Underground", TeleportConstants.SLAYER1),
	IRON_DRAGONS(59, 1, 80, "iron dragon", "Karamja Underground", TeleportConstants.SLAYER1),
	STEEL_DRAGONS(60, 1, 85, "steel dragon", "Karamja Underground", TeleportConstants.SLAYER1),
	WALL_BEAST(61, 35, 30, "wall beast", "Lumbridge Swamp Caves", TeleportConstants.SLAYER1),
	CAVE_SLIME(62, 17, 15, "cave slime", "", TeleportConstants.SLAYER1),
	CAVE_BUG(63, 7, 1, "cave bug", "", TeleportConstants.SLAYER1),
	SHADE(64, 1, 30, "shade", "", TeleportConstants.SLAYER1),
	CROCODILE(65, 1, 50, "crocodile", "", TeleportConstants.SLAYER1),
	DARK_BEASTS(66, 90, 90, "dark beast", "Iowerth Dungeon", TeleportConstants.SLAYER1), // TODO no idea pls fix
	LIZARD(68, 22, 1, "lizard", "", TeleportConstants.SLAYER1),
	FEVER_SPIDER(69, 42, 40, "fever spider", "", TeleportConstants.SLAYER1),
	HARPIE_BUG_SWARM(70, 33, 45, "harpie bug swarm", "", TeleportConstants.SLAYER1),
	SEA_SNAKE(71, 40, 50, "sea snake", "", TeleportConstants.SLAYER1),
	WYVERN(72, 72, 70, "skeletal wyvern", "Asgarnia Ice Cave", TeleportConstants.SLAYER1),
	KILLERWATT(73, 37, 50, "Killerwatt", "", TeleportConstants.SLAYER1),
	MUTATED_ZYGOMITES(74, 57, 60, "mutated zygomites", "", TeleportConstants.SLAYER1),
	ICEFIENDS(75, 1, 20, "icefiend", "Ice Mountain", TeleportConstants.SLAYER1),
	MINOTAURS(76, 1, 7, "minotaur", "Stronghold of Security", TeleportConstants.SLAYER1),
	FLESH_CRAWLER(77, 1, 15, "flesh crawler", "Stronghold of Security", TeleportConstants.SLAYER1),
	CATABLEPON(78, 34, 20, "catablepon", "Stronghold of Security", TeleportConstants.SLAYER1),
	ANKOUS(79, 1, 40, "ankou", "Gnome stronghold", TeleportConstants.SLAYER1),
	CAVE_HORRORS(80, 58, 85, "cave horror", "Mos Le'Harmless Cave", TeleportConstants.SLAYER1),
	JUNGLE_HORROR(81, 1, 65, "jungle horror", "", TeleportConstants.SLAYER1),
	DESERT_LIZARD(82, 22, 30, "desert lizard", "", TeleportConstants.SLAYER1),
	SUQAH(83, 1, 1, "suqah", "", TeleportConstants.SLAYER1),
	BRINE_RATE(84, 47, 45, "brine rate", "", TeleportConstants.SLAYER1),
	MINIONS_OF_SCABARAS(85, 1, 85, "minions of scabaras"),
	TERROR_DOGS(86, 40, 60, "terror dog", "", TeleportConstants.SLAYER1),
	MOLANISK(87, 39, 50, "molanisk", "", TeleportConstants.SLAYER1),
	WATERFIEND(88, 1, 75, "waterfiend", "", TeleportConstants.SLAYER1),
	SPIRITUAL_CREATURES(89, 63, 60, "spiritual creatures", "Godwars Dungeon", TeleportConstants.GODWARS_DUNGEON),
	LIZARDMEN(90, 1, 45, "lizardmen", (p, s) -> VarPlayerRepository.REPTILE_GOT_RIPPED.get(p) != 0),
	MAGIC_AXES(91, 1, 1, "magic axe", "Level 56 Wilderness", TeleportConstants.SLAYER1),
	CAVE_KRAKENS(92, 87, 80, "cave kraken", "Kraken Cave", TeleportConstants.KRAKEN),
	MITHRIL_DRAGON(93, 1, 85, "mithril dragon", (p, s) -> VarPlayerRepository.I_HOPE_YOU_MITH_ME.get(p) != 0),
	AVIANSIES(94, 1, 60, "aviansie", (p, s) -> VarPlayerRepository.WATCH_THE_BIRDIE.get(p) != 0),
	SMOKE_DEVILS(95, 93, 85, "smoke devil", "Yanille Underground", TeleportConstants.SMOKE_DEVIL),
	TZHARR(96, 1, 1, "tzhaar", (p, s) -> VarPlayerRepository.HOT_STUFF.get(p) != 0),
	ABYSSAL_DEMONS(97, 1, 85, "abyssal demon", "Abyssal Area", TeleportConstants.SLAYER1),
	BOSSES(98, 1, 1, "boss", (p, s) -> VarPlayerRepository.LIKE_A_BOSS.get(p) != 0),
	MAMMOTHS(99, 1, 1, "mammoth", "Level 9 Wilderness", TeleportConstants.SLAYER1), // wilderness warning
	ROGUES(100, 1, 1, "rogue", "Level 51 Wilderness", TeleportConstants.SLAYER1),
	ENTS(101, 1, 1, "ents", "", TeleportConstants.SLAYER1),
	BANDITS(102, 1, 1, "bandit", "Level 23 Wilderness", TeleportConstants.SLAYER1),
	DARK_WARRIORS(103, 1, 1, "dark warrior", "Level 14 Wilderness", TeleportConstants.SLAYER1),
	LAVA_DRAGONS(104, 1, 1, "lava dragon", "Level 36 Wilderness", TeleportConstants.SLAYER1),
	FOSSIL_ISLAND_WYVERNS(106, 66, 60, "fossil island wyvern", "Fossil Island Underground", TeleportConstants.SLAYER1),
	REVENANTS(107, 1, 1, "revenant", "Revenant Dungeon", TeleportConstants.REVENANTS),
	ADAMANT_DRAGONS(108, 1, 1, "adamant dragon", "Lithkren", TeleportConstants.SLAYER1), // TODO no idea
	RUNE_DRAGONS(109, 1, 1, "rune dragon", "Lithkren", TeleportConstants.SLAYER1), // TODO no idea
	WYRMS(111, 62, 1, "Wyrms", "Karuulm Slayer Dungeon", TeleportConstants.SLAYER1),
	DRAKES(112, 84, 1, "drake", "Karuulm Slayer Dungeon", TeleportConstants.SLAYER1),
	HYDRAS(113, 95, 1, "hydras", "Karuulm Slayer Dungeon", TeleportConstants.SLAYER1),
	SOURHOG(114, 1, 7, "sourhog", "Sourhog cave", TeleportConstants.SLAYER1),

	;

	private final int uid;
	private final int req;
	private final int cbreq;
	private final String category;
	private String locationName;

	public final BiFunction<Player, SlayerMaster, Boolean> canAssign;

	private Position position;

	private static final Map<Integer, SlayerCreature> lookup = new HashMap<>();

	static {
		for (SlayerCreature sc : values()) {
			lookup.put(sc.uid, sc);
		}
	}

	SlayerCreature(int uid, int req, int cbreq, String category) {
		this.uid = uid;
		this.req = req;
		this.cbreq = cbreq;
		this.category = category;

		this.canAssign = null;
	}

	SlayerCreature(int uid, int req, int cbreq, String category, BiFunction<Player, SlayerMaster, Boolean> canAssign) {
		this.uid = uid;
		this.req = req;
		this.cbreq = cbreq;
		this.category = category;
		this.canAssign = canAssign;

	}

	SlayerCreature(int uid, int req, int cbreq, String category, String locationName, Position location) {
		this.uid = uid;
		this.req = req;
		this.cbreq = cbreq;
		this.category = category;
		this.locationName = locationName;
		this.position = location;
		this.canAssign = null;
	}

	public static SlayerCreature lookup(int uid) {
		return lookup.get(uid);
	}

	public boolean contains(NPC npc) {
		if (npc.getDef().combatInfo == null || npc.getDef().combatInfo.slayer_tasks == null)
			return false;

		for (String s : npc.getDef().combatInfo.slayer_tasks) {
			if (category.toLowerCase().contains(s.toLowerCase()))
				return true;
		}

		return false;
	}

	public int getUid() {
		return uid;
	}

	public int getReq() {
		return req;
	}

	public int getCbreq() {
		return cbreq;
	}

	public String getCategory() {
		return category;
	}

	public String getLocationName() {
		return locationName;
	}

	public Position getPosition() {
		return position;
	}
}
