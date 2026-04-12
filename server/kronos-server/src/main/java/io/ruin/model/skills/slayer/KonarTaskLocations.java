package io.ruin.model.skills.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.Arrays;
import java.util.Optional;

public class KonarTaskLocations {

	static NPC npc;

	public static void assignLocation(Player player, String taskName) {
		Optional<Task> task = Arrays.stream(Task.values()).filter(s -> s.name.equals(taskName)).findFirst();

		if (!task.isPresent())
			return;

		TaskLocation location = Random.get(task.get().locations);
		player.taskLocation = location.ordinal();
	}

	private enum Task {
		ABERRANT_SPECTRE("Aberrant Spectres", TaskLocation.SLAYER_TOWER, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.STRONGHOLD_SLAYER_CAVE),
		ABYSSAL_DEMONS("Abyssal Demons", TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.SLAYER_TOWER),
		ADAMANT_DRAGON("Adamant Dragons", TaskLocation.LITHKREN_VAULT),
		ANKOU("Ankou", TaskLocation.STRONGHOLD_OF_SECURITY, TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.CATACOMBS_OF_KOUREND),
		BLACK_DEMON("Black Demons", TaskLocation.TAVERLY_DUNGEON, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		BLACK_DRAGON("Black Dragons", TaskLocation.TAVERLY_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		BLOODVELD("Bloodveld", TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.GODWARS_DUNGEON, TaskLocation.SLAYER_TOWER, TaskLocation.CATACOMBS_OF_KOUREND),
		BLUE_DRAGON("Blue Dragons", TaskLocation.TAVERLY_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		BRINE_RAT("Brine Rats", TaskLocation.BRINE_RAT_CAVERN),
		BRONZE_DRAGON("Bronze Dragons", TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		IRON_DRAGON("Iron Dragons", TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		STEEL_DRAGON("Steel Dragons", TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		JELLY("Jellies", TaskLocation.RELEKKA_SLAYER_DUNGEON, TaskLocation.CATACOMBS_OF_KOUREND),
		CAVE_KRAKEN("Cave Kraken", TaskLocation.KRAKEN_COVE),
		SPIRITUAL_CREATURES("Spiritual Creatures", TaskLocation.GODWARS_DUNGEON),
		DAGANNOTH("Dagannoth", TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.LIGHTHOUSE, TaskLocation.WATERBIRTH_ISLAND),
		DARK_BEAST("Dark Beasts", TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.MOURNER_TUNNELS),
		DRAKE("Drakes", TaskLocation.KARUULM_SLAYER_DUNGEON),
		DUST_DEVIL("Dust Devils", TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.SMOKE_DUNGEON),
		FIRE_GIANT("Fire Giants", TaskLocation.KARUULM_SLAYER_DUNGEON, TaskLocation.BRIMHAVEN_DUNGEON, TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.WATERFALL_DUNGEON),
		GARGOYLE("Gargoyles", TaskLocation.SLAYER_TOWER),
		GREATER_DEMON("Greater Demons", TaskLocation.CHASM_OF_FIRE, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.TAVERLY_DUNGEON, TaskLocation.KARUULM_SLAYER_DUNGEON, TaskLocation.BRIMHAVEN_DUNGEON),
		HELLHOUND("Hellhounds", TaskLocation.STRONGHOLD_SLAYER_CAVE, TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.TAVERLY_DUNGEON, TaskLocation.KARUULM_SLAYER_DUNGEON),
		HYDRA("Hydras", TaskLocation.KARUULM_SLAYER_DUNGEON),
		KALPHITE("Kalphite", TaskLocation.KALPHITE_LAIR, TaskLocation.KALPHITE_CAVE),
		KURASK("Kurask", TaskLocation.RELEKKA_SLAYER_DUNGEON),
		NECHRYAEL("Nechryael", TaskLocation.CATACOMBS_OF_KOUREND, TaskLocation.SLAYER_TOWER),
		RUNE_DRAGON("Rune Dragons", TaskLocation.LITHKREN_VAULT),
		SKELETAL_WYVERN("Skeletal Wyverns", TaskLocation.ASGARNIAN_ICE_DUNGEON),
		SMOKE_DEVIL("Smoke Devils", TaskLocation.SMOKE_DEVIL_DUNGEON),
		TROLL("Trolls", TaskLocation.DEATH_PLATEAU),
		TUROTH("Turoth", TaskLocation.RELEKKA_SLAYER_DUNGEON),
		WATERFIEND("Waterfiends", TaskLocation.ANCIENT_CAVERN),
		WYRM("Wyrms", TaskLocation.KARUULM_SLAYER_DUNGEON);

		private String name;
		private final TaskLocation[] locations;

		Task(String name, TaskLocation... location) {
			this.name = name;
			this.locations = location;
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
		ANCIENT_CAVERN("Ancient Cavern",
			new Bounds(1730, 5310, 1800, 5379, -1),
			new Bounds(1730, 5310, 1800, 5379, 1)
		),
		CATACOMBS_OF_KOUREND("Catacombs of Kourend", new Bounds(1588, 9978, 1747, 10110, -1)),
		SLAYER_TOWER("Slayer Tower",
			new Bounds(3401, 3528, 3455, 3582, -1),
			new Bounds(3401, 3528, 3455, 3582, 2),
			new Bounds(3400, 9926, 3450, 9975, 3)),
		TAVERLY_DUNGEON("Taverly Dungeon", new Bounds(2812, 9729, 2969, 9855, -1),
			new Bounds(2812, 9729, 2969, 9855, 0)),
		WATERFALL_DUNGEON("Waterfall Dungeon", new Bounds(2558, 9860, 2596, 9916, -1)),
		STRONGHOLD_SLAYER_CAVE("Stronghold Slayer Cave", new Bounds(2385, 9766, 2496, 9841, -1)),
		ASGARNIAN_ICE_DUNGEON("Asgarnian Ice Dungeon", new Bounds(2980, 9531, 3085, 9601, -1)),
		BRIMHAVEN_DUNGEON("Brimhaven Dungeon", new Bounds(2625, 9404, 2751, 9599, -1)),
		ABYSSAL_AREA("Abyssal Area", new Bounds(3007, 4863, 3071, 4926, -1)),
		WATERBIRTH_ISLAND("Waterbirth Island", new Bounds(2426, 10109, 2565, 10178, -1)),
		GODWARS_DUNGEON("Godwars Dungeon", new Bounds(2816, 5255, 2942, 5370, -1)),
		BRINE_RAT_CAVERN("Brine Rat Cavern", new Bounds(2686, 10116, 2751, 10175, -1)),
		SMOKE_DUNGEON("Smoke Dungeon", new Bounds(3200, 9344, 3326, 9404, -1)),
		KALPHITE_LAIR("Kalphite Lair", new Bounds(3455, 9473, 3517, 9531, -1)),
		KARUULM_SLAYER_DUNGEON("Karuulm Slayer Dungeon", new Bounds(1248, 10146, 1390, 10282, -1)),
		LITHKREN_VAULT("Lithkren Vault", new Bounds(1536, 5058, 1596, 5118, -1)),
		LIGHTHOUSE("Lighthouse", new Bounds(2498, 4610, 2542, 4665, -1)),
		KALPHITE_CAVE("Kalphite Slayer Cave", new Bounds(3264, 9473, 3340, 9542, -1)),
		SMOKE_DEVIL_DUNGEON("Smoke Devil Dungeon", new Bounds(2345, 9408, 2431, 9470, -1)),
		MOURNER_TUNNELS("Mourner Tunnels", new Bounds(1855, 4608, 2045, 4670, -1)),
		EVIL_CHICKEN_LAIR("Evil Chicken's Lair", new Bounds(2445, 4353, 2493, 4408, -1)),
		CHASM_OF_FIRE("Chasm of Fire", new Bounds(1409, 10050, 1468, 10108, -1)),
		OGRE_ENCLAVE("Ogre Enclave", new Bounds(2561, 9407, 2621, 9469, -1));

		private final String name;
		private final Bounds[] boundaries;

		TaskLocation(String name, Bounds... boundaries) {
			this.name = name;
			this.boundaries = boundaries;
		}

		public String getName() {
			return name;
		}

		public boolean inside(Position pos) {
			for (Bounds b : boundaries)
				if (b.inBounds(pos.getX(), pos.getY(), pos.getZ(), 0))
					return true;
			return false;
		}
	}

}
