package player.chat.filter;

import io.ruin.model.entity.player.Player;
import lombok.experimental.ExtensionMethod;
import net.rsprot.protocol.game.outgoing.misc.player.ChatFilterSettings;
import player.chat.filter.attribute.Attributes;

@ExtensionMethod(Attributes.class)
public class ChatFiltering {

	public enum FilterPublic {
		STANDARD,
		SHOW_AUTOCHAT,
		FRIENDS,
		NONE,
		HIDE,
		;

		public int intoRaw() {
			switch (this) {
				case STANDARD -> {
					return 0;
				}
				case FRIENDS -> {
					return 1;
				}
				case NONE -> {
					return 2;
				}
				case HIDE -> {
					return 3;
				}
				case SHOW_AUTOCHAT -> {
					return 4;
				}
				default -> {
					return 0;
				}
			}
		}

		public static FilterPublic fromRaw(int raw) {
			switch (raw) {
				case 0 -> {
					return FilterPublic.STANDARD;
				}

				case 1 -> {
					return FilterPublic.FRIENDS;
				}

				case 2 -> {
					return FilterPublic.NONE;
				}

				case 3 -> {
					return FilterPublic.HIDE;
				}

				case 4 -> {
					return FilterPublic.SHOW_AUTOCHAT;
				}

				default -> {
					return FilterPublic.STANDARD;
				}
			}
		}
	}

	public static void setPublic(Player plr, FilterPublic filter) {
		plr.chatFilter().filterPublic = filter;
	}

	public static void update(Player plr) {
		var filter = plr.chatFilter();
		var msgOut = new ChatFilterSettings(filter.filterPublic.intoRaw(), 0);
		plr.getPacketSender().write(msgOut);
	}
}
