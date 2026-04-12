package io.ruin.model.skills.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.DonatorBonus;
import io.ruin.model.entity.player.Player;
import io.ruin.model.var.VarPlayerRepository;

public class SuperiorSlayer {

	public static boolean isSuperiorCreature(int id) {
		return switch (id) {
			case 9258, 7395, 7393, 7391, 7390, 7403, 7402, 7410, 7398, 7397, 7389, 7401, 7388, 7404, 7407, 7400, 7399,
				 7405, 7411, 7406, 7392, 7409, 7396, 10397, 10398, 10400, 10402, 9465 -> true;
			default -> false;
		};
	}

	public static void trySpawn(Player player, SlayerCreature task, NPC npc) {
		if (VarPlayerRepository.BIGGER_AND_BADDER.get(player) == 0) {
			return;
		}
		if (VarPlayerRepository.SLAYER_TASK.get(player) != task.getUid()) {
			return;
		}

		int superior = getSuperior(task, npc.getId());

		if (superior == -1 || superior == npc.getId())
			return;

		int odds = 100 - DonatorBonus.BONUS_CHANCE_ON_SUPERIOR_SLAYER.handleBonus(player);

		odds = (int) (odds * getDonatorSpawnChanceModifier(player));

		if (Random.get(odds) == 1) {
			System.out.println("spawned");
			NPC boss = new NPC(superior);

			boss.spawn(npc.getPosition());
			player.getPacketSender().sendHintIcon(boss);
			boss.ownerId = player.getUserId();
			player.sendMessage("<col=ff0000>A superior foe has appeared...</col>");
		}
	}

	private static double getDonatorSpawnChanceModifier(Player player) {
		return switch (player.getSecondaryGroup()) {
			case DONATOR -> 0.98;
			case SUPER_DONATOR -> 0.96;
			case ELITE_DONATOR -> 0.94;
			case NOBLE_DONATOR -> 0.92;
			case GOLD_DONATOR -> 0.9;
			case PLATINUM_DONATOR -> 0.88;
			case LEGENDARY_DONATOR -> 0.85;
			case SUPREME_DONATOR -> 0.82;
			default -> 1;
		};
	}

	public static void trySpawnFromName(Player player, NPC npc) {
		if (VarPlayerRepository.BIGGER_AND_BADDER.get(player) == 0) {
			return;
		}


		int superior = getSuperiorFromName(npc.getDef().name);

		if (superior == -1 || superior == npc.getId())
			return;

		int odds = 100 - DonatorBonus.BONUS_CHANCE_ON_SUPERIOR_SLAYER.handleBonus(player);

		switch (CombatAchievementSystem.getTier(player.combatAchievementPoints)) {
			case MEDIUM -> odds  = (int) (odds * 0.98);
			case HARD -> odds  = (int) (odds * 0.96);
			case ELITE -> odds  = (int) (odds * 0.9);
			case MASTER -> odds  = (int) (odds * 0.8);
			case GRANDMASTER -> odds  = (int) (odds * 0.7);
		}


		if (Random.get(odds) == 1) {
			NPC boss = new NPC(superior);
			boss.spawn(npc.getPosition());
			player.getPacketSender().sendHintIcon(boss);
			boss.ownerId = player.getUserId();
			player.sendMessage("<col=ff0000>A superior foe has appeared...</col>");
		}
	}

	static int getSuperior(SlayerCreature task, int npc) {
		return switch (task) {
			case BASILISKS -> {
				if (npc == 9293)
					yield 9258;
				yield 7395;
			}
			case COCKATRICE -> 7393;
			case BANSHEE -> {
				if (npc == 7272)
					yield 7391;
				yield 7390;
			}
			case ABERRANT_SPECTRES -> {
				if (npc == 7279)
					yield 7403;
				yield 7402;
			}
			case ABYSSAL_DEMON -> 7410;
			case BLOODVELDS -> {
				if (npc == 7276)
					yield 7398;
				yield 7397;
			}
			case CAVE_CRAWLER -> 7389;
			case CAVE_HORRORS -> 7401;
			case CRAWLING_HANDS -> 7388;
			case DUST_DEVILS -> 7404;
			case GARGOYLE -> 7407;
			case JELLIES -> {
				if (npc == 7277)
					yield 7400;
				yield 7399;
			}
			case KURASK -> 7405;
			case NECHRYAEL -> 7411;
			case SMOKE_DEVILS -> 7406;
			case ROCKSLUG -> 7392;
			case DARK_BEASTS -> 7409;
			case INFERNAL_MAGE -> 7396;
			case TUROTH -> 10397;
			case WYRMS -> 10398;
			case DRAKES -> 10400;
			case HYDRAS -> 10402;
			case PYREFIEND -> {
				if (npc == 6762)
					yield 9465;
				yield 7394;
			}
			default -> -1;
		};
	}

	public static int getSuperiorFromName(String name) {
		return switch (name) {
			case "Basilisk" -> 9258;
			case "Cockatrice" -> 7393;
			case "Banshee" -> 7391;
			case "Aberrant spectre" -> 7403;
			case "Abyssal demon" -> 7410;
			case "Bloodveld" -> 7398;
			case "Cave crawler" -> 7389;
			case "Cave horror" -> 7401;
			case "Crawling hand" -> 7388;
			case "Dust devil" -> 7404;
			case "Gargoyle" -> 7407;
			case "Jelly" -> 7400;
			case "Kurask" -> 7405;
			case "Nechryael" -> 7411;
			case "Smoke devil" -> 7406;
			case "Rockslug" -> 7392;
			case "Dark beast" -> 7409;
			case "Infernal mage" -> 7396;
			case "Turoth" -> 10397;
			case "Wyrms" -> 10398;
			case "Drake" -> 10400;
			case "Hydra" -> 10402;
			case "Pyrefiend" -> 9465;
			case "Greater nechryael" -> 7411;
			case "Mutated bloodveld" -> 7398;
			case "Twisted banshee" -> 7391;
			case "Deviant spectre" -> 7403;
			default -> -1;
		};
	}
}
