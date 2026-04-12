package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.CombatUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;

import java.util.ArrayList;
import java.util.List;

public class VenatorBow {

	private static final int ESSENCE = 27616;
	private static final int ESSENCE_PER_CHARGE = 1;
	private static final int MAX_CHARGE = 20000;

	public static int getSize(Iterable<NPC> iterable, NPC target) {
		int count = 0;
		for (NPC ignored : iterable) {
			if (ignored == target)
				continue;
			if (ignored.getPosition().isWithinDistance(target.getPosition(), 5))
				count++;
		}
		return count;
	}

	public static List<NPC> getNPCs(Iterable<NPC> iterable, NPC target) {
		List<NPC> npcs = new ArrayList<>();
		for (NPC ignored : iterable) {
			if (target == ignored)
				continue;
			if (ignored.getCombat() == null)
				continue;
			if (ignored.getPosition().distance(target.getPosition()) < 5)
				npcs.add(ignored);
		}
		return npcs;
	}

	private static void check(Player player, Item item) {
		if (item.getId() == 27610) {
			player.sendMessage("Your Venator bow has " + NumberUtils.formatNumber(item.getAttributeInt(AttributeTypes.CHARGES)) + " charges remaining.");
		}
	}


	private static void charge(Player player, Item bow, Item ess) {
		int currentCharges = AttributeExtensions.getCharges(bow);
		int allowedAmount = MAX_CHARGE - currentCharges;
		if (allowedAmount == 0) {
			player.sendMessage("Your Venator bow is already full charged.");
			return;
		}
		int addAmount = Math.min(ess.getAmount(), allowedAmount);
		bow.setId(27610);
		AttributeExtensions.addCharges(bow, addAmount);
		ess.incrementAmount(-addAmount);
		check(player, bow);
	}

	private static void createBow(Player player, Item shard, Item shard2) {
		if (shard.getId() == 27614 && shard2.getId() == 27614) {
			int shardCount = player.getInventory().getAmount(27614);
			if (shardCount >= 5) {
				player.getInventory().remove(27614, 5);
				player.getInventory().add(27612, 1);
				player.sendMessage("You combine the shards to create a Venator bow.");
			} else {
				player.sendMessage("You need at least 5 shards to create a Venator bow.");
			}
		}
	}


	private static void breakDownShard(Player player, Item shard, Item pestle) {
		player.dialogue(new YesNoDialogue("Are you sure you want to break down the shard?", "You will receive 50,000 ancient essence for doing so.", shard, () -> {
			player.getInventory().remove(27614, 1);
			player.getInventory().add(ESSENCE, 50000);
			player.sendMessage("You break down the shards into ancient essence.");
		}));
	}


	public static void handlePassiveEffect(Player player, NPC target) {
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (weapon == null || weapon.getId() != 27610)
			return;
		if (AttributeExtensions.hasAttribute(weapon, AttributeTypes.CHARGES)) {
			int charges = AttributeExtensions.getCharges(AttributeTypes.CHARGES, weapon);
			if (charges < 1)
				return;
			if (player.inMulti()) {
				int maxDamage = CombatUtils.getMaxDamage(player, player.getCombat().getAttackStyle(), player.getCombat().getAttackType());
				maxDamage = (maxDamage * 2) / 3;
				int finalMaxDamage = maxDamage;
				World.startEvent(e -> {
					int npcCount = getSize(target.localNpcs(), target);
					if (npcCount > 1 && !getNPCs(target.localNpcs(), target).isEmpty()) {
						NPC targetNpc = Random.get(getNPCs(target.localNpcs(), target));
						int delay = Projectile.arrow(2291)[0].send(target, targetNpc);
						e.delay(World.getTicks(delay) + 1);
						targetNpc.hit(new Hit(player, AttackStyle.RANGED).randDamage(finalMaxDamage));
						npcCount = getSize(target.localNpcs(), targetNpc);
						if (npcCount > 0) {
							int getNPCCount = getNPCs(target.localNpcs(), targetNpc).size();
							if (getNPCCount > 0) {
								targetNpc = Random.get(getNPCs(target.localNpcs(), target));
								if (targetNpc == null) {
									return;
								}
								delay = Projectile.arrow(2291)[0].send(target, targetNpc);
								e.delay(World.getTicks(delay) + 1);
								targetNpc.hit(new Hit(player, AttackStyle.RANGED).randDamage(finalMaxDamage));
							}
						}
					}
				});
			}
		}
	}

	public static void register() {
		ItemItemAction.register(27610, ESSENCE, VenatorBow::charge);
		ItemItemAction.register(27612, ESSENCE, VenatorBow::charge);
		ItemItemAction.register(27614, ItemID.PESTLE_AND_MORTAR, VenatorBow::breakDownShard);
		ItemItemAction.register(27614, 27614, VenatorBow::createBow);
		ItemAction.registerInventory(27610, "check", VenatorBow::check);
		ItemAction.registerEquipment(27610, "check", VenatorBow::check);
		ItemAction.registerInventory(27610, "uncharge", VenatorBow::uncharge);
		ItemAction.registerInventory(27614, 1, VenatorBow::createBow2);
		ObjType.get(27610).addPostTargetDefendListener(((player, item, hit, target) -> {
			int charges = AttributeExtensions.getCharges(item);
			if (charges <= 1) {
				player.sendMessage("Your Venator bow has ran out of charges!");
				item.setId(27612);
			}
			if (charges > 0)
				AttributeExtensions.deincrementCharges(item, 1);
		}));
	}

	private static void createBow2(Player player, Item shard) {
		if (shard.getId() == 27614) {
			int shardCount = player.getInventory().getAmount(27614);
			if (shardCount >= 5) {
				player.getInventory().remove(27614, 5);
				player.getInventory().add(27612, 1);
				player.sendMessage("You combine the shards to create a Venator bow.");
			} else {
				player.sendMessage("You need at least 5 shards to create a Venator bow.");
			}
		}
	}

	private static void uncharge(Player player, Item item) {
		int reqSlots = 0;
		int bloodAmount = AttributeExtensions.getCharges(item);
		if (bloodAmount > 0) {
			if (!player.getInventory().hasId(ESSENCE))
				reqSlots++;
		}
		if (player.getInventory().getFreeSlots() < reqSlots) {
			player.sendMessage("You dont have enough inventory space to uncharge the Venator bow");
			return;
		}
		player.dialogue(new YesNoDialogue("Are you sure you want to uncharge it?", "Your Venator bow will become uncharged.",

			item, () -> {
			if (bloodAmount > 0)
				player.getInventory().add(ESSENCE, bloodAmount);
			AttributeExtensions.setCharges(item, 0);
			item.setId(27612);
		}));
	}
}
