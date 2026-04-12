package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.model.entity.player.Player;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.resumed.ResumePCountDialog;
import net.rsprot.protocol.game.incoming.resumed.ResumePNameDialog;
import net.rsprot.protocol.game.incoming.resumed.ResumePObjDialog;
import net.rsprot.protocol.game.incoming.resumed.ResumePStringDialog;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import java.util.function.Consumer;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = {RESUME_P_NAMEDIALOG, RESUME_P_COUNTDIALOG, RESUME_P_OBJDIALOG, RESUME_P_STRINGDIALOG})
public class InputHandler implements Incoming {

	public static class Name implements MessageConsumer<Player, ResumePNameDialog> {

		@Override
		public void consume(Player player, ResumePNameDialog msg) {
			handleString(player, msg.getName());
		}
	}

	public static class Count implements MessageConsumer<Player, ResumePCountDialog> {

		@Override
		public void consume(Player player, ResumePCountDialog msg) {
			handleCount(player, msg.getCount());
		}
	}

	public static class Obj implements MessageConsumer<Player, ResumePObjDialog> {

		@Override
		public void consume(Player player, ResumePObjDialog msg) {
			handleObj(player, msg.getObj());
		}
	}

	public static class String implements MessageConsumer<Player, ResumePStringDialog> {

		@Override
		public void consume(Player player, ResumePStringDialog msg) {
			handleString(player, msg.getString());
		}
	}

	private static void handleCount(Player player, int value) {
		var consumer = player.consumerInt;
		if (consumer != null) {
			player.consumerInt = null;
			consumer.accept(value);
			if (player.retryIntConsumer) {
				player.consumerInt = consumer;
				player.retryIntConsumer = false;
			}
		}
	}

	private static void handleObj(Player player, int value) {
		var consumer = player.consumerInt;
		if (consumer != null) {
			player.consumerInt = null;
			consumer.accept(value);
			if (player.retryIntConsumer) {
				player.consumerInt = consumer;
				player.retryIntConsumer = false;
			}
		}
	}

	private static void handleString(Player player, java.lang.String value) {
		var consumer = player.consumerString;
		if (consumer != null) {
			player.consumerString = null;
			consumer.accept(value);
			if (player.retryStringConsumer) {
				player.consumerString = consumer;
				player.retryStringConsumer = false;
			}
		}
	}

	@Override
	public void handle(Player player, InBuffer in, int opcode) {
		throw new UnsupportedOperationException("Unimplemented method 'handle'");
	}
}
