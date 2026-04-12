package io.ruin.model.skills.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.Arrays;
import java.util.Optional;

public class KonarData {

	public static void assignLocation(Player player, int taskUuid) {
		Optional<Task> task = Arrays.stream(Task.values()).filter(s -> s.uid == taskUuid).findFirst();

		if (!task.isPresent() && taskUuid != 98)
			return;
		if (taskUuid == 98) {
			player.slayerTaskPosition = null;
			player.taskInWilderness = false;
			player.slayerLocation = 0;
			return;
		}

		int index = Random.get(task.get().taskTeleports.length - 1);
		TaskLocation location = task.get().locations[index];
		player.slayerTaskPosition = task.get().taskTeleports[index];
		player.taskInWilderness = false;
		player.slayerLocation = location.ordinal();

	}

	private enum Task {
		ABERRANT_SPECTRE(41, new Position[]{new Position(3422, 3536, 1), new Position(1608, 10005, 0), new Position(2452, 9776, 0)}, TaskLocation.SLAYER_TOWER, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.STRONGHOLD_SLAYER_CAVE),
		ABYSSAL_DEMON(42, new Position[]{new Position(1673, 10088, 0), new Position(3045, 4886, 0), new Position(3416, 3564, 2)}, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.ABYSSAL_AREA, TaskLocation.SLAYER_TOWER),
		ADAMANT_DRAGON(108, new Position[]{new Position(1562, 5075, 0)}, TaskLocation.LITHKREN_VAULT),
		ANKOU(79, new Position[]{new Position(2312, 5224, 0), new Position(2473, 9807, 0), new Position(1653, 9996, 0)}, TaskLocation.STRONGHOLD_OF_SECURITY, TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.CATACOMBS_OF_KOUREND),
		AVIANSIE(94, new Position[]{new Position(2870, 5264, 2)}, TaskLocation.GODWARS_DUNGEON),
		BASILISK(43, new Position[]{new Position(2741, 10007, 0)}, TaskLocation.FREMENNIK_SLAYER_DUNGEON),
		BRINE_RAT(84, new Position[]{new Position(2716, 10133, 0)}, TaskLocation.BRINE_RAT_CAVERN),
		FOSSIL_ISLAND_WYVERN(106, new Position[]{new Position(3603, 10229, 0)}, TaskLocation.WYVERN_CAVE),
		LIZARDMEN(90, new Position[]{new Position(1452, 3692, 0), new Position(1500, 3700, 0), new Position(1277, 3662, 0), new Position(1313, 3677, 0)}, TaskLocation.BATTLEFRONT, TaskLocation.LIZARDMAN_CANYON, TaskLocation.KEBOS_SWAMP, TaskLocation.MOLCH),
		MITHRIL_DRAGON(93, new Position[]{new Position(1777, 5344, 1)}, TaskLocation.ANCIENT_CAVERN),
		MUTATED_ZYGOMITES(74, new Position[]{new Position(3681, 3857, 0), new Position(2416, 4372, 0)}, TaskLocation.FOSSIL_ISLAND, TaskLocation.ZANARIS),
		RED_DRAGON(26, new Position[]{new Position(2718, 9503, 0), new Position(1623, 10074, 0), new Position(1831, 9935, 0), new Position(1924, 9008, 1)}, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.FORTHOS_DUNGEON, TaskLocation.MYTHS_GUILD_DUNGEON),
		WATERFIEND(88, new Position[]{new Position(1741, 5343, 0), new Position(2252, 10000, 0)}, TaskLocation.ANCIENT_CAVERN, TaskLocation.KRAKEN_COVE),
		BLACK_DEMON(30, new Position[]{new Position(2876, 9772, 0), new Position(2710, 9472, 0), new Position(1716, 10093, 0)}, TaskLocation.TAVERLY_DUNGEON, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		BLACK_DRAGON(27, new Position[]{new Position(2823, 9826, 0), new Position(2460, 4386, 0), new Position(1950, 8980, 1), new Position(1613, 10085, 0)}, TaskLocation.TAVERLY_DUNGEON, TaskLocation.EVIL_CHICKEN_LAIR, TaskLocation.MYTHS_GUILD_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		BLOODVELD(48, new Position[]{new Position(2466, 9827, 0), new Position(2887, 5325, 2), new Position(3422, 3566, 1), new Position(1678, 10075, 0)}, TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.GODWARS_DUNGEON, TaskLocation.SLAYER_TOWER, TaskLocation.CATACOMBS_OF_KOUREND),
		BLUE_DRAGON(25, new Position[]{new Position(2894, 9792, 0), new Position(2570, 9445, 0), new Position(1900, 9000, 1), new Position(1631, 10084, 0)}, TaskLocation.TAVERLY_DUNGEON, TaskLocation.OGRE_ENCLAVE, TaskLocation.MYTHS_GUILD_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		BRONZE_DRAGON(58, new Position[]{new Position(2722, 9486, 0), new Position(1642, 10098, 0)}, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		IRON_DRAGON(59, new Position[]{new Position(2710, 9469, 0), new Position(1666, 10093, 0)}, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		STEEL_DRAGON(60, new Position[]{new Position(2710, 9469, 0), new Position(1607, 10048, 0)}, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		JELLY(50, new Position[]{new Position(2704, 10022, 0), new Position(1694, 9998, 0)}, TaskLocation.RELEKKA_SLAYER_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		CAVE_KRAKEN(92, new Position[]{new Position(2292, 10008, 0)}, TaskLocation.KRAKEN_COVE),
		DAGANNOTH(35, new Position[]{new Position(1678, 9997, 0), new Position(2515, 4632, 0), new Position(2442, 10146, 0)}, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.LIGHTHOUSE, TaskLocation.WATERBIRTH_ISLAND),
		DARK_BEAST(66, new Position[]{new Position(2029, 4650, 0)}, TaskLocation.MOURNER_TUNNELS),
		DRAKE(112, new Position[]{new Position(1312, 10222, 1)}, TaskLocation.KARUULM_SLAYER_DUNGEON),
		DUST_DEVIL(49, new Position[]{new Position(1714, 10029, 0), new Position(3215, 9351, 0)}, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.SMOKE_DUNGEON),
		FIRE_GIANT(16, new Position[]{new Position(1296, 10207, 2), new Position(2636, 9507, 2), new Position(2395, 9787, 0), new Position(1624, 10048, 0), new Position(2566, 9877, 0)}, TaskLocation.KARUULM_SLAYER_DUNGEON, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.WATERFALL_DUNGEON),
		GARGOYLE(46, new Position[]{new Position(3439, 3538, 2)}, TaskLocation.SLAYER_TOWER),
		GREATER_DEMON(29, new Position[]{new Position(1424, 10084, 2), new Position(1684, 10098, 0), new Position(2854, 9749, 0), new Position(1294, 10206, 1), new Position(2638, 9506, 2)}, TaskLocation.CHASM_OF_FIRE, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.TAVERLY_DUNGEON, TaskLocation.KARUULM_SLAYER_DUNGEON, TaskLocation.BRIMHAVEN_DUNGEON),
		HELLHOUND(31, new Position[]{new Position(2420, 9784, 0), new Position(1640, 10056, 0), new Position(2867, 9840, 0), new Position(1331, 10208, 2), new Position(2727, 9690, 0)}, TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.TAVERLY_DUNGEON, TaskLocation.KARUULM_SLAYER_DUNGEON, TaskLocation.WITCHAVEN_DUNGEON),
		HYDRA(113, new Position[]{new Position(1334, 10237, 0)}, TaskLocation.KARUULM_SLAYER_DUNGEON),
		KALPHITE(53, new Position[]{new Position(3500, 9500, 0), new Position(3300, 9500, 0)}, TaskLocation.KALPHITE_LAIR, TaskLocation.KALPHITE_CAVE),
		KURASK(45, new Position[]{new Position(2705, 9996, 0)}, TaskLocation.RELEKKA_SLAYER_DUNGEON),
		NECHRYAEL(52, new Position[]{new Position(1688, 10075, 0), new Position(3433, 3566, 2)}, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.SLAYER_TOWER),
		RUNE_DRAGON(109, new Position[]{new Position(1580, 5073, 0)}, TaskLocation.LITHKREN_VAULT),
		SKELETAL_WYVERN(72, new Position[]{new Position(3056, 9555, 0)}, TaskLocation.ASGARNIAN_ICE_DUNGEON),
		SMOKE_DEVIL(95, new Position[]{new Position(2379, 9452, 0)}, TaskLocation.SMOKE_DEVIL_DUNGEON),
		TROLL(18, new Position[]{new Position(2846, 10108, 2), new Position(2870, 3589, 0)}, TaskLocation.TROLL_STRONGHOLD, TaskLocation.DEATH_PLATEAU),
		TUROTH(36, new Position[]{new Position(2731, 9999, 0)}, TaskLocation.RELEKKA_SLAYER_DUNGEON),
		WYRM(111, new Position[]{new Position(1281, 10188, 0)}, TaskLocation.KARUULM_SLAYER_DUNGEON);

		private final int uid;
		private final TaskLocation[] locations;
		private Position[] taskTeleports;

		Task(int uid, Position[] taskTeleports, TaskLocation... location) {
			this.uid = uid;
			this.locations = location;
			this.taskTeleports = taskTeleports;
		}
	}

	public enum TaskLocation {
		NONE(""),
		STRONGHOLD_OF_SECURITY("Stronghold of Security",
			new Bounds(1853, 5184, 1919, 5246, -1),
			new Bounds(1982, 5183, 2049, 5247, -1),
			new Bounds(2113, 5248, 2175, 5310, -1),
			new Bounds(2302, 5184, 2368, 5247, -1)),
		DEATH_PLATEAU("Death Plateau", new Bounds(2836, 3579, 2890, 3607, -1)),
		RELEKKA_SLAYER_DUNGEON("Relekka Slayer Dungeon", new Bounds(2680, 9950, 2814, 10045, -1)),
		KRAKEN_COVE("Kraken Cove", new Bounds(2238, 9983, 2304, 10048, -1)),
		CATACOMBS_OF_KOUREND("Catacombs of Kourend", new Bounds(1588, 9978, 1747, 10110, -1)),
		SLAYER_TOWER("Slayer Tower",
			new Bounds(3403, 2529, 3454, 3584, -1)
//				new Bounds(3403, 2529, 3454, 3584, 1),
//				new Bounds(3403, 2529, 3454, 3584, 2)
		),
		TAVERLY_DUNGEON("Taverly Dungeon", new Bounds(2812, 9666, 2969, 9855, -1)),
		WITCHAVEN_DUNGEON("Witchaven Dungeon", new Bounds(2686, 9663, 2750, 9720, -1)),
		WATERFALL_DUNGEON("Waterfall Dungeon", new Bounds(2558, 9860, 2596, 9916, -1)),
		STRONGHOLD_SLAYER_CAVE("Stronghold Slayer Cave", new Bounds(2385, 9766, 2496, 9841, -1)),
		ASGARNIAN_ICE_DUNGEON("Asgarnian Ice Dungeon", new Bounds(2980, 9531, 3085, 9601, -1)),
		BRIMHAVEN_DUNGEON("Brimhaven Dungeon", new Bounds(2625, 9404, 2751, 9599, -1)),
		ABYSSAL_AREA("Abyssal Area", new Bounds(3007, 4863, 3071, 4926, -1)),
		MYTHS_GUILD_DUNGEON("Myths' Guild Dungeon", new Bounds(1876, 8960, 2072, 9085, -1)),
		WATERBIRTH_ISLAND("Waterbirth Island", new Bounds(2434, 10119, 2557, 10178, -1)),
		GODWARS_DUNGEON("Godwars Dungeon", new Bounds(2816, 5255, 2942, 5370, -1)),
		BRINE_RAT_CAVERN("Brine Rat Cavern", new Bounds(2686, 10116, 2751, 10175, -1)),
		SMOKE_DUNGEON("Smoke Dungeon", new Bounds(3200, 9344, 3326, 9404, -1)),
		KALPHITE_LAIR("Kalphite Lair", new Bounds(3455, 9473, 3517, 9531, -1)),
		TROLL_STRONGHOLD("Troll Stronghold", new Bounds(2823, 10045, 2871, 10112, -1)),
		KARUULM_SLAYER_DUNGEON("Karuulm Slayer Dungeon", new Bounds(1248, 10146, 1390, 10282, -1)),
		LITHKREN_VAULT("Lithkren Vault", new Bounds(1536, 5058, 1596, 5118, -1)),
		LIGHTHOUSE("Lighthouse", new Bounds(2498, 4610, 2542, 4665, -1)),
		KALPHITE_CAVE("Kalphite Cave", new Bounds(3264, 9473, 3340, 9542, -1)),
		SMOKE_DEVIL_DUNGEON("Smoke Devil Dungeon", new Bounds(2345, 9408, 2431, 9470, -1)),
		MOURNER_TUNNELS("Mourner Tunnels", new Bounds(1855, 4608, 2045, 4670, -1)),
		EVIL_CHICKEN_LAIR("Evil Chicken's Lair", new Bounds(2445, 4353, 2493, 4408, -1)),
		CHASM_OF_FIRE("Chasm of Fire", new Bounds(1409, 10050, 1468, 10108, -1)),
		OGRE_ENCLAVE("Ogre Enclave", new Bounds(2561, 9407, 2621, 9469, -1)),

		FREMENNIK_SLAYER_DUNGEON("Fremmenik Slayer Dungeon", new Bounds(2689, 9921, 2814, 10046, -1)),
		WYVERN_CAVE("Wyvern Cave", new Bounds(3587, 10177, 3646, 10307, 0)),
		BATTLEFRONT("Battlefront", new Bounds(1408, 3690, 1454, 3727, -1)),
		LIZARDMAN_CANYON("Lizardman Canyon", new Bounds(1474, 3676, 1574, 3721, -1)),
		KEBOS_SWAMP("Kebos Swamp", new Bounds(1153, 3585, 1341, 3645, -1)),
		MOLCH("Molch", new Bounds(1281, 3648, 1343, 3710, -1)),
		ANCIENT_CAVERN("Ancient Cavern", new Bounds(1729, 5317, 1788, 5375, -1)),
		FORTHOS_DUNGEON("Forthos Dungeon", new Bounds(1779, 9882, 1865, 9995, -1)),
		FOSSIL_ISLAND("Fossil Island", new Bounds(3588, 3715, 3838, 3900, -1)),
		ZANARIS("Zanaris", new Bounds(2315, 4345, 2495, 4482, -1)),

		;

		private final String name;
		private final Bounds[] boundaries;

		TaskLocation(String name, Bounds... boundaries) {
			this.name = name;
			this.boundaries = boundaries;
		}

		public String getName() {
			return name;
		}

		public Bounds[] getBoundaries() {
			return boundaries;
		}

		public boolean inside(Position pos) {
			for (Bounds b : boundaries) {
				if (b.inBounds(pos.getX(), pos.getY(), pos.getZ(), 0))
					return true;
			}
			return false;
		}
	}

}
