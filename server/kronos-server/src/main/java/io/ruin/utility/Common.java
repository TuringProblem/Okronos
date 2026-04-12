package io.ruin.utility;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author R-Y-M-R
 * @date 2/2/2022
 * Contains common utilities.
 */
public class Common {

	/**
	 * Checks if player has minimum level(s)
	 *
	 * @param p
	 * @param minimumLevel
	 * @param boostsAllowed
	 * @param stat
	 * @return
	 */
	public static boolean hasStatRequirements(Player p, int minimumLevel, boolean boostsAllowed, StatType... stat) {
		for (StatType s : stat) {
			final int mylvl = boostsAllowed ? p.getStats().get(s).currentLevel : p.getStats().get(s).fixedLevel;
			if (mylvl < minimumLevel) {
				p.sendMessage("You need " + s.descriptiveName + " level of " + minimumLevel + " to use this.");
				return false;
			}
		}
		return true;
	}

	public static final void openDoorEffects(Player p) {
		p.privateSound(62, 1, 25);
		p.animate(4282);
	}

	// the system magic skillcape, nex, etc uses
	public static String getRemainingTime(Long attribute) {
		long ms = attribute - System.currentTimeMillis();
		long hours = TimeUnit.MILLISECONDS.toHours(ms);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);

		return (hours >= 1 ? (hours + " hour" + (hours > 1 ? "s" : "") + " and ") : "") +
			Math.max((minutes - TimeUnit.HOURS.toMinutes(hours)), 1) + " minute" +
			((minutes - TimeUnit.HOURS.toMinutes(hours)) > 1 ? "s" : "");
	}

	public static int convertSecondsToTicksInt(int seconds) {
		return (int) convertSecondsToTicks(seconds);
	}

	public static double convertSecondsToTicks(int seconds) {
		return (double) seconds / 0.6;
	}

	public static boolean clippingIsEmpty(Position pos) {
		final Tile tile = Tile.get(pos);
		if (tile == null || tile.clipping != 0) {
			return false;
		}
		return true;
	}

	public static int countNonNullSpawnedObjects(List<GameObject> objects) {
		int count = 0;
		for (GameObject s : objects) {
			if (s != null) {
				if (!s.isRemoved()) {
					if (s.isSpawned()) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * Format usernames, etc
	 *
	 * @param s
	 * @return
	 */
	public static String formatNoun(String s) {
		if (s.length() <= 1) {
			return s.toUpperCase(); // if name is "g" or someshit, return "G".
		}
		String newStr = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
		return newStr;
	}

	public static void sendTwoItemMessage(Player player, Item item, Item item2, String message) {
		player.dialogue(new ItemDialogue().two((item.getDef().isNote() ? item.getId() - 1 : item.getId()), item2.getDef().isNote() ? item2.getId() - 1 : item2.getId(), message));
	}

	public static void getPVMGear(Player player, boolean ranged) {
		if (player.isOwner()) {
			player.addEvent(e -> {
				int xp = Stat.xpForLevel(99);
				for (int i = 0; i < StatType.VALUES.length; i++) {
					Stat stat = player.getStats().get(i);
					stat.currentLevel = stat.fixedLevel = 99;
					stat.experience = xp;
					stat.updated = true;
				}

				player.getCombat().updateLevel();
				player.getAppearance().update();
				//player.getMovement().teleport(NEX_BOUNDS);
				//player.setGodMode(true);
				//player.sendMessage("Type ::nexx to spawn nex.");

				e.delay(1);

				player.getInventory().clear();
				Item[] gear = new Item[]{
					new Item(ItemID.ARMADYL_CROSSBOW, 1), new Item(ItemID.ARMADYL_HELMET, 1), new Item(ItemID.ARMADYL_CHESTPLATE, 1),
					new Item(ItemID.ARMADYL_CHAINSKIRT, 1), new Item(ItemID.AMULET_OF_FURY, 1), new Item(ItemID.AVAS_ACCUMULATOR, 1),
					new Item(ItemID.PEGASIAN_BOOTS, 1), new Item(ItemID.SPECTRAL_SPIRIT_SHIELD, 1), new Item(ItemID.BARROWS_GLOVES),
					new Item(ItemID.ARCHERS_RING_I),
					new Item(ItemID.RUBY_BOLTS_E, 3000)
				};
				if (!ranged) {
					gear = new Item[]{
						new Item(ItemID.BANDOS_CHESTPLATE, 1), new Item(ItemID.HELM_OF_NEITIZNOT, 1), new Item(ItemID.BANDOS_TASSETS, 1),
						new Item(ItemID.DRAGON_CLAWS, 1), new Item(ItemID.AMULET_OF_FURY, 1), new Item(ItemID.INFERNAL_CAPE, 1),
						new Item(ItemID.PRIMORDIAL_BOOTS, 1), new Item(ItemID.BARROWS_GLOVES),
						new Item(ItemID.BERSERKER_RING_I),
						new Item(ItemID.RUBY_BOLTS_E, 3000)
					};
				}
				for (Item i : gear) {
					player.getEquipment().equip(i);
				}
				e.delay(1);
				Item[] inventory = new Item[]{
					new Item(ItemID.SUPERANTIPOISON4, 1),
					new Item(ItemID.RANGING_POTION4, 1),
					new Item(ItemID.SUPER_DEFENCE4, 1),
					new Item(ItemID.SARADOMIN_BREW4, 10),
					new Item(ItemID.SUPER_RESTORE4, 10),
					new Item(ItemID.DARK_CRAB, 5),
					//new Item(ItemID.DRAGON_DART, 5000),
				};
				if (!ranged) {
					inventory = new Item[]{
						new Item(ItemID.DRAGON_CLAWS, 1),
						new Item(ItemID.DARK_CRAB, 27),
					};
				}
				for (Item i : inventory) {
					player.getInventory().add(i);
				}
			});
		} else {
			player.sendMessage("You are not owner and cannot use this command.");
		}
	}

	public static boolean handleEscapeEvent(Player p) {
		if (VarPlayerRepository.ESCAPE_CLOSES.get(p) == 1) {
			return true;
		}
		p.sendFilteredMessage("You can enable the setting to close interfaces with the escape key.");
		return false;
	}

}
