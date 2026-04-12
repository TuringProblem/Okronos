package io.ruin.api.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.function.Supplier;

public class MathUtils {

	@FunctionalInterface
	public static interface ToBigIntegerFunction<T> {
		BigInteger applyAsBigInteger(T value);
	}

	public static <T> Comparator<T> comparing(ToBigIntegerFunction<? super T> keyExtractor) {
		return (Comparator<T>) (c1, c2) -> keyExtractor.applyAsBigInteger(c1).compareTo(
				keyExtractor.applyAsBigInteger(c2));
	}

	public static BigInteger div(BigInteger left, long right) {
		return left.divide(BigInteger.valueOf(right));
	}

	public static double div(BigInteger left, double right) {
		return new BigDecimal(left).divide(BigDecimal.valueOf(right)).doubleValue();
	}

	public static BigInteger min(BigInteger a, BigInteger b) {
		if (a.compareTo(b) < 0) {
			return a;
		}
		return b;
	}

	public static BigInteger max(BigInteger a, BigInteger b) {
		if (a.compareTo(b) >= 0) {
			return a;
		}
		return b;
	}

	public static boolean lt(BigInteger a, BigInteger b) {
		if (a.compareTo(b) < 0) {
			return true;
		}
		return false;
	}

	public static boolean lte(BigInteger a, BigInteger b) {
		if (a.compareTo(b) <= 0) {
			return true;
		}
		return false;
	}

	public static boolean gt(long a, BigInteger b) {
		if (BigInteger.valueOf(a).compareTo(b) > 0) {
			return true;
		}
		return false;
	}

	public static boolean gt(BigInteger a, BigInteger b) {
		if (a.compareTo(b) > 0) {
			return true;
		}
		return false;
	}

	public static boolean gte(BigInteger a, BigInteger b) {
		if (a.compareTo(b) >= 0) {
			return true;
		}
		return false;
	}

	public static boolean gt(BigInteger a, long b) {
		if (a.compareTo(BigInteger.valueOf(b)) > 0) {
			return true;
		}
		return false;
	}

	public static boolean eq(BigInteger a, BigInteger b) {
		if (a.compareTo(b) == 0) {
			return true;
		}
		return false;
	}
}
