package io.ruin.model.skills.slayer.master;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.DonatorBonus;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.shop.ShopManager;
import io.ruin.model.skills.slayer.*;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BossMaster {

	public static final int bossMaster = 10529;

	private static void assignTask(Player player) {
		if (player.getCombat().getLevel() < 100) {
			player.dialogue(
				new NPCDialogue(bossMaster, "You need a combat level of at least 100 to receive a task from me.")
			);
			return;
		}


		VarPlayerRepository.SLAYER_MASTER.set(player, SlayerMaster.NOMAD_ID);
		VarPlayerRepository.SLAYER_TASK.set(player, 98);
		SlayerTask def = randomBossTask(player);

		if (def == null) {
			player.dialogue(new NPCDialogue(bossMaster, "I'm sorry, but I don't have any tasks for you at the moment."));
			return;
		}


		int min = def.getMinAmount();
		int max = def.getMaxAmount();


		int task_amt = Random.get(min, max);
		player.currentBossSlayerAmount = task_amt;
		player.bossSlayerName = new NPC(def.getNpcIds()[0]).getDef().name;
		player.bossSlayerStartAmount = task_amt;
		player.bossSlayerPosition = def.getLocation();
		player.bossTaskInWildy = def.wilderness;
		player.dialogue(new NPCDialogue(bossMaster, "Your new task is to kill " + task_amt + " " + def.getTaskName() + "."));
	}

	public static void skipTask(Player player) {
		player.dialogue(new OptionsDialogue("Which task do you want to skip?",
			new Option("Boss Slayer Task", () -> {
				if (player.bossSlayerName == null) {
					player.dialogue(new NPCDialogue(bossMaster, "You don't have a boss slayer task to skip."));
					return;
				}
				int cost = 30 - DonatorBonus.REDUCTION_OF_CANCEL_SLAYER_TASK.handleBonus(player);
				if (VarPlayerRepository.SLAYER_POINTS.get(player) < cost) {
					player.dialogue(new NPCDialogue(bossMaster, "You need " + cost + " Slayer points to skip that task."));
					return;
				}
				player.bossSlayerName = null;
				player.bossSlayerStartAmount = 0;
				player.currentBossSlayerAmount = 0;

				VarPlayerRepository.SLAYER_POINTS.set(player, VarPlayerRepository.SLAYER_POINTS.get(player) - cost);
				player.dialogue(new NPCDialogue(bossMaster, "Task skipped. You now have " + VarPlayerRepository.SLAYER_POINTS.get(player) + " Slayer points remaining."));
			}),
			new Option("Regular Slayer Task", () -> {
				SlayerUnlock.cancelTask(player);
			}),
			new Option("Cancel", player::closeDialogue)));
	}

	public static void handleBossSlayerKill(Player player, NPC npc) {

		if (npc != null && npc.getDef() != null && npc.getDef().name.equalsIgnoreCase(player.bossSlayerName)) {
			if (Random.get(35) == 0 && npc.getMaxHp() > 250) {
				SlayerMaster.SlayerKey slayerKey = SlayerMaster.SlayerKey.TIER_4;
				if (npc.getDef().name.equalsIgnoreCase("hydra"))
					slayerKey = SlayerMaster.SlayerKey.TIER_3;
				int slayerKeyId = slayerKey.getId();
				Position spawnPosition;
				spawnPosition = player.getPosition();
				new GroundItem(slayerKeyId, 1).owner(player).position(spawnPosition).spawn();
			} else if (Random.get(75) == 0 && npc.getMaxHp() <= 250 && npc.getMaxHp() >= 100) {
				SlayerMaster.SlayerKey slayerKey = SlayerMaster.SlayerKey.TIER_3;
				int slayerKeyId = slayerKey.getId();
				Position spawnPosition;
				spawnPosition = player.getPosition();
				new GroundItem(slayerKeyId, 1).owner(player).position(spawnPosition).spawn();
			}
			player.currentBossSlayerAmount--;
			player.getStats().addXp(StatType.Slayer, npc.getMaxHp() * 2, true);
			player.slayerTaskKills++;
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SLAYER_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
			if (player.currentBossSlayerAmount == 0) {
				player.currentBossSlayerAmount = 0;
				player.bossSlayerName = null;
				player.bossSlayerStartAmount = 0;
				player.bossSlayerStreak++;
				final int spree = player.bossSlayerStreak;
				int basePoints = 35 * (World.doubleSlayer
					&& !player.getName().equalsIgnoreCase("normal") ? 2 : 1)
					+ DonatorBonus.BONUS_SLAYER_POINTS.handleBonus(player);
				final int total = SlayerMaster.points(8, spree) + basePoints;
				final int current = VarPlayerRepository.SLAYER_POINTS.get(player);
				VarPlayerRepository.SLAYER_POINTS.set(player, current + total);
				player.sendMessage("<col=7F00FF>You've completed " + player.bossSlayerStreak + " boss tasks in a row and received " + (total) + " points. Return to a Nomad for a new task.");

			} else {
				SuperiorSlayer.trySpawnFromName(player, npc);
			}
		}
	}

	public static SlayerTask randomBossTask(Player player) {
		List<SlayerTask> tasks = new ArrayList<>();
		Arrays.stream(NomadTask.values()).forEach(task -> tasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount(),
			task.wilderness
		)));

		List<SlayerTask> filteredTasks = new ArrayList<>();

		for (SlayerTask task : tasks) {
			if (task != null && player.getStats().get(StatType.Slayer).fixedLevel >= task.getSlayerRequirement()) {

				if (!player.slayerCombatCheck && player.getCombat().getLevel() < task.getCombatRequirement()) {
					continue;
				}

				filteredTasks.add(task);
			}
		}

		SlayerTask def = null;

		def = Random.get(filteredTasks);

		if (def == null) {
			return null;
		}

		//VarPlayerRepository.BOSS_TASK.set(player, configId);
		return def;
	}

	public static void handleInteraction(Player player, int option) {
		boolean talked = player.getStats().get(StatType.Slayer).experience > 0;

		switch (option) {
			case 1:
				if (talked) {
					player.dialogue(
						new NPCDialogue(bossMaster, "'Ello, and what are you after then?"),
						new OptionsDialogue(new Option("I need another assignment.", () -> {
							player.dialogue(new PlayerDialogue("I need another assignment.").action(() -> giveTask(player)));
						}), new Option("Have you any rewards for me, or anything to trade?", () -> {
							player.dialogue(
								new PlayerDialogue("Have you any rewards for me, or anything to trade?"),
								new NPCDialogue(bossMaster, "I have quite a few rewards you can earn, and a wide<br>variety of Slayer equipment for sale."),
								new OptionsDialogue(new Option("Look at rewards", () -> {
									SlayerUnlock.openRewards(player);
								}), new Option("Look at shop", () -> {
									NewShopHandler.openShop(player, NewShopHandler.slayerStore);
								}), new Option("Cancel", player::closeDialogue))
							);
						}), new Option("Let's talk about the difficulty of my assignments.", () -> {
							player.dialogue(new PlayerDialogue("Let's talk about the difficulty of my assignments."));

							if (!player.slayerCombatCheck) {
								player.dialogue(new NPCDialogue(bossMaster, "The Slayer Masters will take your combat level into<br>account when choosing tasks for you, so you shouldn't<br>get anything too hard."),
									new OptionsDialogue(new Option("That's fine - I don't want anything too tough.", () -> {
										player.dialogue(new PlayerDialogue("That's fine - I don't want anything too tough."),
											new NPCDialogue(bossMaster, "Okay, we'll keep checking your combat level."));
									}), new Option("Stop checking my combat level - I can take anything!", () -> {
										player.slayerCombatCheck = true;
										player.dialogue(new PlayerDialogue("Stop checking my combat level - I can take anything!"),
											new NPCDialogue(bossMaster, "Okay, from now on, all the Slayer Masters will assign<br>you anything from their lists, regardless of your combat<br>level."));
									})));
							} else {
								player.dialogue(new NPCDialogue(bossMaster, "The Slayer Masters may currently assign you any<br>task in our lists, regardless of your combat level."),
									new OptionsDialogue(new Option("That's fine - I can handle any task.", () -> {
										player.dialogue(new PlayerDialogue("That's fine - I can handle any task."),
											new NPCDialogue(bossMaster, "That's the spirit!"));
									}), new Option("In future, please don't give anything too rough.", () -> {
										player.slayerCombatCheck = false;
										player.dialogue(new PlayerDialogue("In future, please don't give anything too rough."),
											new NPCDialogue(bossMaster, "Okay, from now on, all the Slayer Masters will take<br>your combat level into account when choosing tasks for<br>you, so you shouldn't get anything too hard."));
									})));
							}
						}), new Option("Er... Nothing...", () -> {
							player.dialogue(new PlayerDialogue("Er... Nothing..."));
						}))
					);
				} else {
					player.dialogue(new OptionsDialogue(new Option("Who are you?", () -> {
						player.dialogue(new PlayerDialogue("Who are you?"),
							new NPCDialogue(bossMaster, "I'm one of the elite Slayer Masters."),
							new OptionsDialogue(new Option("What's a slayer?", () -> {
								player.dialogue(new PlayerDialogue("What's a slayer?"),
									new NPCDialogue(bossMaster, "Oh dear, what do they teach you in school?"),
									new PlayerDialogue("Well... er..."),
									new NPCDialogue(bossMaster, "I suppose I'll have to educate you then. A slayer is<br>someone who is trained " +
										"to fight specific creatures. They<br>know these creatures' every weakness and strength. " +
										"As<br>you can guess it makes killing them a lot easier."),
									new OptionsDialogue(new Option("Wow, can you teach me?", () -> {
										player.dialogue(new PlayerDialogue("Wow, can you teach me?"),
											new NPCDialogue(bossMaster, "Hmmm well I'm not so sure..."),
											new PlayerDialogue("Pleeeaasssse!"),
											new NPCDialogue(bossMaster, "Oh okay then, you twisted my arm. You'll have to train<br>against specific groups of creatures."),
											new PlayerDialogue("Okay, what's first?").action(() -> {
												player.getInventory().addOrDrop(new Item(4155, 1));
												assignTask(player);

												SlayerCreature task = SlayerCreature.lookup(VarPlayerRepository.SLAYER_TASK.get(player));

												if (task != null) {
													int num = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);
													player.dialogue(new NPCDialogue(bossMaster, "We'll start you off hunting " + SlayerUnlock.taskName(player, task.getUid()) + ", you'll need to kill " + num + "<br>of them."));
												}
											}));
									}), new Option("Sounds useless to me", () -> {
										player.dialogue(new PlayerDialogue("Sounds useless to me."),
											new NPCDialogue(bossMaster, "Suit yourself."));
									})));
							}), new Option("Never heard of you...", () -> {
								player.dialogue(new PlayerDialogue("Never heard of you..."),
									new NPCDialogue(bossMaster, "That's because my foe never lives to tell of me. We<br>slayers are a dangerous bunch."));
							})));
					}), new Option("Er... Nothing...", () -> {
						player.dialogue(new PlayerDialogue("Er... Nothing..."));
					})));
				}
				break;
			case 3:
				giveTask(player);
				break;
			case 5:
				SlayerUnlock.openRewards(player);
				break;
		}
	}

	private static void giveTask(Player player) {
		if (player.getStats().get(StatType.Slayer).fixedLevel < 50 && player.getCombat().getLevel() < 100) {
			player.dialogue(new NPCDialogue(bossMaster, "Sorry, but you're not strong enough to be taught by<br>me. Your best trainer would be " + SlayerMaster.bestMaster(player) + "."));
			return;
		}


		if (player.getCombat().getLevel() < 100) {
			player.dialogue(
				new NPCDialogue(bossMaster, "You need a combat level of at least 100 to receive a task from me.")
			);
			return;
		}
		if (player.getStats().get(StatType.Slayer).fixedLevel < 90) {
			player.dialogue(
				new NPCDialogue(bossMaster, "You need a Slayer level of at least 90 to receive a task from me.")
			);
			return;
		}
        if (VarPlayerRepository.LIKE_A_BOSS.get(player) == 0) {
            player.dialogue(
                    new NPCDialogue(bossMaster, "You need to unlock boss slayer tasks before you can receive a task from me.")
            );
            return;

        }

		if (player.bossSlayerName != null) {

			player.dialogue(
				new NPCDialogue(bossMaster, "You already have a task to kill " + player.bossSlayerName + ". You have " + player.currentBossSlayerAmount + " remaining."),
				new OptionsDialogue(
					new Option("I'd like to skip this task. I have " + VarPlayerRepository.SLAYER_POINTS.get(player) + " Slayer points.", () -> {
						int slayerPointsBeforeSkip = VarPlayerRepository.SLAYER_POINTS.get(player);
						int bonusReduction = DonatorBonus.REDUCTION_OF_CANCEL_SLAYER_TASK.handleBonus(player);
						int cost = 30 - bonusReduction;

						if (slayerPointsBeforeSkip >= cost) {
							skipTask(player);
						} else {
							player.dialogue(new NPCDialogue(bossMaster, "Sorry, you don't have enough Slayer points to skip that task."));
						}
					}),
					new Option("Great, thanks!", () -> {
						player.dialogue(new PlayerDialogue("Okay, great!"));
					})
				));
			return;
		}

		assignTask(player);


		player.dialogue(
			new NPCDialogue(bossMaster, "Excellent, you're doing great. Your new task is to kill " + player.currentBossSlayerAmount + " " + player.bossSlayerName + "."),
			new OptionsDialogue(
				new Option("I'd like to skip this task. I have " + VarPlayerRepository.SLAYER_POINTS.get(player) + " Slayer points.", () -> {
					int slayerPointsBeforeSkip = VarPlayerRepository.SLAYER_POINTS.get(player);
					int bonusReduction = DonatorBonus.REDUCTION_OF_CANCEL_SLAYER_TASK.handleBonus(player);
					int cost = 30 - bonusReduction;

					if (slayerPointsBeforeSkip >= cost) {
						skipTask(player);
					} else {
						player.dialogue(new NPCDialogue(bossMaster, "Sorry, you don't have enough Slayer points to skip that task."));
					}
				}),
				new Option("Great, thanks!", () -> {
					player.dialogue(new PlayerDialogue("Okay, great!"));
				})
			));
	}

	public static void register() {
		NPCAction.register(bossMaster, 1, (player, npc) -> handleInteraction(player, 1));
		NPCAction.register(bossMaster, 3, (player, npc) -> NewShopHandler.openShop(player, NewShopHandler.slayerStore));
		NPCAction.register(bossMaster, 2, (player, npc) -> handleInteraction(player, 3));
		NPCAction.register(bossMaster, 4, (player, npc) -> handleInteraction(player, 5));
	}
}
