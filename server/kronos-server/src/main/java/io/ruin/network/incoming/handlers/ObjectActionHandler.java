package io.ruin.network.incoming.handlers;

import io.ruin.cache.LocType;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.DebugMessage;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.locs.OpLoc;
import net.rsprot.protocol.game.incoming.locs.OpLoc6;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import java.util.Arrays;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = {OPLOC1, OPLOC2, OPLOC3, OPLOC4, OPLOC5, OPLOC6})
public class ObjectActionHandler implements MessageConsumer<Player, OpLoc> {

	public static class Examine implements MessageConsumer<Player, OpLoc6> {

		@Override
		public void consume(Player player, OpLoc6 msg) {
			handleExamine(player, msg.getId());
		}

	}

	@Override
	public void consume(Player player, OpLoc msg) {
		handle(player, msg.getOp(), msg.getX(), msg.getZ(), msg.getId(), msg.getControlKey() ? 1 : 0);
	}

	private static void handle(Player player, int option, int x, int y, int id, int ctrlRun) {
		if (player.isLocked())
			return;
		// BotDetection.handleAction(player, "OBJECT_ACTION");
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
		player.sendFilteredMessage("Unhandled object action: option=" + option);
	}

	private static void handleExamine(Player player, int id) {
		LocType def = LocType.get(id);
		if (def != null) {
			if (player.debug) {
				DebugMessage debug = new DebugMessage()
					.add("id", id)
					.add("models", Arrays.toString(def.modelIds))
					.add("name", def.name)
					.add("options", Arrays.toString(def.options))
					.add("varpbitId", def.varpBitId)
					.add("varpId", def.varpId);
				player.sendFilteredMessage("[ObjectAction] " + debug.toString());
			}
		}
	}

	public static void handleAction(Player player, int option, int objectId, int objectX, int objectY, int ctrlRun) {
		if (objectId == -1)
			return;
		GameObject gameObject = Tile.getObject(objectId, objectX, objectY, player.getPosition().getZ());
		if (gameObject == null)
			return;
		if (player.debug) {
			DebugMessage debug = new DebugMessage()
				.add("option", option)
				.add("id", gameObject.id())
				.add("name", gameObject.getDef().name)
				.add("x", gameObject.x)
				.add("y", gameObject.y)
				.add("z", gameObject.z)
				.add("type", gameObject.type())
				.add("direction", gameObject.direction())
				.add("options", Arrays.toString(gameObject.getDef().options))
				.add("varpbitId", gameObject.getDef().varpBitId)
				.add("models", Arrays.toString(gameObject.getDef().modelIds))
				.add("varpId", gameObject.getDef().varpId);
			player.sendFilteredMessage("[ObjectAction] " + debug.toString());
		}
		player.getMovement().setCtrlRun(ctrlRun == 1);
		player.getRouteFinder().routeObject(gameObject, () -> {
			player.getPacketSender().resetMapFlag();
			int i = option - 1;
			if (i < 0 || i >= 5)
				return;
			ObjectAction action = null;
			ObjectAction[] actions = gameObject.actions;
			if (actions != null)
				action = actions[i];
			if (action == null && (actions = gameObject.getDef().defaultActions) != null)
				action = actions[i];
			if (action != null) {
				action.handle(player, gameObject);
				return;
			}

			player.sendMessage("Nothing interesting happens.");
		});
	}

}
