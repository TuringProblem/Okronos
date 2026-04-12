package io.ruin.cache;

/**
 * Remember: If we add/remove/change a color, remember to also change it in the custom JournalInterface client class!
 */
public enum Color {

	RED("FF0000"),
	LIGHT_RED("db5b58"),
	YELLOW("c2a532"),
	YELLOW2("FFCC00"),
	GREEN("27ae60"),
	CANDY_PINK("e20b76"),

	ORANGE("FFA500"),
	ORANGE_RED("FF4500"),
	GOLD2("F7E521"),
	TOMATO("FF6347"),
	CRIMSON("DC143C"),
	MAROON("800000"),

	BLUE("0000FF"),
	BLACK("000000"),
	COOL_BLUE("0040ff"),
	BABY_BLUE("1E90FF"),
	CYAN("00FFFF"),
	TEAL("008080"),

	PURPLE("800080"),
	VIOLET("EE82EE"),
	NOBLE("8D501B"),
	PINK("FFC0CB"),
	OWNER("00B6FF"),

	WHITE("FFFFFF"),
	WHEAT("F5DEB3"),
	SILVER("C0C0C0"),
	PURPLE2("B209CA"),

	OLIVE("808000"),
	BRONZE("D37E2A"),
	GOLD("FFD700"),
	ORANGE2("FC7306"),
	MAGENTA("E121F7"),
	ONYX("78757A"),

	DARK_RED("6f0000"),
	DARK_GREEN("006600"),
	PVP_RED("D80808"),

	MOD_YELL("696969"),
	DEV_YELL("420420"),

	RAID_PURPLE("ef20ff");

	public final String html;

	Color(String html) {
		this.html = html;
	}

	public String tag() {
		return "<col=" + html + ">";
	}

	public String wrap(String s) {
		return tag() + s + "</col>";
	}

}