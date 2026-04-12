package io.ruin.model.content;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.content.maps.*;
import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.itembreaking.ItemBreakingHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.Toxins;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.ItemBreaking;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.shop.ShopManager;
import io.ruin.model.skills.BotPrevention;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.thieving.Stall;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import static io.ruin.cache.ItemID.COINS_995;

public class HomeHandler {

	static Bounds homeArea = new Bounds(3073, 3462, 3123, 3520, -1);

	private static void openTeleportInterface(Player player) {
		if (player.getPosition().regionId() == 12342) {
			player.teleportInterface.open(player);
		}
	}

	private static void enterGrotesqueGuardians(Player player) {
		player.dialogue(
				new OptionsDialogue("Are you sure you want to fight grotesque guardians?",
						new Option("Yes.", () -> {
							player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GROTESQUE_GUARDIANS;
							player.getInstanceTokenInterface().startInstance(player, true);
						}),
						new Option("No.", player::closeDialogue)));
	}

	public static void init() {
		var tradingPost = GameObject.spawn(46241, 3088, 3487, 0, 10, 2);
		var elvenChest = GameObject.spawn(36582, 3100, 3508, 0, 10, 5);
		var slayerChest = GameObject.spawn(46243, 3100, 3509, 0, 10, 1);
		var singingBowl = GameObject.spawn(36552, 3091, 3510, 0, 10, 5);
		var pohPortal = GameObject.spawn(15478, 3085, 3511, 0, 10, 2);
		var altar = GameObject.spawn(411, 3095, 3506, 0, 10, 0);
		var upgradeStation = GameObject.spawn(46241, 3088, 3489, 0, 10, 2);
		var upgradeStation2 = GameObject.spawn(53226, 3088, 3491, 0, 10, 1);
		var gauntletBankChest = GameObject.spawn(4483, 3037, 6129, 1, 10, 2);
		if (!SummerEvent.disabled) {
			var summerNPC = new NPC(1805).spawn(new Position(3102, 3493, 0), Direction.NORTH);
			SummerEvent.newEventStart();
		}
	}

	public static void switchBook(Player player, SpellBook book, boolean altar) {
		if (book.isActive(player) && altar) {
			player.dialogue(new MessageDialogue("You're already using that spellbook."));
			return;
		}
		if (altar) {
			player.sendMessage("You pray at the altar...");
			player.startEvent(event -> {
				player.animate(645);
				event.delay(1);
				player.sendMessage("... and gain the knowledge of %s magic.".formatted(book.name));
			});
		}
		if (player.getCombat().autocastSpell != null) {
			player.getCombat().autocastSpell = null;
			VarPlayerRepository.AUTOCAST.set(player, 0);
			player.sendMessage("You have stopped casting.");
		}
		book.setActive(player);
		player.homeSpellbookAltarsUsed++;
		if (player.homeSpellbookAltarsUsed == Achievements.OH_CUL.getCompletionAmount())
			player.sendMessage("<col=000080>You have completed the achievement: <col=800000>%s"
					.formatted(Achievements.OH_CUL.getAchievementName()));

	}

	public static void drinkFromPool(Player player) {
		if (player.wildernessLevel > 0 || player.inTob) {
			return; // Exit the method if in the wilderness
		}
		if (player.teleportListener != null && !player.teleportListener.allow(player)) {
			return;
		}

		VarPlayerRepository.SPECIAL_ENERGY.set(player, 1000);
		player.getMovement().restoreEnergy(100);
		player.getStats().get(StatType.Prayer).restore();
		for (Stat stat : player.getStats().get()) {
			if (stat != player.getStats().get(StatType.Hitpoints) && stat.currentLevel < stat.fixedLevel)
				stat.alter(stat.fixedLevel);
		}
		player.setHp(player.getMaxHp());
		player.cureVenom(1);
		player.cureVenom(1);
		VarPlayerRepository.POISONED.set(player, 0);
		if (player.isEnvenomed())
			player.toxins.cure(Toxins.ToxinType.VENOM, 0);
		if (player.isPoisoned())
			player.toxins.cure(Toxins.ToxinType.POSION, 0);
		player.sendFilteredMessage("The effects of the ornate pool replenish you.");
		player.homePoolsUsed++;
		if (player.homePoolsUsed == Achievements.JUST_A_LITTLE_BOOST.getCompletionAmount())
			player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
					+ Achievements.JUST_A_LITTLE_BOOST.getAchievementName());

		// reset veng issue
		VarPlayerRepository.VENG_COOLDOWN.set(player, 0);
		player.vengeanceActive = false;
		player.getPacketSender().sendWidgetTimerCustom(Widget.VENGEANCE, 0);
	}

	public static double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	public static void HandleHomeStall(Player player, Stall stall, GameObject object, int replacementID) {
		int thieveLevelReq = 1;
		int maxGp = 10000;
		int minGp = 5000;
		int experience = 50;
		if (object.getId() == 11734) {
			thieveLevelReq = 25;
			maxGp = 15000;
			minGp = 7500;
			experience = 100;
		} else if (object.getId() == 11733) {
			thieveLevelReq = 50;
			maxGp = 20000;
			minGp = 10000;
			experience = 200;
		} else if (object.getId() == 11731) {
			thieveLevelReq = 75;
			maxGp = 30000;
			minGp = 15000;
			experience = 280;
		}
		if (!player.getStats().check(StatType.Thieving, thieveLevelReq, "steal from the stall"))
			return;
		if (player.getInventory().isFull()) {
			player.privateSound(2277);
			player.dialogue(new MessageDialogue("Your inventory is too full to hold any more."));
			return;
		}
		if (player.edgevilleStallCooldown.isDelayed())
			return;

		if (BotPrevention.isBlocked(player)) {
			player.sendMessage("You can't steal from a stall while a guard is watching you.");
			return;
		}

		int finalMinGp = minGp;
		int finalMaxGp = maxGp;
		int finalExperience = experience;
		player.startEvent(event -> {
			player.sendFilteredMessage("You attempt to steal from the stall...");
			player.lock();
			player.animate(832);
			event.delay(1);
			Stall.replaceStall(stall, object, replacementID, player);
			Item loot = new Item(995, Random.get(finalMinGp, finalMaxGp));
			player.getInventory().add(loot);
			player.edgevilleStallCooldown.delay(3);
			float petChance = 5000;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				petChance *= c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				petChance *= 0.8F;
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				petChance *= 0.85F;

			petChance *= getPetDonatorBoost(player);
			if (Random.get((int) petChance) == 0)
				Pet.ROCKY.unlock(player, 0);
			player.getStats().addXp(StatType.Thieving, finalExperience, true);
			// BotPrevention.attemptBlock(player);
			player.unlock();
			if (Random.get(50) == 0 && player.randomEventPrevent > 30) {
				player.getMovement().startTeleport(e -> {
					player.animate(3864);
					player.graphics(1039);
					player.privateSound(200, 0, 10);
					e.delay(2);
					player.getMovement().teleport(3087, 3496, 0);
					player.sendMessage("A guard catches you stealing and sends you home!");
				});
			} else {
				player.randomEventPrevent++;
			}
		});
	}

	public static void register() {
		ItemNPCAction.register(7456, (player, item, obj) -> {
			if (!ItemBreaking.isBroken(item.getId())) {
				player.dialogue(new MessageDialogue("You can't repair that."));
				return;
			}
			var broken = ItemBreaking.get(item.getId());
			int price = (int) ((1d - (player.getStats().get(StatType.Smithing).currentLevel / 200d)) * broken.coinRepairCost)
					* 10;
			player.dialogue(new YesNoDialogue("Repair the item?",
					"Repairing this item will cost " + NumberUtils.formatNumber(price) + " coins. Continue?", broken.fixedId, 1,
					() -> {
						if (!player.getInventory().contains(COINS_995, price)) {
							player.dialogue(new MessageDialogue("Not enough coins in your inventory."));
							return;
						}
						player.getInventory().remove(COINS_995, price);
						item.setId(broken.fixedId);
					}));
		});
		MapListener.registerBounds(homeArea).onEnter(HomeHandler::drinkFromPool);
		NPCAction.register(3842, 1, (player, npc) -> ShopManager.openIfExists(player, "MagicWeaponStore"));
		NPCAction.register(3842, 3, (player, npc) -> ShopManager.openIfExists(player, "MagicArmourStore"));
		NPCAction.register(2883, 1, (player, npc) -> ShopManager.openIfExists(player, "RangedWeaponStore"));
		NPCAction.register(2883, 3, (player, npc) -> ShopManager.openIfExists(player, "RangedArmourStore"));
		NPCAction.register(2882, 1, (player, npc) -> ShopManager.openIfExists(player, "MeleeWeaponStore"));
		NPCAction.register(2882, 3, (player, npc) -> ShopManager.openIfExists(player, "MeleeArmourStore"));
		NPCAction.register(1482, 1, (player, npc) -> ShopManager.openIfExists(player, "ConsumablesStore"));
		NPCAction.register(10619, 1, (player, npc) -> ShopManager.openIfExists(player, "SkillingShop"));
		NPCAction.register(8532, 2, (player, npc) -> ShopManager.openIfExists(player, "HerbloreStore"));
		NPCAction.register(10631, 2, (player, npc) -> ShopManager.openIfExists(player, "Stardust"));
		NPCAction.register(10631, 1, (player, npc) -> ShopManager.openIfExists(player, "Stardust"));
		NPCAction.register(2879, 2, (player, npc) -> ShopManager.openIfExists(player, "CraftingStore"));
		NPCAction.register(2817, 2, (player, npc) -> ShopManager.openIfExists(player, "GeneralStore"));
		NPCAction.register(2817, 1, (player, npc) -> ShopManager.openIfExists(player, "GeneralStore"));
		NPCAction.register(9053, "Trade",
				(player, npc) -> ShopManager.openIfExists(player, "347ca8b3-dd43-4a46-bf05-3a452e5e1f3c"));
		NPCAction.register(5523, 1, (player, npc) -> NewShopHandler.openShop(player, NewShopHandler.donatorPointStore));
		NPCAction.register(2989, "trade",
				(player, npc) -> NewShopHandler.openShop(player, NewShopHandler.reasonPointStore));
		NPCAction.register(4058, "trade", (player, npc) -> NewShopHandler.openShop(player, NewShopHandler.votePointStore));
		NPCAction.register(5527, "trade",
				(player, npc) -> NewShopHandler.openShop(player, NewShopHandler.achievementPointStore));
		NPCAction.register(7456, 1, (player, npc) -> {
			player.dialogue(new NPCDialogue(7456, "Use your broken items on me to get them repaired."));
		});
		NPCAction.register(7456, 3, (player, npc) -> {
			player.dialogue(new NPCDialogue(7456, "Use your broken items on me to get them repaired."));
		});
		NPCAction.register(8968, 1, (player, npc) -> {
			if (!player.isAdmin() && !player.isManager()) {
				player.sendMessage("Only Admins can trade this shop!");
			} else
				NewShopHandler.openShop(player, NewShopHandler.adminStore);
		});
		ObjectAction.register(7325, "use", (player, obj) -> {
			player.dialogue(
					new OptionsDialogue("Where would you like to go?",
							new Option("Angry dungeon", () -> {
								player.getMovement().teleport(3106, 5279, 0);
								player.sendMessage(Color.DARK_RED, "You find yourself in a land of confusion.");
							}),
							new Option("Vyrewatch Dungeon", () -> {
								player.dialogue(
										new OptionsDialogue("Which map would you like to go to?",
												new Option("Super Donator", () -> {
													if (player.totalDonated < 50) {
														player.sendMessage("You need to be a Super Donator to access this area.");
														return;
													}
													player.getMovement().teleport(VyrewatchDonatorMap.getTeleportPosition());
													player.sendMessage(Color.DARK_RED, "You find yourself in a land of blood.");
												}),
												new Option("Noble Donator", () -> {
													if (player.totalDonated < 250) {
														player.sendMessage("You need to be a Noble Donator to access this area.");
														return;
													}
													player.getMovement().teleport(VyrewatchSecondaryDonatorMap.getTeleportPosition());
													player.sendMessage(Color.DARK_RED, "You find yourself in a land of blood.");
												})));
							}),
							new Option("Catacombs", () -> {
								player.dialogue(
										new OptionsDialogue("Which map would you like to go to?",
												new Option("Elite Donator", () -> {
													if (player.totalDonated < 100) {
														player.sendMessage("You need to be a Elite Donator to access this area.");
														return;
													}
													player.getMovement().teleport(CatacombDonatorMap.getTeleportPosition());
													player.sendMessage(Color.DARK_RED, "You find yourself in a land of darkness.");
												}),
												new Option("Gold Donator", () -> {
													if (player.totalDonated < 500) {
														player.sendMessage("You need to be a Gold Donator to access this area.");
														return;
													}
													player.getMovement().teleport(CatacombSecondaryDonatorMap.getTeleportPosition());
													player.sendMessage(Color.DARK_RED, "You find yourself in a land of darkness.");
												}),
												new Option("Legendary Donator", () -> {
													if (player.totalDonated < 2_500) {
														player.sendMessage("You need to be a Legendary Donator to access this area.");
														return;
													}
													player.getMovement().teleport(CatacombTertiaryDonatorMap.getTeleportPosition());
													player.sendMessage(Color.DARK_RED, "You find yourself in a land of darkness.");
												})));
							}),
							new Option("Clue Farming", () -> {
								player.dialogue(
										new OptionsDialogue("Which tier would you like to farm?",
												new Option("Easy", () -> {
													player.dialogue(
															new OptionsDialogue("Which map would you like to go to?",
																	new Option("Elite Donator", () -> {
																		if (player.totalDonated < 100) {
																			player.sendMessage("You need to be a Elite Donator to access this area.");
																			return;
																		}
																		player.getMovement().teleport(EasyClueScrollDonatorMap.getTeleportPosition());
																	}),
																	new Option("Gold Donator", () -> {
																		if (player.totalDonated < 500) {
																			player.sendMessage("You need to be a Gold Donator to access this area.");
																			return;
																		}
																		player.getMovement()
																				.teleport(EasyClueScrollSecondaryDonatorMap.getTeleportPosition());
																	})));
												}),
												new Option("Medium", () -> {
													player.dialogue(
															new OptionsDialogue("Which map would you like to go to?",
																	new Option("Noble Donator", () -> {
																		if (player.totalDonated < 250) {
																			player.sendMessage("You need to be a Noble Donator to access this area.");
																			return;
																		}
																		player.getMovement().teleport(MediumClueScrollDonatorMap.getTeleportPosition());
																	}),
																	new Option("Platinum Donator", () -> {
																		if (player.totalDonated < 1_000) {
																			player.sendMessage("You need to be a Platinum Donator to access this area.");
																			return;
																		}
																		player.getMovement()
																				.teleport(MediumClueScrollSecondaryDonatorMap.getTeleportPosition());
																	})));
												}),
												new Option("Hard", () -> {
													player.dialogue(
															new OptionsDialogue("Which map would you like to go to?",
																	new Option("Gold Donator", () -> {
																		if (player.totalDonated < 500) {
																			player.sendMessage("You need to be a Gold Donator to access this area.");
																			return;
																		}
																		player.getMovement().teleport(HardClueScrollDonatorMap.getTeleportPosition());
																	}),
																	new Option("Legendary Donator", () -> {
																		if (player.totalDonated < 2_500) {
																			player.sendMessage("You need to be a Legendary Donator to access this area.");
																			return;
																		}
																		player.getMovement()
																				.teleport(HardClueScrollSecondaryDonatorMap.getTeleportPosition());
																	})));
												})));
							})));

		});
		ObjectAction.register(13999, "climb-up", (player, obj) -> {
			player.getMovement().teleport(1230, 2565, 2);
			player.sendMessage(Color.DARK_RED, "You find yourself back in lucidity.");
		});

		ObjType.forEach(itemDef -> {
			if (itemDef != null) {
				ItemObjectAction.register(itemDef.id, 53226, (player, item, obj) -> {
					if (ItemBreakingHandler.getTotalAttachments(item).isEmpty()) {
						player.sendMessage("This item does not have any perks to break.");
						return;
					}
					ItemBreakingHandler.removeItemPerk(player, item);
				});
			}
		});
		ObjectAction.register(33410, 1, (player, obj) -> openTeleportInterface(player));
		ObjectAction.register(33410, 2, (player, obj) -> openTeleportInterface(player));
		ObjectAction.register(31681, 2, (player, obj) -> enterGrotesqueGuardians(player));
		ObjectAction.register(31681, 1, (player, obj) -> enterGrotesqueGuardians(player));
		ObjectAction.register(31672, 1, (player, obj) -> enterGrotesqueGuardians(player));
		ObjectAction.register(31672, 2, (player, obj) -> enterGrotesqueGuardians(player));
		ObjectAction.register(53226, 2, (player, obj) -> player.getItemUpgradeInterface().open(player, true));
		ObjectAction.register(53226, 3, (player, obj) -> player.getItemBreakInterface().open(player));
		ObjectAction.register(33410, 3, (player, obj) -> openTeleportInterface(player));
		ObjectAction.register(31861, actions -> {
			actions[1] = (player, obj) -> {
				player.dialogue(
						new OptionsDialogue("Select which prayer book you'd like to switch to:",
								new Option("Modern", () -> switchBook(player, SpellBook.MODERN, true)),
								new Option("Ancient", () -> switchBook(player, SpellBook.ANCIENT, true)),
								new Option("Lunar", () -> switchBook(player, SpellBook.LUNAR, true)),
								new Option("Arceuus", () -> switchBook(player, SpellBook.ARCEUUS, true))));
			};
			actions[2] = (player, obj) -> switchBook(player, SpellBook.MODERN, true);
			actions[3] = (player, obj) -> switchBook(player, SpellBook.ANCIENT, true);
			actions[4] = (player, obj) -> switchBook(player, SpellBook.LUNAR, true);
			actions[5] = (player, obj) -> switchBook(player, SpellBook.ARCEUUS, true);
		});
	}
}
