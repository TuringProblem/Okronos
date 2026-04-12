package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.content.HomeHandler;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;

public class LunarTeleport extends Spell {

	public static final LunarTeleport MOONCLAN_TELEPORT = new LunarTeleport(69, 66.0, new Bounds(2113, 3915, 2113, 3915, 0), Rune.LAW.toItem(1), Rune.ASTRAL.toItem(2), Rune.EARTH.toItem(2));
	public static final LunarTeleport OURANIA_TELEPORT = new LunarTeleport(71, 69.0, new Bounds(2468, 3247, 2468, 3247, 0), Rune.LAW.toItem(1), Rune.ASTRAL.toItem(2), Rune.EARTH.toItem(6));
	public static final LunarTeleport WATERBIRTH_TELEPORT = new LunarTeleport(72, 71.0, new Bounds(2547, 3755, 2547, 3755, 0), Rune.LAW.toItem(1), Rune.ASTRAL.toItem(2), Rune.WATER.toItem(1));
	public static final LunarTeleport BARBARIAN_TELEPORT = new LunarTeleport(75, 76.0, new Bounds(2542, 3568, 2542, 3568, 0), Rune.LAW.toItem(2), Rune.ASTRAL.toItem(2), Rune.FIRE.toItem(3));
	public static final LunarTeleport KHAZARD_TELEPORT = new LunarTeleport(78, 80.0, new Bounds(2636, 3167, 2636, 3167, 0), Rune.LAW.toItem(2), Rune.ASTRAL.toItem(2), Rune.WATER.toItem(4));
	public static final LunarTeleport FISHING_GUILD_TELEPORT = new LunarTeleport(85, 89.0, new Bounds(2611, 3391, 2611, 3391, 0), Rune.LAW.toItem(3), Rune.ASTRAL.toItem(3), Rune.WATER.toItem(10));
	public static final LunarTeleport CATHERBY_TELEPORT = new LunarTeleport(87, 92.0, new Bounds(2803, 3449, 2803, 3449, 0), Rune.LAW.toItem(3), Rune.ASTRAL.toItem(3), Rune.WATER.toItem(10));
	public static final LunarTeleport ICE_PLATEAU_TELEPORT = new LunarTeleport(89, 96.0, new Bounds(2973, 3940, 2973, 3940, 0), Rune.LAW.toItem(3), Rune.ASTRAL.toItem(3), Rune.WATER.toItem(8));


	public int getLvlReq() {
		return lvlReq;
	}

	public double getXp() {
		return xp;
	}

	public Item[] getRunes() {
		return runes;
	}

	public LunarTeleport(int lvlReq, double xp, Bounds bounds, Item... runes) {
		this.lvlReq = lvlReq;
		this.xp = xp;
		this.runes = runes;
		registerClick(lvlReq, xp, true, runes, (p, i) -> teleport(p, bounds));
	}

	public LunarTeleport(int lvlReq, double xp, Bounds[] bounds, Item... runes) {
		this.lvlReq = lvlReq;
		this.xp = xp;
		this.runes = runes;
		registerClick(lvlReq, xp, true, runes, (p, i) -> teleport(p, bounds[i]));
	}

	public LunarTeleport(int lvlReq, double xp, int x, int y, int z, Item... runes) {
		this.lvlReq = lvlReq;
		this.xp = xp;
		this.runes = runes;
		registerClick(lvlReq, xp, true, runes, (p, i) -> teleport(p, x, y, z));
	}

	private final int lvlReq;
	private final double xp;
	private final Item[] runes;

	public static boolean teleport(Player player, Bounds bounds) {
		return teleport(player, bounds.randomX(), bounds.randomY(), bounds.z);
	}

	public static boolean teleport(Player player, Position position) {
		return teleport(player, position.getX(), position.getY(), position.getZ());
	}

	public static boolean teleport(Player player, int x, int y, int z) {
		return player.getMovement().startTeleport(e -> {
			e.setCancelCondition(() -> player.teleportListener != null && !player.teleportListener.allow(player));
			if (player.getInventory().contains(25104)) {
				player.sendMessage("The crystal of memories stores your last location as an available teleport.");
				player.crystalMemoryPosition = player.getPosition().copy();
			}
			player.animate(8805);
			player.graphics(1822, 92, 0);
			//player.publicSound(200);
			e.delay(5);
			player.animate(8807);
			player.graphics(1823, 92, 0);
			e.delay(3);
			player.getMovement().teleport(x, y, z);
		});
	}
}