package io.ruin.network.incoming.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.misc.client.MapBuildComplete;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = {MAP_BUILD_COMPLETE})
public class RegionUpdateHandler implements MessageConsumer<Player, MapBuildComplete> {

	@Override
	public void consume(Player player, MapBuildComplete msg) {

	}
}
