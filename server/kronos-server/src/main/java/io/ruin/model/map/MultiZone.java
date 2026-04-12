package io.ruin.model.map;

import io.ruin.model.activities.bosses.zulrah.Zulrah;
import io.ruin.model.activities.wilderness.BloodyChest;

public class MultiZone {

	/**
	 * Adding
	 */

	public static void add(int x, int y, int z) {
		set(x, y, z, true);
	}

	public static void add(Bounds... bounds) {
		set(true, bounds);
	}

	/**
	 * Removing
	 */

	public static void remove(int x, int y, int z) {
		set(x, y, z, false);
	}

	public static void remove(Bounds... bounds) {
		set(false, bounds);
	}

	/**
	 * Setting
	 */

	private static void set(boolean multi, Bounds... bounds) {
		for (Bounds b : bounds) {
			for (int x = b.swX; x <= b.neX; x++) {
				for (int y = b.swY; y <= b.neY; y++) {
					if (b.z == -1) {
						/**
						 * All heights
						 */
						for (int z = 0; z < 4; z++)
							set(x, y, z, multi);
					} else {
						/**
						 * Fixed height
						 */
						set(x, y, b.z, multi);
					}
				}
			}
		}
	}

	public static void set(int x, int y, int z, boolean multi) {
		Tile.get(x, y, z, true).multi = multi;
	}

	/**
	 * Loading
	 */

	public static void load() {
		/**
		 * By regions
		 */
		int[] regions = {
			/** Safe: **/
			11827, 11828, 11829, //Falador
			12341, //Barbarian Village
			8253, 8252, 8508, 8509, 8254, //Lunar Isle:
			9273, 9017, //Piscatoris Fishing Colony
			9532, 9276, //Fremennik Isles
			10809, 10810, 10554, //Relleka
			10549, //Ranging Guild
			10034, //Battlefield
			10029, //Feldip hills
			11318, //White wolf mountain
			11575, //Burthope
			11577, 11578, //Trollheim
			11050, 11051, 10794, 10795,//Apeatoll
			12590, //Bandit camp
			13105, //Al Kharid
			12337, //Wizards tower
			12338, //Draynor Village
			11602, 11603, 11346, 11347, 11345, 11601, //Godwars Dungeon
			13131, 13387, //FFA clan wars, top half
			11844, //Corporeal beast
			11589, 11588, //Dagannoths
			5690, 5689, //Zeah lizanman pit
			14682, //Kraken cave
			8023, //Gnome Stronghold crash site (monkey madness)
			13972, // Kalphite queen lair
			13204, 13205, // Kalphite cave
			12363, 12362, 12106, 11851, 11850, // Abyssal Sire
			14938, 14939, // Smokedevil room in Nieve's cave + kalphite hive room
			9023, // vorkath island
			12889, // olm chamber
			12126, //Zalcano

			/** Wildy: (uses 8x8 chunks for some sections as well as chunks) **/
			12599, 12600, //Wilderness Ditch
			12855, 12856, //Mammoths (lvl 9)
			13111, 13112, 13113, 13114, 13115, 13116, 13117, 13118, 13119, 12862, 12863, //Varrock -> GDZ
			12857, 12858, 12859, 12860, 12861, //East graveyard (lvl 17)
			13372, 13373, //East of Callisto (lvl 41)
			12604, //Black chins (lvl 33)
			12348, //Wildy GWD & Center wildy north of lava maze
			12088, 12089, //North of dark warriors (lvl 17)
			12961, //Scorpia pit
			9033, // KBD zone
			9551, //Fight caves
			9043, //Inferno
			9807, 9808, 10063, 10064, // Mor UI Rek
			12107, //Abyss
			9619, //Smoke devil dungeon
			/*12960, 12958, 12957,*/
			6810, // Skotizo lair
			10536, // Pest Control battlegrounds
			15515, //Nightmare of ashihama
			12124, //Xamphur
			14132, //Judge of yama

			/**
			 * Wild slayer cave
			*/
			13469,
			13725,
			13470,
			13726,
			// Mount Karuulm
			5022, 5023,
			5278, 5279, 5280,
			5535, 5536,
		};
		for (int regionId : regions)
			set(true, Bounds.fromRegion(regionId));
		// Lava maze dungeon (for the bloody chest)
		set(true, BloodyChest.BLOODY_DUNGEON);
		/*
		 * By chunks
		 */
		int[] chunks = {
			// Chaos temple - Crazy Arch 44s
			24117724, 24117725, 24117726,
			24183260, 24183261, 24183262,

			//KBD Cage
			24642018, 24642019, 24642020, 24642021, 24642022, 24642023,
			24707554, 24707555, 24707556, 24707557, 24707558, 24707559,
			24773090, 24773091, 24773092, 24773093, 24773094, 24773095,
			24838626, 24838627, 24838628, 24838629, 24838630, 24838631,
			24904162, 24904163, 24904164, 24904165, 24904166, 24904167,

			//Rune rocks north of KBD cage
			24969699, 24969700, 24969702, 24969703, 25035238, 25035239,
			25100774, 25100775,

			// Wilderness agility course at 55 wilderness
			24445417, 24510953, 24576489,
			24445418, 24510954, 24576490,
			24445419
		};
		for (int chunk : chunks) {
			int chunkAbsX = (chunk >> 16) << 3;
			int chunkAbsY = (chunk & 0xffff) << 3;
			set(true, new Bounds(chunkAbsX, chunkAbsY, chunkAbsX + 7, chunkAbsY + 7, 0));
		}
		/**
		 * By bounds
		 */
		Bounds[] bounds = {

			/*Sand crabs*/
			new Bounds(1728, 3461, 1791, 3392, 0),
			new Bounds(1792, 3420, 1799, 3392, 0),
			new Bounds(1729, 3461, 1851, 3467, 0),
			new Bounds(1851, 3460, 1792, 3451, 0),
			new Bounds(1734, 3467, 1747, 3474, 0),
			new Bounds(1729, 3468, 1733, 3472, 0),
			new Bounds(1731, 3473, 1734, 3474, 0),
			new Bounds(1748, 3472, 1751, 3468, 0),
			new Bounds(1751, 3471, 1754, 3468, 0),
			new Bounds(1754, 3470, 1756, 3468, 0),
			new Bounds(1785, 3467, 1813, 3468, 0),
			new Bounds(1811, 3468, 1787, 3469, 0),
			new Bounds(1788, 3470, 1809, 3469, 0),
			new Bounds(1725, 3472, 1667, 3476, 0),
			new Bounds(1709, 3456, 1666, 3494, 0),
			new Bounds(1719, 3476, 1702, 3478, 0),
			new Bounds(1715, 3477, 1700, 3479, 0),
			new Bounds(1705, 3481, 1713, 3477, 0),
			new Bounds(1712, 3482, 1706, 3480, 0),

			/**
			 * Wilderness expansion
			 * */
			new Bounds(3174, 3986, 3250, 4072, 0),
			new Bounds(3072, 4007, 3075, 4017, 0),
			new Bounds(3070, 4011, 3076, 4017, 0),
			new Bounds(3076, 4010, 3079, 4016, 0),
			new Bounds(3080, 4012, 3081, 4014, 0),

			//MM2 monkeys
			new Bounds(2754, 9180, 2779, 9214, 0),
			// nex lair
			new Bounds(2909, 5187, 2941, 5220, 0),
			/* wilderness agility area */
			new Bounds(2984, 3912, 3007, 3927, 0),
			/* waterbirth dungeon */
			new Bounds(2433, 10115, 2560, 10177, 0),
			new Bounds(1792, 4330, 1984, 4452, 0),
			new Bounds(1792, 4330, 1984, 4452, 1),
			new Bounds(1792, 4330, 1984, 4452, 2),
			new Bounds(1792, 4330, 1984, 4452, 3),

			new Bounds(3152, 3752, 3326, 3841, 0),
			new Bounds(3192, 3648, 3326, 3841, 0),

			/* catacombs of kourend */
			new Bounds(1598, 9963, 1766, 10067, -1),
			new Bounds(1638, 10067, 1737, 10111, -1),

			/* Kraken boss room */
			new Bounds(2270, 10019, 2293, 10045, -1),

			/* Zulrah arena */
			Zulrah.SHRINE_BOUNDS,

			/* Wilderness Godwars */
			new Bounds(3013, 10108, 3078, 10177, 0),

			/* Raids source area */
			new Bounds(3264, 5152, 3400, 5727, -1)

			/* Revs caves */
/*                new Bounds(3233, 10229, 3235, 10231, -1),
                new Bounds(3136, 10061, 3263, 10228, -1),
                new Bounds(3208, 10048, 3263, 10082, -1),

                new Bounds(1357, 10193, 1378, 10220, 1)*/
		};
		for (Bounds b : bounds)
			set(true, b);
	}

}
