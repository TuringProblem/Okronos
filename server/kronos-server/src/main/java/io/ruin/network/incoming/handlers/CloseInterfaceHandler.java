package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.model.entity.player.Player;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.misc.user.CloseModal;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = {CLOSE_MODAL})
public class CloseInterfaceHandler implements Incoming, MessageConsumer<Player, CloseModal> {

	@Override
	public void consume(Player player, CloseModal msg) {
		player.closeInterfaces();
	}

	@Override
	public void handle(Player player, InBuffer in, int opcode) {
	}

}
