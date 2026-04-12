package io.ruin.utility;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.openhft.hashing.LongHashFunction;

/**
 * Originally created by Pim De Witte.
 * <p>
 * Performance drastically improved by over an order of magnitude by Thomas G. P. Nappo (Jire).
 * Garbage production has been eliminated as well.
 */
public final class BadWords {

	private static final String[] emptyComboWords = new String[]{};
	private static final Long2ObjectMap<String[]> words = new Long2ObjectOpenHashMap<>();
	private static int largestWordLength = 0;
	
	public static void flag(String word, String... ignoreComboWords) {
		if (word.length() > largestWordLength) {
			largestWordLength = word.length();
		}
		words.put(LongHashFunction.xx().hashChars(word), ignoreComboWords);
	}
	
	public static void loadBadWords() {
		for (String blacklisted : blacklist) {
			flag(blacklisted, emptyComboWords);
		}
		flag("scape",
				"runescape",
				"landscape",
				"machinescape",
				"fashionscape",
				"07scape",
				"2007scape",
				"osrscape",
				"osrsscape",
				"moparscape",
				"didyscape",
				"scapeing"
		);
	}
	
	private static final String[] blacklist = {
			/* Bad words */
			"nigger",
			"cunt",
			"fag",
			"retard",
			"faggot",

			/* Advertisement basics */
			"www",
			".com",
			".org",
			".net",
			".io",
			".ps",
			".tk",
			"dotcom",
			"dotorg",
			"dotnet",
			"dottk",
			
			/* Individual server names */
			"kratos",
			"atlas",
			"osscape",
			"alora",
			"elkoy",
			"osrune",
			"guthixp",
			"dawntained",
			"locopk",
			"imagineps",
			"nearreal",
			"pkhonor",
			"dreamsc",
			"manicps",
			"imagineps",
			"draganoth",
			"alosps",
			"rsps2",
			"lostisle",
			"necrotic",
			"redrune",
			"deathwish",
			"pkowned",
			"osbase",
			"beastpk",
			"roatpk",
			"rsgenesis",
			"trinityps",
			"boxrune",
			"runique",
			"furiousp",
			"novus",
			"ikov",
			"joinmy",
			"atarax",
			"nardahp",
			"illerai",
			"letspk",
			"ratedpixel",
			"cloudnine",
			"viceos",
			"deprivedr",
			"exoria",
			"simplicityp",
			"cruxp",
			"ospkz",
			"scapewar",
			"amberp",
			"diviner",
			"osunity",
			"amulius",
			"zenyteps",
			"zenyteosrs"
	};
	
	private static final char[][] leetspeakToNormal = {
			{'1', 'i'},
			{'!', 'i'},
			{'3', 'e'},
			{'4', 'a'},
			{'@', 'a'},
			{'5', 's'},
			{'7', 't'},
			{'0', 'o'},
			{'9', 'g'},
			
			/* Additional leetspeak support that Jire added. */
			{'6', 'g'},
			{'$', 's'},
			{'&', 'a'},
			{'(', 'c'},
			{')', 'd'},
			{'+', 't'}
	};
	
	private static final ThreadLocal<StringBuilder> sb = ThreadLocal.withInitial(StringBuilder::new); // make this regular if you don't need thread safety.
	
	/**
	 * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
	 */
	public static boolean containsBadWord(String input) {
		if (input == null) {
			return false;
		}
		
		StringBuilder sb = BadWords.sb.get();
		sb.setLength(0);
		
		removeLeetspeak:
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isLetter(c)) {
				sb.append(Character.toLowerCase(c));
			} else {
				for (char[] conversion : leetspeakToNormal) {
					if (c == conversion[0]) {
						sb.append(conversion[1]);
						continue removeLeetspeak;
					}
				}
			}
		}
		
		// iterate over each letter in the word
		for (int start = 0; start < sb.length(); start++) {
			// from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
			for (int offset = 1; offset < (sb.length() + 1 - start) && offset < largestWordLength; offset++) {
				long hash = LongHashFunction.xx().hashChars(sb, start, offset);
				if (words.containsKey(hash)) {
					// for example, if you want to say the word bass, that should be possible.
					String[] ignoreCheck = words.get(hash);
					boolean ignore = false;
					for (int s = 0; s < ignoreCheck.length; s++) {
						if (indexOf(sb, ignoreCheck[s]) >= 0) {
							ignore = true;
							break;
						}
					}
					if (!ignore) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private static int indexOf(CharSequence source, CharSequence target) {
		int sourceCount = source.length();
		int targetCount = target.length();
		int sourceOffset = 0;
		int targetOffset = 0;
		
		if (0 >= sourceCount) {
			return (targetCount == 0 ? sourceCount : -1);
		}
		if (targetCount == 0) {
			return 0;
		}
		
		char first = target.charAt(targetOffset);
		int max = sourceOffset + (sourceCount - targetCount);
		
		for (int i = sourceOffset; i <= max; i++) {
			/* Look for first character. */
			if (source.charAt(i) != first) {
				while (++i <= max && source.charAt(i) != first) ;
			}
			
			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && source.charAt(j)
						== target.charAt(k); j++, k++)
					;
				
				if (j == end) {
					/* Found whole string. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}

	public static String filterBadWords(String input) {
		if (input == null) {
			return null;  // If no input, nothing to filter
		}

		// Use the thread-local StringBuilder for normalization (same as containsBadWord)
		StringBuilder sb = BadWords.sb.get();
		sb.setLength(0);

		// Mapping from normalized string index -> original input index
		int[] indexMap = new int[input.length()];
		int normalizedLength = 0;

		// 1. Build normalized string (letters only, leetspeak converted)
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isLetter(c)) {
				// Keep letters (in lowercase)
				sb.append(Character.toLowerCase(c));
				indexMap[normalizedLength++] = i;
			} else {
				// Convert common leetspeak symbols to letters, or skip others
				boolean replaced = false;
				for (char[] conv : leetspeakToNormal) {
					if (c == conv[0]) {
						sb.append(conv[1]);
						indexMap[normalizedLength++] = i;
						replaced = true;
						break;  // Found a mapping, appended the letter
					}
				}
				// If no conversion found and not a letter, we ignore this character (do not append)
			}
		}

		// 2. Scan for bad word substrings in the normalized content
		int normalizedEnd = normalizedLength;  // effective length of sb content
		boolean[] toReplace = new boolean[input.length()];
		for (int start = 0; start < normalizedEnd; start++) {
			// Only check substrings up to max bad word length to limit checks&#8203;:contentReference[oaicite:12]{index=12}
			for (int length = 1; length <= largestWordLength && start + length <= normalizedEnd; length++) {
				long hash = LongHashFunction.xx().hashChars(sb, start, length);
				if (!words.containsKey(hash)) {
					continue;  // not a bad word, check next substring
				}
				// Potential bad word found – verify ignore list conditions
				String[] ignoreList = words.get(hash);
				boolean ignore = false;
				if (ignoreList != null) {
					for (String ignoreWord : ignoreList) {
						// If an allowed word appears in the normalized input, ignore this bad word
						if (indexOf(sb, ignoreWord) >= 0) {
							ignore = true;
							break;
						}
					}
				}
				if (ignore) {
					continue;  // skip marking this occurrence
				}
				// Mark all corresponding original indices for replacement with '*'
				for (int j = 0; j < length; j++) {
					int origIndex = indexMap[start + j];
					toReplace[origIndex] = true;
				}
			}
		}

		// 3. Build the output string with '*' in place of bad word characters
		char[] outputChars = input.toCharArray();
		for (int i = 0; i < outputChars.length; i++) {
			if (toReplace[i]) {
				outputChars[i] = '*';
			}
		}

		return new String(outputChars);
	}

}
