package io.ruin.model.skills.runecrafting;

import com.google.common.collect.Lists;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Misc;
import io.ruin.utility.Utils;
import lombok.AllArgsConstructor;

import java.util.List;

public class Wisps {

	public static void register() {
		Runes.register();
	}

	@AllArgsConstructor
	public enum Runes {
		RUNE_SONE(556, 564, 563, 566),
		RUNE_STWO(554, 562, 560, 565),
		RUNES_THREE(555, 561, 9075, 560),
		RUNES_FOUR(557, 563, 561, 21880);

		public int runeId, rune2Id, rune3Id, rune4Id;


		private static int LightWisp = 15023;
		private static int AncientWisp = 15024;
		private static int HolyWisp = 15025;
		private static int DarkWisp = 15026;

		public static void register() {
			NPCAction.register(LightWisp, "Harvest", (player, npc) -> Lightwisp(player, npc));
			NPCAction.register(AncientWisp, "Harvest", (player, npc) -> AncientWisp(player, npc));
			NPCAction.register(DarkWisp, "Harvest", (player, npc) -> DarkWisp(player, npc));
			NPCAction.register(HolyWisp, "Harvest", (player, npc) -> HolyWisp(player, npc));
		}

		private static void Lightwisp(Player p, NPC npc) {
			if (p.getStats().check(StatType.Runecrafting, 30)) {
				p.startEvent(e -> {
					while (true) {
						if (Random.rollDie(25, 1)) {
							int xTele;
							int yTele;
							xTele = Misc.random(1, 3);
							yTele = Misc.random(1, 3);
							p.getMovement().teleport(p.getAbsX() + xTele, p.getAbsY() + yTele, p.getHeight());
							p.face(npc);
							p.graphics(245, 124, 0);

							p.sendMessage(Color.RED.wrap("The magic embraces you!"));
							return;
						}
						List<Runes> runes = Lists.newArrayList(Runes.values());
						p.animate(798);
						p.face(npc);
						p.getStats().addXp(StatType.Runecrafting, 250, true);
						Runes runes1 = Utils.randomTypeOfList(runes);
						p.getInventory().add(runes1.runeId, 15);
						e.delay(5);
					}
				});
			} else {
				p.sendMessage("You need a runecrafting level of 30!");
			}
		}

		private static void AncientWisp(Player p, NPC npc) {
			if (p.getStats().check(StatType.Runecrafting, 75)) {
				p.startEvent(e -> {
					while (true) {
						if (Random.rollDie(15, 1)) {
							int xTele;
							int yTele;
							xTele = Misc.random(1, 3);
							yTele = Misc.random(1, 3);
							p.getMovement().teleport(p.getAbsX() + xTele, p.getAbsY() + yTele, p.getHeight());
							p.face(npc);
							p.graphics(245, 124, 0);

							p.sendMessage(Color.RED.wrap("The magic embraces you!"));
							return;
						}
						List<Runes> runes = Lists.newArrayList(Runes.values());
						p.animate(798);
						p.face(npc);
						p.getStats().addXp(StatType.Runecrafting, 750, true);
						Runes runes1 = Utils.randomTypeOfList(runes);
						p.getInventory().add(runes1.rune3Id, 35);
						e.delay(5);
					}
				});
			} else {
				p.sendMessage("You need a runecrafting level of 75!");
			}
		}

		private static void DarkWisp(Player p, NPC npc) {
			if (p.getStats().check(StatType.Runecrafting, 90)) {
				p.startEvent(e -> {
					while (true) {
						if (Random.rollDie(10, 1)) {
							int xTele;
							int yTele;
							xTele = Misc.random(1, 3);
							yTele = Misc.random(1, 3);
							p.getMovement().teleport(p.getAbsX() + xTele, p.getAbsY() + yTele, p.getHeight());
							p.face(npc);
							p.graphics(245, 124, 0);

							p.sendMessage(Color.RED.wrap("The magic embraces you!"));
							return;
						}
						List<Runes> runes = Lists.newArrayList(Runes.values());
						p.animate(798);
						p.face(npc);
						p.getStats().addXp(StatType.Runecrafting, 1000, true);
						Runes runes1 = Utils.randomTypeOfList(runes);
						p.getInventory().add(runes1.rune4Id, 45);
						e.delay(5);
					}
				});
			} else {
				p.sendMessage("You need a runecrafting level of 75!");
			}
		}

		private static void HolyWisp(Player p, NPC npc) {
			if (p.getStats().check(StatType.Runecrafting, 50)) {
				p.startEvent(e -> {
					while (true) {
						if (Random.rollDie(20, 1)) {
							int xTele;
							int yTele;
							xTele = Misc.random(1, 3);
							yTele = Misc.random(1, 3);
							p.getMovement().teleport(p.getAbsX() + xTele, p.getAbsY() + yTele, p.getHeight());
							p.face(npc);
							p.graphics(245, 124, 0);

							p.sendMessage(Color.RED.wrap("The magic embraces you!"));
							return;
						}
						List<Runes> runes = Lists.newArrayList(Runes.values());
						p.animate(798);
						p.face(npc);
						p.getStats().addXp(StatType.Runecrafting, 500, true);
						Runes runes1 = Utils.randomTypeOfList(runes);
						p.getInventory().add(runes1.rune2Id, 25);
						e.delay(5);
					}
				});
			} else {
				p.sendMessage("You need a runecrafting level of 75!");
			}
		}
	}
}
