package io.ruin.model.skills.smithing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ruin.cache.ItemID;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.storage.CoalBag;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class SmeltBar {
	private static final Logger logger = LoggerFactory.getLogger(SmeltBar.class);

	private static void open(Player player) {
		/*
		 * If a player has a mould in inventory and gold ore, Open mould interface rather than bars
		 */
		Integer[] moulds = {1592, 1595, 1597, 11065};
		for (int id : moulds) {
			if (player.getInventory().contains(new Item(id)) && player.getInventory().contains(new Item(2357))) {
				player.openInterface(ToplevelComponent.MAINMODAL, Interface.MOULD);
				return;
			}
		}

		SkillDialogue.make(player,
			(p, item) -> {
				if (item.getDef().smithBar == null) {
					logger.error("Smith bar is null for an item: " + item.getId());
					return;
				}
				smelt(player, item.getDef().smithBar, item.getAmount());
			},
			new SkillItem(2349),
			//new SkillItem(9467),
			new SkillItem(2351),
			new SkillItem(2353),
			new SkillItem(2355),
			new SkillItem(2357),
			new SkillItem(2359),
			new SkillItem(2361),
			new SkillItem(2363),
			new SkillItem(30112));
	}

	public static void smelt(Player player, SmithBar bar, int smeltAmount) {
		player.removeDialogueInterface();
		if (!player.getStats().check(StatType.Smithing, bar.smeltLevel, "smelt that bar"))
			return;
		boolean useCoalBag = player.getInventory().hasId(CoalBag.COAL_BAG);
		player.startEvent(event -> {
			event.setCancelCondition(() -> player.getInventory().findItem(bar.smeltItems.get(0).getId()) == null);
			int remaining = smeltAmount;
			while (remaining-- > 0) {
				int baggedCoalUsed = 0;
				boolean usingNotedCoal = false;
				for (Item item : bar.smeltItems) {
					int id = item.getId();
					int amount = item.getAmount();
					if (id == CoalBag.COAL && useCoalBag) {
						int baggedCoalRemaining = player.baggedCoal - baggedCoalUsed;
						if (baggedCoalRemaining >= amount) {
							baggedCoalUsed += amount;
							continue;
						}
						amount -= baggedCoalRemaining;
						baggedCoalUsed = player.baggedCoal;
					}
					if (id == CoalBag.COAL && player.totalDonated >= 250 && !player.getInventory().contains(id, amount) && player.getInventory().contains(CoalBag.COAL + 1, amount)) {
						usingNotedCoal = true;
					} else if (!player.getInventory().contains(id, amount)) {
						if (remaining == (smeltAmount - 1))
							player.sendMessage("You don't have enough ore to make this.");
						else
							player.sendMessage("You've ran out of ores to continue smelting.");
						return;
					}
				}
				player.animate(899);
				for (Item item : bar.smeltItems) {
					int id = item.getId();
					int amount = item.getAmount();
					if (id == CoalBag.COAL && baggedCoalUsed > 0) {
						player.baggedCoal -= baggedCoalUsed;
						amount -= baggedCoalUsed;
						if (amount == 0) {
							/* all required coal came from bag */
							continue;
						}
					}
					if (usingNotedCoal && id == CoalBag.COAL) {
						int notedCoal = player.getInventory().getAmount(CoalBag.COAL + 1);
						if (amount > notedCoal) {
							player.getInventory().remove(CoalBag.COAL + 1, notedCoal);
							player.getInventory().remove(CoalBag.COAL, amount - notedCoal);
						} else {
							player.getInventory().remove(CoalBag.COAL + 1, amount);
						}
					} else {
						player.getInventory().remove(id, amount);
					}
				}
				bar.counter.increment(player, 1);
				player.getInventory().add(bar.itemId, 1);
				PerkTaskHandler.handleGatherResource(player, bar.itemId, 1);
				DailyTasks.handleItemObtained(player, bar.itemId, StatType.Smithing);
				double xp = bar.smeltXp;
				if (bar == SmithBar.GOLD && player.getEquipment().hasId(776)) // goldsmith gauntlets
					xp *= 3.5;
				player.getStats().addXp(StatType.Smithing, xp, true);
				event.delay(2);
			}
		});
	}

	private static void makeCannonBall(Player player, Item item, GameObject obj) {
		if (player.getInventory().contains(27012))
			makeDoubleCannonballs(player, item, obj);
		else if (player.getInventory().contains(ItemID.AMMO_MOULD))
			makeCannonballs(player, item, obj);
	}

	public static void register() {
		ObjectAction.register("furnace", "smelt", (player, obj) -> open(player));
		ObjectAction.register("lava forge", "smelt", (player, obj) -> open(player));
		ObjectAction.register("clay forge", "smelt", (player, obj) -> open(player));
		for (SmithBar smithBar : SmithBar.VALUES) {
			for (Item item : smithBar.smeltItems) {
				ItemObjectAction.register(item.getId(), "furnace", (player, item1, obj) -> smelt(player, smithBar, 1));
				ItemObjectAction.register(item.getId(), "lava forge", (player, item1, obj) -> smelt(player, smithBar, 1));
				ItemObjectAction.register(item.getId(), "clay forge", (player, item1, obj) -> smelt(player, smithBar, 1));
				ItemObjectAction.register(item.getId(), "sulphur vent", (player, item1, obj) -> smelt(player, smithBar, 1));
			}
		}
		ItemObjectAction.register(ItemID.AMMO_MOULD, "furnace", SmeltBar::makeCannonballs);
		ItemObjectAction.register(27012, "furnace", SmeltBar::makeDoubleCannonballs);
		ItemObjectAction.register(ItemID.STEEL_BAR, "furnace", SmeltBar::makeCannonBall);
		ItemObjectAction.register(ItemID.AMMO_MOULD, "clay forge", SmeltBar::makeCannonballs);
		ItemObjectAction.register(27012, "clay forge", SmeltBar::makeDoubleCannonballs);
		ItemObjectAction.register(ItemID.STEEL_BAR, "clay forge", SmeltBar::makeCannonBall);
	}

	private static void makeCannonballs(Player player, Item item, GameObject object) {
		SkillDialogue.make(player, new SkillItem(ItemID.CANNONBALL).addAction((p, amount, event) -> {
			if (player.getInventory().hasId(ItemID.STEEL_BAR) && player.getInventory().hasId(ItemID.AMMO_MOULD)) {
				p.startEvent(e -> {
					int amt = amount;
					while (amt-- > 0) {
						if (!player.getInventory().hasId(ItemID.STEEL_BAR)) {
							player.sendMessage("You don't have any steel bars.");
							return;
						}
						int balls_per_bar = 4;
						player.animate(899);
						player.sendMessage("You heat the steel bar into a liquid state.");
						e.delay(1);
						player.sendMessage("You pour the molten metal into your ammo mould.");
						e.delay(1);
						player.animate(827);
						player.sendMessage("The molten metal cools slowly to form 4 cannonballs.");
						e.delay(2);
						player.getInventory().remove(ItemID.STEEL_BAR, 1);
						player.getInventory().add(ItemID.CANNONBALL, balls_per_bar);
						player.getStats().addXp(StatType.Smithing, 6.4 * balls_per_bar, true);
						player.sendMessage("You remove the cannonballs from the mould.");
					}
				});
			} else {
				player.sendMessage("You need an ammo mould to do that.");
			}
		}));
	}

	private static void makeDoubleCannonballs(Player player, Item item, GameObject object) {
		SkillDialogue.make(player, new SkillItem(ItemID.CANNONBALL).addAction((p, amount, event) -> {
			if (player.getInventory().hasId(ItemID.STEEL_BAR) && player.getInventory().hasId(27012)) {
				p.startEvent(e -> {
					int amt = amount;
					while (amt-- > 0) {
						if (player.getInventory().getAmount(ItemID.STEEL_BAR) < 2) {
							player.sendMessage("You don't have enough steel bars.");
							return;
						}
						int balls_per_bar = 8;
						player.animate(899);
						player.sendMessage("You heat the steel bar into a liquid state.");
						e.delay(1);
						player.sendMessage("You pour the molten metal into your double ammo mould.");
						e.delay(1);
						player.animate(827);
						player.sendMessage("The molten metal cools slowly to form 8 cannonballs.");
						e.delay(2);
						player.getInventory().remove(ItemID.STEEL_BAR, 2);
						player.getInventory().add(ItemID.CANNONBALL, balls_per_bar);
						player.getStats().addXp(StatType.Smithing, 6.4 * balls_per_bar, true);
						player.sendMessage("You remove the cannonballs from the mould.");
					}
				});
			} else {
				player.sendMessage("You need a double ammo mould to do that.");
			}
		}));
	}

}
