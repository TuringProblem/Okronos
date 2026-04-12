package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.ground.GroundItemAction;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.objs.OpObj;
import net.rsprot.protocol.game.incoming.objs.OpObj6;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = {OPOBJ1, OPOBJ2, OPOBJ3, OPOBJ4, OPOBJ5, OPOBJ6})
public class GroundItemActionHandler implements Incoming, MessageConsumer<Player, OpObj> {

	public static class Examine implements MessageConsumer<Player, OpObj6> {

		@Override
		public void consume(Player player, OpObj6 msg) {
			handleExamine(player, msg.getId());
		}
	}

	@Override
	public void consume(Player player, OpObj msg) {
		handle(player, msg.getId(), msg.getX(), msg.getZ(), msg.getOp(), msg.getControlKey() ? 1 : 0);
	}

	@Override
	public void handle(Player player, InBuffer in, int opcode) {
	}

	private static void handle(Player player, int id, int x, int y, int option, int ctrlRun) {
		if (player.isLocked())
			return;
		player.resetActions(true, true, true);
		if (option == 1) {
			handleAction(player, option, id, x, y, ctrlRun);
			return;
		}
		if (option == 2) {
			handleAction(player, option, id, x, y, ctrlRun);
			return;
		}
		if (option == 3) {
			handleAction(player, option, id, x, y, ctrlRun);
			return;
		}
		if (option == 4) {
			handleAction(player, option, id, x, y, ctrlRun);
			return;
		}
		if (option == 5) {
			handleAction(player, option, id, x, y, ctrlRun);
			return;
		}
		player.sendFilteredMessage("Unhandled ground item action: option=" + option + " opcode=" + option);
	}

	private static void handleExamine(Player player, int id) {
		ObjType itemDef = ObjType.get(id);
		if (itemDef != null) {
			if (itemDef.examine != null) {
				player.sendMessage(itemDef.examine);
			} else if (itemDef.name != null) {
				player.sendMessage("It's a " + itemDef.name);
			} else {
				player.sendMessage("Unknown.");
			}
		}
	}

	private static void handleAction(Player player, int option, int groundItemId, int x, int y, int ctrlRun) {
		int z = player.getHeight();
		Tile tile = Tile.get(x, y, z, false);
		if (tile == null)
			return;
		GroundItem groundItem = tile.getPickupItem(groundItemId, player.getName());
		if (groundItem == null)
			return;
		ObjType def = ObjType.get(groundItem.id);
		player.getMovement().setCtrlRun(ctrlRun == 1);
		player.getRouteFinder().routeGroundItem(groundItem, distance -> {
			int i = option - 1;
			if (i < 0 || i >= 5)
				return;
			if (option == def.pickupOption) {
				groundItem.pickup(player, distance);
				return;
			}
			GroundItemAction action;
			if (def.groundActions != null && (action = def.groundActions[i]) != null) {
				action.handle(player, groundItem, distance);
				return;
			}
			player.sendMessage("Nothing interesting happens.");
		});
	}
}
