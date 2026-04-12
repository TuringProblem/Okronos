package io.ruin.model.activities.raids.toa;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.RaidingRestorations;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.raids.toa.rooms.*;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.util.*;

public class ToAObjects {

	private static boolean allRoomsCompleted(Player player) {
		if (player.getCurrentToARaid() == null)
			return false;
		if (player.getName().equalsIgnoreCase("Kal") || player.getName().equalsIgnoreCase("rogero47")
				|| player.getName().equalsIgnoreCase("Deathless"))
			return true;
		return player.getCurrentToARaid().getRooms().get("baba").roomCompleted &&
				player.getCurrentToARaid().getRooms().get("zebak").roomCompleted &&
				player.getCurrentToARaid().getRooms().get("kephri").roomCompleted &&
				player.getCurrentToARaid().getRooms().get("akkha").roomCompleted;
	}

	private static void openToaParties(Player player, GameObject gameObject) {
		player.getToaPartyInterface().open(player);

	}

	private static void enterBabaRoom(Player player, GameObject gameObject) {
		if (player.getCurrentToARaid().getRooms().get("baba").roomCompleted) {
			player.dialogue(new PlayerDialogue("This room has already been completed."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom == null
				&& !player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName())) {
			player.dialogue(new PlayerDialogue("The party leader must enter the room first."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom != null
				&& !player.getCurrentToARaid().currentRoom.equalsIgnoreCase("baba")) {
			player.dialogue(new PlayerDialogue("You must enter the same room the party leader did."));
			return;
		}
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName()))
				player.getCurrentToARaid().currentRoom = "baba";
			player.currentToaEnterPosition = player.getPosition().copy();
			player.getMovement().teleport(player.getCurrentToARaid().getRooms().get("baba").getEnterPosition());
		}
	}

	private static void useZebakStairs(Player player, GameObject obj) {
		if (player.getPosition().getY() < obj.getPosition().getY()) {
			player.sendMessage("You can't reach that.");
			return;
		}
		player.getMovement().teleport(player.getPosition().getRegion().baseX + 30,
				player.getPosition().getRegion().baseY + 40, 0);
	}

	private static void enterRewardsRoom(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			player.dialogue(new OptionsDialogue("Are you sure you want to enter the rewards room?",
					new Option("Yes, I'm ready.", () -> {
						player.tombsOfAmascutKills.messageOnKill();
						player.tombsOfAmascutKills.increment(player);
						player.currentToaEnterPosition = player.getPosition().copy();
						player.getMovement().teleport(player.getCurrentToARaid().getRooms().get("reward").getEnterPosition());
						if (player.getCurrentToARaid() != null) {
							if (player.getCurrentToARaid().totalDeaths == 0
									&& player.getCurrentToARaid().invocations.contains(Invocations.ON_A_DIET)
									&& player.getCurrentToARaid().invocations.contains(Invocations.DEHYDRATION)) {
								Objects.requireNonNull(player.combatAchievementsList
										.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.YOU_ARE_NOT_PREPARED.ordinal()))
										.getCombatAchievement()).check(player);
							}
						}
						calculateLoot(player, player.getCurrentToARaid().getInvocationLevel());
					}),
					new Option("Nevermind.")));
		}
	}

	private static List<Integer> uniqueDrops = Arrays.asList(
			27277,
			27226,
			27229,
			27232,
			25985,
			26219,
			25975,
			27248);

	public static int containerId = 45;

	public static void openLootSarcophagus(Player player, GameObject object) {
		if (player.toaReward == null || player.toaReward.isEmpty()) {
			player.dialogue(new PlayerDialogue("You have no loot to claim."));
			return;
		}
		Map<Integer, Integer> lootMap = new HashMap<>();
		for (Item item : player.toaReward) {
			if (lootMap.containsKey(item.getId())) {
				lootMap.put(item.getId(), lootMap.get(item.getId()) + item.getAmount());
			} else {
				lootMap.put(item.getId(), item.getAmount());
			}
		}
		player.toaReward.clear();

		for (Map.Entry<Integer, Integer> entry : lootMap.entrySet()) {
			player.toaReward.add(new Item(entry.getKey(), entry.getValue()));
		}

		player.openInterface(ToplevelComponent.MAINMODAL, 771);
		player.getPacketSender().sendItems(
				-1,
				10,
				ToAObjects.containerId,
				player.toaReward.toArray(new Item[0]));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				771 << 16 | 10, ToAObjects.containerId,
				2, 4, 1, -1, "", "", "", "", "");
		containerId++;
	}

	private static void enterElidinisRoom(Player player, GameObject gameObject) {
		if (!allRoomsCompleted(player) && !player.getName().equalsIgnoreCase("Kal1234")) {
			player.dialogue(new PlayerDialogue("You must complete all rooms before entering the final boss room."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom == null
				&& !player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName())) {
			player.dialogue(new PlayerDialogue("The party leader must enter the room first."));
			return;
		}
		if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {
			player.getCurrentToARaid()
					.scaleNPC(((TumekenWardenRoom) player.getCurrentToARaid().getRooms().get("wardensp2")).getTumekenWarden(), 4);
			player.getCurrentToARaid().scaleNPC(
					((ElidinisWardenRoom) player.getCurrentToARaid().getRooms().get("wardensp1")).getElidinisWarden(), 8);
		}
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName()))
				player.getCurrentToARaid().currentRoom = "wardensp1";
			player.currentToaEnterPosition = player.getPosition().copy();
			player.getMovement().teleport(player.getCurrentToARaid().getRooms().get("wardensp1").getEnterPosition());
		}
	}

	private static void enterZebakRoom(Player player, GameObject gameObject) {
		if (player.getCurrentToARaid().getRooms().get("zebak").roomCompleted) {
			player.dialogue(new PlayerDialogue("This room has already been completed."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom == null
				&& !player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName())) {
			player.dialogue(new PlayerDialogue("The party leader must enter the room first."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom != null
				&& !player.getCurrentToARaid().currentRoom.equalsIgnoreCase("zebak")) {
			player.dialogue(new PlayerDialogue("You must enter the same room the party leader did."));
			return;
		}
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			System.out.println("leader: " + player.getCurrentToARaid().currentParty.getLeader());
			if (player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName()))
				player.getCurrentToARaid().currentRoom = "zebak";
			System.out.println("current room: " + player.getCurrentToARaid().currentRoom);
			player.currentToaEnterPosition = player.getPosition().copy();
			player.getMovement().teleport(player.getCurrentToARaid().getRooms().get("zebak").getEnterPosition());
		}
	}

	private static void movePlayerToLobby(Player player) {
		player.getMovement().teleport(new Position(3359, 9113, 0));
		TombsOfAmascut.confiscateItems(player);
		player.deathEndListener = null;
		player.deathStartListener = null;
		player.teleportListener = null;
		player.currentToaEnterPosition = null;
		player.currentToARaidId = -1;
		player.getEquipment().sendUpdates();
		player.getCombat().init(player);

	}

	public static void leaveRaid(Player player, GameObject gameObject) {
		if (player.getCurrentToARaid() == null) {
			movePlayerToLobby(player);
			return;
		}
		if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {
			// TODO: Make a player within the raid leader instead of disbanding the raid
			player.dialogue(new OptionsDialogue("Are you sure you want to disband the raid? (This will remove everyone)",
					new Option("Yes, I'm sure.", () -> {
						var raid = player.getCurrentToARaid();
						raid.currentParty.getMembers().forEach(name -> {
							Player plr = World.getPlayer(name);
							if (plr != null)
								movePlayerToLobby(plr);
						});
						player.currentToaEnterPosition = null;
						player.currentToARaidId = -1;
						player.getEquipment().sendUpdates();

						raid.destroyMaps(player);
						TombsOfAmascutManager.getRaidParty(player).started = false;
						TombsOfAmascutManager.removeRaid(raid);
					}),
					new Option("Nevermind.")));
		} else {
			player.dialogue(new OptionsDialogue("Are you sure you want to leave the raid?",
					new Option("Yes, I'm sure.", () -> {
						TombsOfAmascut.confiscateItems(player);
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().currentParty.removeMember(player.getName());

						player.getMovement().teleport(new Position(3359, 9113, 0));
						player.deathEndListener = null;
						player.deathStartListener = null;
						player.teleportListener = null;
						player.currentToaEnterPosition = null;
						player.currentToARaidId = -1;
					}),
					new Option("Nevermind.")));
		}
	}

	private static void enterKephriRoom(Player player, GameObject gameObject) {
		if (player.getCurrentToARaid().getRooms().get("kephri").roomCompleted) {
			player.dialogue(new PlayerDialogue("This room has already been completed."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom == null
				&& !player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName())) {
			player.dialogue(new PlayerDialogue("The party leader must enter the room first."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom != null
				&& !player.getCurrentToARaid().currentRoom.equalsIgnoreCase("kephri")) {
			player.dialogue(new PlayerDialogue("You must enter the same room the party leader did."));
			return;
		}
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName()))
				player.getCurrentToARaid().currentRoom = "kephri";
			player.currentToaEnterPosition = player.getPosition().copy();
			player.getMovement().teleport(player.getCurrentToARaid().getRooms().get("kephri").getEnterPosition());
		}
	}

	private static void enterAkkhaRoom(Player player, GameObject gameObject) {
		if (player.getCurrentToARaid().getRooms().get("akkha").roomCompleted) {
			player.dialogue(new PlayerDialogue("This room has already been completed."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom == null
				&& !player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName())) {
			player.dialogue(new PlayerDialogue("The party leader must enter the room first."));
			return;
		}
		if (player.getCurrentToARaid().currentRoom != null
				&& !player.getCurrentToARaid().currentRoom.equalsIgnoreCase("akkha")) {
			player.dialogue(new PlayerDialogue("You must enter the same room the party leader did."));
			return;
		}
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName()))
				player.getCurrentToARaid().currentRoom = "akkha";
			player.currentToaEnterPosition = player.getPosition().copy();
			player.getMovement().teleport(player.getCurrentToARaid().getRooms().get("akkha").getEnterPosition());
		}
	}

	private static void enterLobby(Player player, GameObject gameObject, boolean fromNPC) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			if (player.currentToaEnterPosition != null) {
				if (player.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(player.getName())) {
					player.getCurrentToARaid().currentRoom = null;
					if (!fromNPC) {
						player.getCurrentToARaid().currentParty.getMembers().forEach(p -> {
							Player plr = World.getPlayer(p);
							if (plr.getCurrentToARaid() == null) {
								return;
							}
							plr.getCurrentToARaid().playerInRoom.remove(plr);
							var target = plr.currentToaEnterPosition == null ? player.currentToaEnterPosition : plr.currentToaEnterPosition;
							plr.getMovement().teleport(target);
						});
					}
				}
				player.getCurrentToARaid().playerInRoom.remove(player);
				player.getMovement().teleport(player.currentToaEnterPosition);
			}
		}
	}

	private static void fastStartZebakFight(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.getCurrentToARaid().playerInRoom.add(player);
			player.getCurrentToARaid().setDeathPosition(player.getPosition().copy());
			player.getMovement().teleport(
					new Position(player.getPosition().getX() - 14, player.getPosition().getY(), player.getPosition().getZ()));
		}
	}

	private static void startZebakFightWithWarning(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.dialogue(new OptionsDialogue("Are you sure you want to teleport into the boss arena?",
					new Option("Yes, I'm ready.", () -> {
						fastStartZebakFight(player, gameObject);
					}),
					new Option("Nevermind.")));
		}
	}

	private static void fastStartAkkhaFight(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.getCurrentToARaid().playerInRoom.add(player);
			player.getCurrentToARaid().setDeathPosition(player.getPosition().copy());
			player.getMovement().teleport(new Position(player.getPosition().getRegion().baseX + 41,
					player.getPosition().getRegion().baseY + 32, player.getPosition().getZ()));
		}
	}

	private static void startAkkhaFightWithWarning(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.dialogue(new OptionsDialogue("Are you sure you want to teleport into the boss arena?",
					new Option("Yes, I'm ready.", () -> {
						fastStartAkkhaFight(player, gameObject);
					}),
					new Option("Nevermind.")));
		}
	}

	private static void fastStartKephriFight(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.getCurrentToARaid().playerInRoom.add(player);
			player.getCurrentToARaid().setDeathPosition(player.getPosition().copy());
			player.getMovement().teleport(new Position(player.getPosition().getRegion().baseX + 24,
					player.getPosition().getRegion().baseY + 32, player.getPosition().getZ()));
		}
	}

	private static void startKephriFightWithWarning(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.dialogue(new OptionsDialogue("Are you sure you want to teleport into the boss arena?",
					new Option("Yes, I'm ready.", () -> {
						fastStartKephriFight(player, gameObject);
					}),
					new Option("Nevermind.")));
		}
	}

	private static void startElidinisFightWithWarning(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.dialogue(new OptionsDialogue("Are you sure you want to teleport into the boss arena?",
					new Option("Yes, I'm ready.", () -> {
						fastStartElidinisFight(player, gameObject);
					}),
					new Option("Nevermind.")));
		}
	}

	private static void fastStartElidinisFight(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.getCurrentToARaid().playerInRoom.add(player);
			player.getCurrentToARaid().setDeathPosition(player.getPosition().copy());
			player.getMovement().teleport(new Position(player.getPosition().getRegion().baseX + 32,
					player.getPosition().getRegion().baseY + 45, player.getPosition().getZ()));
		}
	}

	private static void fastStartBabaFight(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.getCurrentToARaid().playerInRoom.add(player);
			player.getCurrentToARaid().setDeathPosition(player.getPosition().copy());
			player.getMovement().teleport(new Position(player.getPosition().getRegion().baseX + 23,
					player.getPosition().getRegion().baseY + 32, player.getPosition().getZ()));
		}
	}

	static boolean calculateHelpfulSpiritLoot(Player player) {

		if (!player.toaChaosLoot.isEmpty()) {
			return false;
		}
		double lootMultiplier = 1.0;
		if (player.getCurrentToARaid().getInvocations().contains(Invocations.NEED_LESS_HELP)) {
			lootMultiplier = 0.66;
		} else if (player.getCurrentToARaid().getInvocations().contains(Invocations.NEED_SOME_HELP)) {
			lootMultiplier = 0.33;
		} else if (player.getCurrentToARaid().getInvocations().contains(Invocations.NO_HELP_NEEDED)) {
			lootMultiplier = 0.1;
		}
		/*
		 * Life
		 */
		int lifeBrews = Random.get(9, 14);
		int lifeRestores = Random.get(4, 9);
		int lifeAmbrosia = Random.get(1) == 0 ? 1 : 2;
		if (player.getCurrentToARaid().getInvocations().contains(Invocations.NEED_SOME_HELP)) {
			lifeAmbrosia = Random.get(1);
		}
		if (player.getCurrentToARaid().getInvocations().contains(Invocations.NEED_LESS_HELP)) {
			lifeAmbrosia = Random.get(1);
		}
		lifeBrews = (int) (lifeBrews * lootMultiplier);
		lifeRestores = (int) (lifeRestores * lootMultiplier);
		if (lifeBrews < 1)
			lifeBrews = 1;
		if (lifeRestores < 1)
			lifeRestores = 1;
		player.toaLifeLoot.add(new Item(27327, lifeRestores));
		player.toaLifeLoot.add(new Item(27315, lifeBrews));
		if (lifeAmbrosia > 0)
			player.toaLifeLoot.add(new Item(27347, lifeAmbrosia));
		if (!player.getCurrentToARaid().getInvocations().contains(Invocations.ON_A_DIET)) {
			player.toaLifeLoot.add(new Item(27323, 1));
			player.toaLifeLoot.add(new Item(27335, 1));
		}

		/*
		 * Chaos
		 */
		int chaosBrews = Random.get(5, 11);
		int chaosRestores = Random.get(4, 8);
		int chaosSmellingSalts = Random.get(1);
		int chaosAmbrosia = 1;
		if (player.getCurrentToARaid().getInvocations().contains(Invocations.NEED_SOME_HELP)) {
			chaosAmbrosia = Random.get(1);
		}
		if (player.getCurrentToARaid().getInvocations().contains(Invocations.NEED_LESS_HELP)) {
			chaosAmbrosia = Random.get(1);
		}
		if (player.getCurrentToARaid().getInvocations().contains(Invocations.NO_HELP_NEEDED)) {
			chaosAmbrosia = 0;
		}
		chaosBrews = (int) (chaosBrews * lootMultiplier);
		chaosRestores = (int) (chaosRestores * lootMultiplier);
		if (chaosBrews < 1)
			chaosBrews = 1;
		if (chaosRestores < 1)
			chaosRestores = 1;
		player.toaChaosLoot.add(new Item(27329, chaosRestores));
		player.toaChaosLoot.add(new Item(27317, chaosBrews));
		if (chaosSmellingSalts > 0)
			player.toaChaosLoot.add(new Item(27343, chaosSmellingSalts));

		if (chaosAmbrosia > 0)
			player.toaChaosLoot.add(new Item(27347, chaosAmbrosia));

		/*
		 * Power
		 */
		player.toaPowerLoot.add(new Item(27343, Random.get(1, 2)));
		player.toaPowerLoot.add(new Item(27339, Random.get(1, 2)));
		player.setToaSuppliesClaimed(player.getToaSuppliesClaimed() + 1);

		return true;

	}

	private static double getPetDonatorBoost(Player player) {
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

	private static void calculateLoot(Player player, int raidInvocationValue) {
		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMBS_OF_AMASCUT_ADEPT.ordinal()))
				.getCombatAchievement()).check(player);
		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMBS_OF_AMASCUT_CHAMPION.ordinal()))
				.getCombatAchievement()).check(player);
		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMBS_OF_AMASCUT_NOVICE.ordinal()))
				.getCombatAchievement()).check(player);
		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMB_LOOTER.ordinal()))
				.getCombatAchievement()).check(player);
		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMB_RAIDER.ordinal()))
				.getCombatAchievement()).check(player);
		if (raidInvocationValue >= 50) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.MOVIN_ON_UP.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (raidInvocationValue >= 100) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CONFIDENT_RAIDER.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (raidInvocationValue >= 450 && player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().totalDeaths == 0) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.EXPERT_RAIDER.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (raidInvocationValue == 590 && player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().totalDeaths == 0) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.AMASCUTS_REMNANT.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (player.getCurrentToARaid() != null && player.getCurrentToARaid().totalDeaths == 0) {
			if (player.getCurrentToARaid().invocations.contains(Invocations.OVERCLOCKED)
					&& player.getCurrentToARaid().invocations.contains(Invocations.OVERCLOCKED2)
					&& player.getCurrentToARaid().invocations.contains(Invocations.JUNGLE_JAPES)
					&& player.getCurrentToARaid().invocations.contains(Invocations.KEEP_BACK)
					&& player.getCurrentToARaid().invocations.contains(Invocations.BOULDERDASH)
					&& player.getCurrentToARaid().invocations.contains(Invocations.SHAKING_THINGS_UP)
					&& player.getCurrentToARaid().invocations.contains(Invocations.STAY_VIGILANT)
					&& player.getCurrentToARaid().invocations.contains(Invocations.MIND_THE_GAP)
					&& player.getCurrentToARaid().invocations.contains(Invocations.DOUBLE_TROUBLE)
					&& player.getCurrentToARaid().invocations.contains(Invocations.FEELING_SPECIAL)
					&& player.getCurrentToARaid().invocations.contains(Invocations.AERIAL_ASSAULT)
					&& player.getCurrentToARaid().invocations.contains(Invocations.LIVELY_LARVAE)
					&& player.getCurrentToARaid().invocations.contains(Invocations.MEDIC)
					&& player.getCurrentToARaid().invocations.contains(Invocations.MORE_OVERLORDS)
					&& player.getCurrentToARaid().invocations.contains(Invocations.INSANITY)
					&& player.getCurrentToARaid().invocations.contains(Invocations.ACCELERATION)
					&& player.getCurrentToARaid().invocations.contains(Invocations.GOTTA_HAVE_FAITH)
					&& player.getCurrentToARaid().invocations.contains(Invocations.BLOOD_THINNERS)
					&& player.getCurrentToARaid().invocations.contains(Invocations.UPSET_STOMACH)
					&& player.getCurrentToARaid().invocations.contains(Invocations.BLOWING_MUD)
					&& player.getCurrentToARaid().invocations.contains(Invocations.NOT_JUST_A_HEAD)
					&& player.getCurrentToARaid().invocations.contains(Invocations.ARTERIAL_SPRAY)
					&& player.getCurrentToARaid().invocations.contains(Invocations.PENETRATION)) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.MAYBE_IM_THE_BOSS.ordinal()))
						.getCombatAchievement()).check(player);
			}
		}
		if (player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().perfectAkkha && player.getCurrentToARaid().perfectZebak) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_TOMB_START.ordinal()))
						.getCombatAchievement()).check(player);
			}
		}
		if (player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().perfectBaba && player.getCurrentToARaid().perfectKephri) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_TOMB_DODGER.ordinal()))
						.getCombatAchievement()).check(player);
			}
		}
		if (player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().perfectBaba && player.getCurrentToARaid().perfectKephri
					&& player.getCurrentToARaid().perfectAkkha && player.getCurrentToARaid().perfectWarden
					&& player.getCurrentToARaid().perfectZebak) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_TOMBS_OF_AMASCUT.ordinal()))
						.getCombatAchievement()).check(player);
			}
		}
		if (raidInvocationValue >= 300) {
			if (player.getCurrentToARaid() != null && player.activePerksList.isEmpty()
					&& player.getCurrentToARaid().currentParty.getMembers().size() == 1) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERKLESS_TOMBS.ordinal()))
						.getCombatAchievement()).check(player);
			}
			player.tombsOfAmascutExpertKills.increment(player);
			if (player.tombsOfAmascutExpertKills.getKills() >= 75) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.EXPERT_TOMB_LOOTER.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (player.tombsOfAmascutExpertKills.getKills() >= 250) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.EXPERT_TOMB_LOOTERII.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (player.getCurrentToARaid() != null && !player.getCurrentToARaid().butDamageFailed) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.BUT_DAMAGE.ordinal()))
						.getCombatAchievement()).check(player);
			}
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.EXPERT_TOMB_EXPLORER.ordinal()))
					.getCombatAchievement()).check(player);
		}
		player.toaReward.clear();
		int deaths = player.getCurrentToARaid().playerDeaths.getOrDefault(player.getName(), 0);
		if (deaths < 1 && player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().currentParty.getMembers().size() > 1) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.HARDCORE_RAIDERS.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (deaths < 1 && player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().currentParty.getMembers().size() < 2) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.HARDCORE_TOMBS.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (player.getCurrentToARaid() != null && !player.getCurrentToARaid().suppliesClaimed && deaths < 1) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.HELPFUL_SPIRIT_WHO.ordinal()))
					.getCombatAchievement()).check(player);
		}
		int baseRate = 150;
		int petBaseRate = 500;
		float lootMultiplier = 0.2f;
		boolean canReceiveCursedPhalanx = false;
		int cursedPhalanxRate = 1000;
		if (raidInvocationValue >= 590) {
			baseRate = 6;
			petBaseRate = 120;
			lootMultiplier = 2.5f;
			canReceiveCursedPhalanx = true;
			cursedPhalanxRate = 50;
		} else if (raidInvocationValue >= 550) {
			baseRate = 10;
			petBaseRate = 140;
			lootMultiplier = 2.3f;
			canReceiveCursedPhalanx = true;
			cursedPhalanxRate = 75;
		} else if (raidInvocationValue >= 500) {
			baseRate = 14;
			petBaseRate = 160;
			lootMultiplier = 2.0f;
			canReceiveCursedPhalanx = true;
			cursedPhalanxRate = 100;
		} else if (raidInvocationValue >= 450) {
			baseRate = 18;
			petBaseRate = 200;
			lootMultiplier = 2.2f;
		} else if (raidInvocationValue >= 400) {
			baseRate = 22;
			petBaseRate = 200;
			lootMultiplier = 1.7f;
		} else if (raidInvocationValue >= 350) {
			baseRate = 25;
			petBaseRate = 250;
			lootMultiplier = 1.5f;
		} else if (raidInvocationValue >= 299) {
			baseRate = 27;
			petBaseRate = 280;
			lootMultiplier = 1.3f;
		} else if (raidInvocationValue >= 250) {
			baseRate = 30;
			petBaseRate = 300;
			lootMultiplier = 1.0f;
		} else if (raidInvocationValue >= 200) {
			baseRate = 35;
			petBaseRate = 350;
			lootMultiplier = 0.8f;
		} else if (raidInvocationValue >= 150) {
			baseRate = 50;
			petBaseRate = 400;
			lootMultiplier = 0.4f;
		} else if (raidInvocationValue >= 100) {
			baseRate = 100;
			petBaseRate = 450;
			lootMultiplier = 0.3f;
		} else if (raidInvocationValue >= 50) {
			baseRate = 140;
			petBaseRate = 475;
			lootMultiplier = 0.25f;
		}
		baseRate += (deaths * 12);
		List<Item> reward = new ArrayList<>();
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
			ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			petBaseRate *= c.getPetChanceBoost();
		}
		if (player.petDropBonus.isDelayed())
			petBaseRate *= 0.8F;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.DOUBLE_RAID_POINTS)) {
			baseRate /= 2;
		}
		petBaseRate *= getPetDonatorBoost(player);
		if (player.getEquipment().get(Equipment.SLOT_RING) != null && AttributeExtensions
				.hasAttribute(player.getEquipment().get(Equipment.SLOT_RING), AttributeTypes.RAID_UNIQUE_CHARM)) {
			int level = AttributeExtensions.getCharges(AttributeTypes.RAID_UNIQUE_CHARM,
					player.getEquipment().get(Equipment.SLOT_RING));
			double multiplier = 1.0 - (level * 0.05);
			baseRate *= multiplier;
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.RAIDING_RESTORATIONS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.RAIDING_RESTORATIONS);
			RaidingRestorations c = (RaidingRestorations) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			double multiplier = 1.0 + c.getLootChance();
			baseRate *= multiplier;
		}
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
			petBaseRate *= 0.85F;
		player.toaPetRate = petBaseRate;
		if(Random.get(player.toaPetRate) == 0) {
			Item pet = new Item(Pet.TUMEKENS_GUARDIAN.itemId);
			pet.lootBroadcast = Broadcast.GLOBAL;
			reward.add(pet);
		}
		if (canReceiveCursedPhalanx && Random.get(cursedPhalanxRate) == 0) {
			Item cursedPhalanx = new Item(27248, 1);
			cursedPhalanx.lootBroadcast = Broadcast.GLOBAL;
			reward.add(cursedPhalanx);
		}
		if (Random.get(baseRate) == 0) {
			Item rewardItem = UNIQUES.rollItem();
			rewardItem.lootBroadcast = Broadcast.GLOBAL;
			reward.add(rewardItem);
		} else {
			int rolls = Random.get(2, 4);
			for (int i = 0; i < rolls; i++) {
				Item loot = GENERIC_LOOT.rollItem();
				int amount = loot.getAmount();
				amount *= lootMultiplier;
				loot.setAmount(amount);
				reward.add(loot);
			}
		}
		player.toaReward.addAll(reward);
		SummerEvent.handleKill(player, "Tombs of Amascut");
		player.varbitSend(14373, hasUniqueDrop(player) ? 1 : 0);
	}

	// NOTE: only valid after calculateLoot
	private static boolean hasUniqueDrop(Player player) {
		for (var item : player.toaReward) {
			if (uniqueDrops.contains(item.getId())) {
				return true;
			}
		}
		return false;
	}

	public static final LootTable UNIQUES = new LootTable().addTable(1,
			new LootItem(27277, 1, 1).broadcast(Broadcast.GLOBAL),
			new LootItem(27226, 1, 3).broadcast(Broadcast.GLOBAL),
			new LootItem(27229, 1, 3).broadcast(Broadcast.GLOBAL),
			new LootItem(27232, 1, 3).broadcast(Broadcast.GLOBAL),
			new LootItem(25985, 1, 6).broadcast(Broadcast.GLOBAL),
			new LootItem(26219, 1, 9).broadcast(Broadcast.GLOBAL),
			new LootItem(25975, 1, 12).broadcast(Broadcast.GLOBAL));

	public static final LootTable GENERIC_LOOT = new LootTable().addTable(1,
			new LootItem(995, 2500000, 1),
			new LootItem(ItemID.DRAGON_DART, 85, 150, 1),
			new LootItem(ItemID.MAHOGANY_PLANK + 1, 30, 80, 1),
			new LootItem(ItemID.MAGIC_LOGS + 1, 30, 80, 1),
			new LootItem(ItemID.YEW_LOGS + 1, 30, 80, 1),
			new LootItem(ItemID.REDWOOD_LOGS + 1, 30, 80, 1),
			new LootItem(ItemID.UNCUT_DIAMOND + 1, 30, 80, 1),
			new LootItem(ItemID.UNCUT_RUBY + 1, 30, 80, 1),
			new LootItem(ItemID.UNCUT_RED_TOPAZ + 1, 30, 80, 1),
			new LootItem(ItemID.RAW_MANTA_RAY + 1, 30, 80, 1),
			new LootItem(ItemID.RAW_SHARK + 1, 30, 80, 1),
			new LootItem(ItemID.RAW_ANGLERFISH + 1, 30, 80, 1),
			new LootItem(ItemID.BATTLESTAFF + 1, 10, 30, 1),
			new LootItem(ItemID.UNCUT_DRAGONSTONE + 1, 10, 30, 1),
			new LootItem(ItemID.POTATO_CACTUS + 1, 100, 150, 1),
			new LootItem(ItemID.RED_SPIDERS_EGGS + 1, 100, 150, 1),
			new LootItem(ItemID.GOLD_BAR + 1, 50, 150, 1),
			new LootItem(ItemID.TEAK_PLANK + 1, 50, 150, 1),
			new LootItem(ItemID.GOLD_ORE + 1, 50, 150, 1),
			new LootItem(ItemID.ADAMANTITE_BAR + 1, 50, 70, 1),
			new LootItem(ItemID.RUNITE_BAR + 1, 50, 150, 1),
			new LootItem(30570, 1, 2, 1));

	private static void startBabaFightWithWarning(Player player, GameObject gameObject) {
		if (player.currentToARaidId != -1 && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().deadMembers.contains(player.getName())) {
				player.dialogue(new PlayerDialogue("You are unable to use objects as a ghost."));
				return;
			}
			player.dialogue(new OptionsDialogue("Are you sure you want to teleport into the boss arena?",
					new Option("Yes, I'm ready.", () -> {
						fastStartBabaFight(player, gameObject);
					}),
					new Option("Nevermind.")));
		}
	}



	public static void register() {
		InterfaceHandler.register(771, h -> {
			h.actions[4] = (SimpleAction) p -> {
				if (p.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
					p.sendMessage("You cannot bank items as an Ultimate Ironman.");
					return;
				}
				for (Item item : p.toaReward) {
					if (item == null || item.getAmount() < 1)
						continue;
					if (uniqueDrops.contains(item.getId()) && Objects.nonNull(p.getCurrentToARaid())) {
						String message = p.getName() + " just received " + item.getDef().name + " ";
						Broadcast.WORLD.sendNewsDropMessage(p, Icon.ADMINISTRATOR, "<col=000000>" + p.getName(),
								" just received " + item.getDef().descriptiveName + " from Tombs of Amascut ("
										+ p.getCurrentToARaid().getInvocationLevel() + ")");

						RareDropHook.sendDiscordMessage(() -> {
							var jsonObject = new JSONObject();
							jsonObject.put("player", p.getName());
							jsonObject.put("game_mode", p.getGameMode());
							jsonObject.put("item_id", item.getId());
							jsonObject.put("item_name", item.getDef().name);
							jsonObject.put("source", "Tombs of Amascut (%s)".formatted(p.getCurrentToARaid().getInvocationLevel()));
							jsonObject.put("total_attempts", Utils.formatMoneyString(p.tombsOfAmascutExpertKills.getKills()));
							return jsonObject;
						});

					}
					if(item.getId() == Pet.TUMEKENS_GUARDIAN.itemId) {
						Pet.TUMEKENS_GUARDIAN.unlock(p, p.tombsOfAmascutKills.getKills());
						continue;
					}
					p.addToCollectionLog(item);
					if (item.getDef().isNote())
						item.setId(item.getDef().unnotedId());
					p.getBank().add(item.getId(), item.getAmount());
				}
				p.toaReward.clear();
				p.closeInterface(ToplevelComponent.MAINMODAL);
			};
			h.actions[6] = (SimpleAction) p -> {
				for (Item item : p.toaReward) {
					if (item == null || item.getAmount() < 1)
						continue;
					if (uniqueDrops.contains(item.getId())) {
						String message = p.getName() + " just received " + item.getDef().name + " ";
						Broadcast.WORLD.sendNewsDropMessage(p, Icon.ADMINISTRATOR, "<col=000000>" + p.getName(),
								" just received " + item.getDef().descriptiveName + " from Tombs of Amascut ("
										+ p.getCurrentToARaid().getInvocationLevel() + ")");

						RareDropHook.sendDiscordMessage(() -> {
							var jsonObject = new JSONObject();
							jsonObject.put("player", p.getName());
							jsonObject.put("game_mode", p.getGameMode());
							jsonObject.put("item_id", item.getId());
							jsonObject.put("item_name", item.getDef().name);
							jsonObject.put("source", "Tombs of Amascut (%s)".formatted(p.getCurrentToARaid().getInvocationLevel()));
							jsonObject.put("total_attempts", Utils.formatMoneyString(p.tombsOfAmascutExpertKills.getKills()));
							return jsonObject;
						});

					}
					if(item.getId() == Pet.TUMEKENS_GUARDIAN.itemId) {
						Pet.TUMEKENS_GUARDIAN.unlock(p, p.tombsOfAmascutKills.getKills());
						continue;
					}
					if (!item.getDef().stackable && item.getAmount() > 1)
						item.setId(item.getDef().notedId);
					p.addToCollectionLog(item);
					p.getInventory().addOrDrop(item.getId(), item.getAmount());
				}
				p.toaReward.clear();
				p.closeInterface(ToplevelComponent.MAINMODAL);
			};
			h.actions[8] = (SimpleAction) p -> {
				p.toaReward.clear();
				p.closeInterface(ToplevelComponent.MAINMODAL);
			};
		});
		ObjectAction.register(32656, 1, (player, object) -> {
			if (player.theatreReward.isEmpty()) {
				player.sendMessage("You have nothing to claim from the Theatre of Blood.");
				return;
			}
			player.openInterface(ToplevelComponent.MAINMODAL, 23);
			player.getPacketSender().sendItems(
					-1,
					12,
					ToAObjects.containerId,
					player.theatreReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					23 << 16 | 12, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			containerId++;
		});
		ObjectAction.register(46078, 1, (player, object) -> {
			if (player.toaReward.isEmpty()) {
				player.sendMessage("You have nothing to claim from the Tombs of Amascut.");
				return;
			}
			Map<Integer, Integer> lootMap = new HashMap<>();
			for (Item item : player.toaReward) {
				if (lootMap.containsKey(item.getId())) {
					lootMap.put(item.getId(), lootMap.get(item.getId()) + item.getAmount());
				} else {
					lootMap.put(item.getId(), item.getAmount());
				}
			}
			player.toaReward.clear();

			for (Map.Entry<Integer, Integer> entry : lootMap.entrySet()) {
				player.toaReward.add(new Item(entry.getKey(), entry.getValue()));
			}
			player.openInterface(ToplevelComponent.MAINMODAL, 771);
			player.getPacketSender().sendItems(
					-1,
					10,
					ToAObjects.containerId,
					player.toaReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					771 << 16 | 10, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			containerId++;
		});
		ObjectAction.register(45128, 1, ToAObjects::leaveRaid);
		ObjectAction.register(46164, 1, ToAObjects::enterAkkhaRoom);
		ObjectAction.register(46168, 1, ToAObjects::enterElidinisRoom);
		ObjectAction.register(45138, 1, ToAObjects::enterRewardsRoom);
		ObjectAction.register(46168, 2, ToAObjects::enterElidinisRoom);
		ObjectAction.register(46164, 2, ToAObjects::enterAkkhaRoom);
		ObjectAction.register(46161, 1, ToAObjects::enterZebakRoom);
		ObjectAction.register(46161, 2, ToAObjects::enterZebakRoom);
		ObjectAction.register(46155, 1, ToAObjects::enterKephriRoom);
		ObjectAction.register(46155, 2, ToAObjects::enterKephriRoom);
		ObjectAction.register(46158, 1, ToAObjects::enterBabaRoom);
		ObjectAction.register(46158, 2, ToAObjects::enterBabaRoom);
		ObjectAction.register(45543, 1, (player, object) -> ToAObjects.enterLobby(player, object, false));
		ObjectAction.register(46220, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(46218, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(44547, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(44545, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(29994, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(46216, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(46215, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(46219, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(46217, 1, ToAObjects::openLootSarcophagus);
		ObjectAction.register(46068, 1, ToAObjects::openToaParties);
		ObjectAction.register(45144, 1, (player, object) -> ToAObjects.enterLobby(player, object, false));
		ObjectAction.register(46055, 1, (player, object) -> ToAObjects.enterLobby(player, object, false));
		ObjectAction.register(45129, 1, (player, object) -> ToAObjects.enterLobby(player, object, false));
		ObjectAction.register(45844, 1, (player, object) -> ToAObjects.enterLobby(player, object, false));
		ObjectAction.register(45506, 1, ToAObjects::startZebakFightWithWarning);
		ObjectAction.register(45506, 2, ToAObjects::fastStartZebakFight);
		ObjectAction.register(45866, 1, ToAObjects::startAkkhaFightWithWarning);
		ObjectAction.register(45866, 2, ToAObjects::fastStartAkkhaFight);
		ObjectAction.register(45505, 1, ToAObjects::startKephriFightWithWarning);
		ObjectAction.register(45579, 1, ToAObjects::startElidinisFightWithWarning);
		ObjectAction.register(45505, 2, ToAObjects::fastStartKephriFight);
		ObjectAction.register(45754, 1, ToAObjects::startBabaFightWithWarning);
		ObjectAction.register(45754, 2, ToAObjects::fastStartBabaFight);
		ObjectAction.register(46223, 2, (player, object) -> player.getBank().open());
		ObjectAction.register(46223, 1, (player, object) -> player.getBank().open());
		ObjectAction.register(46089, 1, (player, object) -> {
			if (TombsOfAmascutManager.getRaidParty(player) == null) {
				player.dialogue(new PlayerDialogue("You need to create a party before starting a raid."));
				return;
			}
			StringBuilder unclaimedLootPlayers = new StringBuilder();
			boolean hasUnclaimedLoot = false;
			for (String name : TombsOfAmascutManager.getRaidParty(player).getMembers()) {
				Player p = World.getPlayer(name);
				if (p != null) {
					if (!p.toaReward.isEmpty()) {
						hasUnclaimedLoot = true;
						unclaimedLootPlayers.append(p.getName()).append(", ");
						p.sendMessage("Your party leader tried to start a raid.");
						p.sendMessage("You have unclaimed loot from your previous raid, loot it from the chest by the entrance.");
					}
				}
			}
			if (hasUnclaimedLoot) {
				player.sendMessage("The following players in your party have unclaimed loot: "
						+ (unclaimedLootPlayers.substring(0, unclaimedLootPlayers.length() - 2)));
				return;
			}
			TombsOfAmascutManager.getRaidParty(player).enterRaid(player);
		});
		NPCAction.register(11693, "leave", (player, npc) -> {
			leaveRaid(player, null);
		});
		NPCAction.register(11693, "talk-to", (player, npc) -> {
			leaveRaid(player, null);
		});
		NPCAction.register(11689, 1, (player, npc) -> {
			player.dialogue(new OptionsDialogue("Would you like to return to the lobby?",
					new Option("Yes, I'm ready.", () -> {
						if (player.getCurrentToARaid() == null)
							return;
						if (player.getCurrentToARaid().currentRoom != null
								&& !player.getCurrentToARaid().getRooms().get(player.getCurrentToARaid().currentRoom).roomCompleted) {
							if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {
								player.getCurrentToARaid().getRooms()
										.get(player.getCurrentToARaid().currentRoom).roomCompleted = true;
							}
						}
						ToAObjects.enterLobby(player, null, false);
						Map<String, ToARoom> uncompletedRooms = new HashMap<>();
						if (!player.getCurrentToARaid().getRooms().get("zebak").roomCompleted)
							uncompletedRooms.put("zebak", player.getCurrentToARaid().getRooms().get("zebak"));
						if (!player.getCurrentToARaid().getRooms().get("akkha").roomCompleted)
							uncompletedRooms.put("akkha", player.getCurrentToARaid().getRooms().get("akkha"));
						if (!player.getCurrentToARaid().getRooms().get("kephri").roomCompleted)
							uncompletedRooms.put("kephri", player.getCurrentToARaid().getRooms().get("kephri"));
						if (!player.getCurrentToARaid().getRooms().get("baba").roomCompleted)
							uncompletedRooms.put("baba", player.getCurrentToARaid().getRooms().get("baba"));

						if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {
							System.out.println("leader");
							System.out.println("uncompleted rooms: " + uncompletedRooms.size());
							if (uncompletedRooms.size() == 2) {
								NPC spirit = new NPC(11694).spawn(player.getCurrentToARaid().getRooms().get("lobby").map.convertX(3548),
										player.getCurrentToARaid().getRooms().get("lobby").map.convertY(5154), 0, Direction.SOUTH, 0);
							}
						}
						if (uncompletedRooms.size() == 2 && player.getToaSuppliesClaimed() == 0)
							calculateHelpfulSpiritLoot(player);
						if (!uncompletedRooms.isEmpty()) {
							if (player.getToaSuppliesClaimed() == 1)
								calculateHelpfulSpiritLoot(player);
							if (player.getCurrentToARaid().getInvocations().contains(Invocations.WALK_THE_PATH)) {
								if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {

									String roomName = new ArrayList<>(uncompletedRooms.keySet())
											.get(Random.get(uncompletedRooms.size() - 1));
									final int[] newLevel = {0};
									if (roomName.equalsIgnoreCase("zebak")) {
										newLevel[0] = player.getCurrentToARaid().getZebakPathLevel() + 1;
										player.getCurrentToARaid().scaleNPC(
												((ZebakRoom) player.getCurrentToARaid().getRooms().get("zebak")).getZebak(), newLevel[0]);
									}
									if (roomName.equalsIgnoreCase("akkha")) {
										newLevel[0] = player.getCurrentToARaid().getAkkhaPathLevel() + 1;
										player.getCurrentToARaid().scaleNPC(
												((AkkhaRoom) player.getCurrentToARaid().getRooms().get("akkha")).getAkkha(), newLevel[0]);
									}
									if (roomName.equalsIgnoreCase("kephri")) {
										newLevel[0] = player.getCurrentToARaid().getKephriPathLevel() + 1;
										player.getCurrentToARaid().scaleNPC(
												((KephriRoom) player.getCurrentToARaid().getRooms().get("kephri")).getKephri(), newLevel[0]);

									}
									if (roomName.equalsIgnoreCase("baba")) {
										newLevel[0] = player.getCurrentToARaid().getBabaPathLevel() + 1;
										player.getCurrentToARaid().scaleNPC(
												((BabaRoom) player.getCurrentToARaid().getRooms().get("baba")).getBaba(), newLevel[0]);

									}
									player.getCurrentToARaid().currentParty.getMembers().forEach(name -> {
										Player plr = World.getPlayer(name);
										if (plr != null) {
											if (roomName.equalsIgnoreCase("zebak")) {
												plr.getCurrentToARaid().setZebakPathLevel(newLevel[0]);
												plr.sendMessage("The Zebak path has been upgraded to level " + newLevel[0] + ".");
											}
											if (roomName.equalsIgnoreCase("akkha")) {
												plr.getCurrentToARaid().setAkkhaPathLevel(newLevel[0]);
												plr.sendMessage("The Akkha path has been upgraded to level " + newLevel[0] + ".");
											}
											if (roomName.equalsIgnoreCase("kephri")) {
												plr.getCurrentToARaid().setKephriPathLevel(newLevel[0]);
												plr.sendMessage("The Kephri path has been upgraded to level " + newLevel[0] + ".");
											}
											if (roomName.equalsIgnoreCase("baba")) {
												plr.getCurrentToARaid().setBabaPathLevel(newLevel[0]);
												plr.sendMessage("The Baba path has been upgraded to level " + newLevel[0] + ".");
											}
										}
									});

								}
							}
						}
						player.closeDialogue();

					}),
					new Option("Nevermind.")));
		});
		NPCAction.register(11689, 3, (player, npc) -> {
			if (player.getCurrentToARaid() == null)
				return;
			if (player.getCurrentToARaid().currentRoom != null
					&& !player.getCurrentToARaid().getRooms().get(player.getCurrentToARaid().currentRoom).roomCompleted) {
				if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {
					player.getCurrentToARaid().getRooms()
							.get(player.getCurrentToARaid().currentRoom).roomCompleted = true;
				}
			}
			ToAObjects.enterLobby(player, null, false);
			Map<String, ToARoom> uncompletedRooms = new HashMap<>();
			if (!player.getCurrentToARaid().getRooms().get("zebak").roomCompleted)
				uncompletedRooms.put("zebak", player.getCurrentToARaid().getRooms().get("zebak"));
			if (!player.getCurrentToARaid().getRooms().get("akkha").roomCompleted)
				uncompletedRooms.put("akkha", player.getCurrentToARaid().getRooms().get("akkha"));
			if (!player.getCurrentToARaid().getRooms().get("kephri").roomCompleted)
				uncompletedRooms.put("kephri", player.getCurrentToARaid().getRooms().get("kephri"));
			if (!player.getCurrentToARaid().getRooms().get("baba").roomCompleted)
				uncompletedRooms.put("baba", player.getCurrentToARaid().getRooms().get("baba"));

			if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {
				System.out.println("leader");
				System.out.println("uncompleted rooms: " + uncompletedRooms.size());
				if (uncompletedRooms.size() == 2) {
					player.getCurrentToARaid().currentParty.getMembers().forEach(p -> {
						Player plr = World.getPlayer(p);
						if (plr != null) {
							if (plr.getToaSuppliesClaimed() == 0)
								calculateHelpfulSpiritLoot(plr);
						}
					});
					NPC spirit = new NPC(11694).spawn(player.getCurrentToARaid().getRooms().get("lobby").map.convertX(3548),
							player.getCurrentToARaid().getRooms().get("lobby").map.convertY(5154), 0, Direction.SOUTH, 0);
				} else if (uncompletedRooms.isEmpty()) {
					player.getCurrentToARaid().currentParty.getMembers().forEach(p -> {
						Player plr = World.getPlayer(p);
						if (plr != null) {
							if (plr.getToaSuppliesClaimed() == 1)
								calculateHelpfulSpiritLoot(plr);
						}
					});

				}
			}

			if (!uncompletedRooms.isEmpty()) {

				if (player.getCurrentToARaid().getInvocations().contains(Invocations.WALK_THE_PATH)) {
					if (player.getName().equalsIgnoreCase(player.getCurrentToARaid().currentParty.getLeader())) {

						String roomName = new ArrayList<>(uncompletedRooms.keySet()).get(Random.get(uncompletedRooms.size() - 1));
						final int[] newLevel = {0};
						if (roomName.equalsIgnoreCase("zebak")) {
							newLevel[0] = player.getCurrentToARaid().getZebakPathLevel() + 1;
							player.getCurrentToARaid()
									.scaleNPC(((ZebakRoom) player.getCurrentToARaid().getRooms().get("zebak")).getZebak(), newLevel[0]);
						}
						if (roomName.equalsIgnoreCase("akkha")) {
							newLevel[0] = player.getCurrentToARaid().getAkkhaPathLevel() + 1;
							player.getCurrentToARaid()
									.scaleNPC(((AkkhaRoom) player.getCurrentToARaid().getRooms().get("akkha")).getAkkha(), newLevel[0]);
						}
						if (roomName.equalsIgnoreCase("kephri")) {
							newLevel[0] = player.getCurrentToARaid().getKephriPathLevel() + 1;
							player.getCurrentToARaid().scaleNPC(
									((KephriRoom) player.getCurrentToARaid().getRooms().get("kephri")).getKephri(), newLevel[0]);

						}
						if (roomName.equalsIgnoreCase("baba")) {
							newLevel[0] = player.getCurrentToARaid().getBabaPathLevel() + 1;
							player.getCurrentToARaid()
									.scaleNPC(((BabaRoom) player.getCurrentToARaid().getRooms().get("baba")).getBaba(), newLevel[0]);

						}
						player.getCurrentToARaid().currentParty.getMembers().forEach(name -> {
							Player plr = World.getPlayer(name);
							if (plr != null) {
								if (roomName.equalsIgnoreCase("zebak")) {
									plr.getCurrentToARaid().setZebakPathLevel(newLevel[0]);
									plr.sendMessage("The Zebak path has been upgraded to level " + newLevel[0] + ".");
								}
								if (roomName.equalsIgnoreCase("akkha")) {
									plr.getCurrentToARaid().setAkkhaPathLevel(newLevel[0]);
									plr.sendMessage("The Akkha path has been upgraded to level " + newLevel[0] + ".");
								}
								if (roomName.equalsIgnoreCase("kephri")) {
									plr.getCurrentToARaid().setKephriPathLevel(newLevel[0]);
									plr.sendMessage("The Kephri path has been upgraded to level " + newLevel[0] + ".");
								}
								if (roomName.equalsIgnoreCase("baba")) {
									plr.getCurrentToARaid().setBabaPathLevel(newLevel[0]);
									plr.sendMessage("The Baba path has been upgraded to level " + newLevel[0] + ".");
								}
							}
						});

					}
				}
			}
			player.closeDialogue();
		});
	}

}
