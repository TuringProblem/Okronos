package io.ruin.model.item.actions.impl.combine;

import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.skills.Tool;
import io.ruin.model.stat.StatType;

public class ZombieAxe {

	private static final int BROKEN_ZOMBIE_AXE = 28813;
	private static final int ZOMBIE_AXE = 28810;


	public static void register() {

		/*
		 * Repairing zombie axe
		 */
		int[] brokenAxe = {BROKEN_ZOMBIE_AXE};
		for (int itemID : brokenAxe) {
			ItemObjectAction.register(itemID, "anvil", (player, item, obj) -> {
				if (player.getStats().get(StatType.Smithing).currentLevel < 70) {
					player.sendMessage("You don't have the proper smithing level to repair this axe, as it requires 70 smithing.");
					return;
				}
				Item hammer = player.getInventory().findItem(Tool.HAMMER);
				Item imcandoHammer = player.getInventory().findItem(Tool.IMCANDO_HAMMER);
				boolean wieldingImcandoHammer = player.getEquipment().hasId(Tool.IMCANDO_HAMMER);
				boolean wieldingDragonWarhammer = player.getEquipment().hasId(ItemID.DRAGON_WARHAMMER);

				if (hammer == null && imcandoHammer == null && !wieldingImcandoHammer && !wieldingDragonWarhammer) {
					player.sendMessage("You need a hammer to repair the zombie axe.");
					return;
				}

				World.startEvent(event -> {
					player.lock();
					player.animate(898);
					event.delay(6);
					player.sendMessage("You successfully repair the broken zombie axe.");
					item.setId(ZOMBIE_AXE);
					player.unlock();
				});
			});
		}
	}
}
