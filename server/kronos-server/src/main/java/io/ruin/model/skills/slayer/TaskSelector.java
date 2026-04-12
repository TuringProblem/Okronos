package io.ruin.model.skills.slayer;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import java.util.ArrayList;

public class TaskSelector {
	// normal 50 - 150
	// boss 10 - 35
	public static void TaskSelect(Player player) {
		//Easy:
		//  - Spiders
		//  - Skeletons
		//  - Hill Giants
		//  - Ghosts
		//  - Bats
		player.dialogue(new OptionsDialogue("Select your TASK Difficulty!",
			new Option("Easy Slayer Task!", () -> {
				player.dialogue(new OptionsDialogue("Select your TASK!",

					new Option("Ghosts!", () -> {
						//Slayer.set(player, SlayerTask.Type.EASY);
						VarPlayerRepository.SLAYER_TASK.set(player, 12);
						VarPlayerRepository.SLAYER_MASTER.set(player, 1);
						int value = Random.get(50, 150);
						VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, value);
						player.slayerTaskName = "Ghosts";
						player.sendMessage("Your new task is to kill " + (Color.RED.wrap("" + value) + " X " + (Color.RED.wrap((player.slayerTaskName)) + ".<br>Good luck!")));
						if (player.Dslayertask == 2) {
							player.slayertasktimer = System.currentTimeMillis();
							player.Dslayertask = 3;
						}
						player.Dslayertask += 1;
					}),
					new Option("Monkas!", () -> {
						//Slayer.set(player, SlayerTask.Type.EASY);
						VarPlayerRepository.SLAYER_TASK.set(player, 1);
						VarPlayerRepository.SLAYER_MASTER.set(player, 1);
						int value = Random.get(50, 150);
						VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, value);
						player.slayerTaskName = "Monkeys";
						player.sendMessage("Your new task is to kill " + (Color.RED.wrap("" + value) + " X " + (Color.RED.wrap((player.slayerTaskName)) + ".<br>Good luck!")));
						if (player.Dslayertask == 2) {
							player.slayertasktimer = System.currentTimeMillis();
							player.Dslayertask = 3;
						}
						player.Dslayertask += 1;
//                            new Option("Bats!", () -> {
//                                Slayer.set(player, SlayerTask.Type.EASY);
//                                player.slayerTaskName = "Bats";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.TURAEL;
//                                player.slayerTaskType = SlayerTask.Type.EASY;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            })
//                    ));
//                }),
//                //
//                //Medium:
//                //  - Fire Giants
//                //  - Bloodvelds
//                //  - Dust Devils
//                //  - Cave Horrors
//                //  - Blue Dragons
//                //VANNAKA
//                new Option("Medium Slayer Task!", () -> {
//                    player.dialogue(new OptionsDialogue("Select your TASK!",
//                            new Option("Fire Giants!", () -> {
//                                Slayer.set(player, SlayerTask.Type.MEDIUM);
//                                player.slayerTaskName = "Fire Giants";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.VANNAKA;
//                                player.slayerTaskType = SlayerTask.Type.MEDIUM;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Bloodveld!", () -> {
//                                Slayer.set(player, SlayerTask.Type.MEDIUM);
//                                player.slayerTaskName = "Bloodveld";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.VANNAKA;
//                                player.slayerTaskType = SlayerTask.Type.MEDIUM;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Dust Devils!", () -> {
//                                Slayer.set(player, SlayerTask.Type.MEDIUM);
//                                player.slayerTaskName = "Dust Devils";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.VANNAKA;
//                                player.slayerTaskType = SlayerTask.Type.MEDIUM;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Cave Horrors!", () -> {
//                                Slayer.set(player, SlayerTask.Type.MEDIUM);
//                                player.slayerTaskName = "Cave Horrors";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.VANNAKA;
//                                player.slayerTaskType = SlayerTask.Type.MEDIUM;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Blue Dragons!", () -> {
//                                Slayer.set(player, SlayerTask.Type.MEDIUM);
//                                player.slayerTaskName = "Blue Dragons";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.VANNAKA;
//                                player.slayerTaskType = SlayerTask.Type.MEDIUM;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            })
//                    ));
//                }),
//                //Hard:
//                //  - Black Demons
//                //  - Hellhounds
//                //  - Cave Krakens
//                //  - Abyssal Demons
//                //  - Smoke Devils
//                //NIEVE
//                new Option("Hard Slayer Task!", () -> {
//                    player.dialogue(new OptionsDialogue("Select your TASK!",
//                            new Option("Black Demons!", () -> {
//                                Slayer.set(player, SlayerTask.Type.HARD);
//                                player.slayerTaskName = "Black Demons";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.NIEVE;
//                                player.slayerTaskType = SlayerTask.Type.HARD;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Hellhounds!", () -> {
//                                Slayer.set(player, SlayerTask.Type.HARD);
//                                player.slayerTaskName = "Hellhounds";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.NIEVE;
//                                player.slayerTaskType = SlayerTask.Type.HARD;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Cave Kraken!", () -> {
//                                Slayer.set(player, SlayerTask.Type.HARD);
//                                player.slayerTaskName = "Cave Kraken";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.NIEVE;
//                                player.slayerTaskType = SlayerTask.Type.HARD;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Abyssal Demons!", () -> {
//                                Slayer.set(player, SlayerTask.Type.HARD);
//                                player.slayerTaskName = "Abyssal Demons";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.NIEVE;
//                                player.slayerTaskType = SlayerTask.Type.HARD;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Smoke Devils!", () -> {
//                                Slayer.set(player, SlayerTask.Type.HARD);
//                                player.slayerTaskName = "Smoke Devils";
//                                player.slayerTaskRemaining = Random.get(50,150);
//                                player.currentSlayerMaster = SlayerMaster.NIEVE;
//                                player.slayerTaskType = SlayerTask.Type.HARD;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            })
//                    ));
//                }),
//                //Boss:
//                //  - Zulrah
//                //  - Dagg Kings
//                //  - Shamans
//                //  - Aby sire
//                //  - GWD   - Kree'arra
//                //          - General Graardor
//                //          - Kril Tsustaroth
//                //          - Commander Zilyanna
//                //DURADEL
//                new Option("Boss Slayer Task!", () -> {
//                    player.dialogue(new OptionsDialogue("Select your TASK!",
//                            new Option("Zulrah!", () -> {
//                                Slayer.set(player, SlayerTask.Type.BOSS);
//                                player.slayerTaskName = "Zulrah";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                player.slayerTaskType = SlayerTask.Type.BOSS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Dagannoth Kings!", () -> {
//                                Slayer.set(player, SlayerTask.Type.BOSS);
//                                player.slayerTaskName = "Dagannoth Kings";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                player.slayerTaskType = SlayerTask.Type.BOSS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("The Abyssal Sire!", () -> {
//                                Slayer.set(player, SlayerTask.Type.BOSS);
//                                player.slayerTaskName = "The Abyssal Sire";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                player.slayerTaskType = SlayerTask.Type.BOSS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("The Alchemical Hydra!", () -> {
//                                Slayer.set(player, SlayerTask.Type.BOSS);
//                                player.slayerTaskName = "The Alchemical Hydra";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                player.slayerTaskType = SlayerTask.Type.BOSS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("GWD Bosses!", () -> {
//                                player.dialogue(new OptionsDialogue("Select your TASK!",
//                                        new Option("Kree'arra!", () -> {
//                                            Slayer.set(player, SlayerTask.Type.BOSS);
//                                            player.slayerTaskName = "Kree'arra";
//                                            player.slayerTaskRemaining = Random.get(10,35);
//                                            player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                            player.slayerTaskType = SlayerTask.Type.BOSS;
//                                            player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                            if (player.Dslayertask == 2){
//                                                player.slayertasktimer = System.currentTimeMillis();
//                                                player.Dslayertask = 3;
//                                            }
//                                            player.Dslayertask += 1;
//                                        }),
//                                        new Option("Commander Zilyana!", () -> {
//                                            Slayer.set(player, SlayerTask.Type.BOSS);
//                                            player.slayerTaskName = "Commander Zilyana";
//                                            player.slayerTaskRemaining = Random.get(10,35);
//                                            player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                            player.slayerTaskType = SlayerTask.Type.BOSS;
//                                            player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                            if (player.Dslayertask == 2){
//                                                player.slayertasktimer = System.currentTimeMillis();
//                                                player.Dslayertask = 3;
//                                            }
//                                            player.Dslayertask += 1;
//                                        }),
//                                        new Option("General Graardor!", () -> {
//                                            Slayer.set(player, SlayerTask.Type.BOSS);
//                                            player.slayerTaskName = "General Graardor";
//                                            player.slayerTaskRemaining = Random.get(10,35);
//                                            player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                            player.slayerTaskType = SlayerTask.Type.BOSS;
//                                            player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                            if (player.Dslayertask == 2){
//                                                player.slayertasktimer = System.currentTimeMillis();
//                                                player.Dslayertask = 3;
//                                            }
//                                            player.Dslayertask += 1;
//                                        }),
//                                        new Option("K'ril Tsutsaroth!", () -> {
//                                            Slayer.set(player, SlayerTask.Type.BOSS);
//                                            player.slayerTaskName = "K'ril Tsutsaroth";
//                                            player.slayerTaskRemaining = Random.get(10,35);
//                                            player.currentSlayerMaster = SlayerMaster.DURADEL;
//                                            player.slayerTaskType = SlayerTask.Type.BOSS;
//                                            player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                            if (player.Dslayertask == 2){
//                                                player.slayertasktimer = System.currentTimeMillis();
//                                                player.Dslayertask = 3;
//                                            }
//                                            player.Dslayertask += 1;
//                                        })
//                                ));
//                            })
//                    ));
//                }),
//                //Wilderness:
//                //  - Spiders
//                //  - Bears
//                //  - Revenants
//                //  - Green Dragons
//                //  - Scorpions
//                //KRYSTILIA
//                new Option("Wilderness Slayer Task!", () -> {
//                    player.dialogue(new OptionsDialogue("Select your TASK!",
//                            new Option("Bears!", () -> {
//                                Slayer.set(player, SlayerTask.Type.WILDERNESS);
//                                player.slayerTaskName = "Bears";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.KRYSTILIA;
//                                player.slayerTaskType = SlayerTask.Type.WILDERNESS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Revenants!", () -> {
//                                Slayer.set(player, SlayerTask.Type.WILDERNESS);
//                                player.slayerTaskName = "Revenants";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.KRYSTILIA;
//                                player.slayerTaskType = SlayerTask.Type.WILDERNESS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Green Dragons!", () -> {
//                                Slayer.set(player, SlayerTask.Type.WILDERNESS);
//                                player.slayerTaskName = "Green Dragons";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.KRYSTILIA;
//                                player.slayerTaskType = SlayerTask.Type.WILDERNESS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Scorpions!", () -> {
//                                Slayer.set(player, SlayerTask.Type.WILDERNESS);
//                                player.slayerTaskName = "Scorpions!";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.KRYSTILIA;
//                                player.slayerTaskType = SlayerTask.Type.WILDERNESS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;
//                            }),
//                            new Option("Venenatis!", () -> {
//                                Slayer.set(player, SlayerTask.Type.WILDERNESS);
//                                player.slayerTaskName = "Venenatis";
//                                player.slayerTaskRemaining = Random.get(10,35);
//                                player.currentSlayerMaster = SlayerMaster.KRYSTILIA;
//                                player.slayerTaskType = SlayerTask.Type.WILDERNESS;
//                                player.sendMessage("Your new task is to kill " + (Color.RED.wrap(String.valueOf(player.slayerTaskRemaining)) + " X " + (Color.RED.wrap(String.valueOf(player.slayerTaskName)) + ".<br>Good luck!")));
//                                if (player.Dslayertask == 2){
//                                    player.slayertasktimer = System.currentTimeMillis();
//                                    player.Dslayertask = 3;
//                                }
//                                player.Dslayertask += 1;

					})
				));
			})
		));
	}

	private ArrayList<SlayerTask> getPossibleTasks(Player player) {
		ArrayList<SlayerTask> possibleTasks = new ArrayList<>();
		int i = 29;
		for (SlayerTask task : Slayer.getTasksForMaster(VarPlayerRepository.SLAYER_MASTER.get(player))) {
			SlayerCreature slayerCreature = SlayerCreature.lookup(task.getTaskId());
			if (slayerCreature == null) continue;
			if (player.getStats().get(StatType.Slayer).fixedLevel >= slayerCreature.getReq() && !SlayerUnlock.blocked(player, task.getTaskId())) {

				if (!player.slayerCombatCheck && player.getCombat().getLevel() < slayerCreature.getCbreq()) {
					continue;
				}

//                if (slayerCreature.canAssign != null && !slayerCreature.canAssign.apply(player, SlayerMaster.master(player))) {
//                    continue;
//                }
				i = i + 6;
			}

		}
		return possibleTasks;
	}
}
