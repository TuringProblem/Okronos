package io.ruin.api.utils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

	public static double get() {

		return ThreadLocalRandom.current().nextDouble();
	}

	public static int get(int maxRange) {

		return (int) (get() * (maxRange + 1D));
	}

	public static int get2(int maxRange) {
		if (maxRange <= 0) {
			return 0;
		}
		return (int) (get() * maxRange) + 1;
	}

	public static int get(int minRange, int maxRange) {

		return minRange + get(maxRange - minRange);
	}

	public static int get(int... values) {

		return values[get(values.length - 1)];
	}

	public static <T> T get(T[] values) {

		return values[get(values.length - 1)];
	}

	@Nullable
	public static <T> T get(final List<T> list) {
		if (list == null) {
			return null;
		}
		final int size = list.size();
		if (size < 1) {
			return null;
		}
		return list.get(get(size - 1));
	}

	public static <T> T get(List<T> list, Object exclude) {
		T result;
		while ((result = get(list)) == exclude) {
		}
		return result;
	}

	public static long getLong() {

		return ThreadLocalRandom.current().nextLong();
	}

	public static boolean rollDie(int maxRoll) {

		return rollDie(maxRoll, 1);
	}

	public static boolean rollDie(int sides, int chance) {

		return get(1, sides) <= chance;
	}

	public static boolean rollPercent(int percent) {
		return get() <= (percent * 0.01);
	}


	public static boolean rollPercent(double percent) {
		return Math.random() <= percent;
	}

}
