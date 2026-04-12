package io.ruin.network.incoming.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.misc.user.Teleport;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = { MOVE_SCRIPTED })
public class TeleportHandler implements MessageConsumer<Player, Teleport> {

	@Override
	public void consume(Player player, Teleport msg) {
		if (!player.isCommunityAdmin()) {
			return;
		}
		player.getMovement().teleport(msg.getX(), msg.getZ(), msg.getLevel());
	}

}
