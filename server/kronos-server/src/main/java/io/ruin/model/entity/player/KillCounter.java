package io.ruin.model.entity.player;

import com.google.gson.annotations.Expose;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.NPCType;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class KillCounter {

	private static List<Function<Player, KillCounter>> bossList, slayerList;

	public static void register() {
		/* Setting kill counters on npc defs */
		/* bosses that are killed through normal means */
		set(p -> p.alchemicalHydraKills, "alchemical hydra");
		set(p -> p.kreeArraKills, "kree'Arra");
		set(p -> p.scurriusKills, "scurrius");
		set(p -> p.araxxorKills, "araxxor");
		set(p -> p.commanderZilyanaKills, "commander Zilyana");
		set(p -> p.generalGraardorKills, "general Graardor");
		set(p -> p.taintedUnicornKills, "tainted Unicorn");
		set(p -> p.ophidiaKills, "ophidia");
		set(p -> p.grotesqueGuardianKills, "dusk");
		set(p -> p.lizardmanShamanKills, "lizardman Shaman");
		set(p -> p.catalystBruteKills, "catalyst Brute");
		set(p -> p.catalystRangerKills, "catalyst Ranger");
		set(p -> p.elderChaosDruidKills, "elder Chaos Druid");
		set(p -> p.catalystMagerKills, "catalyst Mager");
		set(p -> p.krilTsutsarothKills, "k'ril Tsutsaroth");
		set(p -> p.dagannothRexKills, "dagannoth Rex");
		set(p -> p.dagannothPrimeKills, "dagannoth Prime");
		set(p -> p.dagannothSupremeKills, "dagannoth Supreme");
		set(p -> p.giantMoleKills, "giant Mole");
		set(p -> p.kalphiteQueenKills, "kalphite Queen");
		set(p -> p.yamaKills, "yama");
		set(p -> p.tormentedDemonKills, "tormented Demon");
		set(p -> p.kingBlackDragonKills, "king black Dragon");
		set(p -> p.callistoKills, "callisto");
		set(p -> p.venenatisKills, "venenatis");
		set(p -> p.vetionKills, "vet'ion");
		set(p -> p.chaosElementalKills, "chaos Elemental");
		set(p -> p.chaosFanaticKills, "chaos Fanatic");
		set(p -> p.crazyArchaeologistKills, "crazy Archaeologist");
		set(p -> p.scorpiaKills, 6615);
		set(p -> p.corporealBeastKills, "corporeal Beast");
		set(p -> p.zulrahKills, "zulrah");
		set(p -> p.krakenKills, 494);
		set(p -> p.thermonuclearSmokeDevilKills, "thermonuclear Smoke Devil");
		set(p -> p.cerberusKills, "cerberus");
		set(p -> p.abyssalSireKills, "abyssal Sire");
		set(p -> p.scurriusKills, "scurrius");
		set(p -> p.araxxorKills, "araxxor");
		set(p -> p.skotizoKills, "skotizo");
		set(p -> p.oborKills, "obor");
		set(p -> p.nightmareofAshihamaKills, "the nightmare");
		set(p -> p.leviathanKills, "leviathan");
		set(p -> p.whispererKills, "the whisperer");
		set(p -> p.dukeKills, "duke sucellus");
		set(p -> p.vardorvisKills, "vardorvis");
		set(p -> p.sarachnisKills, "sarachnis");
		set(p -> p.phantomMuspahKills, "phantom muspah");
		set(p -> p.nexKills, "nex");
		set(p -> p.derangedArchaeologistKills, "deranged Archaeologist");
		set(p -> p.elvargKills, 6118);
		set(p -> p.vorkathKills, "vorkath");
		set(p -> p.galvekKills, "galvek");
		set(p -> p.vyrewatchSentinelKills, "vyrewatch sentinel");
		set(p -> p.argentavisKills, "argentavis");
		set(p -> p.solHereditKills, "sol heredit");
		set(p -> p.malakarKills, "malakar");
		set(p -> p.voteBossKills, "vote boss");
		set(p -> p.globalBossKills, "blood reaper");
		set(p -> p.globalBossKills, "stone poltegeist");
		set(p -> p.balanceElementalKills, "Balance elemental");

		/* Slayer monsters */
		set(p -> p.crawlingHandKills, "crawling Hand");
		set(p -> p.caveBugKills, "cave Bug");
		set(p -> p.caveCrawlerKills, "cave Crawler");
		set(p -> p.bansheeKills, "banshee");
		set(p -> p.caveSlimeKills, "cave Slime");
		set(p -> p.rockslugKills, "rockslug");
		set(p -> p.desertLizardKills, "desert Lizard");
		set(p -> p.cockatriceKills, "cockatrice");
		set(p -> p.pyrefiendKills, "pyrefiend");
		set(p -> p.mogreKills, "mogre");
		set(p -> p.harpieBugSwarmKills, "harpie Bug Swarm");
		set(p -> p.wallBeastKills, "wall Beast");
		set(p -> p.killerwattKills, "killerwatt");
		set(p -> p.molaniskKills, "molanisk");
		set(p -> p.basiliskKills, "basilisk");
		set(p -> p.drakeKills, "drake");
		set(p -> p.seaSnakeKills, "sea Snake");
		set(p -> p.terrorDogKills, "terror Dog");
		set(p -> p.feverSpiderKills, "fever Spider");
		set(p -> p.infernalMageKills, "infernal Mage");
		set(p -> p.brineRatKills, "brine Rat");
		set(p -> p.bloodveldKills, "bloodveld");
		set(p -> p.jellyKills, "jelly");
		set(p -> p.turothKills, "turoth");
		set(p -> p.mutatedZygomiteKills, "mutated Zygomite");
		set(p -> p.caveHorrorKills, "cave Horror");
		set(p -> p.aberrantSpectreKills, "aberrant Spectre");
		set(p -> p.wyrmKills, "wyrm");
		set(p -> p.spiritualRangerKills, "spiritual Ranger");
		set(p -> p.dustDevilKills, "dust Devil");
		set(p -> p.spiritualWarriorKills, "spiritual Warrior");
		set(p -> p.kuraskKills, "kurask");
		set(p -> p.skeletalWyvernKills, "skeletal Wyvern");
		set(p -> p.gargoyleKills, "gargoyle");
		set(p -> p.nechryaelKills, "nechryael");
		set(p -> p.spiritualMageKills, "spiritual Mage");
		set(p -> p.abyssalDemonKills, "abyssal Demon");
		set(p -> p.caveKrakenKills, "cave Kraken");
		set(p -> p.darkBeastKills, "dark Beast");
		set(p -> p.smokeDevilKills, "smoke Devil");
//        set(p -> p.superiorCreatureKills, "superior Creature"); // need a proper way to add this
		set(p -> p.brutalBlackDragonKills, "brutal Black Dragon");
		set(p -> p.fossilIslandWyvernsKills, 7792, 7793, 7794, 7795);

		set(p -> p.torturedGorillaKills, "tortured gorilla");

		/* Other monsters */
		set(p -> p.adamantDragonKills, "adamant dragon");
		set(p -> p.runeDragonKills, "rune dragon");
		set(p -> p.demonicGorillaKills, "demonic gorilla");
		set(p -> p.jungleDemonKills, "jungle demon");
		set(p -> p.recruitwalkerKills, "recruit walker");
		set(p -> p.sergeantwalkerKills, "sergeant walker");
		set(p -> p.commanderwalkerKills, "commander walker");
		set(p -> p.superiorwalkerKills, "superior walker");
		set(p -> p.revenantKills, 7881, 7931, 7932, 7933, 7934, 7935, 7936, 7937, 7938, 7939, 7940);
		set(p -> p.revenantMaledictusKills, "revenant maledictus");


		/* Setting counter properties */
		LoginListener.register(p -> {
			p.kreeArraKills.setName("Kree'Arra").messageOnKill();
			p.voteBossKills.setName("Vote Boss").messageOnKill();
			p.commanderZilyanaKills.setName("Commander Zilyana").messageOnKill();
			p.generalGraardorKills.setName("General Graardor").messageOnKill();
			p.taintedUnicornKills.setName("Tainted Unicorn").messageOnKill();
			p.ophidiaKills.setName("Ophidia").messageOnKill();
			p.grotesqueGuardianKills.setName("Grotesque Guardians").messageOnKill();
			p.lizardmanShamanKills.setName("Lizardman Shaman").messageOnKill();
			p.elderChaosDruidKills.setName("Elder Chaos Druid").messageOnKill();
			p.catalystRangerKills.setName("Catalyst Ranger").messageOnKill();
			p.catalystMagerKills.setName("Catalyst Mager").messageOnKill();
			p.catalystBruteKills.setName("Catalyst Brute").messageOnKill();
			p.krilTsutsarothKills.setName("K'ril Tsutsaroth").messageOnKill();
			p.dagannothRexKills.setName("Dagannoth Rex").messageOnKill();
			p.dagannothPrimeKills.setName("Dagannoth Prime").messageOnKill();
			p.dagannothSupremeKills.setName("Dagannoth Supreme").messageOnKill();
			p.giantMoleKills.setName("Giant Mole").messageOnKill();
			p.kalphiteQueenKills.setName("Kalphite Queen").messageOnKill();
			p.kingBlackDragonKills.setName("King Dragon").messageOnKill();
			p.callistoKills.setName("Callisto").messageOnKill();
			p.venenatisKills.setName("Venenatis").messageOnKill();
			p.vetionKills.setName("Vet'ion").messageOnKill();
			p.chaosElementalKills.setName("Chaos Elemental").messageOnKill();
			p.chaosFanaticKills.setName("Chaos Fanatic").messageOnKill();
			p.crazyArchaeologistKills.setName("Crazy Archaeologist").messageOnKill();
			p.scorpiaKills.setName("Scorpia").messageOnKill();
			p.corporealBeastKills.setName("Corporeal Beast").messageOnKill();
			p.zulrahKills.setName("Zulrah").messageOnKill();
			p.jadCounter.setName("TzTok-Jad").messageOnKill();
			p.zukKills.setName("TzKal-Zuk").messageOnKill();
			p.krakenKills.setName("Kraken").messageOnKill();
			p.thermonuclearSmokeDevilKills.setName("Thermonuclear Smoke Devil").messageOnKill();
			p.cerberusKills.setName("Cerberus").messageOnKill();
			p.abyssalSireKills.setName("Abyssal Sire").messageOnKill();
			p.scurriusKills.setName("Scurrius").messageOnKill();
			p.araxxorKills.setName("Araxxor").messageOnKill();
			p.yamaKills.setName("Yama").messageOnKill();
			p.tormentedDemonKills.setName("Tormented Demon").messageOnKill();
			p.alchemicalHydraKills.setName("Alchemical Hydra").messageOnKill();
			p.skotizoKills.setName("Skotizo").messageOnKill();
			p.wintertodtKills.setName("Wintertodt").messageOnKill();
			p.oborKills.setName("Obor").messageOnKill();
			p.nightmareofAshihamaKills.setName("The nightmare").messageOnKill();
			p.leviathanKills.setName("Leviathan").messageOnKill();
			p.whispererKills.setName("The Whisperer").messageOnKill();
			p.dukeKills.setName("Duke Sucellus").messageOnKill();
			p.vardorvisKills.setName("Vardorvis").messageOnKill();
			p.sarachnisKills.setName("Sarachnis").messageOnKill();
			p.phantomMuspahKills.setName("Phantom Muspah").messageOnKill();
			p.nexKills.setName("Nex").messageOnKill();
			p.chambersofXericKills.setName("Chambers of Xeric").messageOnKill();
			p.olmOnlyKills.setName("CoX [Olm only]").messageOnKill();
			p.theatreOfBloodKills.setName("Theatre of Blood").messageOnKill();
			p.moonsOfPerilKills.setName("Moons of Peril").messageOnKill();
			p.tombsOfAmascutKills.setName("Tombs of Amascut").messageOnKill();
			p.dominionOfEchoesKills.setName("Dominion of Echoes").messageOnKill();
			p.derangedArchaeologistKills.setName("Deranged Archaeologist").messageOnKill();
			p.barrowsChestsLooted.setName("Barrows Chests").messageOnKill();
			p.elvargKills.setName("Elvarg").messageOnKill();
			p.vorkathKills.setName("Vorkath").messageOnKill();
			p.argentavisKills.setName("Argentavis").messageOnKill();
			p.solHereditKills.setName("Sol Heredit").messageOnKill();
			p.malakarKills.setName("Malakar").messageOnKill();
			p.galvekKills.setName("Galvek").messageOnKill();
			p.balanceElementalKills.setName("Balance elemental").messageOnKill();

			p.crawlingHandKills.setName("Crawling Hands");
			p.caveBugKills.setName("Cave Bugs");
			p.caveCrawlerKills.setName("Cave Crawlers");
			p.bansheeKills.setName("Banshees");
			p.caveSlimeKills.setName("Cave Slimes");
			p.rockslugKills.setName("Rockslugs");
			p.desertLizardKills.setName("Desert Lizards");
			p.cockatriceKills.setName("Cockatrices");
			p.pyrefiendKills.setName("Pyrefiends");
			p.mogreKills.setName("Mogres");
			p.harpieBugSwarmKills.setName("Harpie Bug Swarms");
			p.wallBeastKills.setName("Wall Beasts");
			p.killerwattKills.setName("Killerwatts");
			p.molaniskKills.setName("Molanisks");
			p.basiliskKills.setName("Basilisks");
			p.drakeKills.setName("Drake");
			p.seaSnakeKills.setName("Sea Snakes");
			p.terrorDogKills.setName("Terror Dogs");
			p.feverSpiderKills.setName("Fever Spiders");
			p.infernalMageKills.setName("Infernal Mages");
			p.brineRatKills.setName("Brine Rats");
			p.bloodveldKills.setName("Bloodvelds");
			p.jellyKills.setName("Jellies");
			p.turothKills.setName("Turoth");
			p.mutatedZygomiteKills.setName("Mutated Zygomites");
			p.caveHorrorKills.setName("Cave Horrors");
			p.aberrantSpectreKills.setName("Aberrant Spectres");
			p.wyrmKills.setName("Wyrms");
			p.spiritualRangerKills.setName("Spiritual Rangers");
			p.dustDevilKills.setName("Dust Devils");
			p.spiritualWarriorKills.setName("Spiritual Warriors");
			p.kuraskKills.setName("Kurask");
			p.skeletalWyvernKills.setName("Skeletal Wyverns");
			p.gargoyleKills.setName("Gargoyles");
			p.nechryaelKills.setName("Nechryael");
			p.spiritualMageKills.setName("Spiritual Mages");
			p.abyssalDemonKills.setName("Abyssal Demons");
			p.caveKrakenKills.setName("Cave Krakens");
			p.darkBeastKills.setName("Dark Beasts");
			p.smokeDevilKills.setName("Smoke Devils");
			p.superiorCreatureKills.setName("Superior Creatures");
			p.brutalBlackDragonKills.setName("Brutal Black Dragons");
			p.fossilIslandWyvernsKills.setName("Fossil Island Wyvernss");
		});

		/* Building list that will be used in the boss interface  - Bosses not ingame are commented out*/
		bossList = Arrays.asList(
			p -> p.kreeArraKills,
			p -> p.commanderZilyanaKills,
			p -> p.generalGraardorKills,
			p -> p.taintedUnicornKills,
			p -> p.ophidiaKills,
			p -> p.lizardmanShamanKills,
			p -> p.catalystBruteKills,
			p -> p.catalystMagerKills,
			p -> p.catalystRangerKills,
			p -> p.elderChaosDruidKills,
			p -> p.krilTsutsarothKills,
			p -> p.dagannothRexKills,
			p -> p.dagannothPrimeKills,
			p -> p.dagannothSupremeKills,
			p -> p.giantMoleKills,
			p -> p.kalphiteQueenKills,
			p -> p.kingBlackDragonKills,
			p -> p.callistoKills,
			p -> p.venenatisKills,
			p -> p.vetionKills,
			p -> p.chaosElementalKills,
			p -> p.chaosFanaticKills,
			p -> p.crazyArchaeologistKills,
			p -> p.scorpiaKills,
			p -> p.barrowsChestsLooted,
			p -> p.corporealBeastKills,
			p -> p.zulrahKills,
			p -> p.jadCounter,
			p -> p.zukKills,
			p -> p.krakenKills,
			p -> p.thermonuclearSmokeDevilKills,
			p -> p.cerberusKills,
			p -> p.abyssalSireKills,
			p -> p.skotizoKills,
			p -> p.wintertodtKills,
			p -> p.elvargKills,
//                p -> p.oborKills,
			p -> p.chambersofXericKills,
			p -> p.theatreOfBloodKills,
			p -> p.voteBossKills,
//                p -> p.derangedArchaeologistKills
			p -> p.vorkathKills,
			p -> p.argentavisKills,
			p -> p.solHereditKills,
			p -> p.galvekKills
		);

		/* Slayer list */
		slayerList = Arrays.asList(
			p -> p.crawlingHandKills,
			p -> p.caveBugKills,
			p -> p.caveCrawlerKills,
			p -> p.bansheeKills,
			p -> p.caveSlimeKills,
			p -> p.rockslugKills,
			p -> p.desertLizardKills,
			p -> p.cockatriceKills,
			p -> p.pyrefiendKills,
//                p -> p.mogreKills,
//                p -> p.harpieBugSwarmKills,
//                p -> p.wallBeastKills,
//                p -> p.killerwattKills,
//                p -> p.molaniskKills,
			p -> p.basiliskKills,
//                p -> p.seaSnakeKills,
//                p -> p.terrorDogKills,
//                p -> p.feverSpiderKills,
			p -> p.infernalMageKills,
//                p -> p.brineRatKills,
			p -> p.bloodveldKills,
			p -> p.jellyKills,
			p -> p.turothKills,
//                p -> p.mutatedZygomiteKills,
			p -> p.caveHorrorKills,
			p -> p.aberrantSpectreKills,
			p -> p.spiritualRangerKills,
			p -> p.dustDevilKills,
			p -> p.spiritualWarriorKills,
			p -> p.kuraskKills,
			p -> p.skeletalWyvernKills,
			p -> p.gargoyleKills,
			p -> p.nechryaelKills,
			p -> p.spiritualMageKills,
			p -> p.abyssalDemonKills,
			p -> p.caveKrakenKills,
			p -> p.darkBeastKills,
			p -> p.smokeDevilKills,
//                p -> p.superiorCreatureKills,
			p -> p.brutalBlackDragonKills
//                p -> p.fossilIslandWyvernsKills
		);

		/* Interface handler */
		InterfaceHandler.register(Interface.KILL_COUNTER, h -> {
			h.actions[16] = (SlotAction) (p, slot) -> p.activeKillLogSlot = slot;
			h.actions[25] = (SimpleAction) KillCounter::resetStreak;
			h.closedAction = (player, integer) -> {
				player.activeKillLogList = null;
				player.activeKillLogSlot = -1;
			};
		});
	}

	public static void openOwnBoss(Player player) {
		open(player, player, bossList, "Boss Kill Log");
	}

	public static void openOwnSlayer(Player player) {
		open(player, player, slayerList, "Slayer Kill Log");
	}

	public static void openBoss(Player player, Player killer) {
		open(player, killer, bossList, killer.getName() + "'s Boss Kill Log");
	}

	public static void openSlayer(Player player, Player killer) {
		open(player, killer, slayerList, killer.getName() + "'s Slayer Kill Log");
	}

	public static int getKills(String name, Player player) {
		int kills = 0;
		for (Function<Player, KillCounter> bosses :
			bossList) {
			KillCounter kc = bosses.apply(player);
			if (kc.getName().equalsIgnoreCase(name)) {
				kills = kc.getKills();
			}
		}
		return kills;
	}

	private static void resetStreak(Player p) {
		if (p.activeKillLogList == null || p.activeKillLogSlot < 0 || p.activeKillLogSlot >= p.activeKillLogList.size()) {
			return;
		}
		KillCounter counter = p.activeKillLogList.get(p.activeKillLogSlot).apply(p);
		counter.resetStreak();
		p.sendMessage("Your " + counter.getName() + " streak has been reset.");
		p.getPacketSender().sendClientScript(1588, "i", p.activeKillLogSlot);
		p.activeKillLogSlot = -1;
	}

	private static void open(Player player, Player killer, List<Function<Player, KillCounter>> list, String title) {
		StringBuilder names = new StringBuilder();
		StringBuilder totalCounts = new StringBuilder();
		StringBuilder streaks = new StringBuilder();
		for (Function<Player, KillCounter> f : list) {
			KillCounter kc = f.apply(player);
			if (kc.getName() == null) {
				names.append("null");
			} else {
				names.append(kc.getName());
			}
			names.append("|");
			totalCounts.append(NumberUtils.formatNumber(kc.getKills()));
			totalCounts.append("|");
			streaks.append(NumberUtils.formatNumber(kc.getStreak()));
			streaks.append("|");
		}
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.KILL_COUNTER);
		player.getPacketSender().sendClientScript(1584,
			"sssis",
			names.toString(),
			totalCounts.toString(),
			streaks.toString(),
			list.size(),
			title
		);
		if (player == killer) {
			player.getPacketSender().sendIfEvents(Interface.KILL_COUNTER, 16, 0, list.size(), 2);
			player.activeKillLogList = list;
		} else {
			player.getPacketSender().setHidden(Interface.KILL_COUNTER, 16, true);
		}
	}

	private static void set(Function<Player, KillCounter> get, String... names) {
		List<String> search = Arrays.asList(names);
		NPCType.cached.values().stream()
			.filter(Objects::nonNull)
			.filter(def -> {
				for (String name : search)
					if (def.name.toLowerCase().contains(name.toLowerCase())) {
						return true;
					}
				return false;
			}).forEach(def -> def.killCounter = get);
	}

	private static void set(Function<Player, KillCounter> get, Integer... ids) {
		List<Integer> search = Arrays.asList(ids);
		NPCType.cached.values().stream()
			.filter(Objects::nonNull)
			.filter(def -> search.contains(def.id))
			.forEach(def -> def.killCounter = get);
	}

	private int kills;
	private int streak;
	private transient String name;
	private transient boolean messageOnKill;

	public void increment(Player player) {
		kills++;
		streak++;
		combatAchievementKillCheck(player);
		if (messageOnKill)
			player.sendMessage("Your " + name + " kill count is: " + Color.RED.wrap(NumberUtils.formatNumber(kills)));
	}

	private void combatAchievementKillCheck(Player player) {
		if (name == null)
			return;
		if (name.equalsIgnoreCase("Pyrefiends")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PYREFIEND_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Basilisks")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.BASILISK_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Bloodvelds")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.BLOODVELD_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Jellies")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.JELLY_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Cave Horrors")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CAVE_HORROR_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Aberrant Spectres")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.ABERRANT_SPECTRE_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Barrows Chests")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.BARROWS_NOVICE.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Wyrms")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.WYRM_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Dust devils")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DUST_DEVIL_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Skeletal wyverns")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SKELATAL_WYVERN_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Gargoyles")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.GARGOYLE_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Nechryael")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NECHRYAEL_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Drake")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DRAKE_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Smoke Devils")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SMOKE_DEVIL_ADEPT.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Abyssal demons")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.ABYSSAL_DEMON_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Lizardman Shaman")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.LIZARDMAN_SHAMAN_ADEPT.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Wintertodt")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.WINTERTODT_ADEPT.ordinal())).
				getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.WINTERTODT_NOVICE.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Sarachnis")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SARACHNIS_ADEPT.ordinal())).
				getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SARACHNIS_NOVICE.ordinal())).
				getCombatAchievement()).check(player);
		} else if (name.equalsIgnoreCase("Chaos Fanatic")) {
			player.chaosFanaticKillsSincePotion++;
			if (player.chaosFanaticKillsSincePotion >= 10) {
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PRAYING_TO_THE_GODS.ordinal())).
					getCombatAchievement()).check(player);
			}
		} else if (name.equalsIgnoreCase("Thermonuclear Smoke Devil")) {
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.THERMONUCLEAR_SMOKE_DEVIL_ADEPT.ordinal())).
				getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.THERMONUCLEAR_SMOKE_DEVIL_NOVICE.ordinal())).
				getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.THERMONUCLEAR_SMOKE_DEVIL_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
		}
	}

	public void resetStreak() {
		streak = 0;
	}

	public int getKills() {
		return kills;
	}

	public int getStreak() {
		return streak;
	}

	public String getName() {
		return name;
	}

	public KillCounter setName(String name) {
		this.name = name;
		return this;
	}

	public KillCounter messageOnKill() {
		messageOnKill = true;
		return this;
	}

	public void addToKillsAndStreakPlus5(Player player) {
		kills += 5;
		streak += 5;
	}

	public void addToKillsAndStreakPlus10(Player player) {
		kills += 10;
		streak += 10;
	}


	public void addToKillsAndStreak(Player p, int i) {
	}
}
