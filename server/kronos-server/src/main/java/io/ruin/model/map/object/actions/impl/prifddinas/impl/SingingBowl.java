package io.ruin.model.map.object.actions.impl.prifddinas.impl;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public enum SingingBowl {

	CRYSTAL_HELM(70, 70, 2500, 23971, 50),
	CRYSTAL_LEGS(72, 72, 5000, 23979, 100),
	CRYSTAL_BODY(74, 74, 7500, 23975, 150),
	CRYSTAL_AXE(76, 76, 6000, 23673, 120),
	CRYSTAL_HARPOON(76, 76, 6000, 23762, 120),
	CRYSTAL_PICKAXE(76, 76, 6000, 23680, 120),
	CRYSTAL_BOW(78, 78, 10000, 23983, 40),
	CRYSTAL_HALBERD(78, 78, 10000, 23987, 40),
	CRYSTAL_SHIELD(78, 78, 10000, 23991, 40),
	ENHANCED_CKEY(80, 80, 12000, 23951, 10),
	BLADE_OF_SAELDOR(82, 82, 5000, 23995, 100),
	BOW_OF_FAERDHINEN(82, 82, 5000, 25865, 100),

	;
//    ETERNAL_CRYSTAL(80, 80, 12000, 23946, 100);

	public final int levelRequired;

	public final int levelRequired2;

	public final double experience;

	public final int product;

	public final int shardAmount;

	SingingBowl(int levelRequired, int levelRequired2, double experience, int product, int shardAmount) {
		this.levelRequired = levelRequired;
		this.levelRequired2 = levelRequired2;
		this.experience = experience;
		this.product = product;
		this.shardAmount = shardAmount;
		Objects.requireNonNull(ObjType.get(product)).singingBowl = this;
	}

	public static void Enhanced(Player player) {

	}

	private static void open(Player player) {
		SkillDialogue.make(
			player,
			new SkillItem(23971).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23971), amount);
			}),
			new SkillItem(23979).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23979), amount);
			}),
			new SkillItem(23975).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23975), amount);
			}),
			new SkillItem(23673).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23673), amount);
			}),
			new SkillItem(23762).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23762), amount);
			}),
			new SkillItem(23680).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23680), amount);
			}),
			new SkillItem(23983).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23983), amount);
			}),
			new SkillItem(23987).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23987), amount);
			}),
			new SkillItem(23991).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23991), amount);
			}),
			new SkillItem(23951).addAction((p, amount, event) -> {
				makeItem(player, ObjType.get(23951), amount);
			})

		);
	}

	private static void openEnhanced(Player player) {
		SkillDialogue.make(
			player,
			(p, item) -> makeItem(player, item.getDef(), 1),
			new SkillItem(23995),
			new SkillItem(25865)
		);
	}


	private static void makeItem(Player player, ObjType def, int amount) {
		player.removeDialogueInterface();
		SingingBowl singingBowl = def.singingBowl;


		AtomicInteger amountToMake = new AtomicInteger(amount);
		player.startEvent(event -> {
			if (!player.getStats().check(StatType.Crafting, singingBowl.levelRequired)) {
				player.sendMessage("<col=880000>You need atleast level " + singingBowl.levelRequired + " Crafting to continue");
				return;
			}
			if (!player.getStats().check(StatType.Smithing, singingBowl.levelRequired2)) {
				player.sendMessage("<col=880000>You need atleast level " + singingBowl.levelRequired2 + " Smithing to continue");
				return;
			}
			if (player.getInventory().getAmount(23962) < 10) {
				player.sendMessage("<col=880000>You don't have the required items to continue.");
				return;
			}

			switch (singingBowl) {
				case CRYSTAL_HELM: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(23956)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(23956, 1);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().add(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
							return;
						}
						event.delay(1);
					}
					return;
				}
				case CRYSTAL_BODY: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(23956, 3)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(23956, 3);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().add(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
							return;
						}
						event.delay(1);
					}
					return;
				}
				case CRYSTAL_LEGS: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(23956, 2)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(23956, 2);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().add(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
							return;
						}
						event.delay(1);
					}
					return;
				}
				case CRYSTAL_BOW:
				case CRYSTAL_HALBERD:
				case CRYSTAL_SHIELD: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(4207)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(4207, 1);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().add(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
							return;
						}
						event.delay(1);
					}
					return;
				}
				case ENHANCED_CKEY: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(989) || !player.getInventory().contains(23962, singingBowl.shardAmount)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(989, 1);
							player.getInventory().remove(23962, 10);
							player.animate(899);
							player.getInventory().addOrDrop(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
						}
						event.delay(1);
					}
					return;
				}
				case CRYSTAL_AXE: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(23953) && !player.getInventory().contains(6739)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(23953, 1);
							player.getInventory().remove(6739, 1);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().addOrDrop(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
						}
						event.delay(1);
					}
					return;
				}
				case CRYSTAL_HARPOON: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(23953) && !player.getInventory().contains(21028)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(23953, 1);
							player.getInventory().remove(21028, 1);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().addOrDrop(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
						}
						event.delay(1);
					}
					return;
				}
				case CRYSTAL_PICKAXE: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(23953) && !player.getInventory().contains(11920)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
							return;
						}
						player.getInventory().remove(23953, 1);
						player.getInventory().remove(11920, 1);
						player.getInventory().remove(23962, singingBowl.shardAmount);
						player.animate(899);
						player.getInventory().addOrDrop(singingBowl.product, 1);
						double xpPickaxe = singingBowl.experience;
						player.getStats().addXp(StatType.Crafting, xpPickaxe, false);
						player.getStats().addXp(StatType.Smithing, xpPickaxe, false);
						event.delay(1);
					}
					return;
				}
				case BLADE_OF_SAELDOR: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(25859) || !player.getInventory().contains(23962, singingBowl.shardAmount)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(25859, 1);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().addOrDrop(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
						}
						event.delay(1);
					}
					return;
				}
				case BOW_OF_FAERDHINEN: {
					while (amountToMake.getAndDecrement() > 0) {
						if (!player.getInventory().contains(25859) || !player.getInventory().contains(23962, singingBowl.shardAmount)) {
							player.sendMessage("<col=880000>You don't have the required items to continue.");
						} else {
							player.getInventory().remove(25859, 1);
							player.getInventory().remove(23962, singingBowl.shardAmount);
							player.animate(899);
							player.getInventory().addOrDrop(singingBowl.product, 1);
							double xp = singingBowl.experience;
							player.getStats().addXp(StatType.Crafting, xp, false);
							player.getStats().addXp(StatType.Smithing, xp, false);
						}
						event.delay(1);
					}
					return;
				}
			}
		});
	}


	public static void register() {
		ObjectAction.register(36552, "sing-crystal", (player, obj) -> {
			player.dialogue(
				new OptionsDialogue("What would you like to make?",
					new Option("Enhanced crystal weapons.", (p) -> openEnhanced(player)),
					new Option("Standard crystal items.", (p) -> open(player)
					)
				));
		});
		ItemObjectAction.register(ItemID.BOW_OF_FAERDHINAD, 36552, (player, item, obj) -> {
			player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
				if (!player.getInventory().contains(23962, 250)) {
					player.sendMessage("You don't have enough shards to do this.");
					return;
				}
				item.remove();
				player.getInventory().remove(23962, 250);
				player.getInventory().addOrDrop(25859, 1);
				player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
			}));
		});
		ItemObjectAction.register(25896, 36552, (player, item, obj) -> {
			player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
				if (!player.getInventory().contains(23962, 250)) {
					player.sendMessage("You don't have enough shards to do this.");
					return;
				}
				item.remove();
				player.getInventory().remove(23962, 250);
				player.getInventory().addOrDrop(25859, 1);
				player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
			}));
		});
		ItemObjectAction.register(24551, 36552, (player, item, obj) -> {
			player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
				if (!player.getInventory().contains(23962, 250)) {
					player.sendMessage("You don't have enough shards to do this.");
					return;
				}
				item.remove();
				player.getInventory().remove(23962, 250);
				player.getInventory().addOrDrop(25859, 1);
				player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
			}));
		});
		ItemObjectAction.register(25867, 36552, (player, item, obj) -> {
			player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
				if (!player.getInventory().contains(23962, 250)) {
					player.sendMessage("You don't have enough shards to do this.");
					return;
				}
				item.remove();
				player.getInventory().remove(23962, 250);
				player.getInventory().addOrDrop(25859, 1);
				player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
			}));
		});
		ItemObjectAction.register(25862, 36552, (player, item, obj) -> {
			player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
				if (!player.getInventory().contains(23962, 250)) {
					player.sendMessage("You don't have enough shards to do this.");
					return;
				}
				item.remove();
				player.getInventory().remove(23962, 250);
				player.getInventory().addOrDrop(25859, 1);
				player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
			}));
		});
		for (int i = 25870; i < 25880; i += 2) {
			ItemObjectAction.register(i, 36552, (player, item, obj) -> {
				player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
					if (!player.getInventory().contains(23962, 250)) {
						player.sendMessage("You don't have enough shards to do this.");
						return;
					}
					item.remove();
					player.getInventory().remove(23962, 250);
					player.getInventory().addOrDrop(25859, 1);
					player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
				}));
			});
		}
		for (int i = 25884; i < 25896; i += 2) {
			ItemObjectAction.register(i, 36552, (player, item, obj) -> {
				player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
					if (!player.getInventory().contains(23962, 250)) {
						player.sendMessage("You don't have enough shards to do this.");
						return;
					}
					item.remove();
					player.getInventory().remove(23962, 250);
					player.getInventory().addOrDrop(25859, 1);
					player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
				}));
			});
		}
		ItemObjectAction.register(ItemID.BLADE_OF_SAELDOR, 36552, (player, item, obj) -> {
			player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
				if (!player.getInventory().contains(23962, 250)) {
					player.sendMessage("You don't have enough shards to do this.");
					return;
				}
				item.remove();
				player.getInventory().remove(23962, 250);
				player.getInventory().addOrDrop(25859, 1);
				player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
			}));
		});
		ItemObjectAction.register(23997, 36552, (player, item, obj) -> {
			player.dialogue(new YesNoDialogue("Are you sure you want to revert your<br>" + item.getDef().name + " back to a seed?", "This will cost you 250 shards", item, () -> {
				if (!player.getInventory().contains(23962, 250)) {
					player.sendMessage("You don't have enough shards to do this.");
					return;
				}
				item.remove();
				player.getInventory().remove(23962, 250);
				player.getInventory().addOrDrop(25859, 1);
				player.sendMessage("You revert the " + item.getDef().name + " into an enhanced crystal weapon seed.");
			}));
		});
	}


}
