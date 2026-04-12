package discord.webhooks;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-23
 */
public class Util {

	public static String getMapLocation(int x, int y, int z) {
		return "[%s](https://explv.github.io/?centreX=%d&centreY=%d&centreZ=%d&zoom=10)"
			.formatted("[%d, %d, %d]".formatted(x, y, z), x, y, z);
	}
}
