package io.ruin.api.utils;

import java.text.NumberFormat;

public class StringUtils {

	private static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static boolean vowelStart(String word) {
		if (word.isEmpty())
			return false;
		char c = Character.toLowerCase(word.charAt(0));
		return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
	}

	public static String aOrAnFormatted(String word) {
		return "%s %s".formatted(vowelStart(word) ? "an" : "a", word);
	}

	public static String capitalizeFirst(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static String fixCaps(String message) {
		char[] chars = message.toCharArray();
		boolean allowCap = true, forceCap = true;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (forceCap) {
				if (c != ' ') {
					forceCap = false;
					c = chars[i] = Character.toUpperCase(c);
				}
			} else if (!allowCap)
				c = chars[i] = Character.toLowerCase(c);

			if (c == '.' || c == '!' || c == '?') {
				forceCap = true;
			} else {
				allowCap = !Character.isLetter(c);// (c == ' ');
			}
		}
		return new String(chars);
	}

	public static long stringToLong(String string) {
		long l = 0L;
		int i_0_ = string.length();
		int i_1_ = 0;
		for (/**/; (~i_0_) < (~i_1_); i_1_++) {
			l *= 37L;
			int i_2_ = string.charAt(i_1_);
			if ((~i_2_) <= -66 && (~i_2_) >= -91)
				l += (long) (-64 + i_2_);
			else if (i_2_ >= 97 && i_2_ <= 122)
				l += (long) (-96 + i_2_);
			else if (i_2_ >= 48 && i_2_ <= 57)
				l += (long) (27 - (-i_2_ + 48));
			if ((~l) <= -177917621779460414L)
				break;
		}
		for (/**/; ((~l % 37L) == -1L && (~l) != -1L); l /= 37L) {
			/* empty */
		}
		return l;
	}

	public static String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static String getFormattedEnumName(Enum<?> e) {
		return fixCaps(e.name().replace('_', ' '));
	}

	public static String getFormattedEnumName(String name) {
		return fixCaps(name.replace('_', ' '));
	}

	public static String formatNumber(int number) {
		return NumberFormat.getInstance().format(number);
	}

	private static char[] cp1252 = { '\u20ac', '\0', '\u201a', '\u0192',
			'\u201e', '\u2026', '\u2020', '\u2021', '\u02c6', '\u2030',
			'\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018',
			'\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
			'\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e',
			'\u0178' };

	public static char cp1252ToChar(byte i) {
		int i_35_ = i & 0xff;
		if (0 == i_35_) {
			throw new IllegalArgumentException("Non cp1252 character 0x" + Integer.toString(i_35_, 16) + " provided");
		}
		if (i_35_ >= 128 && i_35_ < 160) {
			int i_36_ = cp1252[i_35_ - 128];
			if (0 == i_36_) {
				i_36_ = 63;
			}
			i_35_ = i_36_;
		}
		return (char) i_35_;
	}

	public static byte charToCp1252(char c) {
		byte i_18_;
		if (c > 0 && c < '\u0080' || c >= '\u00a0' && c <= '\u00ff') {
			i_18_ = (byte) c;
		} else if ('\u20ac' == c) {
			i_18_ = (byte) -128;
		} else if (c == '\u201a') {
			i_18_ = (byte) -126;
		} else if (c == '\u0192') {
			i_18_ = (byte) -125;
		} else if (c == '\u201e') {
			i_18_ = (byte) -124;
		} else if ('\u2026' == c) {
			i_18_ = (byte) -123;
		} else if ('\u2020' == c) {
			i_18_ = (byte) -122;
		} else if ('\u2021' == c) {
			i_18_ = (byte) -121;
		} else if ('\u02c6' == c) {
			i_18_ = (byte) -120;
		} else if ('\u2030' == c) {
			i_18_ = (byte) -119;
		} else if (c == '\u0160') {
			i_18_ = (byte) -118;
		} else if (c == '\u2039') {
			i_18_ = (byte) -117;
		} else if ('\u0152' == c) {
			i_18_ = (byte) -116;
		} else if (c == '\u017d') {
			i_18_ = (byte) -114;
		} else if ('\u2018' == c) {
			i_18_ = (byte) -111;
		} else if ('\u2019' == c) {
			i_18_ = (byte) -110;
		} else if ('\u201c' == c) {
			i_18_ = (byte) -109;
		} else if ('\u201d' == c) {
			i_18_ = (byte) -108;
		} else if ('\u2022' == c) {
			i_18_ = (byte) -107;
		} else if (c == '\u2013') {
			i_18_ = (byte) -106;
		} else if ('\u2014' == c) {
			i_18_ = (byte) -105;
		} else if (c == '\u02dc') {
			i_18_ = (byte) -104;
		} else if (c == '\u2122') {
			i_18_ = (byte) -103;
		} else if ('\u0161' == c) {
			i_18_ = (byte) -102;
		} else if (c == '\u203a') {
			i_18_ = (byte) -101;
		} else if ('\u0153' == c) {
			i_18_ = (byte) -100;
		} else if ('\u017e' == c) {
			i_18_ = (byte) -98;
		} else if (c == '\u0178') {
			i_18_ = (byte) -97;
		} else {
			i_18_ = (byte) 63;
		}
		return i_18_;
	}

	public static boolean method7704(char c) {
		if (c > 0 && c < '\u0080' || c >= '\u00a0' && c <= '\u00ff') {
			return true;
		}
		if (c != 0) {
			char[] cs = cp1252;
			for (int i_0_ = 0; i_0_ < cs.length; i_0_++) {
				char c_1_ = cs[i_0_];
				if (c_1_ == c) {
					return true;
				}
			}
		}
		return false;
	}

	public static String decodeCp1252(byte[] bytes, int offset, int length) {
		char[] cs = new char[length];
		int resultSize = 0;
		for (int i = 0; i < length; i++) {
			int i_18_ = bytes[offset + i] & 0xff;
			if (0 != i_18_) {
				if (i_18_ >= 128 && i_18_ < 160) {
					int i_19_ = cp1252[i_18_ - 128];
					if (0 == i_19_) {
						i_19_ = 63;
					}
					i_18_ = i_19_;
				}
				cs[resultSize++] = (char) i_18_;
			}
		}
		return new String(cs, 0, resultSize);
	}

	public static byte[] encodeCp1252(CharSequence string) {
		int i_5_ = string.length();
		byte[] is = new byte[i_5_];
		for (int i_6_ = 0; i_6_ < i_5_; i_6_++) {
			char c = string.charAt(i_6_);
			if (c > 0 && c < '\u0080' || c >= '\u00a0' && c <= '\u00ff') {
				is[i_6_] = (byte) c;
			} else if ('\u20ac' == c) {
				is[i_6_] = (byte) -128;
			} else if ('\u201a' == c) {
				is[i_6_] = (byte) -126;
			} else if ('\u0192' == c) {
				is[i_6_] = (byte) -125;
			} else if (c == '\u201e') {
				is[i_6_] = (byte) -124;
			} else if ('\u2026' == c) {
				is[i_6_] = (byte) -123;
			} else if (c == '\u2020') {
				is[i_6_] = (byte) -122;
			} else if ('\u2021' == c) {
				is[i_6_] = (byte) -121;
			} else if ('\u02c6' == c) {
				is[i_6_] = (byte) -120;
			} else if ('\u2030' == c) {
				is[i_6_] = (byte) -119;
			} else if ('\u0160' == c) {
				is[i_6_] = (byte) -118;
			} else if ('\u2039' == c) {
				is[i_6_] = (byte) -117;
			} else if (c == '\u0152') {
				is[i_6_] = (byte) -116;
			} else if (c == '\u017d') {
				is[i_6_] = (byte) -114;
			} else if (c == '\u2018') {
				is[i_6_] = (byte) -111;
			} else if (c == '\u2019') {
				is[i_6_] = (byte) -110;
			} else if ('\u201c' == c) {
				is[i_6_] = (byte) -109;
			} else if ('\u201d' == c) {
				is[i_6_] = (byte) -108;
			} else if (c == '\u2022') {
				is[i_6_] = (byte) -107;
			} else if ('\u2013' == c) {
				is[i_6_] = (byte) -106;
			} else if (c == '\u2014') {
				is[i_6_] = (byte) -105;
			} else if (c == '\u02dc') {
				is[i_6_] = (byte) -104;
			} else if (c == '\u2122') {
				is[i_6_] = (byte) -103;
			} else if (c == '\u0161') {
				is[i_6_] = (byte) -102;
			} else if ('\u203a' == c) {
				is[i_6_] = (byte) -101;
			} else if (c == '\u0153') {
				is[i_6_] = (byte) -100;
			} else if ('\u017e' == c) {
				is[i_6_] = (byte) -98;
			} else if (c == '\u0178') {
				is[i_6_] = (byte) -97;
			} else {
				is[i_6_] = (byte) 63;
			}
		}
		return is;
	}

	public static String formatNoun(String s) {
		if (s.length() <= 1) {
			return s.toUpperCase(); // if name is "g" or someshit, return "G".
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	/**
	 * Properly capitalizes a username for display
	 *
	 * @param username The username to capitalize
	 * @return The properly capitalized username
	 */
	public static String capitalizeName(String username) {
		if (username == null || username.isEmpty())
			return username;

		StringBuilder result = new StringBuilder();
		boolean capitalizeNext = true;

		for (char c : username.toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				result.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
				capitalizeNext = false;
			} else {
				result.append(c);
				capitalizeNext = true;
			}
		}

		return result.toString();
	}

	public static int compareStrings(CharSequence var0, CharSequence var1) {
		int var3 = var0.length();
		int var4 = var1.length();
		int var5 = 0;
		int var6 = 0;
		char var7 = 0;
		char var8 = 0;

		while (var5 - var7 < var3 || var6 - var8 < var4) {
			if (var5 - var7 >= var3) {
				return -1;
			}

			if (var6 - var8 >= var4) {
				return 1;
			}

			char var9;
			if (var7 != 0) {
				var9 = var7;
				boolean var14 = false;
			} else {
				var9 = var0.charAt(var5++);
			}

			char var10;
			if (var8 != 0) {
				var10 = var8;
				boolean var15 = false;
			} else {
				var10 = var1.charAt(var6++);
			}

			var7 = method1217(var9);
			var8 = method1217(var10);
			var9 = standardizeChar(var9);
			var10 = standardizeChar(var10);
			if (var9 != var10 && Character.toUpperCase(var9) != Character.toUpperCase(var10)) {
				var9 = Character.toLowerCase(var9);
				var10 = Character.toLowerCase(var10);
				if (var9 != var10) {
					return lowercaseChar(var9) - lowercaseChar(var10);
				}
			}
		}

		int var16 = Math.min(var3, var4);

		char var12;
		int var17;
		for (var17 = 0; var17 < var16; ++var17) {
			var6 = var17;
			var5 = var17;

			char var11 = var0.charAt(var5);
			var12 = var1.charAt(var6);
			if (var12 != var11 && Character.toUpperCase(var11) != Character.toUpperCase(var12)) {
				var11 = Character.toLowerCase(var11);
				var12 = Character.toLowerCase(var12);
				if (var11 != var12) {
					return lowercaseChar(var11) - lowercaseChar(var12);
				}
			}
		}

		var17 = var3 - var4;
		if (var17 != 0) {
			return var17;
		} else {
			for (int var18 = 0; var18 < var16; ++var18) {
				var12 = var0.charAt(var18);
				char var13 = var1.charAt(var18);
				if (var12 != var13) {
					return lowercaseChar(var12) - lowercaseChar(var13);
				}
			}

			return 0;
		}
	}

	static char method1217(char var0) {
		if (var0 == 198) {
			return 'E';
		} else if (var0 == 230) {
			return 'e';
		} else if (var0 == 223) {
			return 's';
		} else if (var0 == 338) {
			return 'E';
		} else {
			return (char) (var0 == 339 ? 'e' : '\u0000');
		}
	}

	static char standardizeChar(char var0) {
		if (var0 >= 192 && var0 <= 255) {
			if (var0 >= 192 && var0 <= 198) {
				return 'A';
			}

			if (var0 == 199) {
				return 'C';
			}

			if (var0 >= 200 && var0 <= 203) {
				return 'E';
			}

			if (var0 >= 204 && var0 <= 207) {
				return 'I';
			}

			if (var0 == 209) {
				return 'N';
			}

			if (var0 >= 210 && var0 <= 214) {
				return 'O';
			}

			if (var0 >= 217 && var0 <= 220) {
				return 'U';
			}

			if (var0 == 221) {
				return 'Y';
			}

			if (var0 == 223) {
				return 's';
			}

			if (var0 >= 224 && var0 <= 230) {
				return 'a';
			}

			if (var0 == 231) {
				return 'c';
			}

			if (var0 >= 232 && var0 <= 235) {
				return 'e';
			}

			if (var0 >= 236 && var0 <= 239) {
				return 'i';
			}

			if (var0 == 241) {
				return 'n';
			}

			if (var0 >= 242 && var0 <= 246) {
				return 'o';
			}

			if (var0 >= 249 && var0 <= 252) {
				return 'u';
			}

			if (var0 == 253 || var0 == 255) {
				return 'y';
			}
		}

		if (var0 == 338) {
			return 'O';
		} else if (var0 == 339) {
			return 'o';
		} else {
			return var0 == 376 ? 'Y' : var0;
		}
	}

	static int lowercaseChar(char var0) {
		int var2 = var0 << 4;
		if (Character.isUpperCase(var0) || Character.isTitleCase(var0)) {
			var0 = Character.toLowerCase(var0);
			var2 = (var0 << 4) + 1;
		}

		return var2;
	}

}
