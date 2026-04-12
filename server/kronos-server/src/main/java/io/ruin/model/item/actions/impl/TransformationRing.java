package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.actions.ItemAction;

public enum TransformationRing {

	STONE(6583, 2188),
	NATURE(20005, 7314),
	COINS(20017, 7315);
	//EASTER(7927, 5538, 5539, 5540, 5541, 5542, 5543);

	public final int itemId;
	public final int[] npcIds;

	TransformationRing(int itemId, int... npcIds) {
		this.itemId = itemId;
		this.npcIds = npcIds;
	}

	private static void morph(Player player, TransformationRing ring) {
		if (player.isLocked()) {
			player.sendMessage("You cannot use this right now!");
			return;
		}
		player.lock();
		player.getMovement().reset();
		player.getAppearance().setNpcId(ring.npcIds[Random.get(ring.npcIds.length - 1)]);
		player.openInterface(ToplevelComponent.SIDEMODAL, Interface.TRANSFORMATION_RING);
		player.getAppearance().setCustomRenders(-1, -1, -1, -1, -1, -1, -1);
	}

	public static void unmorph(Player player) {
		player.getAppearance().setNpcId(-1);
		player.closeInterface(ToplevelComponent.SIDEMODAL);
		player.getAppearance().removeCustomRenders();
		player.unlock();
	}

	public static void check(Player player) {
		if (player.getAppearance().getNpcId() != -1)
			unmorph(player);
	}

	public static void register() {
		for (TransformationRing ring : values())
			ItemAction.registerInventory(ring.itemId, 2, (player, item) -> morph(player, ring));
		InterfaceHandler.register(Interface.TRANSFORMATION_RING, h -> h.actions[0] = (SimpleAction) TransformationRing::unmorph);
	}

}
