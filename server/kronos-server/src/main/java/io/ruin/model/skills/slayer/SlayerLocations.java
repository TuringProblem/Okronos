package io.ruin.model.skills.slayer;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;

public enum SlayerLocations {
	ABYSSAL_DEMON("Abyssal Demon", new Position(3420, 3563, 2)),
	GREATER_ABYSSAL_DEMON("Greater Abyssal Demon", new Position(1672, 10086, 0)),
	ABERRANT_SPECTRE("Aberrant Spectre", new Position(2452, 9776, 0)),
	ADAMANT_DRAGON("Adamant Dragon", new Position(1562, 5075, 0)),
	ANKOU("Ankou", new Position(1653, 9996, 0)),
	AVIANSIE("Aviansie", new Position(2870, 5264, 2)),
	BASILISK("Basilisk", new Position(2741, 10007, 0)),
	BASILISK_KNIGHT("Basilisk Knight", null),
	BRINE_RAT("Brine Rat", new Position(2716, 10133, 0)),
	FOSSIL_ISLAND_WYVERN("Fossil Island Wyvern", new Position(3603, 10229, 0)),
	SPITTING_WYVERN("Spitting Wyvern", new Position(3603, 10229, 0)),
	LONGTAILED_WYVERN("Long-tailed Wyvern", new Position(3603, 10229, 0)),
	ANCIENT_WYVERN("Ancient Wyvern", new Position(3603, 10229, 0)),
	LIZARDMAN("Lizardman", new Position(1452, 3692, 0)),
	LIZARDMEN("Lizardmen", new Position(1452, 3692, 0)),
	MITHRIL_DRAGON("Mithril Dragon", new Position(1777, 5344, 1)),
	MUTATED_ZYGOMITES("Mutated Zygomite", new Position(3681, 3857, 0)),
	RED_DRAGON("Red Dragon", new Position(2718, 9503, 0)),
	WATERFIEND("Waterfiend", new Position(1741, 5343, 0)),
	BLACK_DEMON("Black Demon", new Position(2876, 9772, 0)),
	BLACK_DRAGON("Black Dragon", new Position(2460, 4386, 0)),
	BLOODVELD("Bloodveld", new Position(1678, 10075, 0)),
	BLUE_DRAGON("Blue Dragon", new Position(2894, 9792, 0)),
	BRONZE_DRAGON("Bronze Dragon", new Position(2722, 9486, 0)),
	IRON_DRAGON("Iron Dragon", new Position(2710, 9469, 0)),
	STEEL_DRAGON("Steel Dragon", new Position(2710, 9469, 0)),
	JELLY("Jelly", new Position(1694, 9998, 0)),
	CAVE_KRAKEN("Cave Kraken", new Position(2292, 10008, 0)),
	DAGANNOTH("Dagannoth", new Position(1678, 9997, 0)),
	DARK_BEAST("Dark Beast", new Position(2029, 4650, 0)),
	DRAKE("Drake", new Position(1312, 10222, 1)),
	DUST_DEVIL("Dust Devil", new Position(1714, 10029, 0)),
	FIRE_GIANT("Fire Giant", new Position(1624, 10048, 0)),
	GARGOYLE("Gargoyle", new Position(3439, 3538, 2)),
	GREATER_DEMON("Greater Demon", new Position(1424, 10084, 2)),
	HELLHOUND("Hellhound", new Position(1640, 10056, 0)),
	HYDRA("Hydra", new Position(1334, 10237, 0)),
	KALPHITE("Kalphite", new Position(3500, 9500, 0)),
	KURASK("Kurask", new Position(2705, 9996, 0)),
	NECHRYAEL("Nechryael", new Position(3433, 3566, 2)),
	RUNE_DRAGON("Rune Dragon", new Position(1580, 5073, 0)),
	SKELETAL_WYVERN("Skeletal Wyvern", new Position(3056, 9555, 0)),
	SMOKE_DEVIL("Smoke Devil", new Position(2379, 9452, 0)),
	TROLL("Troll", new Position(2846, 10108, 2)),
	TUROTH("Turoth", new Position(2731, 9999, 0)),
	WYRM("Wyrm", new Position(1281, 10188, 0)),
	SOL_HEREDIT("Sol Heredit", null),
	ARGENTAVIS("Argentavis", null),
	GENERAL_GRAARDOR("General Graardor", null),
	COMMANDER_ZILYANA("Commander Zilyana", null),
	KREE_ARRA("Kree'arra", null),
	KRIL("K'ril Tsutsaroth", null),
	HUEY("The Hueycoatl", null),
	CORP_BEAST("Corporeal Beast", null),
	NEX("Nex", null),
	ECLIPSE_MOON("Eclipse Moon", null),
	BLUE_MOON("Blue Moon", null),
	BLOOD_MOON("Blood Moon", null),
	SARACHNIS("Sarachnis", null),
	DAG_PRIME("Dagannoth Prime", null),
	DAG_REX("Dagannoth Rex", null),
	DAG_SUPREME("Dagannoth Supreme", null),
	DERANGED_ARCHAEOLOGIST("Deranged Archaeologist", null),
	GIANT_MOLE("Giant Mole", null),
	SCURRIUS("Scurrius", null),
	CHAOS_FANATIC("Chaos Fanatic", null),
	CRAZY_ARCH("Crazy Archaeologist", null),
	SCORPIA("Scorpia", null),
	KBD("King Black Dragon", null),
	CHAOS_ELE("Chaos Elemental", null),
	REV_MALEDICTUS("Revenant Maledictus", null),
	CALVARION("Calvar'ion", null),
	VETION("Vet'ion", null),
	SPINDEL("Spindel", null),
	VENENATIS("Venenatis", null),
	ARTIO("Artio", null),
	CALLISTO("Callisto", null),
	AMOXLIATL("Amoxliatl", null),
	ZULRAH("Zulrah", null),
	VORKATH("Vorkath", null),
	PHANTOM_MUSPAH("Phantom Muspah", null),
	THE_NIGHTMARE("The Nightmare", null),
	DUKE("Duke Sucellus", null),
	LEVIATHAN("The Leviathan", null),
	WHISPERER("The Whisperer", null),
	VARDORVIS("Vardorvis", null),
	OBOR("Obor", null),
	BRYOPHYTA("Bryophyta", null),
	MIMIC("The Mimic", null),
	HESPORI("Hespori", null),
	SKOTIZO("Skotizo", null),
	DUSK("Dusk", null),
	DAWN("Dawn", null),
	SIRE("Abyssal Sire", null),
	KRAKEN("Kraken", null),
	CERBERUS("Cerberus", null),
	ARAXXOR("Araxxor", null),
	THERMONUCLEAR_SMOKE_DEVIL("Thermonuclear smoke devil", null),
	ALCHEMICAL_HYDRA("Alchemical Hydra", null),
	JAD("TzTok-Jad", null),
	ZUK("TzKal-Zuk", null),
	CORRUPTED_HUNLLEF("Corrupted Hunllef", null),
	CRYSTALLINE_HUNLLEF("Crystalline Hunllef", null),
	GREAT_OLM("Great Olm", null),
	VERZIK("Verzik Vitur", null),
	WARDEN("Elidinis' Warden", null),
	GALVEK("Galvek", null),
	DEMONIC_GORILLAS("Demonic Gorillas", null),
	OPHIDIA("Ophidia", null),
	MALAKAR("Malakar", null),
	CATALYST_BRUTE("Catalyst Brute", null),
	CATALYST_RANGER("Catalyst Ranger", null),
	CATALYST_MAGER("Catalyst Mager", null),
	ARMOURED_ZOMBIES("Armoured Zombie", null),
	VYREWATCH_SENTINTEL("Vyrewatch Sentinel", null),
	;

	private String name;
	private Position teleportPosition;

	SlayerLocations(String name, Position teleportPosition) {
		this.name = name;
		this.teleportPosition = teleportPosition;
	}

	public Position getTeleportPosition() {
		return teleportPosition;
	}

	public String getName() {
		return name;
	}

	private static SlayerLocations getSlayerTaskLocation(String name) {
		for (SlayerLocations location : SlayerLocations.values()) {
			if (location.getName().equalsIgnoreCase(name)) {
				return location;
			}
		}
		return null;
	}

	private static void handleInstance(Player player, String name) {
		switch (name) {
			case "sol heredit":
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
				player.getInstanceTokenInterface().startInstance(player, true);
				break;
		}
	}

	public static void teleportToSlayerTask(Player player, String name) {
		SlayerLocations location = getSlayerTaskLocation(name);
		if (location == null) {
			player.sendMessage("No teleport location found.");
			return;
		}
		if (player.teleportListener != null && !player.teleportListener.allow(player)) {
			return;
		}
		if (player.getCombat().isDefending(17)) {
			player.sendMessage("You can't use this in combat.");
			return;
		}
		if (location.teleportPosition == null) {
			handleInstance(player, name.toLowerCase());
			return;
		}
		ModernTeleport.teleport(player, location.teleportPosition);
	}
}
