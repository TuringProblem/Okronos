package io.ruin.model.skills.slayer;

import io.ruin.api.utils.Random;
import io.ruin.cache.NPCType;
import io.ruin.model.World;
import io.ruin.model.activities.newcomertasks.NewcomerTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ASlayingExperience;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCRareDropTable;
import io.ruin.model.entity.player.DonatorBonus;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.slayer.master.Duradel;
import io.ruin.model.skills.slayer.master.Konar;
import io.ruin.model.skills.slayer.master.Krystilia;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.FormatMessage;

import java.util.*;
import java.util.stream.Collectors;

public class SlayerMaster {
	private final int npcId;
	public static SlayerMaster[] masters;
	public static final int TURAEL_ID = 1;
	public static final int MAZCHNA_ID = 2;
	public static final int VANNAKA_ID = 3;
	public static final int CHAELDAR_ID = 4;
	public static final int STEVE_ID = 5;
	public static final int DURADEL_ID = 6;
	public static final int KRYSTILIA_ID = 7;
	public static final int KONAR_ID = 8;
	public static final int NOMAD_ID = 9;

	public SlayerMaster(int npcId) {
		this.npcId = npcId;
	}

	public SlayerTask randomTask(Player player) {
		int last = VarPlayerRepository.SLAYER_TASK.get(player);
		List<SlayerTask> tasks = Slayer.getTasksForMaster(VarPlayerRepository.SLAYER_MASTER.get(player));

		List<SlayerTask> filteredTasks = new ArrayList<>();

		for (SlayerTask task : tasks) {
			if (task != null && task.getTaskId() != last
					&& player.getStats().get(StatType.Slayer).fixedLevel >= task.getSlayerRequirement()
					&& !SlayerUnlock.blocked(player, task.getTaskId())) {

				if (!player.slayerCombatCheck && player.getCombat().getLevel() < task.getCombatRequirement()) {
					continue;
				}

				filteredTasks.add(task);
			}
		}

		SlayerTask def = null;

		///  This was commented out to only allow Nomad to assign Boss taska
//		var availableBossTasks = filteredTasks.stream().filter(task -> task.getTaskId() == 98).count();
//		var shouldPickBossTask = Random.get(0, 100) <= 14;
//		boolean bossEligible = npcId == Konar.KONAR || npcId == Duradel.DURADEL || npcId == Krystilia.KRYSTILIA;
//		if (bossEligible && VarPlayerRepository.LIKE_A_BOSS.get(player) != 0 && availableBossTasks > 0) {
//
//			if (shouldPickBossTask) {
//				for (SlayerTask slayerTask : filteredTasks) {
//					if (slayerTask.getTaskId() == 98) {
//						def = slayerTask;
//					}
//				}
//			}
//		}

		if (def == null) {
			def = filteredTasks.get(Random.get(filteredTasks.size() - 1));
		}

		if (def.getTaskId() == 98) {
			if (npcId == KRYSTILIA_ID) {
				int playerSlayerLevel = player.getStats().get(StatType.Slayer).fixedLevel;
				List<SlayerBoss> wildyBossTasks = Arrays.stream(SlayerBoss.values())
						.filter(task -> task.wilderness && playerSlayerLevel >= task.slayerReq).collect(Collectors.toList());
				SlayerBoss bossTask = wildyBossTasks.get(Random.get(wildyBossTasks.size() - 1));
				int configId = bossTask.configId;
				VarPlayerRepository.BOSS_TASK.set(player, configId);
			} else {
				int playerSlayerLevel = player.getStats().get(StatType.Slayer).fixedLevel;
				List<SlayerBoss> nonWildyBossTasks = Arrays.stream(SlayerBoss.values())
						.filter(task -> !task.wilderness && playerSlayerLevel >= task.slayerReq)
						.collect(Collectors.toList());

				SlayerBoss bossTask = nonWildyBossTasks.get(Random.get(nonWildyBossTasks.size() - 1));
				int configId = bossTask.configId;
				VarPlayerRepository.BOSS_TASK.set(player, configId);
			}
		} else {
			VarPlayerRepository.BOSS_TASK.set(player, 0);
		}

		return def;
	}

	public static SlayerMaster master(int npc) {
		for (SlayerMaster master : masters) {
			if (master.npcId == npc)
				return master;
		}

		return null;
	}

	public static String bestMaster(Player player) {
		final int cmb = player.getCombat().getLevel();

		if (cmb < 20)
			return "Turael in Burthorpe";

		if (cmb < 40)
			return "Mazchna in Canifis";

		if (cmb < 70)
			return "Vannaka in Edgeville<br>Dungeon";

		if (cmb < 75)
			return "Chaeldar in Zanaris.";

		if (cmb < 85)
			return "Konar on Mount<br>Karuulm";

		if (cmb < 100)
			return "Steve in the Gnome<br>Stronghold";

		return "Duradel in Shilo Village";
	}

	public static boolean isTask(Player player, NPC npc) {
		if (player.bossSlayerName != null && npc != null && npc.getDef() != null
				&& npc.getDef().name.equalsIgnoreCase(player.bossSlayerName))
			return true;
		final int task = VarPlayerRepository.SLAYER_TASK.get(player);
		int am = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);
		final int master = VarPlayerRepository.SLAYER_MASTER.get(player);

		SlayerCreature creature = SlayerCreature.lookup(task);
		return task > 0 && am > 0 && master > 0 && creature != null && creature.contains(npc);
	}

	private static int getNextTotemPiece(Player player) {
		int totemPiece = player.lastTotem;
		if (totemPiece == 0)
			return 19679;
		else if (totemPiece == 19679)
			return 19681;
		else if (totemPiece == 19681)
			return 19683;
		else
			return 19679;
	}

	public static final int BOSS_TASK_THRESHOLD = 50;
	public static final int BOSS_TASK_ID = 98;

	public static void handle(Player player, NPC npc) {
		// player.getSlayerLog().update(npc.getId());TODO

		final int task = VarPlayerRepository.SLAYER_TASK.get(player);
		int am = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);
		final int master = VarPlayerRepository.SLAYER_MASTER.get(player);

		SlayerCreature creature = SlayerCreature.lookup(task);

		if (task > 0 && am > 0 && master > 0 && creature != null && (creature.contains(npc))) {
			int combat = npc.getDef().combatLevel;

			if (Random.get(50) == 0) {
				int totemPiece = getNextTotemPiece(player);
				player.lastTotem = totemPiece;
				Position spawnPosition;
				spawnPosition = player.getPosition();
				new GroundItem(totemPiece, 1).owner(player).position(spawnPosition).spawn();
			}

			boolean eligibleForSlayerKey = (master == TURAEL_ID || master == VANNAKA_ID)
					|| master == MAZCHNA_ID && Random.get(75) == 0
					|| (master == STEVE_ID || master == KONAR_ID) || master == CHAELDAR_ID && Random.get(60) == 0
					|| (master == DURADEL_ID || master == KRYSTILIA_ID) && Random.get(60) == 0;

			if (Random.get(60) == 0 && master != KONAR_ID) {
				SlayerKey slayerKey = getSlayerKeyForMaster(master);
				if (slayerKey != null) {
					int slayerKeyId = slayerKey.getId();
					Position spawnPosition;
					spawnPosition = player.getPosition();
					new GroundItem(slayerKeyId, 1).owner(player).position(spawnPosition).spawn();
				}
			}

			if (VarPlayerRepository.SLAYER_TASK.get(player) == BOSS_TASK_ID) {
				int bossChance = Random.get(BOSS_TASK_THRESHOLD);
				if (bossChance == 0) {
					SlayerKey slayerKey = SlayerKey.TIER_4;
					Position spawnPosition;
					spawnPosition = player.getPosition();
					int slayerKeyId = slayerKey.getId();
					new GroundItem(slayerKeyId, 1).owner(player).position(spawnPosition).spawn();
				}
			}

			if (master == KRYSTILIA_ID && (player.wildernessLevel <= 0 && !player.getPosition().inBounds(
					new Bounds(2256, 4680, 2287, 4711)))) {
				player.sendMessage("<col=FF0000>You must kill your slayer assignment within the wilderness to "
						+ "receive experience!");
				return;
			} else if (master == KRYSTILIA_ID) {
				int chance = 200 - (npc.getMaxHp() + npc.getDef().combatLevel / 2);
					if (Random.get(chance) == 1) {
						new GroundItem(23490, 1).owner(player).position(npc.getPosition()).spawn();
					} else {
						chance = (int) (320 - Math.floor(npc.getDef().combatInfo.hitpoints * 0.8));
						if (combat > 200)
							chance = 30;
						if (Random.get(chance) == 1) {
							new GroundItem(21257, 1).owner(player).position(npc.getPosition()).spawn();
						}
					}

			} else if (master == KONAR_ID) {
				boolean notInsideArea = !KonarData.TaskLocation.values()[player.slayerLocation].inside(npc.getPosition().copy())
						&& KonarData.TaskLocation.values()[player.slayerLocation].getName().length() > 2;
				Optional<Bounds> bounds = Arrays.stream(KonarData.TaskLocation.values()[player.slayerLocation].getBoundaries())
						.findFirst();
				if (bounds.isEmpty()) {
					return;
				}
				int boundsRegionId = bounds.get().getRegion().id;
				if ((boundsRegionId == 13623 || boundsRegionId == 13607) && npc.getPosition().getRegion().id == 13723)
					notInsideArea = false;

				if (notInsideArea) {
					String location = KonarData.TaskLocation.values()[player.slayerLocation].getName();
					player.sendMessage(
							"<col=FF0000>You must kill your slayer assignment at the " + location + " to receive experience!");
					return;
				}
				int chance;

				if (combat < 100) {
					chance = (int) (0.2 * Math.pow(combat - 100, 2) + 100);
				} else {
					chance = (int) (-0.2 * combat + 120);

					if (chance > 100)
						chance = 100;

					if (chance < 50)
						chance = 50;
				}

				if (Random.get(60) == 0) {
					SlayerKey slayerKey = getSlayerKeyForMaster(master);
					if (slayerKey != null) {
						int slayerKeyId = slayerKey.getId();
						Position spawnPosition;
						spawnPosition = player.getPosition();
						new GroundItem(slayerKeyId, 1).owner(player).position(spawnPosition).spawn();
					}
				}

				if (Random.get(chance) == 1) {
					new GroundItem(23083, 1).owner(player).position(player.getPosition()).spawn();
					// player.sendMessage("<col=ff0000>Untradable drop: Brimstone key");
				}
			}

			int npcCombat = npc.getDef().combatLevel;
			int coinBoxChance = npcCombat > 100 ? 25 : npcCombat > 75 ? 40 : npcCombat > 50 ? 60 : 100;
			NPCRareDropTable npcRareDropTable = new NPCRareDropTable();
			if (Random.get(250) == 0)
				npcRareDropTable.HandleDrop(player, 0, npc);

			final int hp = NPCType.get(npc.getId()).combatInfo.hitpoints;
			final boolean superior = SuperiorSlayer.getSuperior(creature, npc.getId()) != -1
					&& SuperiorSlayer.getSuperior(creature, npc.getId()) == npc.getId();
			int xp = superior ? hp * 10 : hp;

			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.A_SLAYING_EXPERIENCE)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.A_SLAYING_EXPERIENCE);
				ASlayingExperience c = (ASlayingExperience) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				float xpBoost = 1;
				xpBoost += c.getBoost();
				xp *= xpBoost;
			}
			player.getStats().addXp(StatType.Slayer, xp, true);
			player.slayerTaskKills++;
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SLAYER_VETERAN.ordinal())).
				getCombatAchievement()).check(player);
			VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player) - 1);

			am -= 1;

			if (am == 0) {
				final int spree = ++player.slayerSpree;
				int basePoints = basePoints(master) * (World.doubleSlayer
						&& !player.getName().equalsIgnoreCase("normal") ? 2 : 1)
						+ DonatorBonus.BONUS_SLAYER_POINTS.handleBonus(player);
				final int total = points(master, spree) + basePoints;
				final int current = VarPlayerRepository.SLAYER_POINTS.get(player);
				PerkTaskHandler.handleSlayerTaskCompletion(player, master);
				player.totalSlayerTasksCompleted++;
				if (master == KRYSTILIA_ID) {
					player.wildernessSlayerTasksCompleted++;
					if (player.wildernessSlayerTasksCompleted == Achievements.SLAYERS_GETTING_WILD.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
								+ Achievements.SLAYERS_GETTING_WILD.getAchievementName());
				}
				if (player.totalSlayerTasksCompleted == 1)
					player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
							+ NewcomerTasks.SLAYER_TASK_COMPLETION.getFormattedName() + "!");

				player.sendMessage("<col=7F00FF>You've completed " + spree + " tasks in a row and received " + (total)
						+ " points. Return to a Slayer Master for a new task.");
				VarPlayerRepository.SLAYER_POINTS.set(player, current + total);
			} else {
				SuperiorSlayer.trySpawn(player, creature, npc);
			}
		} else if (VarPlayerRepository.SLAYER_TASK.get(player) == 98) {
			SlayerBoss slayerBoss = SlayerBoss.getSlayerBoss(player, VarPlayerRepository.BOSS_TASK.get(player));
			if (slayerBoss != null && slayerBoss.isTask(npc.getId())) {

				int hp = NPCType.get(npc.getId()).combatInfo.hitpoints;
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.A_SLAYING_EXPERIENCE)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.A_SLAYING_EXPERIENCE);
					ASlayingExperience c = (ASlayingExperience) player.getPlayerPerkHandler().getActivePerks(player)
							.get(perkIndex).getPerk(player);
					float xpBoost = c.getBoost();
					xpBoost += 1;
					hp *= xpBoost;
				}
				player.slayerTaskKills++;
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SLAYER_VETERAN.ordinal())).
					getCombatAchievement()).check(player);
				player.getStats().addXp(StatType.Slayer, hp, true);
				VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player) - 1);

				am -= 1;

				if (am == 0) {
					final int spree = ++player.slayerSpree;
					int basePoints = basePoints(master) * (World.doubleSlayer ? 2 : 1)
							+ DonatorBonus.BONUS_SLAYER_POINTS.handleBonus(player);
					final int total = points(master, spree) + basePoints;
					final int current = VarPlayerRepository.SLAYER_POINTS.get(player);

					if (master == KRYSTILIA_ID) {
						player.wildernessSlayerTasksCompleted++;
						if (player.wildernessSlayerTasksCompleted == Achievements.SLAYERS_GETTING_WILD.getCompletionAmount())
							player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
									+ Achievements.SLAYERS_GETTING_WILD.getAchievementName());
					}
					PerkTaskHandler.handleSlayerTaskCompletion(player, master);
					player.totalSlayerTasksCompleted++;
					if (player.totalSlayerTasksCompleted == 1)
						player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
								+ NewcomerTasks.SLAYER_TASK_COMPLETION.getFormattedName() + "!");
					player.sendMessage("<col=7F00FF>You've completed " + spree + " tasks in a row and received "
							+ (total) + " points. Return to a Slayer Master for a new task.");
					VarPlayerRepository.SLAYER_POINTS.set(player, current + total);
				}
			}
		}
	}

	private static int basePoints(int master) {
		return switch (master) {
			case TURAEL_ID -> 10;
			case MAZCHNA_ID -> 12;
			case VANNAKA_ID -> 15;
			case CHAELDAR_ID -> 18;
			case STEVE_ID, DURADEL_ID -> 20;
			case KRYSTILIA_ID -> 40;
			case KONAR_ID -> 30;
			default -> 0;
		};
	}

	public SlayerTask randomBossTask(Player player) {
		int last = VarPlayerRepository.SLAYER_TASK.get(player);
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
				task.getMaxAmount())));

		List<SlayerTask> filteredTasks = new ArrayList<>();

		for (SlayerTask task : tasks) {
			if (task != null && task.getTaskId() != last
					&& player.getStats().get(StatType.Slayer).fixedLevel >= task.getSlayerRequirement()
					&& !SlayerUnlock.blocked(player, task.getTaskId())) {

				if (!player.slayerCombatCheck && player.getCombat().getLevel() < task.getCombatRequirement()) {
					continue;
				}

				filteredTasks.add(task);
			}
		}
		System.out.println("filteredTasks " + filteredTasks.size());

		SlayerTask def = null;

		def = Random.get(filteredTasks);

		if (def == null) {
			System.out.println("null def ");
			return null;
		}

		// VarPlayerRepository.BOSS_TASK.set(player, configId);

		return def;
	}

	public enum SlayerKey {
		TIER_1(25426),
		TIER_2(25424),
		TIER_3(25432),
		TIER_4(25430);

		private final int id;

		SlayerKey(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	private static SlayerKey getSlayerKeyForMaster(int master) {
		return switch (master) {
			case TURAEL_ID, VANNAKA_ID, MAZCHNA_ID -> SlayerKey.TIER_1;
			case STEVE_ID, KONAR_ID, CHAELDAR_ID -> SlayerKey.TIER_2;
			case DURADEL_ID, KRYSTILIA_ID -> SlayerKey.TIER_3;
			case NOMAD_ID -> SlayerKey.TIER_4;
			default -> null;
		};
	}

	public static int points(int master, int spree) {
		if (spree % 1000 == 0) {
			return switch (master) {
				case MAZCHNA_ID -> 100;
				case VANNAKA_ID -> 200;
				case CHAELDAR_ID -> 500;
				case STEVE_ID, DURADEL_ID -> 750;
				case KRYSTILIA_ID -> 1250;
				case KONAR_ID -> 1000;
				default -> 0;
			};
		}

		if (spree % 250 == 0) {
			return switch (master) {
				case MAZCHNA_ID -> 70;
				case VANNAKA_ID -> 140;
				case CHAELDAR_ID -> 350;
				case STEVE_ID, DURADEL_ID -> 525;
				case KRYSTILIA_ID -> 875;
				case KONAR_ID -> 630;
				default -> 0;
			};
		}

		if (spree % 100 == 0) {
			return switch (master) {
				case MAZCHNA_ID -> 50;
				case VANNAKA_ID -> 100;
				case CHAELDAR_ID -> 250;
				case STEVE_ID, DURADEL_ID -> 375;
				case KRYSTILIA_ID -> 625;
				case KONAR_ID -> 450;
				default -> 0;
			};
		}

		if (spree % 50 == 0) {
			return switch (master) {
				case MAZCHNA_ID -> 15;
				case VANNAKA_ID -> 60;
				case CHAELDAR_ID -> 150;
				case STEVE_ID, DURADEL_ID -> 225;
				case KRYSTILIA_ID -> 375;
				case KONAR_ID -> 270;
				default -> 0;
			};
		}

		if (spree % 10 == 0) {
			return switch (master) {
				case MAZCHNA_ID -> 5;
				case VANNAKA_ID -> 20;
				case CHAELDAR_ID -> 50;
				case STEVE_ID, DURADEL_ID -> 75;
				case KRYSTILIA_ID -> 125;
				case KONAR_ID -> 90;
				default -> 0;
			};
		}
		return 0;
	}

	public int getNpcId() {
		return npcId;
	}

	public static String getTaskText(Player player, int left) {
		String text = "You're still hunting " + SlayerUnlock.taskName(player, VarPlayerRepository.SLAYER_TASK.get(player))
				+ ", with " + left + " to go.<br>Come back when you're finished.";

		int master = VarPlayerRepository.SLAYER_MASTER.get(player);

		if (master == KRYSTILIA_ID) {
			text = "You're still meant to be slaying "
					+ SlayerUnlock.taskName(player, VarPlayerRepository.SLAYER_TASK.get(player))
					+ " in<br>the Wilderness, you have " + left + " to go. Come back when<br>you've finished your task.";
		} else if (master == KONAR_ID) {
			text = "You're still bringing balance to "
					+ SlayerUnlock.taskName(player, VarPlayerRepository.SLAYER_TASK.get(player)) + " at the "
					+ KonarData.TaskLocation.values()[player.slayerLocation].getName() + ", with " + left
					+ " to go.<br>Come back when you're finished.";
		}

		return text;
	}

	public static void check(Player player) {
		int amount = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);

		if (amount > 0) {
			String name = SlayerUnlock.taskName(player, VarPlayerRepository.SLAYER_TASK.get(player));
			String location = "";
			int master = VarPlayerRepository.SLAYER_MASTER.get(player);

			if (master == KRYSTILIA_ID) {
				location = " in the wilderness";
			} else if (master == KONAR_ID && KonarData.TaskLocation.values()[player.slayerLocation].getName().length() > 2) {
				location = " at the " + KonarData.TaskLocation.values()[player.slayerLocation].getName();
			}

			player.sendMessage("You're assigned to kill " + name + "" + location + ", only " + amount + " to go.");
		} else {
			player.sendMessage("You need something new to hunt.");
		}

	}

	public SlayerTask specificTask(Player player, int taskId) {
		int last = VarPlayerRepository.SLAYER_TASK.get(player);
		List<SlayerTask> tasks = Slayer.getTasksForMaster(VarPlayerRepository.SLAYER_MASTER.get(player));

		List<SlayerTask> filteredTasks = new ArrayList<>();

		for (SlayerTask task : tasks) {
			if (task != null && task.getTaskId() != last
					&& player.getStats().get(StatType.Slayer).fixedLevel >= task.getSlayerRequirement()
					&& !SlayerUnlock.blocked(player, task.getTaskId())) {

				if (!player.slayerCombatCheck && player.getCombat().getLevel() < task.getCombatRequirement()) {
					continue;
				}

				filteredTasks.add(task);
			}
		}

		SlayerTask def = null;

		for (SlayerTask task : filteredTasks) {
			if (task.getTaskId() == taskId) {
				def = task;
			}
		}
		VarPlayerRepository.BOSS_TASK.set(player, 0);

		return def;
	}

	public static void canTeleportToTask(Player player) {
		if (player.wildernessLevel > 20) {
			player.sendMessage("You cannot teleport above 20 wilderness.");
			return;

		}
		SlayerCreature task = SlayerCreature.lookup(VarPlayerRepository.SLAYER_TASK.get(player));
		if (task == null) {
			player.sendMessage(FormatMessage.sendColoredMessage("You do not have a task.", FormatMessage.Colors.PURE_RED));
			return;
		}
		Position position = task.getPosition();
		String taskName = SlayerUnlock.taskName(player, VarPlayerRepository.SLAYER_TASK.get(player));
		if (position != null) {
			teleportPlayerToTask(player, position, taskName);
			return;
		}
		player.sendMessageFormat(FormatMessage.sendColoredMessage(
				"Couldn't find a teleport for %s. Let a developer know if you have a location in mind.",
				FormatMessage.Colors.PURE_RED), taskName);
	}

	private static void teleportPlayerToTask(Player player, Position position, String taskName) {
		player.getMovement().teleport(position);
		player.sendMessageFormat(
				FormatMessage.sendBracketsMessage("Slayer", "You arrive at %s", FormatMessage.Colors.BLUE_PRIMARY), taskName);
	}
}
