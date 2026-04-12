package io.ruin.cache;

import com.google.common.collect.ImmutableSet;
import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.api.utils.StringUtils;
import io.ruin.data.impl.npcs.animation;
import io.ruin.data.impl.npcs.npc_combat;
import io.ruin.model.World;
import io.ruin.model.activities.cluescrolls.impl.AnagramClue;
import io.ruin.model.activities.cluescrolls.impl.CrypticClue;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.KillCounter;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.item.loot.LootTable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class NPCType {

	private static final IntSet oldFormatNpcIds;

	static {
		int[] ids = {8064, 8065, 8066, 20016, 763, 12004, 20017, 6477, 12007, 12008, 12009, 12005, 4058, 2989, 5523, 5527,
				6481, 5906, 7456, 10631, 2914, 4753, 5792, 7958, 311, 1482, 2882, 3842, 1501, 2879, 8532, 10619, 12001, 12000,
				7903, 11903};
		oldFormatNpcIds = new IntOpenHashSet(ids.length);
		for (int id : ids) {
			oldFormatNpcIds.add(id);
		}
	}

	public static Int2ObjectMap<NPCType> cached = new Int2ObjectOpenHashMap<>(0);

	public static void load() {
		IndexFile index = Server.fileStore.get(2);
		int size = index.getLastFileId(9) + 1;
		log.info("Loading {} NPC definitions...", size);
		cached = new Int2ObjectOpenHashMap<>(size);

		for (int id = 0; id < size; id++) {
			byte[] data = index.getFile(9, id);
			if (data != null) {
				NPCType def = new NPCType();
				def.id = id;
				def.decode(new InBuffer(data));
				cached.put(id, def);
			}
		}
	}

	public static void forEach(Consumer<NPCType> consumer) {
		for (NPCType def : cached.values()) {
			if (def != null)
				consumer.accept(def);
		}
	}

	public static NPCType get(int id) {
		return cached.get(id);
	}

	/**
	 * Custom data
	 */

	public String descriptiveName;

	public Class<? extends NPCCombat> combatHandlerClass;

	public LootTable lootTable;

	public NPCAction[] defaultActions;

	public HashMap<Integer, ItemNPCAction> itemActions;

	public ItemNPCAction defaultItemAction;

	public AnagramClue anagram;

	public CrypticClue cryptic;

	public npc_combat.Info combatInfo;

	public int attackOption = -1;

	public boolean flightClipping, swimClipping;

	public boolean occupyTiles = true;

	public animation.NpcAnimation animations = new animation.NpcAnimation();

	public boolean ignoreOccupiedTiles;

	// public double giantCasketChance; // only used for bosses atm, other npcs use
	// a formula (see GoldCasket)

	public boolean dragon;

	public boolean rat;

	public boolean leafBladed;

	public boolean demon;

	public boolean undead;

	public boolean animal;

	public boolean insects;

	public boolean ignoreMultiCheck = false;

	public Function<Player, KillCounter> killCounter;

	/**
	 * Cache data
	 */

	public int id;
	public int category;
	public boolean isMinimapVisible = true;
	public int anInt3558;
	public int standAnimation = -1;
	public int size = 1;
	public int walkBackAnimation = -1;
	public String name = "null";
	public int turnLeftAnim = -1;
	public int turnRightAnim = -1;
	public int walkAnimation = -1;
	public int walkLeftAnimation = -1;
	public int walkRightAnimation = -1;
	public boolean isFollower = false;
	public boolean lowPriorityFollowerOps = false;
	public String[] options = new String[5];
	public int combatLevel = -1;
	public boolean hasRenderPriority = false;

	public int headIcon = -1;

	public int[] headIconArchiveIds;
	public short[] headIconSpriteIndex;

	public int rotation = 32;
	public int[] showIds;
	public boolean isClickable = true;
	public boolean rotationFlag = true;
	public short[] retextureToReplace;
	short[] retextureToFind;
	public int varpId = -1;
	public int[] models;
	public short[] recolorToFind;
	int[] headModels;
	public int varpbitId = -1;
	public short[] recolorToReplace;
	int resizeX = 128;
	int resizeY = 128;
	int ambient = 0;
	int contrast = 0;
	public HashMap<Object, Object> attributes;

	static final ImmutableSet DEMON = ImmutableSet.of(
			10507, // Undead combat dummy
			11961, 5908, 5887, 5888, 5891, 5890, 5886, 5889, 5909, 5910, 5911, 5912, 5913, // Abyssal Sire + Tentacles
			11239, 12581, 7410, 7241, 124, 416, 415, 12451, // Abyssal demon
			3132, 7876, 5876, 2048, 1432, 7875, 5877, 6357, 240, 2050, 12386, 7242, 2051, 6295, 5874, 2049, 2052, 12385, 7243,
			7874, 5875, // Black demon
			11293, 11294, // Blood reaver
			487, 3138, 12569, 486, 7276, 7034, 9611, 9573, 7398, 485, 12568, 9610, 7397, 484, // Bloodveld
			6293, 3508, 3509, 12038, 6355, 12037, 1224, // Bouncer
			5866, 5863, 5862, // Cerberus
			6354, 6292, 4987, 8995, 8996, 10938, 10936, 1443, 6382, 6321, 8994, 7860, 7515, 12448, 3130, 3131, // Chronozon /
			14176, 14180, 13600, 13601, 14179, 17028,
			// Doomion /
			// Holthion /
			// Judge of
			// Yama /
			// Jungle
			// demon /
			// Othainian /
			// Porazdir /
			// Tsanon /
			// Zakl'n
			// Gritch
			7144, 7098, 7147, 7145, 7152, 7148, 7095, 7096, 7150, 7097, 7153, 7151, // Demonic gorilla / Tortured gorilla
			12196, 12191, 12167, 12193, 12194, 12166, 12195, 12192, // Duke Sucellus
			12453, 7394, 9465, 12564, 6795, 12563, 6762, 433, 434, 7932, 436, 435, 3139, // Pyre
			7245, 12373, 7871, 2032, 2026, 2025, 7244, 12387, 7872, 2029, 7873, 2030, 2028, 7246, 2027, 2031, 12388, // Greater
																																																								// demons
			6614, 7935, 11463, 7256, 3133, 135, 6613, 12379, 6326, 12108, 104, 12107, 7877, 5064, 12374, 105, 6387, // Hellhounds
			7585, 7584, 4813, 3140, 7586, 2916, 2917, // Ice demon / Icefiend / Waterfiend
			7881, 12377, 5007, 7020, 5728, 5738, // Imp
			12446, 6495, 3129, // K'ril Tsutsaroth
			12362, 2005, 12378, 7657, 7865, 7656, 7866, 12389, 12361, 12390, 3982, 12376, 7248, 2008, 2018, 12365, 2007, 7247,
			12363, 3357, 7664, 12364, 7867, 12360, 2006, // Lesser demon
			11, 11240, 8, 7278, // Nechryael
			7297, 7286 // Skotizo
	);

	static final ImmutableSet UNDEAD_MONSTERS = ImmutableSet.of(26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
			40, 41, 42, 43, 44, 45, 46, 47, 48, 6596, 6597, 6598, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
			3980, 3981, 2501, 2502, 2503, 12762, 12763, // Zombie
			924, 74, 75, 76, 70, 71, 72, 73, 3565, 3972, 3973, 3974, 7265, 130, 77, 78, 79, 80, 81, 6444, 6446, 82, 83, 2521,
			2522, 2523, 2520, 1685, 1686, 1687, 1688, 6442, 2524, 2525, 2526, // Skeleton
			85, 3975, 6815, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 3976, 3977, 3978, 3979, 6816, 6817, 6818, 6819,
			6820, 6821, 6822, 7263, 7264, 5370, 2527, 2528, 2529, 2530, // Ghost
			2514, 2517, // Ankou
			945, 946, 5622, 5623, 5624, 5625, 5626, 5627, // Ghast
			949, 950, 951, 952, 953, 7658, 7659, 7660, 7661, 7662, 717, 720, 721, 722, 723, 725, 726, 727, 728, // Mummy
			448, 449, 451, 452, 453, 454, 456, 457, 7388, // Crawling hand and crushing hand
			2, 3, 4, 5, 6, 7, // Aberrant spectre
			7402, 7279, 7403, // Abhorrent spectre, Deviant spectre, Repugnant spectre
			414, 7272, 7390, 7391, // Twisted / screaming / reg banshee
			6611, 6612, // Vet'ion
			289, // Ghoul
			7604, 7605, 7606, // Skeletal mystic
			7934, 7938, 7936, 7940, 7935, 7939, 7937, 7932, 7881, 7931, 7933, // Revenants
			8026, 8058, 8059, 8060, 8061, // Vorkath
			8063, // Zombified spawn
			8359, 10812, 10813, 11184, // pestilent bloat
			10507, // Undead combat dummy
			6477, 6475, 6476, // Ophidia / Tarn
			3922, // The Draugen
			3969, 3970, 3971, // Zombie rat
			2999, // Tortured soul
			69, // Summoned zombie
			1539, 1541, 5054, 6326, 6387, 6613, 6614, 1538, // Skeleton warlord, thug, hellhounds, brute
			872, 878, 879, // Skogre
			1277, 1278, 1280, 1282, 1284, 1286, 5633, 6740, 7258, 9689, 10589, // Shades
			866, 867, 868, 869, 870, 871, 873, 874, 875, 876, 877, // Zogre
			5281, 5282, 5283 // Monkey zombie

	);

	static final ImmutableSet LEAF_BLADED_NPCS = ImmutableSet.of(
			4158, 11902, 20727, 4160, 11875, 21316);

	static final ImmutableSet ANIMALS = ImmutableSet.of(
			5862, 5863, 5866, 5862, 5863, 5866, 104, 105, 135, 7256, 135, 3133, 7877, 4005, 7250,
			5779, 6499, 6474, 6473);

	static final ImmutableSet INSECTS = ImmutableSet.of(
			7530, 7531, 6504, 6610, 963, 965, 138, 957, 958, 959, 960, 962, 8713, 8715, 8715, 6615,
			6617);

	void decode(InBuffer buffer) {
		for (;;) {
			int opcode = buffer.readUnsignedByte();
			if (opcode == 0)
				break;
			decode(buffer, opcode);
		}
		setCustomFields();

		fixName();

		attackOption = getOption("attack", "fight");
		flightClipping = name.toLowerCase().contains("impling") || name.toLowerCase().contains("butterfly");
		dragon = name.toLowerCase().contains("dragon") || name.equalsIgnoreCase("elvarg") || name.equalsIgnoreCase("wyrm")
				|| name.equalsIgnoreCase("drake") || name.toLowerCase().contains("hydra")
				|| name.toLowerCase().contains("great olm") || name.toLowerCase().contains("wyvern")
				|| name.toLowerCase().contains("vorkath") || name.equalsIgnoreCase("Galvek");
		rat = name.toLowerCase().contains("rat") || name.toLowerCase().contains("scurrius");
		undead = UNDEAD_MONSTERS.contains(id);
		leafBladed = LEAF_BLADED_NPCS.contains(id);
		demon = DEMON.contains(id);
		animal = ANIMALS.contains(id);
		insects = INSECTS.contains(id);
		demon = name.toLowerCase().contains("demon") || name.equalsIgnoreCase("skotizo") || name.contains("mokhaiotl")
				|| name.equalsIgnoreCase("icefiend") || name.equalsIgnoreCase("pyre")
				|| name.equalsIgnoreCase("cerberus") || name.equalsIgnoreCase("imp") || name.toLowerCase().contains("nechryael")
				|| name.toLowerCase().contains("abyssal sire") || name.toLowerCase().contains("k'ril")
				|| name.toLowerCase().contains("balfrug")
				|| name.toLowerCase().contains("tstanon") || name.toLowerCase().contains("zakl'n")
				|| name.toLowerCase().contains("hellhound") || name.toLowerCase().contains("bloodveld");
	}

	public void fixName() {
		if (name != null) {
			if (name.isEmpty())
				descriptiveName = name;
			else if (name.equals("Kalphite Queen") || name.equals("Corporeal Beast") || name.equals("King Black Dragon")
					|| name.equals("Kraken") || name.equals("Thermonuclear Smoke Devil")
					|| name.equalsIgnoreCase("Crazy archaeologist") || name.equalsIgnoreCase("Chaos Fanatic")
					|| name.equalsIgnoreCase("Chaos Elemental"))
				descriptiveName = "the " + name;
			else if (name.toLowerCase().matches("dagannoth (rex|prime|supreme)")
					|| name.equals("Cerberus") || name.equals("Zulrah") || name.equals("Callisto") || name.equals("Venenatis")
					|| name.equals("Vet'ion") || name.equals("Scorpia"))
				descriptiveName = name;
			else if (StringUtils.vowelStart(name))
				descriptiveName = "an " + name;
			else
				descriptiveName = "a " + name;
		}
	}

	public void setCustomFields() {

		/**
		 * Keep updated with client lol
		 */
		if (id == 5527) {
			/* Twiggy O'Korn */
			options[2] = "Rewards";
		} else if (id == 10590) {
			/* walkers shop */
			name = "Hunter Steve";
			options[2] = "Trade";
		} else if (id == 315) {
			options[0] = "Talk-to";
			options[1] = "Set-skull";
			options[2] = "Reset-kdr";
			options[3] = null;
			options[4] = null;
		} else if (id == 1815) {
			name = "Vote Manager";
			options[0] = "Trade";
			options[2] = "Cast-votes";
			options[4] = "Lottery-info";
		} else if (id == 2108) {
			name = "Donation Manager";
			options[0] = "Open-site";
			options[2] = "Donator-store";
		} else if (id == 306) {
			name = World.type.getWorldName() + " Expert";
			options[0] = "Talk-to";
			options[2] = "View-help";
			options[3] = "Task-rewards";
		} else if (id == 3985) {
			name = "Farming Supplies";
			options[0] = "Talk-to";
			options[2] = "Trade";
		} else if (id == 5442) {
			name = "Security Advisor";
			options[2] = "Check Pin Settings";
			options[3] = "Check 2FA Settings";
		} else if (id == 2882) {
			/* Horvik */
			options[0] = "Repair-items";
			options[2] = "Upgrade-items";
		} else if (id == 3894) {
			/* Sigmund the Merchant */
			options[0] = "Buy-items";
			options[2] = "Sell-items";
			options[3] = "Sets";
			options[4] = null;
		} else if (id == 5523) {
			/* Gambling man */
			name = "Gambler";
			options[0] = null;
			options[2] = "Trade";
		} else if (id == 4398) {
			/* ECO Wizard */
			name = World.type.getWorldName() + " Wizard";
			options[0] = "Teleport";
			options[2] = "Teleport-previous";
		} else if (id == 4159) {
			options[0] = null;
			options[1] = null;
			options[2] = null;
			options[3] = null;
		} else if (id == 2462) {
			/* Shanomi */
			options[2] = "Trade";
		} else if (id == 6481) {
			combatLevel = 0;
		} else if (id == 3343) {
			name = World.type.getWorldName() + " Nurse";
		} else if (id == 1603) {
			/* Kolodion */
			name = "Battle Point Exchange";
			options[2] = "Trade";
			options[3] = "Check-points";
		} else if (id == 3278) {
			name = "Construction Worker";
		} else if (id == 1307) {
			options[0] = "Change-looks";
			options[2] = "Skin-unlocks";
			options[3] = null;
			options[4] = null;
		} else if (id == 4225) {
			name = "Shop";
			options[0] = "Untradeable";
		} else if (id == 1199) {
			name = "Shop";
			options[0] = "Consumable";
		} else if (id == 5081) {
			name = "Shop";
			options[0] = "Magic";
		} else if (id == 2153) {
			name = "Shop";
			options[0] = "Melee";
		} else if (id == 4579) {
			name = "Shop";
			options[0] = "Misc";
		} else if (id == 2668) {
			name = "Max hit dummy";
			options[2] = options[3] = options[4] = null;
		} else if (id == 6118) {
			name = "Elvarg";
			combatLevel = 280;
		} else if (id == 3358) {
			name = "Superior walker";
			combatLevel = 420;
			resizeX *= 2;
			resizeY *= 2;
			size = 2;
		} else if (id == 5906) {
			options[2] = null;
		} else if ("Pick-up".equals(options[0]) && "Talk-to".equals(options[2]) && "Chase".equals(options[3])
				&& "Interact".equals(options[4])) {
			options[3] = "Age";
			options[4] = null;
		} else if (id == 1849) {
			name = "Loyalty Fairy";
			options[0] = "About";
		} else if (id == 8407) {
			options[1] = "View Trading Post";
		} else if (id == 7759) {
			options[0] = options[2] = null;
		} else if (id == 7316) {
			name = "Tournament Manager";
			options[0] = "Sign-up";
		} else if (id == 7941) {
			options[2] = options[3] = options[4] = null;
		} else if (id == 3080) { // man at home. remove attack option so people can't be assholes to newbies that
															// are just starting out
			options[1] = null;
			combatLevel = 0;
		} else if (id == 307) { // second guide npc (with no options); originally dr jekyll
			name = World.type.getWorldName() + " Expert";
			options[0] = null;
			options[4] = null;
			standAnimation = 813;
			walkAnimation = 1205;
		} else if (id == 11735) {
			walkAnimation = 6;
		} else if (id == 311) {
			name = "Ironman";
			options[1] = "Open-shop";
			options[2] = null;
		} else if (id == 7951) {
			name = "PvP Event Manager";
			options[0] = "Join-event";
			options[1] = "Create-event";
		} else if (id == 4058) {
			name = "Vote Handler";
			options[1] = "Cast-votes";
		} else if (id == 8009) {
			options[3] = "Metamorphosis";
		} else if (id == 8507) {
			name = "Bloody Merchant";
			options[0] = "Trade";
		} else if (id == 7297) { // Skotizo (For eco world)
			// Replaces Mistag
			copy(7286);
		} else if (id == 6002) {
			name = "Caretaker";
			options[0] = "Appreciation Store";
			options[1] = "Corrupted Store";
		} else if (id == 119) {
			name = "Doomsayer";
		} else if (id == 8500) {
			name = "Old man";
			options[1] = "Trade";
		} else if (id == 8300) { // Ranalph Devere Melee
			copy(3966);
			headIcon = 0;
		} else if (id == 8301) { // Ranalph Devere Range
			copy(3966);
			headIcon = 1;
		} else if (id == 8302) { // Ranalph Devere Mage
			copy(3966);
			headIcon = 2;
		}
	}

	void decode(InBuffer var1, int op) {
		if (op == 1) {
			int var4 = var1.readUnsignedByte();
			models = new int[var4];
			for (int var5 = 0; var5 < var4; var5++)
				models[var5] = var1.readUnsignedShort();
		} else if (op == 2)
			name = var1.readStringCp1252NullTerminated();
		else if (op == 12)
			size = var1.readUnsignedByte();
		else if (op == 13)
			standAnimation = var1.readUnsignedShort();
		else if (op == 14)
			walkAnimation = var1.readUnsignedShort();
		else if (op == 15)
			turnLeftAnim = var1.readUnsignedShort();
		else if (op == 16)
			turnRightAnim = var1.readUnsignedShort();
		else if (op == 17) {
			walkAnimation = var1.readUnsignedShort();
			walkBackAnimation = var1.readUnsignedShort();
			walkLeftAnimation = var1.readUnsignedShort();
			walkRightAnimation = var1.readUnsignedShort();
		} else if (op == 18)
			category = var1.readUnsignedShort();
		else if (op >= 30 && op < 35) {
			options[op - 30] = var1.readStringCp1252NullTerminated();
			if (options[op - 30].equalsIgnoreCase("Hidden"))
				options[op - 30] = null;
		} else if (op == 40) {
			int var4 = var1.readUnsignedByte();
			recolorToFind = new short[var4];
			recolorToReplace = new short[var4];
			for (int var5 = 0; var5 < var4; var5++) {
				recolorToFind[var5] = (short) var1.readUnsignedShort();
				recolorToReplace[var5] = (short) var1.readUnsignedShort();
			}
		} else if (op == 41) {
			int var4 = var1.readUnsignedByte();
			retextureToFind = new short[var4];
			retextureToReplace = new short[var4];
			for (int var5 = 0; var5 < var4; var5++) {
				retextureToFind[var5] = (short) var1.readUnsignedShort();
				retextureToReplace[var5] = (short) var1.readUnsignedShort();
			}
		} else if (op == 60) {
			int var4 = var1.readUnsignedByte();
			headModels = new int[var4];
			for (int var5 = 0; var5 < var4; var5++)
				headModels[var5] = var1.readUnsignedShort();
		} else if (op == 93) {
			isMinimapVisible = false;
		} else if (op >= 74 && op <= 79) {
			var slot = op - 74;
			var stat = var1.readUnsignedShort();
		} else if (op == 95) {
			combatLevel = var1.readUnsignedShort();
		} else if (op == 97)
			resizeX = var1.readUnsignedShort();
		else if (op == 98)
			resizeY = var1.readUnsignedShort();
		else if (op == 99)
			hasRenderPriority = true;
		else if (op == 100)
			ambient = var1.readByte();
		else if (op == 101)
			contrast = var1.readByte() * 5;
		else if (op == 102) {
			if (oldFormatNpcIds.contains(id)) {
				headIcon = var1.readUnsignedShort();
			} else {
				int bitfield = var1.readUnsignedByte();
				int len = 0;
				for (int var5 = bitfield; var5 != 0; var5 >>= 1) {
					++len;
				}

				headIconArchiveIds = new int[len];
				headIconSpriteIndex = new short[len];

				for (int i = 0; i < len; i++) {
					if ((bitfield & 1 << i) == 0) {
						headIconArchiveIds[i] = -1;
						headIconSpriteIndex[i] = -1;
					} else {
						headIconArchiveIds[i] = var1.readBigSmart2();
						headIconSpriteIndex[i] = (short) var1.readUnsignedShortSmartMinusOne();
					}
				}
			}
		} else if (op == 103)
			rotation = var1.readUnsignedShort();
		else if (op == 114)
			var1.readUnsignedShort(); // runAnimation
		else if (op == 115) {
			var1.readUnsignedShort(); // runAnimation
			var1.readUnsignedShort(); // runRotate180Animation
			var1.readUnsignedShort(); // runRotateLeftAnimation
			var1.readUnsignedShort(); // runRotateRightAnimation
		} else if (op == 116) {
			var1.readUnsignedShort(); // crawlAnimation
		} else if (op == 117) {
			var1.readUnsignedShort(); // crawlAnimation
			var1.readUnsignedShort(); // crawlRotate180Animation
			var1.readUnsignedShort(); // crawlRotateLeftAnimation
			var1.readUnsignedShort(); // crawlRotateRightAnimation
		} else if (op == 122) {
			isFollower = true;
		} else if (op == 123) {
			lowPriorityFollowerOps = true;
		} else if (op == 106 || op == 118) {
			varpbitId = var1.readUnsignedShort();
			if (varpbitId == 65535)
				varpbitId = -1;
			varpId = var1.readUnsignedShort();
			if (varpId == 65535)
				varpId = -1;
			int var4 = -1;
			if (op == 118) {
				var4 = var1.readUnsignedShort();
				if (var4 == 65535)
					var4 = -1;
			}
			int var5 = var1.readUnsignedByte();
			showIds = new int[var5 + 2];
			for (int var6 = 0; var6 <= var5; var6++) {
				showIds[var6] = var1.readUnsignedShort();
				if (showIds[var6] == 65535)
					showIds[var6] = -1;
			}
			showIds[var5 + 1] = var4;
		} else if (op == 107)
			isClickable = false;
		else if (op == 109)
			rotationFlag = false;
		else if (op == 111) {
			isFollower = true;
			lowPriorityFollowerOps = true;
		} else if (op == 124)
			var1.readUnsignedShort();
		else if (op == 249) {
			int size = var1.readUnsignedByte();
			if (attributes == null)
				attributes = new HashMap<>();
			for (int i = 0; i < size; i++) {
				boolean stringType = var1.readUnsignedByte() == 1;
				int key = var1.readMedium();
				if (stringType)
					attributes.put(key, var1.readStringCp1252NullTerminated());
				else
					attributes.put(key, var1.readInt());
			}
		} else {
			System.err.println(id + " unknown opcode: " + op);
			System.exit(-1);
		}

	}

	void copy(int otherId) {
		NPCType otherDef = cached.get(otherId);
		try {
			isMinimapVisible = otherDef.isMinimapVisible;
			anInt3558 = otherDef.anInt3558;
			standAnimation = otherDef.standAnimation;
			size = otherDef.size;
			walkBackAnimation = otherDef.walkBackAnimation;
			name = otherDef.name;
			turnLeftAnim = otherDef.turnLeftAnim;
			turnRightAnim = otherDef.turnRightAnim;
			walkAnimation = otherDef.walkAnimation;
			walkLeftAnimation = otherDef.walkLeftAnimation;
			walkRightAnimation = otherDef.walkRightAnimation;
			isFollower = otherDef.isFollower;
			lowPriorityFollowerOps = otherDef.lowPriorityFollowerOps;
			options = otherDef.options == null ? null : otherDef.options.clone();
			combatLevel = otherDef.combatLevel;
			hasRenderPriority = otherDef.hasRenderPriority;
			headIcon = otherDef.headIcon;
			rotation = otherDef.rotation;
			showIds = otherDef.showIds == null ? null : otherDef.showIds.clone();
			isClickable = otherDef.isClickable;
			rotationFlag = otherDef.rotationFlag;
			retextureToReplace = otherDef.retextureToReplace == null ? null : otherDef.retextureToReplace.clone();
			retextureToFind = otherDef.retextureToFind == null ? null : otherDef.retextureToFind.clone();
			varpId = otherDef.varpId;
			models = otherDef.models;
			recolorToFind = otherDef.recolorToFind == null ? null : otherDef.recolorToFind.clone();
			headModels = otherDef.headModels == null ? null : otherDef.headModels.clone();
			varpbitId = otherDef.varpbitId;
			recolorToReplace = otherDef.recolorToReplace == null ? null : otherDef.recolorToReplace.clone();
			resizeX = otherDef.resizeX;
			resizeY = otherDef.resizeY;
			ambient = otherDef.ambient;
			contrast = otherDef.contrast;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean hasOption(String... searchOptions) {
		return getOption(searchOptions) != -1;
	}

	public int getOption(String... searchOptions) {
		if (options != null) {
			for (String s : searchOptions) {
				for (int i = 0; i < options.length; i++) {
					String option = options[i];
					if (s.equalsIgnoreCase(option))
						return i + 1;
				}
			}
		}
		return -1;
	}

	public static void registerCombat(Class<? extends NPCCombat> cmb, int... ids) {
		for (var id : ids) {
			var def = NPCType.get(id);
			if (def == null) {
				log.warn("Unable to find npc def: " + id + " to register combat " + cmb);
				continue;
			}
			def.combatHandlerClass = cmb;
		}
	}

}
