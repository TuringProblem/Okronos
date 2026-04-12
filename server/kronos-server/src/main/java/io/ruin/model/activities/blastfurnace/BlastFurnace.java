package io.ruin.model.activities.blastfurnace;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlastFurnace {

	/*Item IDs*/
	public static final int[] SMITHABLE_ORE_IDS = {436, 438, 440, 444, 442, 447, 449, 451};
	private static final int[] SMITHABLE_BAR_IDS = {2349, 2351, 2353, 2355, 2357, 2359, 2361, 2363};
	private static final int PERFECT_GOLD_BAR = 2365;
	public static final int COAL_ORE_ID = 453;
	private static final int GOLD_NUGGETS = 862;

	/*NPC IDs*/
	private static final int FOREMAN_NPC_ID = 2923;
	public static final int[] ORE_NPC_IDS = {2924, 2925, 2926, 2927, 2928, 2929, 2930, 2931, 2932};
	private static final int PERFECT_GOLD_ORE_NPC_ID = 2933;

	/*Object IDs*/
	public static final int PUMP_ID = 9090;
	private static final int[] BAR_DISPENSERS = {9093, 9094, 9095, 9096};
	private static final int PEDALS = 9097;
	public static final int MELTING_POT = 9098;
	public static final int[] CONVEYOR_BELTS = {9100, 9101};
	public static final int[] DRIVE_BELTS = {9102, 9103, 9107};
	private static final int[] COGS = {9104, 9105};
	private static final int GEARBOX = 9106;
	private static final int BAR_DISPENSOR = 9092;

	public static void register() {
		ObjectAction.register(PUMP_ID, 1, (player, obj) -> operatePump(player, PUMP_ID));
		ObjectAction.register(PUMP_ID, 1, (player, obj) -> operatePump(player, PUMP_ID));
		ObjectAction.register(BAR_DISPENSOR, 1, (player, obj) -> lootDispenser(player));
	}


	public static void operatePump(Player player, int objectId) {
		if (objectId != PUMP_ID) {
			return;
		}
		player.sendMessage("You start operating the pump...");
		player.startEvent(event -> {
			event.delay(Random.get(2));
			player.animate(2793);
			if (Random.get(4) == 1) {
				player.getStats().addXp(StatType.Strength, 4, true);
			} else if (Random.get(64) == 1) {
				player.sendMessage(Color.MAROON.wrap("Your hands slip causing you to hit your arms against the iron handles."));
				player.hit(new Hit().randDamage(2));
				player.forceText("Ouch!");
				player.resetAnimation();
				event.delay(1);
			}
		});
	}

	public static void fixDriveBelt(Player player, int objectId) {
		if (objectId != DRIVE_BELTS[0] && objectId != DRIVE_BELTS[1])
			return;

		player.startEvent(event -> {
			if (!player.getInventory().contains(2347)) {
				player.sendMessage(Color.MAROON.wrap("You need a hammer to do this!"));
				player.resetAnimation();
				event.delay(1);
				return;
			}
			event.delay(Random.get(3));
			player.sendMessage("You attempt to fix the drive belt...");
			player.animate(1755);
			if (objectId == DRIVE_BELTS[0]) {
				player.getPacketSender().sendCreateObject(DRIVE_BELTS[0], 1944, 4967, 0, 10, 1);
				player.sendMessage("The drive belt has already been fixed!");
				player.resetAnimation();
				event.delay(1);
			} else if (Random.get(4) == 1) {
				fixedDriveBelt(player, objectId);
				player.resetAnimation();
				event.delay(1);
			} else if (Random.get(10) == 1) {
				player.sendMessage("@red@Your finger slips and you fail to fix the drive belt, injuring yourself in the process.");
				player.hit(new Hit().randDamage(2));
				player.forceText("Ouch!");
				player.resetAnimation();
				event.delay(1);
			}
			//fixDriveBelt(player, objectId);
		});
	}

	public static void breakDriveBelt(Player player) {
		GameObject.spawn(DRIVE_BELTS[1], 1944, 4967, 0, 10, 0);
		player.sendMessage(Color.MAROON.wrap("The drive belt has snapped and needs repairing!"));
	}

	public static void fixedDriveBelt(Player player, int objectId) {
		GameObject obj1 = GameObject.spawn(DRIVE_BELTS[1], 1944, 4967, 0, 10, 0);
		if (objectId == DRIVE_BELTS[1]) {
			player.getStats().addXp(StatType.Smithing, 150, true);
			player.sendMessage("You have successfully fixed the drive belt.");
			obj1.remove();
			GameObject.spawn(DRIVE_BELTS[0], 1944, 4967, 0, 10, 0);
		} else if (objectId == DRIVE_BELTS[0]) {
			player.sendMessage("The drive belt has already been fixed! fixedDriveBelt");
		}
	}

	public static void placeOresOnBelt(Player player, int objectId) {
		if (objectId == CONVEYOR_BELTS[0]) {

		}
	}

	public enum Ore {
		COPPER(436, 1),
		TIN(438, 1),
		IRON(440, 15),
		SILVER(442, 20),
		COAL(453, 30),
		GOLD(444, 40),
		MITHRIL(447, 50),
		ADAMANTITE(449, 70),
		RUNITE(451, 85);

		private static final List<Ore> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

		private final int oreId;
		private final int levelRequirement;

		Ore(int oreId, int levelRequirement) {
			this.oreId = oreId;
			this.levelRequirement = levelRequirement;
		}

		public static Ore getOre(int oreId) {
			for (Ore ore : VALUES) {
				if (ore.getOreId() == oreId) {
					return ore;
				}
			}
			return null;
		}

		public int getOreId() {
			return oreId;
		}

		public int getLevelRequirement() {
			return levelRequirement;
		}
	}

	private enum Bar {
		COPPER(2349, Ore.COPPER, 2924, new Item(Ore.TIN.getOreId(), 1), 1, 6),
		IRON(2351, Ore.IRON, 2925, null, 2, 13),
		SILVER(2355, Ore.SILVER, 2926, null, 2, 14),
		STEEL(2353, Ore.IRON, 2927, new Item(Ore.COAL.getOreId(), 1), 4, 18),
		GOLD(2357, Ore.GOLD, 2928, null, 3, 23),
		MITHRIL(2359, Ore.MITHRIL, 2930, new Item(Ore.COAL.getOreId(), 2), 5, 30),
		ADAMANTITE(2361, Ore.ADAMANTITE, 2931, new Item(Ore.COAL.getOreId(), 3), 8, 38),
		RUNITE(2363, Ore.RUNITE, 2932, new Item(Ore.COAL.getOreId(), 4), 12, 50);

		private static final List<Bar> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

		private final int barId;
		private final Ore ore;
		private final int npcId;
		private final Item secondaryOre;
		private final int nuggets;
		private final int experience;

		Bar(int barId, Ore ore, int npcId, Item secondaryOre, int nuggets, int experience) {
			this.barId = barId;
			this.ore = ore;
			this.npcId = npcId;
			this.secondaryOre = secondaryOre;
			this.nuggets = nuggets;
			this.experience = experience;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getBarId() {
			return barId;
		}

		public Ore getOre() {
			return ore;
		}

		public Item getSecondaryOre() {
			return secondaryOre;
		}

		public int getNuggets() {
			return nuggets;
		}

		public int getExperience() {
			return experience;
		}
	}

	private static void lootDispenser(Player player) {
		int inSack = VarPlayerRepository.BARS_IN_DESPENSER.get(player);
		if (inSack == 0) {
			player.dialogue(new MessageDialogue("The dispenser is empty."));
			return;
		}
		if (player.getInventory().isFull()) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}
		int lvl = player.getStats().get(StatType.Smithing).fixedLevel;
		while (!player.getInventory().isFull() && VarPlayerRepository.BARS_IN_DESPENSER.get(player) > 0) {
			DispenserBar bar = DispenserBar.get(lvl);
			player.getInventory().add(bar.getItemId(), 1);
			player.getStats().addXp(StatType.Smithing, bar.getXp(), true);
			VarPlayerRepository.BARS_IN_DESPENSER.set(player, VarPlayerRepository.BARS_IN_DESPENSER.get(player) - 1);
			PlayerCounter.SMITHED_BARS.increment(player, 1);
		}
		if (VarPlayerRepository.BARS_IN_DESPENSER.get(player) <= 0)
			player.sendMessage("You collect your bars from the dispenser. The dispenser is now empty.");
		else
			player.sendMessage("You collect your bars from the dispenser. You have " + VarPlayerRepository.BARS_IN_DESPENSER.get(player) + " bars remaining.");
	}
}
