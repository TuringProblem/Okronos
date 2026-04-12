package io.ruin.model.content.itembreaking;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

import java.util.List;
import java.util.stream.Collectors;

public class ItemBreakPerkAttaching {

	private static void attachPerk(Player player, Item itemToPerk, ItemBreakPerks perk) {
		if (ItemUpgradeInterface.getTotalAttachments(itemToPerk) >= ItemUpgradeInterface.getMaxAttachments(itemToPerk)) {
			player.sendMessage("This item already has the maximum amount of attachments.");
			return;
		}
		if (!player.isManager()) {
			return;
		}
		player.integerInput("Enter the level of the perk you want to attach", level -> {
			if (level < 1 || level > 5) {
				player.sendMessage("Invalid level, must be between 1 and 5.");
				return;
			}
			ItemBreakPerks.upgradeItem(player, itemToPerk, perk, level);
		});
	}

	private static void startDialogue(Player player, Item item) {
		List<ItemBreakPerks> availablePerks = ItemBreakPerks.getAvailablePerks(item);
		List<Option> options = availablePerks.stream()
			.limit(4)
			.map(perk -> new Option(perk.name, () -> attachPerk(player, item, perk)))
			.collect(Collectors.toList());
		if (availablePerks.size() > 4) {
			options.add(new Option("Next Page", () -> secondPage(player, item, availablePerks, 4)));
		}
		player.dialogue(new OptionsDialogue(options.toArray(new Option[0])));
	}

	private static void secondPage(Player player, Item item, List<ItemBreakPerks> availablePerks, int startIndex) {
		List<Option> options = availablePerks.stream()
			.skip(startIndex)
			.limit(4)
			.map(perk -> new Option(perk.name, () -> attachPerk(player, item, perk)))
			.collect(Collectors.toList());
		if (startIndex > 0) {
			options.add(new Option("Previous Page", () -> startDialogue(player, item)));
		}
		if (availablePerks.size() > startIndex + 4) {
			options.add(new Option("Next Page", () -> secondPage(player, item, availablePerks, startIndex + 4)));
		}
		player.dialogue(new OptionsDialogue(options.toArray(new Option[0])));
	}


	public static void register() {
		for (ObjType def : ObjType.cached.values()) {
			if (def.equipSlot != -1) {
				ItemItemAction.register(26920, def.id, (player, primary, secondary) -> {
					startDialogue(player, secondary);
				});
			}
		}
	}
}
