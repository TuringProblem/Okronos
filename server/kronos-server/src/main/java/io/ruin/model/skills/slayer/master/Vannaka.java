package io.ruin.model.skills.slayer.master;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.DonatorBonus;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.shop.ShopManager;
import io.ruin.model.skills.slayer.SlayerCreature;
import io.ruin.model.skills.slayer.SlayerMaster;
import io.ruin.model.skills.slayer.SlayerTask;
import io.ruin.model.skills.slayer.SlayerUnlock;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import kotlin.Pair;

public class Vannaka {

	public static final int VANNAKA = 403;

	private static void assignTask(Player player) {
		if (player.getCombat().getLevel() < 40) {
			player.dialogue(
				new NPCDialogue(VANNAKA, "You need a combat level of at least 40 to receive a task from me.")
			);
			return;
		}
		SlayerMaster master = SlayerMaster.master(VANNAKA);

		if (master == null)
			return;

		VarPlayerRepository.SLAYER_MASTER.set(player, SlayerMaster.VANNAKA_ID);
		SlayerTask def = master.randomTask(player);
		VarPlayerRepository.SLAYER_TASK.set(player, def.getTaskId());

		int min = def.getMinAmount();
		int max = def.getMaxAmount();

		if (def.getTaskName().contains("Earth warrior"))
			player.taskInWilderness = true;
		else player.taskInWilderness = false;
		player.slayerTaskPosition = def.getLocation();

		for (Pair<Integer, VarPlayerRepository> creature : SlayerUnlock.multipliable) {
			if (creature.getFirst() == def.getTaskId()) {
				if (creature.getSecond().get(player) != 0) {
					min = (int) (def.getMinAmount() * 1.75);
					max = (int) (def.getMaxAmount() * 1.75);

					System.out.println("Slayer task is extended!");
					break;
				}
			}
		}

		int task_amt = Random.get(min, max);

		if (!player.vannakaUsed) {
			player.vannakaUsed = true;
			player.slayerMasterUsedCounter++;
			if (player.slayerMasterUsedCounter == Achievements.GOT_ANY_CHANGE.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.GOT_ANY_CHANGE.getAchievementName());
		}

		VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, task_amt);
	}

	public static void handleInteraction(Player player, int option) {
		boolean talked = player.getStats().get(StatType.Slayer).experience > 0;

		switch (option) {
			case 1:
				if (talked) {
					player.dialogue(
						new NPCDialogue(VANNAKA, "'Ello, and what are you after then?"),
						new OptionsDialogue(new Option("I need another assignment.", () -> {
							player.dialogue(new PlayerDialogue("I need another assignment.").action(() -> giveTask(player)));
						}), new Option("Have you any rewards for me, or anything to trade?", () -> {
							player.dialogue(
								new PlayerDialogue("Have you any rewards for me, or anything to trade?"),
								new NPCDialogue(VANNAKA, "I have quite a few rewards you can earn, and a wide<br>variety of Slayer equipment for sale."),
								new OptionsDialogue(new Option("Look at rewards", () -> {
									SlayerUnlock.openRewards(player);
								}), new Option("Look at shop", () -> {
									NewShopHandler.openShop(player, NewShopHandler.slayerStore);
								}), new Option("Cancel", player::closeDialogue))
							);
						}), new Option("Let's talk about the difficulty of my assignments.", () -> {
							player.dialogue(new PlayerDialogue("Let's talk about the difficulty of my assignments."));

							if (!player.slayerCombatCheck) {
								player.dialogue(new NPCDialogue(VANNAKA, "The Slayer Masters will take your combat level into<br>account when choosing tasks for you, so you shouldn't<br>get anything too hard."),
									new OptionsDialogue(new Option("That's fine - I don't want anything too tough.", () -> {
										player.dialogue(new PlayerDialogue("That's fine - I don't want anything too tough."),
											new NPCDialogue(VANNAKA, "Okay, we'll keep checking your combat level."));
									}), new Option("Stop checking my combat level - I can take anything!", () -> {
										player.slayerCombatCheck = true;
										player.dialogue(new PlayerDialogue("Stop checking my combat level - I can take anything!"),
											new NPCDialogue(VANNAKA, "Okay, from now on, all the Slayer Masters will assign<br>you anything from their lists, regardless of your combat<br>level."));
									})));
							} else {
								player.dialogue(new NPCDialogue(VANNAKA, "The Slayer Masters may currently assign you any<br>task in our lists, regardless of your combat level."),
									new OptionsDialogue(new Option("That's fine - I can handle any task.", () -> {
										player.dialogue(new PlayerDialogue("That's fine - I can handle any task."),
											new NPCDialogue(VANNAKA, "That's the spirit!"));
									}), new Option("In future, please don't give anything too rough.", () -> {
										player.slayerCombatCheck = false;
										player.dialogue(new PlayerDialogue("In future, please don't give anything too rough."),
											new NPCDialogue(VANNAKA, "Okay, from now on, all the Slayer Masters will take<br>your combat level into account when choosing tasks for<br>you, so you shouldn't get anything too hard."));
									})));
							}
						}), new Option("Er... Nothing...", () -> {
							player.dialogue(new PlayerDialogue("Er... Nothing..."));
						}))
					);
				} else {
					player.dialogue(new OptionsDialogue(new Option("Who are you?", () -> {
						player.dialogue(new PlayerDialogue("Who are you?"),
							new NPCDialogue(VANNAKA, "I'm one of the elite Slayer Masters."),
							new OptionsDialogue(new Option("What's a slayer?", () -> {
								player.dialogue(new PlayerDialogue("What's a slayer?"),
									new NPCDialogue(VANNAKA, "Oh dear, what do they teach you in school?"),
									new PlayerDialogue("Well... er..."),
									new NPCDialogue(VANNAKA, "I suppose I'll have to educate you then. A slayer is<br>someone who is trained " +
										"to fight specific creatures. They<br>know these creatures' every weakness and strength. " +
										"As<br>you can guess it makes killing them a lot easier."),
									new OptionsDialogue(new Option("Wow, can you teach me?", () -> {
										player.dialogue(new PlayerDialogue("Wow, can you teach me?"),
											new NPCDialogue(VANNAKA, "Hmmm well I'm not so sure..."),
											new PlayerDialogue("Pleeeaasssse!"),
											new NPCDialogue(VANNAKA, "Oh okay then, you twisted my arm. You'll have to train<br>against specific groups of creatures."),
											new PlayerDialogue("Okay, what's first?").action(() -> {
												player.getInventory().addOrDrop(new Item(4155, 1));
												assignTask(player);

												SlayerCreature task = SlayerCreature.lookup(VarPlayerRepository.SLAYER_TASK.get(player));

												if (task != null) {
													int num = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);
													player.dialogue(new NPCDialogue(VANNAKA, "We'll start you off hunting " + SlayerUnlock.taskName(player, task.getUid()) + ", you'll need to kill " + num + "<br>of them."));
												}
											}));
									}), new Option("Sounds useless to me", () -> {
										player.dialogue(new PlayerDialogue("Sounds useless to me."),
											new NPCDialogue(VANNAKA, "Suit yourself."));
									})));
							}), new Option("Never heard of you...", () -> {
								player.dialogue(new PlayerDialogue("Never heard of you..."),
									new NPCDialogue(VANNAKA, "That's because my foe never lives to tell of me. We<br>slayers are a dangerous bunch."));
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
		int left = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);

		if (left > 0) {
			String text = SlayerMaster.getTaskText(player, left);
			player.dialogue(new NPCDialogue(VANNAKA, text));
			return;
		}

		assignTask(player);

		SlayerCreature task = SlayerCreature.lookup(VarPlayerRepository.SLAYER_TASK.get(player));

		if (task == null) {
			player.sendMessage("Failed to find a suitable task.");
			return;
		}
		left = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);

		player.dialogue(
			new NPCDialogue(VANNAKA, "Excellent, you're doing great. Your new task is to kill " + left + " " + SlayerUnlock.taskName(player, task.getUid()) + "."),
			new OptionsDialogue(
				new Option("Got any tips for me?", () -> player.dialogue(
					new PlayerDialogue("Got any tips for me?"),
					new NPCDialogue(VANNAKA, SlayerUnlock.tipFor(task)),
					new PlayerDialogue("Great, thanks!")
				)),
				new Option("I'd like to skip this task. I have " + VarPlayerRepository.SLAYER_POINTS.get(player) + " Slayer points.", () -> {
					int slayerPointsBeforeSkip = VarPlayerRepository.SLAYER_POINTS.get(player);
					int bonusReduction = DonatorBonus.REDUCTION_OF_CANCEL_SLAYER_TASK.handleBonus(player);
					int cost = 30 - bonusReduction;

					if (slayerPointsBeforeSkip >= cost) {
						SlayerUnlock.cancelTask(player);
						int slayerPointsAfterSkip = VarPlayerRepository.SLAYER_POINTS.get(player);
						player.dialogue(new NPCDialogue(VANNAKA, "Task skipped. You now have " + slayerPointsAfterSkip + " Slayer points remaining."));
					} else {
						player.dialogue(new NPCDialogue(VANNAKA, "Sorry, you don't have enough Slayer points to skip that task."));
					}
				}),
				new Option("Great, thanks!", () -> {
					player.dialogue(new PlayerDialogue("Okay, great!"));
				})
			));
	}

	public static void register() {
		NPCAction.register(VANNAKA, 1, (player, npc) -> handleInteraction(player, 1));
		NPCAction.register(VANNAKA, "Trade", (player, npc) -> NewShopHandler.openShop(player, NewShopHandler.slayerStore));
		NPCAction.register(VANNAKA, 3, (player, npc) -> handleInteraction(player, 3));
		NPCAction.register(VANNAKA, 5, (player, npc) -> handleInteraction(player, 5));
	}
}
