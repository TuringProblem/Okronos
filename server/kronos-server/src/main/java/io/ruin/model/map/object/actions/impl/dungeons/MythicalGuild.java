package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.actions.ObjectAction;

public enum MythicalGuild {
	GLORY(1704, 11978),
	GLORY_T(10362, 11964),
	SKILLS_NECKLACE(11113, 11968),
	COMBAT_BRACELET(11126, 11972);

	public final int unchargedID, chargedID;

	MythicalGuild(int unchargedID, int chargedID) {
		this.unchargedID = unchargedID;
		this.chargedID = chargedID;
	}

	public static void register() {

		ObjectAction.register(31627, "Climb-up", (player, obj) -> player.getMovement().teleport(2457, 2839, 1));
		ObjectAction.register(32206, "Climb-down", (player, obj) -> player.getMovement().teleport(2457, 2839, 0));
		ObjectAction.register(31626, "Enter", (player, obj) -> player.getMovement().teleport(1936, 9009, 1));
		ObjectAction.register(32205, "Climb-up", (player, obj) -> player.getMovement().teleport(2457, 2849, 0));
		ObjectAction.register(31606, "enter", (player, obj) -> player.getMovement().teleport(1939, 8968, 1));
		ObjectAction.register(31807, "enter", (player, obj) -> player.getMovement().teleport(2445, 2818, 0));

		for (MythicalGuild mythicalGuild : values()) {
			ItemObjectAction.register(mythicalGuild.unchargedID, 31625, (player, uncharged, obj) -> player.startEvent(event -> {
				player.lock();
				player.sendMessage("You dip the jewellery into the fountain..");
				event.delay(1);
				player.animate(832);
				int amount = uncharged.count();
				player.getInventory().remove(uncharged.getId(), amount);
				player.getInventory().add(mythicalGuild.chargedID, amount);
				player.dialogue(new ItemDialogue().one(mythicalGuild.chargedID, "You feel a power emanating from the fountain as it recharges your jewellery."));
				player.unlock();
			}));
		}
	}
}
