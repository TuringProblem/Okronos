package io.ruin.model.item.actions.impl.combine;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

public enum ItemCombining {

	FROZEN_ABYSSAL_WHIP(4151, 12769, 12774, false),
	VOLCANIC_ABYSSAL_WHIP(4151, 12771, 12773, false),
	BLUE_BOW_MIX(12757, 11235, 12766, false),
	YELLOW_BOW_MIX(12761, 11235, 12767, false),
	WHITE_BOW_MIX(12763, 11235, 12768, false),
	ODIUM_WARD(12802, 11926, 12807, true),
	MALEDICTION_WARD(12802, 11924, 12806, true),
	GRANITE_MAUL(12849, 4153, 12848, true),
	DRAGON_PICKAXE(11920, 12800, 12797, true),
	DRAGONFIRE_SHIELD(11286, 1540, 11283, false),
	DRAGONFIRE_WARD(22006, 1540, 22003, false),
	NORMAL_BATTLESTAFF(11787, 12798, 12795, true),
	MYSTIC_BATTLESTAFF(11789, 12798, 12796, true),
	ELIDINIS_WARD_F(25985, ItemID.ARCANE_SPIRIT_SHIELD, 27251, true),
	DARK_INFINITY_HAT(12528, 6918, 12457, true),
	DARK_INFINITY_TOP(12528, 6916, 12458, true),
	DARK_INFINITY_BOTTOMS(12528, 6924, 12459, true),
	LIGHT_INFINITY_HAT(12530, 6918, 12419, true),
	LIGHT_INFINITY_TOP(12530, 6916, 12420, true),
	TAINTED_RING(30472, 30474, 30473, false),
	LIGHT_INFINITY_BOTTOMS(12530, 6924, 12421, true),
	AVERNIC_DEFENDER(12954, 22477, 22322, true),
	SARADOMINS_LIGHT(11791, 13256, 22296, false),
	BONECRUSHER_NECKLACE(22988, 13116, 22986, false),
	DRAGON_HUNTER_LANCE(22966, 11889, 22978, false),
	BRIMSTONE_BOOTS(23037, 22957, 22951, false),
	KODAI_WAND(21043, 6914, 21006, false),
	NEITIZNOT_FACEGUARD(24268, 10828, 24271, false),
	ANCIENT_WYVERNSHIELD(21637, 2890, 21633, false),
	SLAYER_STAFF_E(4170, 21257, 21255, false),
	ULTRACOMPOST(6034, 21622, 21483, false),
	DEVOUT_BOOTS(12598, 22960, 22954, false),
	ANCIENT_SCEPTRE(27627, 4675, 27624, false),
	SMOKE_ANCIENT_SCEPTRE(28274, 27624, 28264, false),
	ICE_ANCIENT_SCEPTRE(28270, 27624, 28262, false),
	BLOOD_ANCIENT_SCEPTRE(28268, 27624, 28260, false),
	SHADOW_ANCIENT_SCEPTRE(28272, 27624, 28266, false),
	BERSERKER_NECKLACE_OR(11128, 23237, 23240, true),
	BONE_STAFF(1391, 28798, 28796, true),
	BONE_MACE(1432, 28798, 28792, true),
	BONE_SHORTBOW(857, 28798, 28794, true),
	RANCOUR(29799, 19553, 29801, true),
	EMBERLIGHT(ItemID.ARCLIGHT, 29580, 29589, false),
	PURGING_STAFF(ItemID.BATTLESTAFF, 29580, 29594, false),
	SCORCHING_BOW(ItemID.MAGIC_LONGBOW_U, 29580, 29591, false),
	BURNING_CLAW(29574, 29574, 29577, false),

	;


	public final int primaryId, secondaryId, combinedId;
	public final boolean reversible;

	ItemCombining(int primaryId, int secondaryId, int combinedId, boolean reversible) {
		this.primaryId = primaryId;
		this.secondaryId = secondaryId;
		this.combinedId = combinedId;
		this.reversible = reversible;
		ObjType.get(combinedId).combinedFrom = this;
	}

	private static void make(Player player, Item item, Item kit, int resultID, boolean reversible) {
		String message;
		if (reversible)
			message = "Combine the " + item.getDef().name + " and " + kit.getDef().name + "?";
		else
			message = "Combining these items will be irreversible";
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", message, resultID, 1, () -> {
				player.animate(713);
				Item result = new Item(resultID, 1);
				AttributeExtensions.putAttributes(result, item.copyOfAttributes());
				item.clearAttributes();
				kit.clearAttributes();
				if(item.getId() == 29574) {
					 kit.remove();
					 item.remove();
				} else {
					item.remove(1);
					kit.remove(1);
				}
				player.getInventory().add(result);
				if (result.getId() == 11283) {
					player.dragonfireShieldsSmithed++;
					if (player.dragonfireShieldsSmithed == Achievements.THIS_WILL_TEACH_THEM.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THIS_WILL_TEACH_THEM.getAchievementName());

				}
				new ItemDialogue().one(resultID, "You apply the " + item.getDef().name + " to the " + kit.getDef().name + ".");
			})
		);
	}

	private static void combineNoxiousHalberd(Player player) {
		if(player.getInventory().hasId(29790) && player.getInventory().hasId(29792) && player.getInventory().hasId(29794)) {
			player.getInventory().remove(29790, 1);
			player.getInventory().remove(29792, 1);
			player.getInventory().remove(29794, 1);
			player.getInventory().add(29796, 1);
			player.sendMessage("You combine the Noxious blade, Noxious pommel and Noxious point to create a Noxious halberd.");
		} else {
			player.sendMessage("You need a Noxious pommel, Noxious point and a Noxious blade to create the Noxious halberd.");
		}
	}

	private static void revert(Player player, Item kit, int primary, int revert) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.sendMessage("You don't have enough inventory space to do this.");
			return;
		}
		Item item = new Item(primary, 1);
		AttributeExtensions.putAttributes(item, kit.copyOfAttributes());
		kit.clearAttributes();
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", "Revert the item back to its normal form and get the kit back?", primary, 1, () -> {
				player.getInventory().add(item);
				kit.setId(revert);
				AttributeExtensions.removeUpgrades(kit);
				new ItemDialogue().one(primary, "You remove the " + kit.getDef().name + " from the " + item.getDef().name + ".");
			})
		);
	}

	public static void register() {
		ItemItemAction.register(29792, 29790, (player, primary, secondary) -> combineNoxiousHalberd(player));
		ItemItemAction.register(29794, 29790, (player, primary, secondary) -> combineNoxiousHalberd(player));
		ItemItemAction.register(29794, 29792, (player, primary, secondary) -> combineNoxiousHalberd(player));
		for (ItemCombining kit : values()) {
			ItemItemAction.register(kit.primaryId, kit.secondaryId, (player, primary, secondary) -> make(player, primary, secondary, kit.combinedId, kit.reversible));
			ItemAction.registerInventory(kit.combinedId, "dismantle", (player, item) -> revert(player, item, kit.primaryId, kit.secondaryId));
			ItemAction.registerInventory(kit.combinedId, "revert", (player, item) -> revert(player, item, kit.primaryId, kit.secondaryId));
			int combinedProtect = ObjType.get(kit.combinedId).protectValue;
			int componentsProtect = Math.max(ObjType.get(kit.primaryId).protectValue, ObjType.get(kit.secondaryId).protectValue);
			if (combinedProtect < componentsProtect)
				ObjType.get(kit.combinedId).protectValue = componentsProtect;
		}

	}

}
