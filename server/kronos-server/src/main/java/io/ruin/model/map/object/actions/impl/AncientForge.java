package io.ruin.model.map.object.actions.impl;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.Torva;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Common;
import io.ruin.utility.Misc;

/**
 * @author R-Y-M-R
 * @date 3/8/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public class AncientForge {
	public static final int id = 42966;
	private static final Item SOME_COMPONENTS = new Item(Torva.BANDOS_COMPONENTS, 1);
	private static final Position FORGE_LOCATION = new Position(2880, 5225, 0);
	private static final String HAMMER = "You need a hammer to do that.";

	public static void register() {
		ObjectAction.register(id, 1, AncientForge::useForge);
		ItemObjectAction.register(ItemID.BANDOS_CHESTPLATE, id, (player, item, obj) -> attemptBreakDown(player, item.getId()));
		ItemObjectAction.register(ItemID.BANDOS_TASSETS, id, (player, item, obj) -> attemptBreakDown(player, item.getId()));
	}

	public static boolean hasHammer(Player player, boolean sarcasm) {
		if (player.getInventory().contains(ItemID.HAMMER)) {
			return true;
		}
		if (sarcasm) {
			player.dialogue(new ItemDialogue().one(ItemID.HAMMER, HAMMER), new PlayerDialogue("Hmm! If only there was a hammer somewhere nearby."), new PlayerDialogue("Oh, what's that over there?"));
		} else {
			player.dialogue(new ItemDialogue().one(ItemID.HAMMER, HAMMER));
		}
		return false;
	}

	private static void useForge(Player player, GameObject obj) {
		player.dialogue(new MessageDialogue("The Ancient Forge can be used to smelt down Bandos gear into Bandos components. Bandos components can be used to repair Torva."),
			new OptionsDialogue("What would you like to do?",
				new Option("Break down Bandos equipment into components.", () -> startBreakdown(player)),
				new Option("Learn about Bandos components.", () -> learnAbout(player))));
	}

	// lol
	private static void startBreakdown(Player player) {
		player.closeDialogue();
		if (!hasHammer(player, true)) {
			return;
		}
		player.dialogue(new MessageDialogue("Any items you break down will be lost."), new OptionsDialogue("What do you want to break down?",
			new Option("Bandos chestplate. Yields 3 components.", () -> attemptBreakDown(player, ItemID.BANDOS_CHESTPLATE)),
			new Option("Bandos tassets. Yields 2 components.", () -> attemptBreakDown(player, ItemID.BANDOS_TASSETS))));
	}


	private static void breakDown(Player player, int item) {
		player.face(FORGE_LOCATION.getX(), FORGE_LOCATION.getY());
		final int amount = getComponentAmount(item);
		final int neededInvSpaces = amount - 1;
		if (amount == -1) {
			player.sendMessage("You can't break down that item.");
			System.out.println(player.getName() + " tried to break down " + ObjType.get(item).name + ". Unhandled.");
			return;
		}
		if (neededInvSpaces > player.getInventory().getFreeSlots()) {
			player.sendMessage("You need at least " + Misc.formatNumber(neededInvSpaces) + " free space" + (neededInvSpaces > 1 ? "s" : "") + " to do that.");
			return;
		}
		final Item breakDown = new Item(item, 1);
		player.startEvent(event -> {
			player.lock();
			event.delay(1);
			if (!player.getInventory().contains(item)) {
				player.unlock();
				return;
			}
			player.getInventory().remove(item, 1);
			player.animate(899);
			player.publicSound(2725);
			event.delay(3);
			player.getInventory().addOrDrop(Torva.BANDOS_COMPONENTS, amount);
			Common.sendTwoItemMessage(player, breakDown, SOME_COMPONENTS, "You break down the " + breakDown.getDef().name + " into " + Misc.formatNumber(amount) + " Bandos components.");
			player.unlock();
		});
	}

	private static int getComponentAmount(int itemid) {
		switch (itemid) {
			case ItemID.BANDOS_CHESTPLATE:
				return 3;
			case ItemID.BANDOS_TASSETS:
				return 2;
			default:
				return -1;
		}
	}


	private static void attemptBreakDown(Player player, int itemid) {
		player.closeDialogue();
		if (!player.getInventory().contains(itemid, 1)) {
			player.dialogue(new MessageDialogue("You need 1x " + ObjType.get(itemid).name + " to break down."));
			return;
		}
		breakDown(player, itemid);
	}


	private static void learnAbout(Player player) {
		player.closeDialogue();
		player.dialogue(new MessageDialogue("This forge is an Ancient Zarosian forge. It's been dormant in Gielinor for years. You can break down Bandos gear at the forge. This process yields Bandos components. Components are then used to repair broken Torva pieces."),
			new PlayerDialogue("If I break down any bandos items, they will be destroyed in the process."),
			new MessageDialogue("Bandos chestplate will yield 3, and Bandos tassets will yield 2 components."),
			new OptionsDialogue("Would you like to begin breaking down Bandos items?",
				new Option("Yes", () -> startBreakdown(player)),
				new Option("No", player::closeDialogue)));
	}

}
