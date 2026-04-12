package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.model.entity.player.Player;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.IdHolder;

import static io.ruin.network.ClientProt204.PING_STATISTICS;

@IdHolder(ids = {PING_STATISTICS})
public class PingHandler implements Incoming {

	@Override
	public void handle(Player player, InBuffer in, int opcode) {
		int dude = in.readIntME2();
		int high = in.readIntLE();
		int fps = in.readUnsignedByteS();
		int garbage = in.readUnsignedByteS();
	}

}
