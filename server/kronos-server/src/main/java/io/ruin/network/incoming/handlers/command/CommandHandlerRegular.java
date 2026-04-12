package io.ruin.network.incoming.handlers.command;

import com.google.gson.reflect.TypeToken;
import discord.webhooks.logs.VotingHook;
import io.ruin.Server;
import io.ruin.cache.Color;
import io.ruin.model.VoteHandler;
import io.ruin.model.World;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.VoteBossHandler;
import io.ruin.model.activities.bosses.instancetoken.InstanceManager;
import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.dailytasks.DailyTasksInterface;
import io.ruin.model.activities.newcomertasks.NewcomerTasks;
import io.ruin.model.activities.tempevents.summerevent.SummerBossesInterface;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.content.RefSystem;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.actions.edgeville.EmblemTrader;
import io.ruin.model.entity.npc.actions.edgeville.VoteGambler;
import io.ruin.model.entity.player.DonatorBonus;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerGroup;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.entity.player.Title;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.ServerTeleports;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.object.owned.impl.DwarfCannon;
import io.ruin.model.skills.events.ShootingStars;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;
import io.ruin.model.skills.slayer.SlayerUnlock;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.process.event.EventWorker;
import io.ruin.services.Loggers;
import io.ruin.services.Punishment;
import io.ruin.services.http.models.store.DonationCommandHandler;
import io.ruin.utility.BadWords;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

public class CommandHandlerRegular {

	public static boolean handle(Player player, String query, String command, String[] args) {
		if (player.jailerName != null) { // If player is in jail
			player.sendMessage("You cannot use commands while in jail.");
			return true;
		}

		switch (command) {

			case "profanity": {
				var toggle = args.length == 1 ? args[0] : "";
				switch (toggle) {
					case "off":
						VarPlayerRepository.PROFANITY_FILTER.set(player, 1);
						player.sendMessage("Profanity filter is now disabled.");
						break;
					case "on":
						VarPlayerRepository.PROFANITY_FILTER.set(player, 0);
						player.sendMessage("Profanity filter is now enabled.");
						break;
					default:
						player.sendMessage("Usage: ::profanity off/on");
						break;
				}
				return true;
			}

			case "discord": {
				player.openUrl(World.type.getWorldName() + " Discord", "https://discord.gg/WeYBAV7S3G");
				return true;
			}

			case "ticket": {
				player.openUrl(World.type.getWorldName() + " Discord",
						"https://ptb.discord.com/channels/1150539550561681508/1199283936153571338");
				return true;
			}
			case "araxxor": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.ARAXXOR;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}
			case "scurrius": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SCURRIUS;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}
			case "deals": {
				player.openUrl(World.type.getWorldName() + " Discord",
						"https://ptb.discord.com/channels/1150539550561681508/1188203513973579836");
				return true;
			}

			case "toa": {
				teleport(player, 3355, 9119, 0);
				return true;
			}

			case "giants": {
				teleport(player, 1417, 3590, 0);
				return true;
			}

			case "sourhog": {
				teleport(player, 3159, 9712, 0);
				return true;
			}

			case "isleofsouls", "ios": {
				teleport(player, 2314, 2919, 0);
				return true;
			}

			case "grimy": {
				teleport(player, 1511, 9716, 0);
				return true;
			}

			case "donated":
			case "claim":
				DonationCommandHandler.handleClaim(player);
				return true;
			case "db", "donboss": {
				teleport(player, DonationBossHandler.teleportPosition);
				if (player.pet != null && player.familiarNPC != null) {
					Pet.pickup(player, player.familiarNPC, player.pet, true);
				}
				return true;
			}

			case "vb", "voteboss": {
				teleport(player, VoteBossHandler.teleportPosition);
				if (player.pet != null && player.familiarNPC != null) {
					Pet.pickup(player, player.familiarNPC, player.pet, true);
				}
				return true;
			}

			case "raffle":
			case "lotterytimer":
			case "voteraffle":
			case "votelottery":
				Set<String> entries = VoteGambler.getEntries();
				List<VoteGambler.Winner> unclaimedRewards = VoteGambler.getUnclaimedRewards();
				long timeUntilRaffle;

				if (VoteGambler.isFirstRaffle()) {
					// Consider the time until the first raffle
					timeUntilRaffle = entries.isEmpty() ? 0
							: 12 * 60 * 60 * 1000
									- (System.currentTimeMillis() - VoteGambler.getEntryStartTime(entries.iterator().next()));
				} else {
					// Consider the time until the next raffle
					timeUntilRaffle = entries.isEmpty() ? 12 * 60 * 60 * 1000 : VoteGambler.calculateTimeUntilNextDrawing();
				}

				if (entries.isEmpty()) {
					player.sendMessage("There are currently no entries in the vote raffle. Enter now!");
				} else if (timeUntilRaffle > 0) {
					long hours = timeUntilRaffle / (60 * 60 * 1000);
					long minutes = (timeUntilRaffle % (60 * 60 * 1000)) / (60 * 1000);
					long seconds = (timeUntilRaffle % (60 * 1000)) / 1000;

					if (VoteGambler.isFirstRaffle()) {
						player.sendMessage("Time until the raffle: " + hours + "h " + minutes + "m " + seconds + "s");
					} else {
						player.sendMessage("Time until the next raffle: " + hours + "h " + minutes + "m " + seconds + "s");
					}
				} else {
					player.sendMessage("Time until next vote raffle: 0h 0m 0s");
				}
				return true;

			case "scrolls":
			case "timers":
			case "scrolltimers":
			case "scrolltime":

				if (player.getBrewImmunityRemaining() > 1)
					player.sendMessage("You currently have " + player.getBrewImmunityRemaining() / 100
							+ " minutes left on your Brew Immunity timer.");
				if (player.getDoubleExpRemaining() > 1)
					player.sendMessage(
							"You currently have " + player.getDoubleExpRemaining() / 100 + " minutes left on your Double exp timer.");
				if (player.getDoubleDropsRemaining() > 1)
					player.sendMessage("You currently have " + player.getDoubleDropsRemaining() / 100
							+ " minutes left on your Double drops timer.");
				if (player.getDamageBoostRemaining() > 1)
					player.sendMessage("You currently have " + player.getDamageBoostRemaining() / 100
							+ " minutes left on your Damage boost timer.");
				if (player.getDamageReductionBoostRemaining() > 1)
					player.sendMessage("You currently have " + player.getDamageReductionBoostRemaining() / 100
							+ " minutes left on your Damage reduction boost timer.");
				if (player.getPrayerBoostBonusRemaining() > 1)
					player.sendMessage("You currently have " + player.getPrayerBoostBonusRemaining() / 100
							+ " minutes left on your Prayer drain reduction timer.");
				if (player.getDropRateBoostRemaining() > 1)
					player.sendMessage("You currently have " + player.getDropRateBoostRemaining() / 100
							+ " minutes left on your 5% Drop rate boost timer.");
				if (player.getPetBoostRemaining() > 1)
					player.sendMessage(
							"You currently have " + player.petDropBonusTimeLeft / 100 + " minutes left on your Pet boost timer.");
				if (player.getBrewImmunityRemaining() <= 1 && player.getDoubleExpRemaining() <= 1
						&& player.getDoubleDropsRemaining() <= 1 && player.getDamageBoostRemaining() <= 1
						&& player.getDamageReductionBoostRemaining() <= 1 && player.getPrayerBoostBonusRemaining() <= 1
						&& player.getDropRateBoostRemaining() <= 1 && player.getPetBoostRemaining() <= 1) {
					player.sendMessage("You currently have no scroll timers active.");
				}
				return true;

			case "voted":
			case "claimvote":
			case "claimed":
				EventWorker.submitHttpGetRequest("voting/claim/" + player.getName(), s -> {
					var type = new TypeToken<List<String>>() {
					}.getType();

					// TODO: This is a list of all the claimed votes, do what you need
					List<String> claimedVotes = s.getResponseObject(type);
					if (claimedVotes.isEmpty()) {
						player.sendMessage("You have no votes to claim, please try again later.");
						return;
					}
					int points = claimedVotes.size() + DonatorBonus.VOTE_POINT_BONUS.handleBonus(player);
					VoteHandler.addHwid(player.hwid);
					player.votePoints += points;
					player.votesClaimed += claimedVotes.size();
					player.claimedVotes++;
					Instant now = Instant.now();
					player.lastVoteClaimInEpoch = now.getEpochSecond();
					if (player.claimedVotes == Achievements.THIRD_PART_CANDIDATE.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
								+ Achievements.THIRD_PART_CANDIDATE.getAchievementName());

					VoteBossHandler.addVote(claimedVotes.size());
					player.getDailyVote().voteCheck();
					if (player.votesClaimed == 1)
						player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
								+ NewcomerTasks.VOTE_CLAIM.getFormattedName() + "!");

					// Add regular vote rewards
					player.getInventory().addOrDrop(30596, claimedVotes.size());
					player.getInventory().addOrDrop(30602, claimedVotes.size());

					// Add single chance for presents per claim

					player.sendMessage(
							"You have received " + points + " vote points, you now have " + player.getVotePoints() + " vote points.");
					player.sendMessage("You are rewarded with a Vote Buff Streak Box and a Vote raffle ticket for each vote.");
					player.getDailyVote().open();
//					VotingWebhook.sendVoted(player);

					var dto = new JSONObject()
						.put("player", player.getName())
						.put("vote_streak", player.voteStreak)
						.put("hwid", player.hwid);
					//VotingHook.sendVoted(dto);

				}, f -> {
					player.sendMessage("You have no votes to claim, please try again later.");
				});
				return true;

			case "commands": {
				player.sendScroll("<col=800000>Commands</col>",
						"<col=800000>Teleport Commands:</col>",
						"::home/::edge", "::train/sandcrabs", "::vb/::gb/::db/::malakar", "::dz",
						"::slayer/::slay", "::shops", "::bossname", "::cityname", "::monstername", "::minigamename",
						"::cox/raids/tob", "::gwd", "::mining", "::miningguild", "::mlm/motherloadmine",
						"::wcguild", "::abyss", "::zmi", "::astrals", "::bloods", "::wraths", "::puropuro", "::fremslayer",
						"::ancientcavern", "::brimdung", "::lumdung", "::edgedung",
						"<col=800000>Misc Commands:</col>",
						"::online/players", "::staff", "::clog", "::yell", "::donated/::claim",
						"::vote/::claimvote/::claimed", "::clear/::empty", "::upgrade",
						"::reclaimcannon", "::breakvials", "::newcomer", "::dailies",
						"::char", "::skull", "::changepass", "::changesecuritypin", "::task/slayertask", "::bosstask",
						"::scrolls/timers", "::raffle/votelottery",
						"<col=800000>Website Commands:</col>",
						"::donate", "::vote", "::discord", "::hiscores");
				return true;
			}

			case "task":
			case "slayertask": {
				player.sendMessage("Your current slayer task is to kill " + VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player)
						+ " " + SlayerUnlock.taskName(player, VarPlayerRepository.SLAYER_TASK.get(player)) + "s.");
				return true;
			}

			case "bosstask":
			case "bossslayertask": {
				player.sendMessage(
						"Your current boss task is to kill " + player.currentBossSlayerAmount + " " + player.bossSlayerName + "s.");
				return true;
			}

			case "instances": {
				List<String> text = new LinkedList<>();
				List<String> instances = new LinkedList<>();

				InstanceManager.instances.forEach((k, v) -> instances.add(k + " - " + v));
				text.add("<col=ff0000><shad=0000000>Instances Active:</col></shad> " + instances.size() + "");
				if (instances.isEmpty())
					text.add("None active!");
				else
					text.addAll(instances);
				player.sendScroll("Instances Active", text.toArray(new String[0]));
				return true;
			}

			case "raid":
			case "raids":
			case "enterraid":
			case "enterraids":
			case "raidstest":
			case "cox":
				teleport(player, 1254, 3558, 0);
				return true;

			case "clear":
			case "empty": {
				if (player.wildernessLevel > 0) {
					player.sendMessage("You cannot clear your inventory in the wilderness.");
					return true;
				}
				player.dialogue(
						new MessageDialogue("Warning! This removes all items from your inventory"),
						new OptionsDialogue("Are you sure you wish to preform this action?",
								new Option("Yes", () -> player.getInventory().clear()),
								new Option("No", () -> player.sendFilteredMessage("You did not empty your inventory."))));
				return true;
			}
			/*
			 * Teleport commands
			 */
			// case "gim": {
			// return true;
			// }

			case "edge": {
				teleport(player, 3085, 3492, 0);
				return true;
			}

			case "claimdonortablet": {
				if (player.isDonator()) {
					player.getInventory().add(new Item(30611, 1));
					player.sendMessage("You have claimed your unlimited use donator teleport tablet.");
				}
				return true;
			}

			case "upgrade": {
				player.getUpgradeSystemInterface().open(player);
				return true;
			}
			case "votestreak":
			case "dailyvote": {
				player.getDailyVote().open();
				return true;
			}
			case "globalboss":
			case "gb": {
				teleport(player, new Position(2900, 3616, 0));
				return true;
			}
			case "reclaimcannon": {
				if (World.isLive() && Duration.between(Server.bootedAt, Instant.now()).getSeconds() < 600) {
					player.sendMessage("Workers are currently starting, try again later.");
					return true;
				}
				World.doCannonReclaim(player.getName(), (reclaim) -> {
					if (reclaim) {
						boolean hasSpace = player.getInventory().hasFreeSlots(DwarfCannon.CANNON_PARTS.length);
						if (!hasSpace) {
							player.sendMessage("You don't have inventory space to reclaim your cannon!");
						} else {
							Instant now = Instant.now();
							player.lastCannonClaimInEpoch = now.getEpochSecond();
							player.getInventory().addOrDrop(DwarfCannon.BARRELS, 1);
							player.getInventory().addOrDrop(DwarfCannon.STAND, 1);
							player.getInventory().addOrDrop(DwarfCannon.BASE, 1);
							player.getInventory().addOrDrop(DwarfCannon.FURNACE, 1);
							player.sendMessage("You have reclaimed your cannon!");
							World.removeCannonReclaim(player.getName());
						}
					} else {
						player.sendMessage("You don't have a cannon to reclaim!");
					}
				});
				return true;
			}

			case "explock": {
				player.experienceLock = !player.experienceLock;
				player.sendMessage("Your experience has been " + (player.experienceLock ? "locked" : "unlocked") + ".");
				return true;
			}

			case "ref",
					"spring",
					"summer",
					"fall",
					"winter": {
				String code;
				if (player.playTime / (24 * 3600) < 1)
					code = "newplayer";
				else
					code = "returning";
				RefSystem.claimReferral(player, code);
				return true;
			}

			case "cas": {
				player.getCombatAchievementInterface().open(player);
				return true;
			}
			case "cacompletedtest": {
				List<CombatAchievements> easy = player.combatAchievementsList.stream()
					.filter(achievement ->
						achievement.getCombatAchievement().getTier() == CombatAchievement.Tier.EASY &&
							player.combatAchievementStore.getOrDefault(achievement.ordinal(), false)
					).toList();
				List<CombatAchievements> medium = player.combatAchievementsList.stream()
					.filter(achievement ->
						achievement.getCombatAchievement().getTier() == CombatAchievement.Tier.MEDIUM &&
							player.combatAchievementStore.getOrDefault(achievement.ordinal(), false)
					).toList();
				List<CombatAchievements> hard = player.combatAchievementsList.stream()
					.filter(achievement ->
						achievement.getCombatAchievement().getTier() == CombatAchievement.Tier.HARD &&
							player.combatAchievementStore.getOrDefault(achievement.ordinal(), false)
					).toList();
				List<CombatAchievements> elite = player.combatAchievementsList.stream()
					.filter(achievement ->
						achievement.getCombatAchievement().getTier() == CombatAchievement.Tier.ELITE &&
							player.combatAchievementStore.getOrDefault(achievement.ordinal(), false)
					).toList();
				List<CombatAchievements> master = player.combatAchievementsList.stream()
					.filter(achievement ->
						achievement.getCombatAchievement().getTier() == CombatAchievement.Tier.MASTER &&
							player.combatAchievementStore.getOrDefault(achievement.ordinal(), false)
					).toList();
				List<CombatAchievements> grandmaster = player.combatAchievementsList.stream()
					.filter(achievement ->
						achievement.getCombatAchievement().getTier() == CombatAchievement.Tier.GRANDMASTER &&
							player.combatAchievementStore.getOrDefault(achievement.ordinal(), false)
					).toList();



				player.sendMessage("Easy combat achievements completed: " + easy.size());
				player.sendMessage("Medium combat achievements completed: " + medium.size());
				player.sendMessage("Hard combat achievements completed: " + hard.size());
				player.sendMessage("Elite combat achievements completed: " + elite.size());
				player.sendMessage("Master combat achievements completed: " + master.size());
				player.sendMessage("Grandmaster combat achievements completed: " + grandmaster.size());
				return true;
			}
			case "sb":
			case "summerboss": {
				SummerEvent.teleportToBoss(player);
				return true;
			}
			case "home": {
				teleportHome(player, 3087, 3496, 0);
				return true;
			}

			case "ab":
			case "activebosses": {
				SummerBossesInterface s = new SummerBossesInterface();
				s.open(player);
				return true;
			}
			case "balanceelemental": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.BALANCE_ELEMENTAL;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}

			case "armouredzombies": {
				teleport(player, new Position(3558, 9778, 0));
				return true;
			}

			case "brine":
			case "brineratcavern":
			case "brinerats": {
				teleport(player, new Position(2717, 10133, 0));
				return true;
			}

			case "monkeys": {
				teleport(player, new Position(2758, 9183, 0));
				return true;
			}

			case "star": {
				if (ShootingStars.ACTIVE == null) {
					player.sendMessage("There is current not an active star to teleport to.");
					return true;
				}
				Position pos = null;
				Position starPosition = ShootingStars.ACTIVE.starSpawn;
				Bounds starBounds = new Bounds(starPosition.getX() - 5, starPosition.getY() - 5, starPosition.getX() + 5,
						starPosition.getY() + 5, starPosition.getZ());
				while (pos == null) {
					Position teleportPosition = starBounds.randomPosition();
					if (Tile.get(teleportPosition, true) != null && Tile.get(teleportPosition, true).clipping == 0)
						pos = teleportPosition;
				}
				if (ShootingStars.ACTIVE != null)
					teleport(player, pos);
				return true;
			}

			case "breakvials": {
				player.breakVials = !player.breakVials;
				player.sendMessage("You have " + (player.breakVials ? "enabled" : "disabled") + " vial breaking.");
				return true;
			}

			case "slayer": {
				teleport(player, new Position(3097, 3511, 0));
				return true;
			}

			case "arg":
			case "argentavis": {
				teleport(player, 2143, 5526, 3);
				return true;
			}

			case "ophidia": {
				player.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
						new Option("Global Ophidia's Lair", () -> {
							Position teleportPosition = new Position(3149, 4658, 0);
							teleport(player, teleportPosition);
						}),
						new Option("Instanced Ophidia's Lair", () -> {
							player.getInstanceTokenInterface().selectedBoss = InstanceMaps.OPHIDIA;
							player.getInstanceTokenInterface().startInstance(player, true);
						})));
				return true;
			}

			case "pm":
			case "muspah":
			case "phantommuspah": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.PHANTOM_MUSPAH;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}

			case "lev":
			case "levi":
			case "leviathan": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.LEVIATHAN;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}
			case "solheredit": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}
			case "duke": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.DUKE;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}
			case "vard":
			case "vardorvis": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.VARDORVIS;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}

			case "whisp":
			case "whisperer": {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.WHISPERER;
				player.getInstanceTokenInterface().startInstance(player, true);
				return true;
			}

			case "crazyarch": {
				teleportDangerous(player, 2978, 3702, 0);
				return true;
			}

			case "newcomer": {
				player.getNewcomerTaskInterface().openInterface(player);
				return true;
			}

			case "dailytasks":
			case "dailytask":
			case "dailies":
			case "dailys": {
				DailyTasksInterface dailyTasksInterface = new DailyTasksInterface();
				dailyTasksInterface.open(player);
				return true;
			}

			case "blackchins": {
				teleportDangerous(player, 3146, 3771, 0);
				return true;
			}

			case "elders": {
				teleportDangerous(player, 3236, 3633, 0);
				return true;
			}

			case "kbd": {
				teleportDangerous(player, 3069, 10255, 0);
				return true;
			}

			case "gauntlet": {
				teleport(player, ServerTeleports.THE_GAUNTLET.getTeleportPos());
				return true;
			}

			case "scorpia": {
				teleportDangerous(player, 3233, 3949, 0);
				return true;
			}

			case "changesecuritypin": {
				int[] bannedPins = { 12345, 54321, 11111, 22222, 33333, 44444, 55555, 66666, 77777, 88888, 99999,
						10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 12121, 21212, 98765 };
				player.integerInput("Enter your new security pin:", pin -> {
					boolean pinTooEasy = false;
					String number = String.valueOf(pin);
					char[] digits = number.toCharArray();
					for (int j = 0; j <= bannedPins.length - 1; j++) {
						if (pin == bannedPins[j]) {
							pinTooEasy = true;
						}
					}
					if (digits.length != 5) {
						player.sendMessage("Your security pin must be 5 digits long.");
						return;
					}
					if (pin == player.securityPin) {
						player.sendMessage("You cannot set your security pin to your current pin.");
						return;
					}
					if (pinTooEasy) {
						player.sendMessage("Your security pin is too easy to guess.");
						return;
					}
					player.securityPin = pin;
					player.sendMessage("Your security pin has been changed to " + pin + ".");
				});
				return true;
			}

			case "venenatis":
			case "ven": {
				teleportDangerous(player, 3320, 3795, 0);
				return true;
			}

			case "cal":
			case "callisto": {
				teleportDangerous(player, 3293, 3848, 0);
				return true;
			}

			case "shops": {
				teleport(player, 3079, 3512, 0);
				return true;
			}

			case "mining": {
				teleport(player, 3089, 3468, 0);
				return true;
			}

			case "slay": {
				teleport(player, 3095, 3512, 0);
				return true;
			}

			case "edgepk": {
				teleport(player, 3088, 3523, 0);
				return true;
			}

			case "ld": {
				teleportDangerous(player, 3204, 3844, 0);
				return true;
			}

			case "vetion":
			case "vet": {
				teleportDangerous(player, 3223, 3789, 0);
				return true;
			}

			case "rogues": {
				teleportDangerous(player, 3286, 3922, 0);
				return true;
			}

			case "gdz": {
				teleportDangerous(player, 3292, 3886, 0);
				return true;
			}
			case "vorkath": {
				teleport(player, ServerTeleports.VORKATH.getTeleportPos());
				return true;
			}

			case "corp": {
				teleport(player, ServerTeleports.CORP.getTeleportPos());
				return true;
			}

			case "kq": {
				teleport(player, ServerTeleports.KQ.getTeleportPos());
				return true;
			}

			case "dks": {
				teleport(player, ServerTeleports.DKS.getTeleportPos());
				return true;
			}

			case "kraken": {
				teleport(player, ServerTeleports.KRAKEN.getTeleportPos());
				return true;
			}

			case "hydra": {
				teleport(player, ServerTeleports.HYDRA.getTeleportPos());
				return true;
			}

			case "cerberus":
			case "cerb": {
				teleport(player, ServerTeleports.CERBERUS.getTeleportPos());
				return true;
			}

			case "giantmole":
			case "rockon-homestead":
			case "mole": {
				teleport(player, ServerTeleports.GIANT_MOLE.getTeleportPos());
				return true;
			}

			case "skotizo": {
				teleport(player, ServerTeleports.SKOTIZO.getTeleportPos());
				return true;
			}

			case "demonics":
			case "demonicgorillas": {
				teleport(player, ServerTeleports.DEMONIC_GORILLA.getTeleportPos());
				return true;
			}

			case "nex": {
				teleport(player, ServerTeleports.NEX.getTeleportPos());
				return true;
			}

			case "nm":
			case "nightmare": {
				teleport(player, ServerTeleports.NIGHTMARE.getTeleportPos());
				return true;
			}

			case "wcguild": {
				teleport(player, ServerTeleports.WOODCUTTING_GUILD.getTeleportPos());
				return true;
			}

			case "seers": {
				teleport(player, ServerTeleports.SEERS.getTeleportPos());
				return true;
			}

			case "hardwoodgrove": {
				teleport(player, ServerTeleports.HARDWOOD_GROVE.getTeleportPos());
				return true;
			}

			case "pleasantpark": {
				teleport(player, ServerTeleports.PLEASANT_PARK.getTeleportPos());
				return true;
			}

			case "abyss": {
				teleport(player, ServerTeleports.ABYSS.getTeleportPos());
				return true;
			}

			case "zmi": {
				teleport(player, ServerTeleports.ZMI.getTeleportPos());
				return true;
			}

			case "astrals": {
				teleport(player, 2156, 3864, 0);
				return true;
			}

			case "bloods": {
				teleport(player, 1719, 3828, 0);
				return true;
			}

			case "miningguild": {
				teleport(player, ServerTeleports.MINING_GUILD.getTeleportPos());
				return true;
			}

			case "mlm":
			case "motherloadmine": {
				teleport(player, ServerTeleports.MOTHERLOAD_MINE.getTeleportPos());
				return true;
			}

			case "amethyst": {
				teleport(player, ServerTeleports.AMETHYST_MINE.getTeleportPos());
				return true;
			}

			case "puropuro": {
				player.puropuroTravelledCounter++;
				if (player.puropuroTravelledCounter == Achievements.MIGHT_NEED_A_JAR_OR_TWO.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
							+ Achievements.MIGHT_NEED_A_JAR_OR_TWO.getAchievementName());
				teleport(player, ServerTeleports.PURO_PURO.getTeleportPos());
				return true;
			}

			case "ardy":
			case "ardougne": {
				teleport(player, ServerTeleports.ARDOUGNE.getTeleportPos());
				return true;
			}

			case "varrock": {
				teleport(player, ServerTeleports.VARROCK.getTeleportPos());
				return true;
			}

			case "prif":
			case "priff":
			case "prifddinas": {
				teleport(player, ServerTeleports.PRIFDDINAS.getTeleportPos());
				return true;
			}

			case "falador":
			case "fally": {
				teleport(player, ServerTeleports.FALADOR.getTeleportPos());
				return true;
			}

			case "lumbridge":
			case "lumby": {
				teleport(player, ServerTeleports.LUMBRIDGE.getTeleportPos());
				return true;
			}

			case "taverley": {
				teleport(player, ServerTeleports.TAVERLEY_DUNGEON.getTeleportPos());
				return true;
			}

			case "canifis": {
				teleport(player, ServerTeleports.CANIFIS.getTeleportPos());
				return true;
			}

			case "catherby": {
				teleport(player, ServerTeleports.CATHERBY_ALLOTMENT.getTeleportPos());
				return true;
			}

			case "pollnivneach": {
				teleport(player, ServerTeleports.POLLNIVNEACH.getTeleportPos());
				return true;
			}

			case "relleka": {
				teleport(player, ServerTeleports.RELLEKA.getTeleportPos());
				return true;
			}

			case "draynor": {
				teleport(player, ServerTeleports.DRAYNOR.getTeleportPos());
				return true;
			}

			case "alkharid": {
				teleport(player, ServerTeleports.AL_KHARID.getTeleportPos());
				return true;
			}

			case "gnomestronghold":
			case "stronghold": {
				teleport(player, ServerTeleports.GNOME_STRONGHOLD.getTeleportPos());
				return true;
			}

			case "tavdung": {
				teleport(player, ServerTeleports.TAVERLEY_DUNGEON.getTeleportPos());
				return true;
			}

			case "slayertower": {
				teleport(player, ServerTeleports.SLAYER_TOWER.getTeleportPos());
				return true;
			}

			case "strongholdcave": {
				teleport(player, ServerTeleports.STRONGHOLD_CAVE.getTeleportPos());
				return true;
			}

			case "catacombs": {
				teleport(player, ServerTeleports.KOUREND_CATACOMBS.getTeleportPos());
				return true;
			}

			case "fremslayer": {
				teleport(player, ServerTeleports.FREMENNIK_SLAYER_CAVE.getTeleportPos());
				return true;
			}

			case "mountkaruulm": {
				teleport(player, ServerTeleports.MOUNT_KARUULM.getTeleportPos());
				return true;
			}

			case "ancientcavern": {
				teleport(player, ServerTeleports.ANCIENT_CAVERN.getTeleportPos());
				return true;
			}

			case "fossilwyverns": {
				teleport(player, ServerTeleports.FOSSIL_ISLAND_WYVERN_CAVE.getTeleportPos());
				return true;
			}

			case "brimdung": {
				teleport(player, ServerTeleports.BRIMHAVEN_DUNGEON.getTeleportPos());
				return true;
			}

			case "darkbeasts": {
				teleport(player, ServerTeleports.DARK_BEAST.getTeleportPos());
				return true;
			}

			case "lumdung": {
				teleport(player, ServerTeleports.LUMMY_DUNGEON.getTeleportPos());
				return true;
			}

			case "ardydung": {
				teleport(player, ServerTeleports.ARDY_DUNGEON.getTeleportPos());
				return true;
			}

			case "lithkren": {
				teleport(player, ServerTeleports.LITHKREN.getTeleportPos());
				return true;
			}

			case "rockcrabs": {
				teleport(player, ServerTeleports.ROCK_CRABS.getTeleportPos());
				return true;
			}

			case "train":
			case "sandcrabs": {
				player.clanChatSpeeches++;
				if (player.clanChatSpeeches == 1)
					player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
							+ NewcomerTasks.MISSING_HOME.getFormattedName() + "!");

				teleport(player, ServerTeleports.SAND_CRABS.getTeleportPos());
				return true;
			}

			case "ammonitecrabs": {
				teleport(player, ServerTeleports.AMMONITE_CRABS.getTeleportPos());
				return true;
			}

			case "yaks": {
				teleport(player, ServerTeleports.YAKS.getTeleportPos());
				return true;
			}

			case "catalysts": {
				teleport(player, ServerTeleports.CATALYSTS.getTeleportPos());
				return true;
			}

			case "chickens": {
				teleport(player, ServerTeleports.CHICKENS.getTeleportPos());
				return true;
			}

			case "chaosdruids": {
				teleport(player, ServerTeleports.CHAOS_DRUID.getTeleportPos());
				return true;
			}

			case "experiments": {
				teleport(player, ServerTeleports.EXPERIMENTS.getTeleportPos());
				return true;
			}

			case "shamans": {
				teleport(player, ServerTeleports.SHAMANS.getTeleportPos());
				return true;
			}

			case "vyrewatches": {
				teleport(player, ServerTeleports.VYREWATCH_SENTINELS.getTeleportPos());
				return true;
			}

			case "birdhouse": {
				teleport(player, ServerTeleports.BIRDHOUSES.getTeleportPos());
				return true;
			}

			case "farmingguild": {
				teleport(player, ServerTeleports.FARMING_GUILD.getTeleportPos());
				return true;
			}

			case "smokedevil": {
				teleport(player, ServerTeleports.SMOKE_DEVIL.getTeleportPos());
				return true;
			}

			case "sire": {
				teleport(player, ServerTeleports.SIRE.getTeleportPos());
				return true;
			}

			case "zulrah": {
				teleport(player, ServerTeleports.ZULRAH.getTeleportPos());
				return true;
			}

			case "gwd":
			case "godwars": {
				teleport(player, 2882, 5311, 2);
				return true;
			}

			case "tob": {
				teleport(player, 3670, 3219, 0);
				return true;
			}

			case "cluearea":
			case "openclues": {
				teleport(player, 2649, 3159, 0);
				return true;
			}

			case "galvek": {
				player.dialogue(new OptionsDialogue("Which Galvek Lair would you like to enter?",
						new Option("Global Galvek Lair", () -> {
							ModernTeleport.teleport(player, ServerTeleports.GALVEK.getTeleportPos());
						}),
						new Option("Instanced Galvek Lair", () -> {
							player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GALVEK;
							player.getInstanceTokenInterface().startInstance(player, true);
						})));
				return true;
			}

			case "warriorsguild": {
				teleport(player, ServerTeleports.WARRIORS_GUILD.getTeleportPos());
				return true;
			}

			case "barrows": {
				teleport(player, ServerTeleports.BARROWS.getTeleportPos());
				return true;
			}

			case "fightcaves": {
				teleport(player, ServerTeleports.FIGHT_CAVES.getTeleportPos());
				return true;
			}

			case "lunar": {
				teleport(player, ServerTeleports.LUNAR_ISLE.getTeleportPos());
				return true;
			}

			case "mosleharmless": {
				teleport(player, ServerTeleports.MOS_LE_HARMLESS.getTeleportPos());
				return true;
			}

			case "burthorpe": {
				teleport(player, ServerTeleports.BURTHORPE.getTeleportPos());
				return true;
			}

			case "shilo": {
				teleport(player, ServerTeleports.SHILO_VILLAGE.getTeleportPos());
				return true;
			}

			case "yanille": {
				teleport(player, ServerTeleports.YANILLE.getTeleportPos());
				return true;
			}

			case "portsarim": {
				teleport(player, ServerTeleports.PORT_SARIM.getTeleportPos());
				return true;
			}

			case "brimhaven": {
				teleport(player, ServerTeleports.BRIMHAVEN.getTeleportPos());
				return true;
			}

			case "apeatoll": {
				teleport(player, ServerTeleports.APE_ATOLL.getTeleportPos());
				return true;
			}

			case "inferno": {
				teleport(player, ServerTeleports.INFERNO.getTeleportPos());
				return true;
			}

			case "pestcontrol": {
				teleport(player, ServerTeleports.PEST_CONTROL.getTeleportPos());
				return true;
			}

			case "malakar": {
				teleport(player, DonationBossHandler.malakarTeleportPosition);
				return true;
			}

			case "wt":
			case "wintertodt": {
				teleport(player, ServerTeleports.WINTERTODT.getTeleportPos());
				return true;
			}

			case "tempoross": {
				teleport(player, ServerTeleports.TEMPOROSS.getTeleportPos());
				return true;
			}

			case "chaosele":
			case "ele": {
				teleportDangerous(player, 3262, 3919, 0);
				return true;
			}

			case "jv":
			case "justvisiting": {
				teleport(player, 3296, 9436, 0);
				return true;
			}

			case "yell": {
				boolean shadow = false;
				if (Punishment.isMuted(player)) {
					if (!player.shadowMute) {
						player.sendMessage("You're muted and can't talk.");
						return true;
					}
					shadow = true;
				}
				String message;
				String title = "";
				if (query.length() < 5 || (message = query.substring(5).trim()).isEmpty()) {
					player.sendMessage("You can't yell an empty message.");
					return true;
				}

				message = BadWords.filterBadWords(message);

				long ms = System.currentTimeMillis();
				long delay = player.yellDelay - ms;
				boolean canYell = player.isDonator() || player.isSuperDonator() || player.isEliteDonator()
						|| player.isNobleDonator() || player.isGoldDonator() || player.isPlatinumDonator()
						|| player.isLegendaryDonator() || player.isSupremeDonator() || player.isAdmin() || player.isSupport()
						|| player.isManager() || player.isModerator();
				if (!canYell) {
					player.sendMessage("You must be a donator to yell!");
					return true;
				}
				if (delay > 0) {
					long seconds = delay / 1000L;
					if (seconds <= 1)
						player.sendMessage("You need to wait 1 more second before yelling again.");
					else
						player.sendMessage("You need to wait " + seconds + " more seconds before yelling again.");
					return true;
				}
				boolean bypassFilter = false;
				int delaySeconds = 60;
				if (player.isAdmin() || player.isSupport() || player.isManager() || player.isModerator()) {
					bypassFilter = true;
					delaySeconds = 0;
				} else if (player.isDonator()) {
					bypassFilter = false;
					delaySeconds = 0;

				}
				if (player.isGroup(PlayerGroup.OWNER)) {
					message = Color.BLACK.wrap("<shad><img=1>[Owner] ") + Color.VIOLET.wrap(title) + "<shad>"
							+ Color.BLACK.wrap(player.getName() + ":") + " " + Color.BLACK.wrap(message);
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = "<img=97>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = "<img=94>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = "<img=102>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = "<img=95>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = "<img=96>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = "<img=91>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = "<img=98>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = "<img=90>" + message;
					}
				} else if (player.isGroup(PlayerGroup.DEVELOPER)) {
					message = Color.DEV_YELL.wrap("<shad><img=80>[Developer] ") + Color.DEV_YELL.wrap(title) + "<shad>"
							+ Color.DEV_YELL.wrap(player.getName() + ":") + " " + Color.DEV_YELL.wrap(message);
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = "<img=97>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = "<img=94>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = "<img=102>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = "<img=95>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = "<img=96>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = "<img=91>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = "<img=98>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = "<img=90>" + message;
					}
				} else if (player.isGroup(PlayerGroup.ADMINISTRATOR)) {
					message = Color.PURPLE.wrap("<shad><img=1>[Head Admin] ") + Color.PURPLE.wrap(title) + "<shad>"
							+ Color.PURPLE.wrap(player.getName() + ":") + " " + Color.PURPLE.wrap(message);
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = "<img=97>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = "<img=94>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = "<img=102>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = "<img=95>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = "<img=96>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = "<img=91>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = "<img=98>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = "<img=90>" + message;
					}
				} else if (player.isGroup(PlayerGroup.COMMUNITY_ADMIN)) {
					message = Color.PURPLE.wrap("<shad><img=1>[Administrator] ") + Color.PURPLE.wrap(title) + "<shad>"
							+ Color.PURPLE.wrap(player.getName() + ":") + " " + Color.PURPLE.wrap(message);
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = "<img=97>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = "<img=94>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = "<img=102>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = "<img=95>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = "<img=96>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = "<img=91>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = "<img=98>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = "<img=90>" + message;
					}
				} else if (player.isGroup(PlayerGroup.HEAD_MODERATOR)) {
					message = Color.CANDY_PINK.wrap(("<shad=000000>") + "<img=0>[Head Mod] " + ("<shad=000000>")
							+ Color.CANDY_PINK.wrap(title) + ("<shad=000000>") + Color.CANDY_PINK.wrap(player.getName() + ":") + " "
							+ ("<shad=000000>") + Color.CANDY_PINK.wrap(message));
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = "<img=97>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = "<img=94>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = "<img=102>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = "<img=95>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = "<img=96>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = "<img=91>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = "<img=98>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = "<img=90>" + message;
					}
				} else if (player.isGroup(PlayerGroup.MODERATOR)) {
					message = Color.MOD_YELL.wrap(("<shad=000000>") + "<img=0>[Moderator] " + ("<shad=000000>")
							+ Color.MOD_YELL.wrap(title) + ("<shad=000000>") + Color.MOD_YELL.wrap(player.getName() + ":") + " "
							+ ("<shad=000000>") + Color.MOD_YELL.wrap(message));
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = "<img=97>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = "<img=94>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = "<img=102>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = "<img=95>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = "<img=96>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = "<img=91>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = "<img=98>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = "<img=90>" + message;
					}
				} else if (player.isGroup(PlayerGroup.SUPPORT)) {
					message = Color.COOL_BLUE.wrap("<shad=000000><img=101>[Support] ") + Color.COOL_BLUE.wrap(title)
							+ "<shad=000000>" + Color.COOL_BLUE.wrap(player.getName() + ":") + " " + Color.COOL_BLUE.wrap(message);
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = "<img=97>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = "<img=94>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = "<img=102>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = "<img=95>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = "<img=96>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = "<img=91>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = "<img=98>" + message;
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = "<img=90>" + message;
					}
				} else {
					if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
						message = Color.ORANGE2.wrap("<shad><img=97>[Supreme Donator] ") + Color.ORANGE2.wrap(title) + "<shad>"
								+ Color.ORANGE2.wrap(player.getName() + ":") + " " + Color.ORANGE2.wrap(message);
					} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
						message = Color.MAGENTA.wrap("<shad><img=94>[Legendary Donator] ") + Color.MAGENTA.wrap(title) + "<shad>"
								+ Color.MAGENTA.wrap(player.getName() + ":") + " " + Color.MAGENTA.wrap(message);
					} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
						message = Color.ONYX.wrap("<shad><img=102>[Platinum Donator] ") + Color.ONYX.wrap(title) + "<shad>"
								+ Color.ONYX.wrap(player.getName() + ":") + " " + Color.ONYX.wrap(message);
					} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
						message = Color.GOLD2.wrap("<shad><img=95>[Gold Donator] ") + Color.GOLD2.wrap(title) + "<shad>"
								+ Color.GOLD2.wrap(player.getName() + ":") + " " + Color.GOLD2.wrap(message);
					} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
						message = Color.NOBLE.wrap("<shad><img=96>[Noble Donator] ") + Color.NOBLE.wrap(title) + "<shad>"
								+ Color.NOBLE.wrap(player.getName() + ":") + " " + Color.NOBLE.wrap(message);
					} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
						message = Color.GREEN.wrap("<shad><img=91>[Elite Donator] ") + Color.GREEN.wrap(title) + "<shad>"
								+ Color.GREEN.wrap(player.getName() + ":") + " " + Color.GREEN.wrap(message);
					} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
						message = Color.BABY_BLUE.wrap("<shad><img=98>[Super Donator] ") + Color.BABY_BLUE.wrap(title) + "<shad>"
								+ Color.BABY_BLUE.wrap(player.getName() + ":") + " " + Color.BABY_BLUE.wrap(message);
					} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
						message = Color.LIGHT_RED.wrap("<shad><img=90>[Donator] ") + Color.LIGHT_RED.wrap(title) + "<shad>"
								+ Color.LIGHT_RED.wrap(player.getName() + ":") + " " + Color.LIGHT_RED.wrap(message);
					}
				}

				if (player.getGameMode().isIronMan()) {
					if (player.getGameMode().isHardcoreIronman()) {
						message = "<img=10>" + message;
					} else if (player.getGameMode().isUltimateIronman()) {
						message = "<img=3>" + message;
					} else if (player.getGameMode().isGroupIronman()) {
						message = "<img=41>" + message;
					} else if (player.getGameMode().isHardcoreGroupIronman()) {
						message = "<img=42>" + message;
					} else {
						message = "<img=2>" + message;
					}
				}

				if (player.titleId != -1 && player.titleId < Title.PRESET_TITLES.length) {
					title = Title.PRESET_TITLES[player.titleId].getPrefix();
					if (player.titleId == 22) {
						title = player.customTitle;
					}
				}

				player.yellDelay = ms + (delaySeconds * 1000L);
				if (shadow) {
					player.getPacketSender().sendMessage(message, player.getName(), 105);
				}

				for (Player p : World.players()) {
					if (!bypassFilter && p.yellFilter && p.getUserId() != player.getUserId())
						continue;
					p.getPacketSender().sendMessage(message, player.getName(), 105);
				}
				Loggers.logYell(player.getUserId(), player.getName(), player.getIp(), message);
				return true;
			}

			/*
			 * case "gamble" : {
			 * long playtime = player.playTime * Server.tickMs();
			 * long days = TimeUnit.MILLISECONDS.toDays(playtime);
			 * if(days < 7) {
			 * player.sendMessage("You are not experienced enough to gamble.");
			 * return false;
			 * }
			 * teleport(player, 1371, 2529, 1);
			 * return true;
			 * }
			 */

			case "dz":
			case "donatorzone":
			case "donatorszone":
			case "donorzone":
			case "dzone": {
				if (player.isDonator()) {
					teleport(player, 1248, 2604, 2);
					HomeHandler.drinkFromPool(player);
				} else {
					player.sendMessage("You need to be a donator to use the donator zone.");
				}
				return true;
			}

			case "dzd": {
				if (player.isDonator()) {
					teleport(player, 1232, 2567, 2);
					HomeHandler.drinkFromPool(player);
				} else {
					player.sendMessage("You need to be a donator to use the donator zone.");
				}
				return true;
			}

			case "ge": {
				teleport(player, 3162, 3486, 0);
				return true;
			}

			case "volcano": {
				teleportDangerous(player, 3367, 3929, 0);
				return true;
			}

			case "arena":
			case "stake":
			case "duel":
			case "da": {
				teleport(player, 3367, 3265, 0);
				return true;
			}

			case "magebank":
			case "mb": {
				teleport(player, 2539, 4716, 0);
				return true;
			}

			case "revs": {
				teleportDangerous(player, 3127, 3832, 0);
				return true;
			}

			case "easts": {
				teleportDangerous(player, 3364, 3666, 0);
				return true;
			}

			case "wests": {
				teleportDangerous(player, 2983, 3598, 0);
				return true;
			}

			case "50":
			case "50s": {
				teleportDangerous(player, 3314, 3912, 0);
				return true;
			}

			case "44":
			case "44s": {
				teleportDangerous(player, 2972, 3869, 0);
				return true;
			}

			case "chins": {
				teleportDangerous(player, 3129, 3777, 0);
				return true;
			}

			case "graves": {
				teleportDangerous(player, 3143, 3677, 0);
				return true;
			}

			case "lms": {
				teleport(player, 3405, 3178, 0);
				return true;
			}

			case "wraths": {
				teleport(player, 2335, 4828, 0);
				return true;
			}

			case "char": {
				if (!player.getEquipment().isEmpty()) {
					player.dialogue(new MessageDialogue("Please remove what your equipment before doing that."));
					return true;
				}
				player.openInterface(ToplevelComponent.MAINMODAL, Interface.MAKE_OVER_MAGE);
				return true;
			}

			case "wyverns":
			case "wyvern": {
				teleport(player, 3604, 10230, 0);
				return true;
			}

			case "skull": {
				if (!player.getCombat().isDead())
					EmblemTrader.skull(player);
				return true;
			}

			case "staff":
			case "staffonline": {
				List<String> text = new LinkedList<>();
				List<String> admin = new LinkedList<>();
				List<String> admins = new LinkedList<>();
				List<String> mods = new LinkedList<>();
				List<String> slaves = new LinkedList<>();
				World.players().forEach(p -> {
					// TODO: If admin has PMs off, ignore
					if (p.isAdmin())
						admin.add(p.getName());
					else if (p.isManager())
						admins.add(p.getName());
					else if (p.isModerator())
						mods.add(p.getName());
					else if (p.isSupport())
						slaves.add(p.getName());
				});

				text.add("<img=1><col=bbbb00><shad=0000000> Admins</col></shad>");
				if (admin.isEmpty())
					text.add("None online!");
				else
					text.addAll(admin);
				text.add("");

				text.add("<img=1><col=bbbb00><shad=0000000> Community Admins</col></shad>");
				if (admins.isEmpty())
					text.add("None online!");
				else
					text.addAll(admins);
				text.add("");

				text.add("<img=0><col=b2b2b2><shad=0000000> Moderators<col></shad>");
				if (mods.isEmpty())
					text.add("None online!");
				else
					text.addAll(mods);
				text.add("");

				text.add("<img=18><col=5bccc4><shad=0000000> Supports</col></shad>");
				if (slaves.isEmpty())
					text.add("None online!");
				else
					text.addAll(slaves);

				player.sendScroll("Staff Online", text.toArray(new String[0]));
				return true;
			}

			case "players":
			case "online": {
				List<String> text = new LinkedList<>();
				List<String> players = new LinkedList<>();
				Iterable<Player> worldPlayers = World.players();
				if (worldPlayers != null) {
					worldPlayers.forEach(p -> {
						String playerName = (p.getName() != null) ? p.getName() : "Unknown Players";
						players.add(playerName);
					});
				}
				text.add("<col=ff0000><shad=0000000>Players Online:</col></shad> " + players.size());

				int maxPlayersToDisplay = 100;
				if (!players.isEmpty()) {
					for (int i = 0; i < Math.min(players.size(), maxPlayersToDisplay); i++) {
						text.add(players.get(i));
					}
				}
				if (players.size() > maxPlayersToDisplay) {
					text.add("...and " + (players.size() - maxPlayersToDisplay) + " more players.");
				}
				try {
					player.sendScroll("Players Online", text.toArray(new String[0]));
				} catch (Exception e) {
					e.printStackTrace();
					player.sendMessage("Error displaying the player list.");
				}
				return true;
			}

			case "rules": {
				player.openUrl(World.type.getWorldName().concat(" Rules"),
						"https://discord.com/channels/1150539550561681508/1150539551027249155");
				return true;
			}

			case "reasonwiki":
			case "wiki": {
				player.openUrl(World.type.getWorldName() + " Wiki", "https://reasonps.fandom.com/wiki/ReasonPS_Wiki");
				return true;
			}
			case "vote": {
				String username = player.getName().replace(" ", "-");
				String url = "https://reasonps.com/vote/" + username;
				player.openUrl(World.type.getWorldName() + " Vote", url);
				return true;
			}

			case "sipsick": {
				player.openUrl("Sipsick", "https://www.youtube.com/@sips1ck");
				return true;
			}
			case "eggy": {
				player.openUrl("Eggy", "https://www.youtube.com/@EggyRS");
				return true;
			}
			case "wizard":
			case "wetwizard": {
				player.openUrl("Wet Wizard", "https://www.youtube.com/@WetWizard");
				return true;
			}
			case "effigy": {
				player.openUrl("Effigy Swiper", "https://www.youtube.com/@effigyswiper");
				return true;
			}
			case "smoothie": {
				player.openUrl("Smoothie RSPS", "https://www.youtube.com/@smoothiersps7941");
				return true;
			}
			case "ruben": {
				player.openUrl("RubenRSPS", "https://www.youtube.com/@RubenRSPS");
				return true;
			}
			case "rspsguy": {
				player.openUrl("RSPSGuy", "https://www.youtube.com/@RSPSguy");
				return true;
			}
			case "slapped": {
				player.openUrl("Slapped", "https://www.youtube.com/@SLAPPEDRSPS");
				return true;
			}
			case "bonkloots": {
				player.openUrl("Bonkloots", "https://www.youtube.com/@Bonkloots");
				return true;
			}
			case "didy": {
				player.openUrl("Didyscape", "https://www.youtube.com/@DidyScape");
				return true;
			}
			case "walkchaos": {
				player.openUrl("Walkchaos", "https://www.youtube.com/@Walkchaos");
				return true;
			}
			case "zig": {
				player.openUrl("PkedByZig", "https://www.youtube.com/@pkedbyzigrsps663");
				return true;
			}

			case "highscores":
			case "hiscores": {
				player.openUrl(World.type.getWorldName() + " Hiscores", "https://reasonps.com/hiscores");
				return true;
			}

			case "donate":
			case "store": {
				player.openUrl(World.type.getWorldName() + " Store", "https://reasonps.com/store");
				return true;
			}

			case "changepass": {
				if (query.length() <= command.length() + 1) {
					player.sendMessage("Please use ::changepass newpassword");
					return true;
				}
				String password = query.substring(command.length() + 1);

				if (password.length() > 20) {
					player.sendMessage("Your password can only be a maximum of 20 characters long.");
					return true;
				}

				player.password = password;
				player.sendMessage("You have changed your password to " + password + "!");
				return true;
			}

		}
		return false;

	}
}
