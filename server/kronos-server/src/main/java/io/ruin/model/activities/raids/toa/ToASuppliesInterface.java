package io.ruin.model.activities.raids.toa;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.map.Direction;

import java.util.HashMap;
import java.util.Map;

public class ToASuppliesInterface {

	public void open(Player player) {
		Map<String, ToARoom> uncompletedRooms = new HashMap<>();
		if (!player.getCurrentToARaid().getRooms().get("zebak").roomCompleted)
			uncompletedRooms.put("zebak", player.getCurrentToARaid().getRooms().get("zebak"));
		if (!player.getCurrentToARaid().getRooms().get("akkha").roomCompleted)
			uncompletedRooms.put("akkha", player.getCurrentToARaid().getRooms().get("akkha"));
		if (!player.getCurrentToARaid().getRooms().get("kephri").roomCompleted)
			uncompletedRooms.put("kephri", player.getCurrentToARaid().getRooms().get("kephri"));
		if (!player.getCurrentToARaid().getRooms().get("baba").roomCompleted)
			uncompletedRooms.put("baba", player.getCurrentToARaid().getRooms().get("baba"));


		if (player.toaLifeLoot == null || player.toaLifeLoot.isEmpty()) {
			if (uncompletedRooms.size() >= 2 && player.getToaSuppliesClaimed() == 0)
				ToAObjects.calculateHelpfulSpiritLoot(player);
			else if (uncompletedRooms.size() > 3 && player.getToaSuppliesClaimed() == 1)
				ToAObjects.calculateHelpfulSpiritLoot(player);
			else
				return;
		}
		if (player.toaChaosLoot == null || player.toaChaosLoot.isEmpty()) {
			if (uncompletedRooms.size() >= 2 && player.getToaSuppliesClaimed() == 0)
				ToAObjects.calculateHelpfulSpiritLoot(player);
			else if (uncompletedRooms.size() > 3 && player.getToaSuppliesClaimed() == 1)
				ToAObjects.calculateHelpfulSpiritLoot(player);
			else
				return;
		}
		if (player.toaPowerLoot == null || player.toaPowerLoot.isEmpty()) {
			if (uncompletedRooms.size() >= 2 && player.getToaSuppliesClaimed() == 0)
				ToAObjects.calculateHelpfulSpiritLoot(player);
			else if (uncompletedRooms.size() > 3 && player.getToaSuppliesClaimed() == 1)
				ToAObjects.calculateHelpfulSpiritLoot(player);
			else
				return;
		}
		player.openInterface(ToplevelComponent.MAINMODAL, 1124);

		int startingLifeItemComponent = 26;
		int startingContainerId = 866;
		for (Item item : player.toaLifeLoot) {
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1124 << 16 | startingLifeItemComponent, startingContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				startingLifeItemComponent,
				startingContainerId,
				item
			);
			startingLifeItemComponent++;
			startingContainerId++;
		}
		int startingChaosItemComponent = 39;
		for (Item item : player.toaChaosLoot) {
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1124 << 16 | startingChaosItemComponent, startingContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				startingChaosItemComponent,
				startingContainerId,
				item
			);
			startingChaosItemComponent++;
			startingContainerId++;
		}
		int startingPowerItemComponent = 52;
		for (Item item : player.toaPowerLoot) {
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1124 << 16 | startingPowerItemComponent, startingContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				startingPowerItemComponent,
				startingContainerId,
				item
			);
			startingPowerItemComponent++;
			startingContainerId++;
		}
	}

	private void claimLifeLoot(Player player) {
		if (player.toaLifeLoot == null || player.toaLifeLoot.isEmpty()) {
			return;
		}
		for (Item item : player.toaLifeLoot) {
			player.getInventory().addOrDrop(item);
		}
		player.getCurrentToARaid().suppliesClaimed = true;
		player.toaLifeLoot.clear();
		player.toaChaosLoot.clear();
		player.toaPowerLoot.clear();
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.sendMessage("You claim the supplies from the helpful spirit.");
	}

	private void claimChaosLoot(Player player) {
		if (player.toaChaosLoot == null || player.toaChaosLoot.isEmpty()) {
			return;
		}
		for (Item item : player.toaChaosLoot) {
			player.getInventory().addOrDrop(item);
		}
		player.getCurrentToARaid().suppliesClaimed = true;
		player.toaLifeLoot.clear();
		player.toaChaosLoot.clear();
		player.toaPowerLoot.clear();
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.sendMessage("You claim the supplies from the helpful spirit.");
	}

	private void claimPowerLoot(Player player) {
		if (player.toaPowerLoot == null || player.toaPowerLoot.isEmpty()) {
			return;
		}
		for (Item item : player.toaPowerLoot) {
			player.getInventory().addOrDrop(item);
		}
		player.getCurrentToARaid().suppliesClaimed = true;
		player.toaLifeLoot.clear();
		player.toaChaosLoot.clear();
		player.toaPowerLoot.clear();
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.sendMessage("You claim the supplies from the helpful spirit.");
	}

	public static void register() {
		NPCAction.register(11694, 1, (player, npc) -> player.getToASuppliesInterface().open(player));
		InterfaceHandler.register(1124, h -> {
			h.actions[32] = (SimpleAction) (p) -> p.getToASuppliesInterface().claimLifeLoot(p);
			h.actions[45] = (SimpleAction) (p) -> p.getToASuppliesInterface().claimChaosLoot(p);
			h.actions[58] = (SimpleAction) (p) -> p.getToASuppliesInterface().claimPowerLoot(p);
		});
	}

}
