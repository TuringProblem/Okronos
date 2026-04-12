package io.ruin.model.item.actions.impl.combine;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

import static io.ruin.model.item.attributes.AttributeTypes.FULLY_LOADED;

public class BloodFury {

	private static final int FURY = 6585;
	private static final int BLOOD_SHARD = 24777;
	private static final int BLOOD_FURY = 24780;

	private static void makeBloodFury(Player player, Item primary, Item secondary, int result) {
		player.dialogue(
			new MessageDialogue("<col=7f0000>Warning!</col><br>Making the amulet of blood fury will consume your amulet of fury."),
			new YesNoDialogue("Are you sure you want to do this?", "If you select yes, your amulet of fury will be destroyed.", result, 1, () -> {
				primary.setId(BLOOD_FURY);
				primary.setUniqueValue(10000);
				secondary.remove();
			})
		);
	}

	private static void revertBloodFury(Player player, Item primary) {
		player.dialogue(
			new MessageDialogue("<col=7f0000>Warning!</col><br>Dissolving the amulet of blood fury does NOT return the amulet of fury, only the blood shard. Are you sure?"),
			new YesNoDialogue("Are you sure you want to do this?", "If you select yes, you will only receive a blood shard.", BLOOD_FURY, 1, () -> {
				primary.setId(BLOOD_SHARD);
				primary.setUniqueValue(0);
			})
		);
	}

	private static void check(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, FULLY_LOADED)) {
			int level = AttributeExtensions.getCharges(FULLY_LOADED, item);
			if(level >= 5) {
				player.sendMessage("Your Amulet of blood fury has infinite charges due to fully loaded being level 5.");
				return;
			}
		}
		player.sendMessage("Your Amulet of blood fury " + NumberUtils.formatNumber(item.getUniqueValue()) + " charges left.");
	}

	public static void register() {
		ItemAction.registerInventory(BLOOD_FURY, "check", BloodFury::check);
		ItemAction.registerEquipment(BLOOD_FURY, "check", BloodFury::check);
		ItemAction.registerInventory(BLOOD_FURY, "revert", BloodFury::revertBloodFury);
		ItemItemAction.register(FURY, BLOOD_SHARD, (player, primary, secondary) -> makeBloodFury(player, primary, secondary, 24780));

		ObjType.get(BLOOD_FURY).addPostTargetDefendListener((player, item, hit, target) -> {
			int charges = item.getUniqueValue();
			if (charges == 0)
				charges = 10000;
			boolean removeScale = true;
			if (AttributeExtensions.hasAttribute(item, FULLY_LOADED)) {
				int level = AttributeExtensions.getCharges(FULLY_LOADED, item);
				int chance = 5 - level;
				if (Random.get(chance) == 0)
					removeScale = false;
			}
			if (removeScale) {
				if (--charges <= 0) {
					item.remove();
					player.getInventory().addOrDrop(BLOOD_SHARD, 1);
				}
				item.setUniqueValue(charges);
			}
		});
	}


}


