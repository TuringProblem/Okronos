package io.ruin.utility;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

/**
 * Basic utility class for anything extra we need since the other one is in a separate dependency...
 */
public final class Utils {

	private Utils() {
	}

	public static <T> T randomTypeOfList(List<T> list) {
		if (list == null || list.size() == 0)
			return null;
		return list.get(new SecureRandom().nextInt(list.size()));
	}

	public static boolean containsIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null) return false;

		final int length = searchStr.length();
		if (length == 0)
			return true;

		for (int i = str.length() - length; i >= 0; i--) {
			if (str.regionMatches(true, i, searchStr, 0, length))
				return true;
		}
		return false;
	}

	public static int largest(int[] arr) {
		int i;

		// Initialize maximum element
		int max = arr[0];

		// Traverse array elements from second and
		// compare every element with current max
		for (i = 1; i < arr.length; i++)
			if (arr[i] > max)
				max = arr[i];

		return max;
	}

	public static String formatMoneyString(long amount) {
		String rawString = String.format("%d", amount);
		int length = rawString.length();

		String result = rawString;
		if (length >= 13) {
			result = rawString.substring(0, rawString.length() - 12) + "," + rawString.substring(rawString.length() - 12, rawString.length() - 9) + "," + rawString.substring(rawString.length() - 9, rawString.length() - 6) + "," + rawString.substring(rawString.length() - 6, rawString.length() - 3) + "," + rawString.substring(rawString.length() - 3, rawString.length());
		} else if (length >= 10) {
			result = rawString.substring(0, rawString.length() - 9) + "," + rawString.substring(rawString.length() - 9, rawString.length() - 6) + "," + rawString.substring(rawString.length() - 6, rawString.length() - 3) + "," + rawString.substring(rawString.length() - 3, rawString.length());

		} else if (length >= 7) {
			result = rawString.substring(0, rawString.length() - 6) + "," + rawString.substring(rawString.length() - 6, rawString.length() - 3) + "," + rawString.substring(rawString.length() - 3, rawString.length());

		} else if (length >= 4) {
			result = rawString.substring(0, rawString.length() - 3) + "," + rawString.substring(rawString.length() - 3, rawString.length());
		}
		return result;
	}

	public static String getDurationAsString(Duration duration) {
		StringBuilder durationAsStringBuilder = new StringBuilder();
		if (duration.toDays() > 0) {
			String postfix = duration.toDays() == 1 ? "" : "s";
			durationAsStringBuilder.append(duration.toDays() + " day");
			durationAsStringBuilder.append(postfix);
		}

		duration = duration.minusDays(duration.toDays());
		long hours = duration.toHours();
		if (hours > 0) {
			String prefix = Utils.isEmpty(durationAsStringBuilder.toString()) ? "" : ", ";
			String postfix = hours == 1 ? "" : "s";
			durationAsStringBuilder.append(prefix);
			durationAsStringBuilder.append(hours + " hr");
			durationAsStringBuilder.append(postfix);
		}

		duration = duration.minusHours(duration.toHours());
		long minutes = duration.toMinutes();
		if (minutes > 0) {
			String prefix = Utils.isEmpty(durationAsStringBuilder.toString()) ? "" : ", ";
			String postfix = minutes == 1 ? "" : "s";
			durationAsStringBuilder.append(prefix);
			durationAsStringBuilder.append(minutes + " min");
			durationAsStringBuilder.append(postfix);
		}

		return durationAsStringBuilder.toString();
	}

	private static boolean isEmpty(String in) {
		return in == null || in.isEmpty();
	}

	public static String formatMemory(long size) {
		if (size <= 0) return "0";
		final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return "%s %s".formatted(new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)), units[digitGroups]);
	}
}
