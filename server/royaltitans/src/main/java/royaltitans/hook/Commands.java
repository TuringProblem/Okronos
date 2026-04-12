package royaltitans.hook;


import royaltitans.RoyalTitansArea;
import static core.api.API.*;

public class Commands {
	public static void register() {
		hook_cmd((ctx) -> {
			var player = ctx.player();
			var command = ctx.command();

			if (!player.is_admin()) {
				return Pass;
			}

			switch (command) {
				case "royal_titans", "rt" -> {
					var area = new RoyalTitansArea();
					area.move(player);
					queue(area);
					return Return;
				}
			}
			return Pass;

		});
	}
}
