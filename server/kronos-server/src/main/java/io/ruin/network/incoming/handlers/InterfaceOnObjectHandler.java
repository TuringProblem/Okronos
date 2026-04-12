package io.ruin.network.incoming.handlers;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.DebugMessage;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.objs.OpObjT;
import net.rsprot.protocol.game.incoming.locs.OpLocT;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;
import static io.ruin.network.incoming.handlers.InterfaceOnGroundItemHandler.handleTelegrab;

public class InterfaceOnObjectHandler {

	@IdHolder(ids = {OPOBJT})
	public static final class FromInterface implements MessageConsumer<Player, OpObjT> {

		@Override
		public void consume(Player player, OpObjT msg) {
			int telegrabHash = 14286875;
			if (msg.getSelectedCombinedId() == telegrabHash) {
				handleTelegrab(player, msg.getId(), msg.getX(), msg.getZ(), msg.getControlKey());
			}
			else {
				handleAction(
					player,
					msg.getSelectedCombinedId(),
					msg.getSelectedSub(),
					msg.getSelectedObj(),
					msg.getId(),
					msg.getX(),
					msg.getZ(),
					msg.getControlKey() ? 1 : 0);
			}
		}
	}

	@IdHolder(ids = {OPLOCT})
	public static final class FromInterfaceLoc implements MessageConsumer<Player, OpLocT> {

		@Override
		public void consume(Player player, OpLocT msg) {
			handleAction(
				player,
				msg.getSelectedCombinedId(),
				msg.getSelectedSub(),
				msg.getSelectedObj(),
				msg.getId(),
				msg.getX(),
				msg.getZ(),
				msg.getControlKey() ? 1 : 0);
		}
	}

	private static void handleAction(Player player, int interfaceHash, int slot, int itemId, int objectId, int objectX,
	                                 int objectY, int ctrlRun) {
		if (player.isLocked())
			return;
		player.resetActions(true, true, true);
		if (objectId == -1)
			return;
		GameObject gameObject = Tile.getObject(objectId, objectX, objectY, player.getPosition().getZ());
		if (gameObject == null)
			return;
		if (player.debug) {
			DebugMessage debug = new DebugMessage()
				.add("interfaceHash", interfaceHash)
				.add("slot", slot)
				.add("itemId", itemId)
				.add("objectId", objectId)
				.add("objectX", objectX)
				.add("objectY", objectY);
			player.sendFilteredMessage("[ObjectAction] " + debug.toString());
		}
		player.getMovement().setCtrlRun(ctrlRun == 1);
		if (ObjType.get(itemId).skipPathingBeforeInteracting) {
			// instant activation not dependant on pathing to target
			action(player, interfaceHash, slot, itemId, gameObject);
			return;
		}
		player.getRouteFinder().routeObject(gameObject, () -> action(player, interfaceHash, slot, itemId, gameObject));
	}

	private static void action(Player player, int interfaceHash, int slot, int itemId, GameObject gameObject) {
		InterfaceAction action = InterfaceHandler.getAction(player, interfaceHash);
		if (action == null)
			return;
		if (slot == 65535)
			slot = -1;
		if (itemId == 65535)
			itemId = -1;
		action.handleOnObject(player, slot, itemId, gameObject);
	}

}
