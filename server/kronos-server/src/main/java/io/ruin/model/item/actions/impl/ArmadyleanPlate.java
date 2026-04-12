package io.ruin.model.item.actions.impl;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.object.actions.impl.AncientForge;
import io.ruin.model.skills.Tool;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Misc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArmadyleanPlate {

	private static final int ARMADYL_HELMET = 11826;

	private static final int ARMADYL_CHESTPLATE = 11828;

	private static final int ARMADYL_CHAINSKIRT = 11830;

	public static final int MASORI_MASK_F = 27235;

	public static final int MASORI_BODY_F = 27238;

	public static final int MASORI_CHAPS_F = 27241;

	public static final int MASORI_MASK = 27226;

	public static final int MASORI_BODY = 27229;

	public static final int MASORI_CHAPS = 27232;

	public static final int ARMADYLEAN_PLATE = 27269;

	public static void register() {
		ItemItemAction.register(ARMADYL_HELMET, Tool.CHISEL, (player, primary, secondary) -> {
			player.startEvent(event -> {
				player.animate(7151);
				primary.remove();
				player.getInventory().addOrDrop(ARMADYLEAN_PLATE, 1);
				player.sendFilteredMessage("You break down the armadyl armour.");
			});
		});
		ItemItemAction.register(ARMADYL_CHESTPLATE, Tool.CHISEL, (player, primary, secondary) -> {
			player.startEvent(event -> {
				player.animate(7151);
				primary.remove();
				player.getInventory().addOrDrop(ARMADYLEAN_PLATE, 4);
				player.sendFilteredMessage("You break down the armadyl armour.");
			});
		});
		ItemItemAction.register(ARMADYL_CHAINSKIRT, Tool.CHISEL, (player, primary, secondary) -> {
			player.startEvent(event -> {
				player.animate(7151);
				primary.remove();
				player.getInventory().addOrDrop(ARMADYLEAN_PLATE, 3);
				player.sendFilteredMessage("You break down the armadyl armour.");
			});
		});
		ItemItemAction.register(ARMADYLEAN_PLATE, MASORI_MASK, (player, primary, secondary) -> {
			player.startEvent(event -> {
				Item imbuedItem = new Item(MASORI_MASK_F);
				Map<String, String> attributes = new HashMap<>();
				attributes.putAll(primary.attributes);
				attributes.putAll(secondary.attributes);
				imbuedItem.attributes.putAll(attributes);
				player.getInventory().remove(primary.getId(), 3);
				secondary.remove();
				player.getInventory().addOrDrop(imbuedItem);
			});
		});
		ItemItemAction.register(ARMADYLEAN_PLATE, MASORI_BODY, (player, primary, secondary) -> {
			player.startEvent(event -> {
				if (player.getInventory().contains(ARMADYLEAN_PLATE, 4)) {
					Item imbuedItem = new Item(MASORI_BODY_F);
					Map<String, String> attributes = new HashMap<>();
					attributes.putAll(primary.attributes);
					attributes.putAll(secondary.attributes);
					imbuedItem.attributes.putAll(attributes);
					player.getInventory().remove(primary.getId(), 3);
					secondary.remove();
					player.getInventory().addOrDrop(imbuedItem);
				} else {
					player.dialogue(new MessageDialogue("You need at least 4 armadylean plates to do this."));
				}
			});
		});
		ItemItemAction.register(ARMADYLEAN_PLATE, MASORI_CHAPS, (player, primary, secondary) -> {
			player.startEvent(event -> {
				if (player.getInventory().contains(ARMADYLEAN_PLATE, 3)) {
					Item imbuedItem = new Item(MASORI_CHAPS_F);
					Map<String, String> attributes = new HashMap<>();
					attributes.putAll(primary.attributes);
					attributes.putAll(secondary.attributes);
					imbuedItem.attributes.putAll(attributes);
					player.getInventory().remove(primary.getId(), 3);
					secondary.remove();
					player.getInventory().addOrDrop(imbuedItem);
				} else {
					player.dialogue(new MessageDialogue("You need at least 3 armadylean plates to do this."));
				}
			});
		});
	}
}
