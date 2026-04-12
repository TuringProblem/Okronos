package io.ruin.network.incoming.handlers;

import io.ruin.model.World;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.npcs.OpNpcT;
import net.rsprot.protocol.game.incoming.players.OpPlayerT;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

public class InterfaceOnEntityHandler {

	@IdHolder(ids = {OPPLAYERT})
	public static final class OnPlayer implements MessageConsumer<Player, OpPlayerT> {

		@Override
		public void consume(Player player, OpPlayerT msg) {
			handleAction(
				player,
				World.getPlayer(msg.getIndex()),
				msg.getSelectedCombinedId(),
				msg.getSelectedSub(),
				msg.getSelectedObj(),
				msg.getControlKey() ? 1 : 0);
		}
	}

	@IdHolder(ids = {OPNPCU})
	public static final class OnNPC implements MessageConsumer<Player, OpNpcT> {

		@Override
		public void consume(Player player, OpNpcT msg) {
			handleAction(
				player,
				World.getNpc(msg.getIndex()),
				msg.getSelectedCombinedId(),
				msg.getSelectedSub(),
				msg.getSelectedObj(),
				msg.getControlKey() ? 1 : 0);
		}

	}

	protected static void handleAction(Player player, Entity target, int interfaceHash, int slot, int itemId,
	                                   int ctrlRun) {
		if (target == null || player.isLocked())
			return;
		player.resetActions(true, true, true);
		player.face(target);
		player.getMovement().setCtrlRun(ctrlRun == 1);
		if (itemId == -1)
			action(player, target, interfaceHash, slot, itemId);
		else
			TargetRoute.sets(player, target, () -> action(player, target, interfaceHash, slot, itemId), interfaceHash);
	}

	private static void action(Player player, Entity target, int interfaceHash, int slot, int itemId) {
		InterfaceAction action = InterfaceHandler.getAction(player, interfaceHash);
		if (action == null)
			return;
		if (slot == 65535)
			slot = -1;
		if (itemId == 65535)
			itemId = -1;
		action.handleOnEntity(player, target, slot, itemId);
	}

}
