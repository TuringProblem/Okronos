package io.ruin.model.item.actions.impl.itemeffects.itemhandlers;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.CrimsonChin;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;

public class AoESwipe {
	public static void consumeCharge(Player player, Item item, Entity entity, AttackStyle attackStyle) {
		if (attackStyle == AttackStyle.CANNON)
			return;
		if (AttributeExtensions.getCharges(AttributeTypes.AOE_SWING, item) > 0) {
			if (entity instanceof NPC) {
				if (Random.get(5) == 0 && player.aoeSwipeDelay.finished()) {
					player.aoeSwipeDelay.delay(8);
					player.graphics(1898);
					if (entity.inMulti()) {
						int entityIndex = player.getClientIndex();
						int targetIndex = entity.getClientIndex();
						int targetCount = 0;
						int maxTargets = 9;
						for (NPC npc : entity.localNpcs()) {
							if (npc == null)
								continue;
							int npcIndex = npc.getClientIndex();
							if (npcIndex == entityIndex || npcIndex == targetIndex)
								continue;
							if (!npc.getPosition().isWithinDistance(entity.getPosition(), 1))
								continue;
							if (npc.getDef().ignoreMultiCheck)
								continue;
							if (!player.getCombat().canAttack(npc, false))
								continue;
							npc.hit(new Hit(player, player.getCombat().getAttackStyle(), player.getCombat().getAttackType()).randDamage(25).setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
							if (++targetCount >= maxTargets)
								break;
						}
					}
				}
				if (!entity.npc.getDef().name.contains("dummy"))
					AttributeExtensions.deincrementCharges(AttributeTypes.AOE_SWING, item, 1);
			}
		} else {
			player.sendMessage(Color.RED.wrap("Your aoe swing effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.AOE_SWING, item);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The aoe swing effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.AOE_SWING, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your aoe swing effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.AOE_SWING, item);
				player.sendMessage("You remove the aoe swing effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.AOE_SWING))
			check(player, item);
	}
}
