package io.ruin.model.item.actions.impl.jewellery;

import io.ruin.model.entity.player.KillCounter;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.skills.slayer.SlayerMaster;

public enum EternalSlayerRing {
	/**
	 * Eternal
	 */
	ETERNAL(21268, -1, -1);

	private final int id, charges, replacementId;

	EternalSlayerRing(int id, int charges, int replacementId) {
		this.id = id;
		this.charges = charges;
		this.replacementId = replacementId;
	}

	public static void register() {
		JeweleryTeleports teleports = new JeweleryTeleports("ring", false,
			new JeweleryTeleports.Teleport("Slayer Tower", new Bounds(3428, 3545, 3429, 3535, 0)),
			new JeweleryTeleports.Teleport("Fremennik Slayer Dungeon", 2794, 3615, 0),
			new JeweleryTeleports.Teleport("Stronghold Slayer Cave", new Bounds(2433, 3421, 2435, 3423, 0)),
			new JeweleryTeleports.Teleport("Dark Beasts", new Bounds(2025, 4635, 2027, 4637, 0))
		);
		for (EternalSlayerRing ring : values()) {
			teleports.register(ring.id, ring.charges, ring.replacementId);
			ItemAction.registerEquipment(ring.id, "check", (player, item) -> SlayerMaster.check(player));
			ItemAction.registerInventory(ring.id, "check", (player, item) -> SlayerMaster.check(player));
			ItemAction.registerEquipment(ring.id, "log", (player, item) -> KillCounter.openOwnSlayer(player));
			ItemAction.registerEquipment(ring.id, "teleport", (player, item) -> SlayerMaster.canTeleportToTask(player));
			ItemAction.registerInventory(ring.id, "rub", (player, item) -> SlayerMaster.canTeleportToTask(player));

		}
	}

}
