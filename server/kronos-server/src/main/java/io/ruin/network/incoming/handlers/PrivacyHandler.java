package io.ruin.network.incoming.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.misc.user.SetChatFilterSettings;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

import io.ruin.HooksV2;

@IdHolder(ids = { CHAT_SETMODE })
public class PrivacyHandler implements MessageConsumer<Player, SetChatFilterSettings> {

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	public static interface Hook {
		public record Accept(Player player, SetChatFilterSettings msg) implements Hook {}
	}

	@Override
	public void consume(Player player, SetChatFilterSettings msg) {
		hooks.handle(new Hook.Accept(player, msg));
	}

}
