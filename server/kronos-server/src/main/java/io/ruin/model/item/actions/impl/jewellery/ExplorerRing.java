package io.ruin.model.item.actions.impl.jewellery;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.var.VarPlayerRepository;

import java.util.concurrent.TimeUnit;

public class ExplorerRing {

	public static int RING4 = 13128;
	public static int RING3 = 13127;
	public static int RING2 = 13126;
	public static int RING1 = 13125;


	public static void register() {
		ItemAction.registerInventory(RING4, "teleport", ExplorerRing::teleport);
		ItemAction.registerInventory(RING4, "functions", ExplorerRing::function);
		ItemAction.registerInventory(RING3, "teleport", ExplorerRing::teleport);
		ItemAction.registerInventory(RING3, "functions", ExplorerRing::function);
		ItemAction.registerInventory(RING2, "teleport", ExplorerRing::teleport);
		ItemAction.registerInventory(RING2, "functions", ExplorerRing::function);
		ItemAction.registerInventory(RING1, "energy", ExplorerRing::staminaBoost);
		ItemAction.registerInventory(RING1, "alchemy", ExplorerRing::function);

		ItemAction.registerEquipment(RING4, "teleport", ExplorerRing::teleport);
		ItemAction.registerEquipment(RING4, "alchemy", ExplorerRing::function);
		ItemAction.registerEquipment(RING4, "energy", ExplorerRing::staminaBoost);

		ItemAction.registerEquipment(RING3, "teleport", ExplorerRing::teleport);
		ItemAction.registerEquipment(RING3, "alchemy", ExplorerRing::function);
		ItemAction.registerEquipment(RING3, "energy", ExplorerRing::staminaBoost);

		ItemAction.registerEquipment(RING2, "teleport", ExplorerRing::teleport);
		ItemAction.registerEquipment(RING2, "alchemy", ExplorerRing::function);
		ItemAction.registerEquipment(RING2, "energy", ExplorerRing::staminaBoost);

		ItemAction.registerEquipment(RING1, "alchemy", ExplorerRing::function);
		ItemAction.registerEquipment(RING1, "energy", ExplorerRing::staminaBoost);
	}

	private static void function(Player player, Item item) {
		if (System.currentTimeMillis() - player.explorerring < 1000 * 86400) {
			player.dialogue(new MessageDialogue("You can only use this once every 24 hours."));
		} else {
			player.getInventory().addOrDrop(995, 300000);
			player.getInventory().addOrDrop(208, 10);
			player.getInventory().addOrDrop(210, 10);
			player.getInventory().addOrDrop(212, 10);
			player.getInventory().addOrDrop(214, 10);
			player.getInventory().addOrDrop(216, 10);
			player.getInventory().addOrDrop(ItemID.NATURE_RUNE, 30);
			player.getInventory().addOrDrop(ItemID.FIRE_RUNE, 150);
			player.explorerring = System.currentTimeMillis();
		}
	}

	private static void teleport(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Select your farming patch",
			new Option("Catherby", () -> {
				player.getMovement().startTeleport(e -> {
					player.animate(3869);
					player.graphics(285, 92, 0);
					player.publicSound(200);
					e.delay(3);
					player.getMovement().teleport(2816, 3464, 0);
				});
			}),
			new Option("Falador", () -> {
				player.getMovement().startTeleport(e -> {
					player.animate(3869);
					player.graphics(285, 92, 0);
					player.publicSound(200);
					e.delay(3);
					player.getMovement().teleport(3054, 3306, 0);
				});
			}),
			new Option("Canafis", () -> {
				player.getMovement().startTeleport(e -> {
					player.animate(3869);
					player.graphics(285, 92, 0);
					player.publicSound(200);
					e.delay(3);
					player.getMovement().teleport(3602, 3524, 0);
				});
			}),
			new Option("Ardougne", () -> {
				player.getMovement().startTeleport(e -> {
					player.animate(3869);
					player.graphics(285, 92, 0);
					player.publicSound(200);
					e.delay(3);
					player.getMovement().teleport(2668, 3373, 0);
				});
			})
		));
	}

	private static void staminaBoost(Player player, Item item) {
		if (player.explorerRingSpecial < System.currentTimeMillis()) {
			player.explorerRingSpecial = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(player.isSapphire() ? 6 : 12);
			player.explorerRingUses = 0;
		}
		switch (item.getId()) {
			case 13128:
				if (player.explorerRingUses >= 3) {
					player.sendMessage("You've already replenishment you're run energy 3 times today. You can use this again in another " + getRemainingTime(player) + ".");
					return;
				}
				player.getMovement().restoreEnergy(100);
				VarPlayerRepository.STAMINA_POTION.set(player, 1);
				player.staminaTicks = 100; // One minute
				player.explorerRingUses++;
				player.getPacketSender().sendWidgetTimerCustom(Widget.STAMINA, 60);
				player.sendMessage("You have used " + player.explorerRingUses + " of your 3 restores for today.");
				break;
			case 13127:
				if (player.explorerRingUses >= 4) {
					player.sendMessage("You've already replenishment you're run energy 4 times today. You can use this again in another " + getRemainingTime(player) + ".");
					return;
				}
				player.getMovement().restoreEnergy(50);
				VarPlayerRepository.STAMINA_POTION.set(player, 1);
				player.staminaTicks = 100; // One minute
				player.explorerRingUses++;
				player.getPacketSender().sendWidgetTimerCustom(Widget.STAMINA, 60);
				player.sendMessage("You have used " + player.explorerRingUses + " of your 4 restores for today.");
				break;
			case 13126:
				if (player.explorerRingUses >= 3) {
					player.sendMessage("You've already replenishment you're run energy 3 times today. You can use this again in another " + getRemainingTime(player) + ".");
					return;
				}
				player.getMovement().restoreEnergy(50);
				VarPlayerRepository.STAMINA_POTION.set(player, 1);
				player.staminaTicks = 100; // One minute
				player.explorerRingUses++;
				player.getPacketSender().sendWidgetTimerCustom(Widget.STAMINA, 60);
				player.sendMessage("You have used " + player.explorerRingUses + " of your 3 restores for today.");
				break;
			case 13125:
				if (player.explorerRingUses >= 2) {
					player.sendMessage("You've already replenishment you're run energy 2 times today. You can use this again in another " + getRemainingTime(player) + ".");
					return;
				}
				player.getMovement().restoreEnergy(50);
				VarPlayerRepository.STAMINA_POTION.set(player, 1);
				player.staminaTicks = 100; // One minute
				player.explorerRingUses++;
				player.getPacketSender().sendWidgetTimerCustom(Widget.STAMINA, 60);
				player.sendMessage("You have used " + player.explorerRingUses + " of your 2 restores for today.");
				break;
		}

	}

	private static String getRemainingTime(Player player) {
		long ms = player.explorerRingSpecial - System.currentTimeMillis();
		long hours = TimeUnit.MILLISECONDS.toHours(ms);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
		return (hours >= 1 ? (hours + " hour" + (hours > 1 ? "s" : "") + " and ") : "") +
			Math.max((minutes - TimeUnit.HOURS.toMinutes(hours)), 1) + " minute" +
			((minutes - TimeUnit.HOURS.toMinutes(hours)) > 1 ? "s" : "");
	}

}
