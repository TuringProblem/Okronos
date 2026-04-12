package io.ruin.network.incoming.handlers;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerAction;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.players.OpPlayer;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = { OPPLAYER1, OPPLAYER2, OPPLAYER3, OPPLAYER4, OPPLAYER5, OPPLAYER6, OPPLAYER7, OPPLAYER8 })
public class PlayerActionHandler implements MessageConsumer<Player, OpPlayer> {

	@Override
	public void consume(Player player, OpPlayer msg) {
		handle(player, msg.getOp(), msg.getIndex(), msg.getControlKey() ? 1 : 0);
	}

	private static void handle(Player player, int option, int targetIndex, int ctrlRun) {
		if (player.isLocked())
			return;
		player.resetActions(true, true, true);

		Player target = World.getPlayer(targetIndex);
		if (target == null)
			return;
		if (targetIndex == player.getIndex())
			return;
		PlayerAction action = player.getAction(option);
		if (action == null)
			return;
		player.getMovement().setCtrlRun(ctrlRun == 1);
		action.accept(player, target);
	}

}
