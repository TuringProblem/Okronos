package npc.nex.utils;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public interface NexDropI {

	static void handleNexDrop(Player plr, NPC npc, int rolls) {
		for (int i = 0; i < rolls; i++) {
			Item rolled = rollRegular(plr);
			plr.addToCollectionLog(rolled);
			plr.localPlayers().forEach(p2 -> {
				if (p2 != null)
					p2.sendFilteredMessage(Color.DARK_GREEN.wrap(plr.getName() + " received a drop: "
							+ NumberUtils.formatNumber(rolled.getAmount()) + " x " + rolled.getDef().name));
			});
			int amount = rolled.getAmount();
			if (amount > 1 && !rolled.getDef().stackable && !rolled.getDef().isNote())
				rolled.setId(rolled.getDef().notedId);
			new GroundItem(rolled).owner(plr)
					.position(plr.getPosition().getX(), plr.getPosition().getY(), plr.getPosition().getZ())
					.spawn();
			if (rolled.getId() == 26372 ||
					rolled.getId() == 26235 ||
					rolled.getId() == 30523 ||
					rolled.getId() == 30522 ||
					rolled.getId() == 30521 ||
					rolled.getId() == 26376 ||
					rolled.getId() == 26378 ||
					rolled.getId() == 26380 ||
					rolled.getId() == 30545) {
				Broadcast.WORLD.sendNewsDropMessage(plr, Icon.ADMINISTRATOR, "<col=000000>%s".formatted(plr.getName()),
						" received <shad=D80808>%dx %s</shad> from a %s! (<col=FC0101>%d KC<col=000000>)"
								.formatted(
										rolled.getAmount(),
										rolled.getDef().name.toLowerCase(),
										npc.getDef().name.toLowerCase(),
										npc.getDef().killCounter.apply(plr).getKills()));

				RareDropHook.sendDiscordMessage(() -> {
					var jsonObject = new JSONObject();
					jsonObject.put("player", plr.getName());
					jsonObject.put("game_mode", plr.getGameMode());
					jsonObject.put("item_id", rolled.getId());
					jsonObject.put("item_name", rolled.getDef().name);
					jsonObject.put("source", npc.getDef().descriptiveName);
					jsonObject.put("total_attempts", Utils.formatMoneyString(npc.getDef().killCounter.apply(plr).getKills() + 1));
					return jsonObject;
				});
			}
		}
		new GroundItem(592, 1).owner(plr)
				.position(plr.getPosition().getX(), plr.getPosition().getY(), plr.getPosition().getZ()).spawn();
		new GroundItem(995, Random.get(100000, 150000)).owner(plr)
				.position(plr.getPosition().getX(), plr.getPosition().getY(), plr.getPosition().getZ()).spawn();

	}


	static LootTable regularTable = new LootTable()
			.addTable(125,
					new LootItem(1320, Random.get(2, 5), 3), // Rune 2h sword
					new LootItem(1276, Random.get(2, 5), 3), // Rune pickaxe
					new LootItem(1304, Random.get(2, 5), 2), // Rune longsword
					new LootItem(1290, Random.get(2, 5), 2), // Rune sword
					new LootItem(5316, Random.get(3, 5), 3), // magic seed

					new LootItem(2, 3000, 3), // cannonballs x3000
					new LootItem(6686, 50, 3), // saradomin brew noted x50
					new LootItem(12696, 25, 2), // super combat pooition noted x25
					new LootItem(5730, 1, 3) // dragon spear
			)
			.addTable(125,
					new LootItem(1128, 5, 3),
					new LootItem(4088, Random.get(2, 3), 3),
					new LootItem(22804, Random.get(50, 125), 3),
					new LootItem(19484, Random.get(50, 125), 3))
			.addTable(125,
					new LootItem(561, Random.get(600, 700), 5),
					new LootItem(21880, Random.get(300, 500), 5),
					new LootItem(563, Random.get(500, 600), 5),
					new LootItem(565, Random.get(500, 700), 5),
					new LootItem(26231, Random.get(20, 100), 5),
					new LootItem(560, Random.get(600, 700), 5))
			.addTable(125,
					new LootItem(450, Random.get(25, 50), 4),
					new LootItem(452, Random.get(15, 20), 4),
					new LootItem(454, Random.get(115, 120), 3))
			.addTable(125,
					new LootItem(995, Random.get(200000, 250000), 3),
					new LootItem(5300, 1, 3),
					new LootItem(1514, Random.get(15, 20), 3),
					new LootItem(3024, 3, 2),
					new LootItem(3052, 3, 2),

					new LootItem(12073, 1, 2))
			.addTable(1,
					new LootItem(26376, 1, 2),
					new LootItem(30545, 1, 3),
					new LootItem(26378, 1, 2),
					new LootItem(26380, 1, 2))
			.addTable(1,
					new LootItem(26235, 1, 2),
					new LootItem(26372, 1, 2));

	public static Item rollRegular(Player player) {
		return regularTable.rollDrop(player);
	}

}
