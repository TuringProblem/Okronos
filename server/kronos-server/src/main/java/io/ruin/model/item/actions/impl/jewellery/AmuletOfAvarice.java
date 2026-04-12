package io.ruin.model.item.actions.impl.jewellery;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.containers.Equipment;

public class AmuletOfAvarice {

	private static void equip(Player player, Item avarice) {
		player.getEquipment().equip(avarice);
		player.startEvent(e -> {
			while (true) {
				Item item = player.getEquipment().get(Equipment.SLOT_AMULET);
				if (item == null || item.getId() != 22557)
					break;
				e.delay(3);
				player.getCombat().skullNormal();
			}
		});
	}


	public static void register() {
		ItemAction.registerInventory(22557, "wear", (player, item) -> player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", "The skull cannot be removed until the amulet is unequipped!", item, () -> {
				equip(player, item);
			})
		));
	}

	public static boolean isWearingAmuletOfAvarice(Entity entity) {
		if (entity.player == null) {
			return false;
		}
		Item amulet = entity.player.getEquipment().get(Equipment.SLOT_AMULET);
		if (amulet != null) {
			return amulet.getId() == ItemID.AMULET_OF_AVARICE;
		}
		return false;
	}

	public static boolean targetIsRevenant(Entity target) {
		if (target.npc == null) {
			return false;
		}

		return target.npc.getDef().name.toLowerCase().contains("revenant");
	}
}
