package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.handlers.OptionScroll;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.ground.GroundItem;

public class CompletionistCape {

	public static void check(Player player) {
		if (!cheackCluecounts(player)) {
			player.sendMessage(Color.DARK_RED.wrap("You need to complete 100 of each clue type to use a completionist cape/hood."));
			return;
		}
		if (!checkTotal99s(player)) {
			return;
		}
		if (!checkKillCounts(player)) {
			player.sendMessage(Color.DARK_RED.wrap("You need to have 100 kill count in every boss besides jad/zuk to use a completionist cape/hood."));
			player.sendMessage(Color.DARK_RED.wrap("Jad & Zuk require 1 kill count"));
			return;
		}
		Item hood = player.getEquipment().get(Equipment.SLOT_HAT);
		if (hood != null && hood.getDef().completionistType)
			remove(player, hood);
		Item cape = player.getEquipment().get(Equipment.SLOT_CAPE);
		if (cape != null && cape.getDef().completionistType)
			remove(player, cape);
	}

	public static boolean wearing(Player player) {
		Item capeId = player.getEquipment().get(Equipment.SLOT_CAPE);
		return capeId != null && capeId.getDef().completionistType;
	}

	private static void remove(Player player, Item item) {
		int id = item.getId();
		item.remove();
		if (player.getInventory().add(id, 1) == 1) {
			player.sendMessage("<col=aa0000>Your " + item.getDef().name + " has been unequipped and added to your inventory because you no longer meet the requirements to wear it.");
			return;
		}
		if (player.getBank().add(id, 1) == 1) {
			player.sendMessage("<col=aa0000>Your " + item.getDef().name + " has been unequipped and added to your bank because you no longer meet the requirements to wear it.");
			return;
		}
		new GroundItem(id, 1).spawn();
		player.sendMessage("<col=aa0000>Your " + item.getDef().name + " has been unequipped and dropped to the floor because you no longer meet the requirements to wear it and have no space in your inventory or bank to store it.");
	}

	public static boolean checkTotal99s(Player player) {
		return player.getStats().total99s >= 22;
	}

	public static boolean cheackCluecounts(Player player) {
		return player.beginnerClueCount >= 100 && player.easyClueCount >= 100 &&
			player.medClueCount >= 100 && player.hardClueCount >= 100 &&
			player.eliteClueCount >= 100;
	}

	public static boolean checkKillCounts(Player player) {
		return player.corporealBeastKills.getKills() >= 300 && player.cerberusKills.getKills() >= 300
			&& player.kreeArraKills.getKills() >= 300 && player.commanderZilyanaKills.getKills() >= 300
			&& player.generalGraardorKills.getKills() >= 300 && player.krilTsutsarothKills.getKills() >= 300
			&& player.dagannothPrimeKills.getKills() >= 300 && player.dagannothRexKills.getKills() >= 300
			&& player.dagannothSupremeKills.getKills() >= 300 && player.giantMoleKills.getKills() >= 300
			&& player.kalphiteQueenKills.getKills() >= 300 && player.kingBlackDragonKills.getKills() >= 300
			&& player.callistoKills.getKills() >= 300 && player.venenatisKills.getKills() >= 300
			&& player.vetionKills.getKills() >= 300 && player.chaosElementalKills.getKills() >= 300
			&& player.chaosFanaticKills.getKills() >= 300 && player.crazyArchaeologistKills.getKills() >= 300
			&& player.scorpiaKills.getKills() >= 300 && player.barrowsChestsLooted.getKills() >= 300
			&& player.zulrahKills.getKills() >= 300 && player.krakenKills.getKills() >= 300
			&& player.thermonuclearSmokeDevilKills.getKills() >= 300
			&& player.abyssalSireKills.getKills() >= 300 && player.skotizoKills.getKills() >= 300
			&& player.wintertodtKills.getKills() >= 100 && player.nightmareofAshihamaKills.getKills() >= 300
			&& player.oborKills.getKills() >= 300 && player.chambersofXericKills.getKills() >= 100
			&& player.theatreOfBloodKills.getKills() >= 100 && player.sarachnisKills.getKills() >= 300
			&& player.alchemicalHydraKills.getKills() >= 300 && player.grotesqueGuardianKills.getKills() >= 300
			&& player.nexKills.getKills() >= 300 && player.jadCounter.getKills() >= 1 && player.zukKills.getKills() >= 1;
	}

	public static void setCompletionstCapeMinKillCount(Player player) {
//        player.jadCounter.setKills(1); player.jadCounter.setStreak(1);
//        player.zukKills.setKills(1); player.zukKills.setStreak(1);
//
//        player.corporealBeastKills.setKills(100); player.corporealBeastKills.setStreak(100);
//        player.cerberusKills.setKills(100); player.cerberusKills.setStreak(100);
//        player.kreeArraKills.setKills(100); player.kreeArraKills.setStreak(100);
//        player.commanderZilyanaKills.setKills(100); player.commanderZilyanaKills.setStreak(100);
//        player.generalGraardorKills.setKills(100); player.generalGraardorKills.setStreak(100);
//        player.krilTsutsarothKills.setKills(100); player.krilTsutsarothKills.setStreak(100);
//        player.dagannothPrimeKills.setKills(100); player.dagannothPrimeKills.setStreak(100);
//        player.dagannothSupremeKills.setKills(100); player.dagannothSupremeKills.setStreak(100);
//        player.dagannothRexKills.setKills(100); player.dagannothRexKills.setStreak(100);
//        player.giantMoleKills.setKills(100); player.giantMoleKills.setStreak(100);
//        player.kalphiteQueenKills.setKills(100); player.kalphiteQueenKills.setStreak(100);
//        player.kingBlackDragonKills.setKills(100); player.kingBlackDragonKills.setStreak(100);
//        player.callistoKills.setKills(100); player.callistoKills.setStreak(100);
//        player.venenatisKills.setKills(100); player.venenatisKills.setStreak(100);
//        player.vetionKills.setKills(100); player.vetionKills.setStreak(100);
//        player.chaosElementalKills.setKills(100); player.chaosElementalKills.setStreak(100);
//        player.chaosFanaticKills.setKills(100); player.chaosFanaticKills.setStreak(100);
//        player.cerberusKills.setKills(100); player.cerberusKills.setStreak(100);
//        player.crazyArchaeologistKills.setKills(100); player.crazyArchaeologistKills.setStreak(100);
//        player.scorpiaKills.setKills(100); player.scorpiaKills.setStreak(100);
//        player.barrowsChestsLooted.setKills(100); player.barrowsChestsLooted.setStreak(100);
//        player.zulrahKills.setKills(100); player.zulrahKills.setStreak(100);
//        player.krakenKills.setKills(100); player.krakenKills.setStreak(100);
//        player.thermonuclearSmokeDevilKills.setKills(100); player.thermonuclearSmokeDevilKills.setStreak(100);
//        player.abyssalSireKills.setKills(100); player.abyssalSireKills.setStreak(100);
//        player.skotizoKills.setKills(100); player.skotizoKills.setStreak(100);
//        player.wintertodtKills.setKills(100); player.wintertodtKills.setStreak(100);
//        player.nightmareofAshihamaKills.setKills(100); player.nightmareofAshihamaKills.setStreak(100);
//        player.nexKills.setKills(100); player.nexKills.setStreak(100);
//        player.oborKills.setKills(100); player.oborKills.setStreak(100);
//        player.chambersofXericKills.setKills(100); player.chambersofXericKills.setStreak(100);
//        player.theatreOfBloodKills.setKills(100); player.theatreOfBloodKills.setStreak(100);
//        player.sarachnisKills.setKills(100); player.sarachnisKills.setStreak(100);
//        player.alchemicalHydraKills.setKills(100); player.alchemicalHydraKills.setStreak(100);
//        player.grotesqueGuardianKills.setKills(100); player.grotesqueGuardianKills.setStreak(100);
//        player.vorkathKills.setKills(100); player.vorkathKills.setStreak(100);
//        player.derangedArchaeologistKills.setKills(100); player.derangedArchaeologistKills.setStreak(100);
//
//        player.crawlingHandKills.setKills(100); player.crawlingHandKills.setStreak(100);
//        player.caveBugKills.setKills(100); player.caveBugKills.setStreak(100);
//        player.caveCrawlerKills.setKills(100); player.caveCrawlerKills.setStreak(100);
//        player.bansheeKills.setKills(100); player.bansheeKills.setStreak(100);
//        player.caveSlimeKills.setKills(100); player.caveSlimeKills.setStreak(100);
//        player.rockslugKills.setKills(100); player.rockslugKills.setStreak(100);
//        player.desertLizardKills.setKills(100); player.desertLizardKills.setStreak(100);
//        player.cockatriceKills.setKills(100); player.cockatriceKills.setStreak(100);
//        player.pyrefiendKills.setKills(100); player.pyrefiendKills.setStreak(100);
//        player.mogreKills.setKills(100); player.mogreKills.setStreak(100);
//        player.harpieBugSwarmKills.setKills(100); player.harpieBugSwarmKills.setStreak(100);
//        player.basiliskKills.setKills(100); player.basiliskKills.setStreak(100);
//        player.infernalMageKills.setKills(100); player.infernalMageKills.setStreak(100);
//        player.bloodveldKills.setKills(100); player.bloodveldKills.setStreak(100);
//        player.jellyKills.setKills(100); player.jellyKills.setStreak(100);
//        player.turothKills.setKills(100); player.turothKills.setStreak(100);
//        player.caveHorrorKills.setKills(100); player.caveHorrorKills.setStreak(100);
//        player.aberrantSpectreKills.setKills(100); player.aberrantSpectreKills.setStreak(100);
//        player.kuraskKills.setKills(100); player.kuraskKills.setStreak(100);
//        player.spiritualMageKills.setKills(100); player.spiritualMageKills.setStreak(100);
//        player.spiritualRangerKills.setKills(100); player.spiritualRangerKills.setStreak(100);
//        player.spiritualWarriorKills.setKills(100); player.spiritualWarriorKills.setStreak(100);
//        player.skeletalWyvernKills.setKills(100); player.skeletalWyvernKills.setStreak(100);
//        player.gargoyleKills.setKills(100); player.gargoyleKills.setStreak(100);
//        player.nechryaelKills.setKills(100); player.nechryaelKills.setStreak(100);
//        player.abyssalDemonKills.setKills(100); player.abyssalDemonKills.setStreak(100);
//        player.darkBeastKills.setKills(100); player.darkBeastKills.setStreak(100);
//        player.smokeDevilKills.setKills(100); player.smokeDevilKills.setStreak(100);
//        player.fossilIslandWyvernsKills.setKills(100); player.fossilIslandWyvernsKills.setStreak(100);
//        player.brutalBlackDragonKills.setKills(100); player.brutalBlackDragonKills.setStreak(100);
//
//        player.superiorCreatureKills.setKills(100); player.superiorCreatureKills.setStreak(100);

		player.beginnerClueCount = 100;
		player.easyClueCount = 100;
		player.medClueCount = 100;
		player.hardClueCount = 100;
		player.eliteClueCount = 100;
		player.masterClueCount = 100;

		player.sendMessage("Kill counts set to: " + Color.DARK_RED.wrap("100"));
		player.sendMessage("You have now unlocked: " + Color.DARK_RED.wrap("Completionist cape"));

	}

	static float Percentage;

	/**
	 * Percentage calculation formula that uses a (float) for the percentage
	 * along with 2 double's. Current and total completionist progress.
	 * The current is multiplied by 100 then divided by the total progress.
	 *
	 * @param player
	 */
	public static void calculateCompletionistProgress(Player player) {
		double current = player.currentCompletionistProgress;
		double total = player.totalCompletionistProgress;

		Percentage = (float) ((current * 100) / total);

		player.sendMessage("Total Completionist Progress:" + Color.DARK_RED.wrap(Percentage + "%"));
	}

	public static void testCompletionistProgress(Player player) {
		player.currentCompletionistProgress = 10500;
		player.sendMessage(Color.DARK_RED.wrap("Current Completionist Progress has been set to: " + player.currentCompletionistProgress));
		calculateCompletionistProgress(player);
	}

	public static void handleClose(Player player) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
	}

	public static void buildCompletionistCapeRequirements(Player player) {
		OptionScroll.open(player, Color.DARK_RED + "Completionist Cape Requirements",
			new Option(" "),
			new Option("<img=66>Achieve 2,277 Total Level " + Color.DARK_RED + NumberUtils.formatNumber(player.getStats().totalLevel) + "/2,277</col>"),
			new Option("<img=20>Obtain 15 Unique Pets " + Color.DARK_RED + NumberUtils.formatNumber(player.uniquePetsObtained) + "/15</col>"),
			new Option("<img=30>Complete 250 Slayer Tasks " + Color.DARK_RED + NumberUtils.formatNumber(player.slayerTasksCompleted) + "/250</col>"),
			new Option(" "),
			new Option("<img=31>Complete 150 Combat Achievements " + Color.DARK_RED + NumberUtils.formatNumber(player.totalWorldTaskPoints) + "/150</col>"),
			//new Option("<img=63>Obtain 200 Items on the Collection Log " + Color.DARK_RED + NumberUtils.formatNumber(CollectionLogInfo.TOTAL_COLLECTABLES) + "/200</col>"),
			new Option(" "),
			new Option("Total Completionist Progress: " + Color.CYAN + ((float) ((player.currentCompletionistProgress * 100) / player.totalCompletionistProgress)) + "%</col>"),
			new Option(" "),
			new Option(" "),
			new Option("Jad Kills: " + Color.DARK_RED + player.jadCounter.getKills() + "/1"),
			new Option("Zuk Kills: " + Color.DARK_RED + player.zukKills.getKills() + "/1"),
			new Option("Corporeal Beast Kills: " + Color.DARK_RED + player.corporealBeastKills.getKills() + "/300"),
			new Option("Cerberus Kills: " + Color.DARK_RED + player.cerberusKills.getKills() + "/300"),
			new Option("Kree Arra Kills: " + Color.DARK_RED + player.kreeArraKills.getKills() + "/300"),
			new Option("Commander Zilyana Kills: " + Color.DARK_RED + player.commanderZilyanaKills.getKills() + "/300"),
			new Option("General Graardor Kills: " + Color.DARK_RED + player.generalGraardorKills.getKills() + "/300"),
			new Option("Kril Tsutsaroth Kills: " + Color.DARK_RED + player.krilTsutsarothKills.getKills() + "/300"),
			new Option("Dagannoth Rex Kills: " + Color.DARK_RED + player.dagannothRexKills.getKills() + "/300"),
			new Option("Dagannoth Prime Kills: " + Color.DARK_RED + player.dagannothPrimeKills.getKills() + "/300"),
			new Option("Dagannoth Supreme Kills: " + Color.DARK_RED + player.dagannothSupremeKills.getKills() + "/300"),
			new Option("Giant Mole Kills: " + Color.DARK_RED + player.giantMoleKills.getKills() + "/300"),
			new Option("Kalphite Queen Kills: " + Color.DARK_RED + player.kalphiteQueenKills.getKills() + "/300"),
			new Option("King Black Dragon Kills: " + Color.DARK_RED + player.kingBlackDragonKills.getKills() + "/300"),
			new Option("Callisto Kills: " + Color.DARK_RED + player.callistoKills.getKills() + "/300"),
			new Option("Venenatis Kills: " + Color.DARK_RED + player.venenatisKills.getKills() + "/300"),
			new Option("Vetion Kills: " + Color.DARK_RED + player.vetionKills.getKills() + "/300"),
			new Option("Chaos Elemental Kills: " + Color.DARK_RED + player.chaosElementalKills.getKills() + "/300"),
			new Option("Chaos Fanatic Kills: " + Color.DARK_RED + player.chaosFanaticKills.getKills() + "/300"),
			new Option("Crazy Archaeologist Kills: " + Color.DARK_RED + player.crazyArchaeologistKills.getKills() + "/300"),
			new Option("Scorpia Kills: " + Color.DARK_RED + player.scorpiaKills.getKills() + "/300"),
			new Option("Barrows Chests Looted: " + Color.DARK_RED + player.barrowsChestsLooted.getKills() + "/300"),
			new Option("Zulrah Kills: " + Color.DARK_RED + player.zulrahKills.getKills() + "/300"),
			new Option("Kraken Kills: " + Color.DARK_RED + player.krakenKills.getKills() + "/300"),
			new Option("Thermonuclear Smoke Devil Kills: " + Color.DARK_RED + player.thermonuclearSmokeDevilKills.getKills() + "/300"),
			new Option("Abyssal Sire Kills: " + Color.DARK_RED + player.abyssalSireKills.getKills() + "/300"),
			new Option("Skotizo Kills: " + Color.DARK_RED + player.skotizoKills.getKills() + "/300"),
			new Option("Wintertodt Count: " + Color.DARK_RED + player.wintertodtKills.getKills() + "/100"),
			new Option("Nightmare of Ashima Kills: " + Color.DARK_RED + player.nightmareofAshihamaKills.getKills() + "/300"),
			new Option("Nex Kills: " + Color.DARK_RED + player.nexKills.getKills() + "/300"),
			new Option("Obor Kills: " + Color.DARK_RED + player.oborKills.getKills() + "/300"),
			new Option("Chambers of Xeric Kills: " + Color.DARK_RED + player.chambersofXericKills.getKills() + "/100"),
			new Option("Theatre of Blood Kills: " + Color.DARK_RED + player.theatreOfBloodKills.getKills() + "/100"),
			new Option("Deranged Archeaologist Kills: " + Color.DARK_RED + player.derangedArchaeologistKills.getKills() + "/300"),
			new Option("Vorkath Kills: " + Color.DARK_RED + player.vorkathKills.getKills() + "/300"),
			new Option("Sarachnis Kills: " + Color.DARK_RED + player.sarachnisKills.getKills() + "/300"),
			new Option("Alchemical Hydra Kills: " + Color.DARK_RED + player.alchemicalHydraKills.getKills() + "/300"),
			new Option("Bryophyta Kills: " + Color.DARK_RED + player.bryophytaKills.getKills() + "/300"),
			new Option("Grotesque Guardians Kills: " + Color.DARK_RED + player.grotesqueGuardianKills.getKills() + "/300"),
			new Option(" "),
			new Option("Close ->", () -> handleClose(player))
		);
		return;
	}

	public enum CompletionistCapes {

		FIRE(6570, 30176),
		SARADOMIN(2412, 30129),
		SARADOMIN_IMBUED(21791, 30133),
		ZAMORAK(2414, 30139),
		ZAMORAK_IMBUED(21795, 30143),
		GUTHIX(2413, 30149),
		GUTHIX_IMBUED(21793, 30153),
		AVA(10499, 30159),
		ASSEMBLER(22109, 30163),
		ARDOUGNE(13124, 30168),
		MYTHICAL(22114, 30172),
		INFERNAL(21295, 30181);

		public final int secondaryId, newCapeId;

		CompletionistCapes(int secondaryId, int newCapeId) {
			this.secondaryId = secondaryId;
			this.newCapeId = newCapeId;
		}
	}

	public static void register() {
		for (CompletionistCapes cape : CompletionistCapes.values()) {
			ItemItemAction.register(30125, cape.secondaryId, (player, primary, secondary) -> {
				player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Combine these capes to create " + ObjType.get(cape.newCapeId).descriptiveName + "?", primary, () -> {
					primary.remove();
					secondary.setId(cape.newCapeId);
					player.dialogue(new ItemDialogue().one(cape.newCapeId, "You infuse the items together to produce " + ObjType.get(cape.newCapeId).descriptiveName + "."));
				}));
			});
			ItemAction.registerInventory(cape.newCapeId, "revert", (player, item) -> {
				if (player.getInventory().getFreeSlots() < 3) {
					player.dialogue(new MessageDialogue("You need at least 2 inventory slots to do this."));
					return;
				}
				player.dialogue(new OptionsDialogue("Are you use you want to revert your completionist cape?",
					new Option("Yes", () -> player.startEvent(event -> {
						player.lock();
						item.setId(cape.secondaryId);
						player.getInventory().add(30125, 1);
						player.dialogue(new ItemDialogue().two(cape.secondaryId, 30125, "You rip the " + ObjType.get(cape.secondaryId).name + " from your completionist cape."));
						player.unlock();
					})),
					new Option("No", player::closeDialogue)
				));
			});
		}
        /*LoginListener.register(p -> p.addEvent(e -> {
            e.delay(1);
         //   check(p);
        }));*/
		ObjType.forEach(def -> def.completionistType = def.name.toLowerCase().contains("completionist cape") || def.name.toLowerCase().contains("completionist hood"));

	}
}
