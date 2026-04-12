package io.ruin.utility;

import io.ruin.model.entity.player.PlayerGroup;

public class FormatMessage {

	public enum Colors {
		BLUE_PRIMARY("1D8FA9"),
		TURQUOISE("2C5364"),
		FADED_PINK("ad5389"),
		PURE_RED("dd1818"),
		SKY_BLUE("1CB5E0"),
		BURNT_ORANGE("F16529"),
		TEAL_SPRING_GREEN("34e89e"),
		DEEP_OCEAN_TURQUOISE("0f3443"),
		NAVY_BLUE("3a6186"),
		DEEP_VIRGIN_PURPLE("89253e"),
		DISCORD_PURPLE("7289DA"),
		SPACE_GREY("434343"),
		VAMPIRE_BLOOD_RED("8E0E00"),
		PEACH_ORANGE("e35d5b"),
		GRASS_GREEN("52c234"),
		ASH_BLUE("3f4c6b"),
		BROWN("402a02"),
		DARK_PURPLE("3d0e63"),
		LIGHT_WHITE("f4fbff"),
		DARK_GREEN("006600"),
		DARK_RED("6f0000");

		private final String color;

		Colors(String color) {
			this.color = color;
		}
	}

	/**
	 * @param bracketText Text inside brackets
	 * @param message     Message you want to say
	 * @param color       @Nullable Sets the color of the bracket text only
	 * @return Formatted bracket message
	 */
	public static String sendBracketsMessage(String bracketText, String message, Colors color) {
		return new StringBuilder().append(addBracket(bracketText, color)).append(message).toString();
	}

	/**
	 * @param bracketText Text inside brackets
	 * @param message     Message you want to say
	 * @param color       @Nullable Sets the colour of the brackets & the message
	 * @ Formatted & both coloured bracket message
	 */
	public static String sendBracketsMessageBothColored(String bracketText, String message, Colors color) {
		return new StringBuilder().append(addBracket(bracketText, color)).append(colorText(message, color)).toString();
	}

	/**
	 * @param message Message you want to send
	 * @param color   Color of the message
	 * @return Formatted colored message
	 */
	public static String sendColoredMessage(String message, Colors color) {
		return new StringBuilder().append(colorText(message, color)).toString();
	}

	public static String sendColoredMessage(String message, String color) {
		return new StringBuilder().append(colorText(message, color)).toString();
	}

	/**
	 * @param bracketText  Text inside brackets
	 * @param bracketColor @Nullable Color of text inside brackets
	 * @param message      Message you want to send
	 * @param messageColor @Nullable Color of the message
	 * @return Individually colored bracket contents & message
	 */
	public static String sendMultiColoredBracketMessage(String bracketText, Colors bracketColor, String message, Colors messageColor) {
		return new StringBuilder().append(addBracket(bracketText, bracketColor)).append(colorText(message, messageColor)).toString();
	}

	/**
	 * @param message Message you want to send
	 * @param rank    Player Group (Rank)
	 * @param color   @Nullable color of the message
	 * @return Message with rank icon attached
	 */
	public static String sendMessageWithRank(String message, PlayerGroup rank, Colors color) {
		return new StringBuilder().append(addCrown(rank)).append(colorText(message, color)).toString();
	}

	/**
	 * @param message Message you want to send
	 * @param iconId  Icon id you want to send ::icons retrieves a list of all of them
	 * @param color   @Nullable color of the message
	 * @return Message with custom icon attached
	 */
	public static String sendMessageWithIcon(String message, int iconId, Colors color) {
		return new StringBuilder().append(addIcon(iconId)).append(colorText(message, color)).toString();
	}

	static String addBracket(String bracketText, Colors color) {
		if (color != null) {
			return new StringBuilder().append("[").append("<col=").append(color.color).append(">").append(bracketText).append("</col>").append("] ").toString();
		} else {
			return new StringBuilder().append("[").append(bracketText).append("] ").toString();
		}
	}

	static String addCrown(PlayerGroup rank) {
		return new StringBuilder().append("<img=").append(rank.clientImgId).append("> ").toString();
	}

	static String addIcon(int iconId) {
		return new StringBuilder().append("<img=").append(iconId).append("> ").toString();
	}

	static String colorText(String message, Colors color) {
		if (color != null) {
			return new StringBuilder().append("<col=").append(color.color).append(">").append(message).append("</col>").toString();
		} else {
			return new StringBuilder().append(message).toString();
		}
	}

	static String colorText(String message, String color) {
		return new StringBuilder().append("<col=").append(color).append(">").append(message).append("</col>").toString();
	}

}
