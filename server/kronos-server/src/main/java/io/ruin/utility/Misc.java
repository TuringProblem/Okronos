package io.ruin.utility;


import io.ruin.cache.Color;
import io.ruin.model.entity.Entity;
import io.ruin.model.map.Position;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

public class Misc {

	private static long lastUpdateTime = 0;
	private static long timeCorrection = 0;

	public static String formatNumber(int number) {
		return NumberFormat.getInstance().format(number);
	}


	public static String stateOf(boolean b) {
		return stateOf(b, false);
	}

	public static String stateOf(boolean b, boolean color) {
		if (color) {
			return b ? Color.GREEN.wrap("Enabled") : Color.RED.wrap("Disabled");
		}
		return b ? "Enabled" : "Enabled";
	}

	public static synchronized long currentTimeCorrectedMillis() {
		long current = System.currentTimeMillis();
		if (current < lastUpdateTime)
			timeCorrection += lastUpdateTime - current;
		lastUpdateTime = current;
		return current + timeCorrection;
	}

	public static String formatStringFirstCapital(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public static int getDistance(Position src, Position dest) {
		return getDistance(src.getX(), src.getY(), dest.getX(), dest.getY());
	}

	public static int getDistance(Position src, int destX, int destY) {
		return getDistance(src.getX(), src.getY(), destX, destY);
	}

	public static int getDistance(int x1, int y1, int x2, int y2) {
		int diffX = Math.abs(x1 - x2);
		int diffY = Math.abs(y1 - y2);
		return Math.max(diffX, diffY);
	}

	public static int random(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;
	}

	public static int random2(int range) {
		return (int) ((java.lang.Math.random() * range) + 1);
	}

	public static int random3(int range) {
		return (int) (java.lang.Math.random() * (range));
	}

	public static String formatPlayerNameForDisplay(String name) {
		if (name == null)
			return "";
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				newName.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}

	public static int getClosestX(Entity src, Entity target) {
		return getClosestX(src, target.getPosition());
	}

	public static int getClosestX(Entity src, Position target) {
		if (src.getSize() == 1)
			return src.getAbsX();
		if (target.getX() < src.getAbsX())
			return src.getAbsX();
		else if (target.getX() >= src.getAbsX() && target.getX() <= src.getAbsX() + src.getSize() - 1)
			return target.getX();
		else
			return src.getAbsX() + src.getSize() - 1;
	}

	public static int getClosestY(Entity src, Entity target) {
		return getClosestY(src, target.getPosition());
	}

	public static int getClosestY(Entity src, Position target) {
		if (src.getSize() == 1)
			return src.getAbsY();
		if (target.getY() < src.getAbsY())
			return src.getAbsY();
		else if (target.getY() >= src.getAbsY() && target.getY() <= src.getAbsY() + src.getSize() - 1)
			return target.getY();
		else
			return src.getAbsY() + src.getSize() - 1;
	}

	public static Position getClosestPosition(Entity src, Entity target) {
		return new Position(getClosestX(src, target), getClosestY(src, target), src.getHeight());
	}

	public static Position getClosestPosition(Entity src, Position target) {
		return new Position(getClosestX(src, target), getClosestY(src, target), src.getHeight());
	}

	public static String format_string(String string, Object... params) {
		return params == null ? string : String.format(string, (Object[]) params);
	}

	public static double randomDouble(double min, double max) {
		return (Math.random() * (max - min) + min);
	}

	public static String formatTime(long time) {
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		seconds = seconds % 60;
		minutes = minutes % 60;
		hours = hours % 24;

		StringBuilder string = new StringBuilder();
		string.append(days > 9 ? days : ("0" + days));
		string.append(":" + (hours > 9 ? hours : ("0" + hours)));
		string.append(":" + (minutes > 9 ? minutes : ("0" + minutes)));
		string.append(":" + (seconds > 9 ? seconds : ("0" + seconds)));
		return string.toString();
	}

	public static String formatTimeNoDay(long time) {
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds = seconds % 60;
		minutes = minutes % 60;
		hours = hours % 24;

		StringBuilder string = new StringBuilder();
		string.append(hours > 9 ? hours : ("0" + hours));
		string.append(":" + (minutes > 9 ? minutes : ("0" + minutes)));
		string.append(":" + (seconds > 9 ? seconds : ("0" + seconds)));
		return string.toString();
	}

	public static String formatTimeNoHr(long time) {
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds = seconds % 60;
		minutes = minutes % 60;
		hours = hours % 24;
		StringBuilder string = new StringBuilder();
		string.append((minutes > 9 ? minutes : ("0" + minutes)));
		string.append(":" + (seconds > 9 ? seconds : ("0" + seconds)));
		return string.toString();
	}

	public static String timeToString(long time) {
		if (time == 0) {
			return "now";
		}
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds = seconds % 60;
		minutes = minutes % 60;
		hours = hours % 24;
		StringBuilder string = new StringBuilder();
		string.append(hours == 0 ? "" : (hours + " hour" + (hours != 1 ? "s, " : ", ")));
		string.append(minutes == 0 ? "" : ((minutes) + " minute") + (minutes != 1 ? "s" : "")).append(seconds == 0 ? "" : (" and ") + (seconds + " seconds"));
		return string.toString();
	}

	public static int getEffectiveDistance(Entity src, Entity target) {
		Position pos = getClosestPosition(src, target);
		Position pos2 = getClosestPosition(target, src);
		return getDistance(pos, pos2);
	}

	public static int random(int range) {
		return (int) (Math.random() * (range + 1));
	}

	public static int getMinutesPassed(long t) {
		int seconds = (int) ((t / 1000) % 60);
		int minutes = (int) (((t - seconds) / 1000) / 60);
		return minutes;
	}

	public static String insertCommasToNumber(String number) {
		return number.length() < 4 ? number : insertCommasToNumber(number
			.substring(0, number.length() - 3))
			+ ","
			+ number.substring(number.length() - 3, number.length());
	}

	public static <T> T randomTypeOfList(List<T> list) {
		return list.get(new SecureRandom().nextInt(list.size()));
	}

	public static String optimizeText(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
				}
			}
		}
		return s;
	}

	public static String currency(final long quantity) {
		if (quantity >= 1000 && quantity < 1_000_000) {
			return quantity / 1000 + "K";
		} else if (quantity >= 1_000_000 && quantity <= 9999999999L) {
			return quantity / 1000000 + "M";
		} else if (quantity >= 10000000000L && quantity <= 9999999999999L) {
			return quantity / 1000000000L + "B";
		} else if (quantity >= 10000000000000L && quantity <= Long.MAX_VALUE) {
			return quantity / 10000000000000L + "T";
		}

		return String.valueOf(quantity);
	}

	public static String currencyLowercase(final long quantity) {
		if (quantity >= 1000 && quantity < 1_000_000) {
			return quantity / 1000 + "k";
		} else if (quantity >= 1_000_000 && quantity <= 9999999999L) {
			return quantity / 1000000 + "m";
		} else if (quantity >= 10000000000L && quantity <= 9999999999999L) {
			return quantity / 1000000000L + "b";
		} else if (quantity >= 10000000000000L && quantity <= Long.MAX_VALUE) {
			return quantity / 10000000000000L + "t";
		}

		return String.valueOf(quantity);
	}

}
