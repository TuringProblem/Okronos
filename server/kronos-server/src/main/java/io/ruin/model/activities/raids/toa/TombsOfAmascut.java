package io.ruin.model.activities.raids.toa;

import io.ruin.model.World;
import io.ruin.model.activities.raids.toa.rooms.*;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.stat.StatType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;

@Slf4j
public class TombsOfAmascut {
	public boolean perfectAkkha = false;
	public boolean perfectBaba = false;
	public boolean perfectKephri = false;
	public boolean perfectZebak = false;
	public boolean perfectWarden = false;
	Map<String, ToARoom> rooms = new HashMap<>();
	List<Invocations> invocations = new ArrayList<>();
	public String currentRoom = null;
	public ToAParty currentParty = null;
	int attemptsRemaining = 10;
	public boolean butDamageFailed = false;
	Position deathPosition = null;

	public int jugRolls = 0;
	public boolean failedPerfectWarden = false;

	public boolean suppliesClaimed = false;

	@Getter
	@Setter
	int invocationLevel = 0;

	@Getter
	@Setter
	int babaPathLevel = 0;
	@Getter
	@Setter
	int kephriPathLevel = 0;
	@Getter
	@Setter
	int zebakPathLevel = 0;
	@Getter
	@Setter
	int akkhaPathLevel = 0;

	static List<Integer> raidItems = Arrays.asList(
			27333,
			27331,
			27329,
			27327,
			27321,
			27319,
			27317,
			27315,
			27343,
			27345,
			27347,
			27349,
			27339,
			27341);

	public static void confiscateItems(Player player) {
		for (Item item : player.getInventory().getItems()) {
			if (item != null && raidItems.contains(item.getId())) {
				item.remove();
			}
		}
	}

	public Instant raidStartTime;

	public void buildRaid(ToAParty party) throws DynamicMap.DynamicMapBuildException {
		for (Invocations invo : party.getActiveInvocations()) {
			addInvocation(invo);
		}
		raidStartTime = Instant.now();

		if (getInvocations().contains(Invocations.TRY_AGAIN))
			attemptsRemaining = 9;
		else if (getInvocations().contains(Invocations.PERSISTANCE))
			attemptsRemaining = 4;
		else if (getInvocations().contains(Invocations.SOFTCORE_RUN))
			attemptsRemaining = 2;
		else if (getInvocations().contains(Invocations.HARDCORE_RUN))
			attemptsRemaining = 0;
		if (getInvocations().contains(Invocations.PATHSEEKER)) {
			babaPathLevel = 1;
			kephriPathLevel = 1;
			zebakPathLevel = 1;
			akkhaPathLevel = 1;
		} else if (getInvocations().contains(Invocations.PATHFINDER)) {
			babaPathLevel = 2;
			kephriPathLevel = 2;
			zebakPathLevel = 2;
			akkhaPathLevel = 2;
		} else if (getInvocations().contains(Invocations.PATHMASTER)) {
			babaPathLevel = 3;
			kephriPathLevel = 3;
			zebakPathLevel = 3;
			akkhaPathLevel = 3;
		}

		rooms.put("lobby", new LobbyRoom());
		rooms.put("reward", new RewardRoom());
		rooms.put("akkha", new AkkhaRoom());
		rooms.put("baba", new BabaRoom());
		rooms.put("kephri", new KephriRoom());
		rooms.put("zebak", new ZebakRoom());
		rooms.put("wardensp1", new ElidinisWardenRoom());
		rooms.put("wardensp2", new TumekenWardenRoom());
		for (Map.Entry<String, ToARoom> entry : rooms.entrySet()) {
			entry.getValue().buildRoom();
			entry.getValue().populateRoom();
			entry.getValue().roomCompleted = false;
		}
		currentParty = party;
	}

	private static final StatType[] SCALED_STATS = { StatType.Defence, StatType.Attack, StatType.Strength, StatType.Magic,
			StatType.Ranged };

	public void scaleNPC(NPC npc, int pathLevel) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;
		factor = 1 + (0.2 * pathLevel);
		factor *= (0.15 * currentParty.getMembers().size());
		if (factor != 0) {
			for (StatType type : SCALED_STATS) {
				double newLevel = npc.getCombat().getStat(type).fixedLevel * factor;
				npc.getCombat().getStat(type).fixedLevel = (int) newLevel;
				npc.getCombat().getStat(type).restore();
			}
		}
		scaleHP(npc, pathLevel);
	}

	public void scaleHP(NPC npc, int pathLevel) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;
		factor = 1 + (0.25 * pathLevel);
		factor *= (0.25 * currentParty.getMembers().size());
		if (factor != 0) {
			double newLevel = npc.getCombat().getStat(StatType.Hitpoints).fixedLevel * factor;
			npc.getCombat().getStat(StatType.Hitpoints).fixedLevel = (int) newLevel;
			npc.getCombat().getStat(StatType.Hitpoints).restore();
		}

	}

	public Map<String, ToARoom> getRooms() {
		return rooms;
	}

	public void addInvocation(Invocations invocation) {
		invocations.add(invocation);
		invocationLevel += invocation.invocationLevel;
	}

	public List<Invocations> getInvocations() {
		return invocations;
	}

	public void setDeathPosition(Position pos) {
		deathPosition = pos;
	}

	public Map<String, Integer> playerDeaths = new HashMap<>();

	public List<String> deadMembers = new ArrayList<>();
	public List<Player> playerInRoom = new ArrayList<>();
	public int totalDeaths = 0;

	public void handlePlayerDeath(Player player) {
		player.resetAnimation();
		totalDeaths++;
		playerDeaths.put(player.getName(), playerDeaths.getOrDefault(player.getName(), 0) + 1);
		if (deathPosition != null) {
			player.getMovement().teleport(deathPosition);
			player.setInvincible(true);
			player.getAppearance().setNpcId(11691);
			player.getAppearance().update();
			player.getCombat().restore();
			if (!deadMembers.contains(player.getName())) {
				deadMembers.add(player.getName());
			}

			if (deadMembers.size() == playerInRoom.size()) {
				handleRoomFailure(player, player.getCurrentToARaid().currentParty, player.getCurrentToARaid().currentRoom);
			}
		}
	}

	private Player getAlivePlayer() {
		for (String name : currentParty.getMembers()) {
			Player player = World.getPlayer(name);
			if (player != null && !deadMembers.contains(name)) {
				return player;
			}
		}
		return null;
	}

	public void teleportDeadPlayers() {
		assert getAlivePlayer() != null : "Nulled active Player; ToA.teleportDeadPlayers()";
		Position pos = getAlivePlayer().getPosition().copy();
		for (String name : deadMembers) {
			Player player = World.getPlayer(name);
			if (player != null) {
				player.sendMessage("The room has been completed, you'll be moved soon.");
			}
		}
		World.startEvent(e -> {
			e.delay(12);
			for (String name : deadMembers) {
				Player player = World.getPlayer(name);
				if (player != null) {
					player.getMovement().teleport(pos);
					player.getAppearance().setNpcId(-1);
					player.setInvincible(false);
					player.getAppearance().update();
				}
			}
			deadMembers.clear();
		}).setCancelCondition(() -> currentParty == null || currentParty.getMembers().isEmpty());
	}

	private void handleRoomFailure(Player p, ToAParty party, String roomName) {
		playerInRoom.clear();
		party.getMembers().forEach(member -> {
			Player player = World.getPlayer(member);
			if (player != null) {
				player.sendMessage("Your party has failed the room.");
				player.getCurrentToARaid().currentRoom = null;
				if (player.currentToaEnterPosition == null) {
					ToAObjects.leaveRaid(player, null);
					player.sendMessage("Failed to find entry position, you have been teleported out of the raid.");
					return;
				}
				player.getMovement().teleport(player.currentToaEnterPosition);
				player.getAppearance().setNpcId(-1);
				player.setInvincible(false);
				player.getAppearance().update();
			}
		});
		deadMembers.clear();

		try {
			switch (roomName) {
				case "akkha":
					p.getCurrentToARaid().getRooms().get(roomName).map.destroy();
					p.getCurrentToARaid().getRooms().replace(roomName, new AkkhaRoom());
					p.getCurrentToARaid().getRooms().get(roomName).buildRoom();
					p.getCurrentToARaid().getRooms().get(roomName).populateRoom();
					break;
				case "baba":
					p.getCurrentToARaid().getRooms().get(roomName).map.destroy();
					p.getCurrentToARaid().getRooms().replace(roomName, new BabaRoom());
					p.getCurrentToARaid().getRooms().get(roomName).buildRoom();
					p.getCurrentToARaid().getRooms().get(roomName).populateRoom();
					break;
				case "kephri":
					p.getCurrentToARaid().getRooms().get(roomName).map.destroy();
					p.getCurrentToARaid().getRooms().replace(roomName, new KephriRoom());
					p.getCurrentToARaid().getRooms().get(roomName).buildRoom();
					p.getCurrentToARaid().getRooms().get(roomName).populateRoom();
					break;
				case "zebak":
					p.getCurrentToARaid().getRooms().get(roomName).map.destroy();
					p.getCurrentToARaid().getRooms().replace(roomName, new ZebakRoom());
					p.getCurrentToARaid().getRooms().get(roomName).buildRoom();
					p.getCurrentToARaid().getRooms().get(roomName).populateRoom();
					break;
				case "wardensp1":
				case "wardensp2":
					p.getCurrentToARaid().getRooms().get("wardensp1").map.destroy();
					p.getCurrentToARaid().getRooms().get("wardensp2").map.destroy();
					p.getCurrentToARaid().getRooms().replace("wardensp1", new ElidinisWardenRoom());
					p.getCurrentToARaid().getRooms().replace("wardensp2", new TumekenWardenRoom());
					p.getCurrentToARaid().getRooms().get("wardensp1").buildRoom();
					p.getCurrentToARaid().getRooms().get("wardensp1").populateRoom();
					p.getCurrentToARaid().getRooms().get("wardensp2").buildRoom();
					p.getCurrentToARaid().getRooms().get("wardensp2").populateRoom();
					break;
			}
		} catch (DynamicMap.DynamicMapBuildException e) {
			p.sendMessage("Unable to build dynamic map.");
			failRaid(p);
			return;
		}

		if (attemptsRemaining-- <= 0) {
			failRaid(p);
		}
	}

	private void failRaid(Player p) {
		destroyMaps(p);
		var pParty = TombsOfAmascutManager.getRaidParty(p);
		if (pParty != null) {
			pParty.started = false;
		}

		var pRaid = p.getCurrentToARaid();
		var pCurrentParty = pRaid == null ? null : pRaid.currentParty;
		if (pCurrentParty != null) {
			pCurrentParty.getMembers().forEach(name -> {
				Player plr = World.getPlayer(name);
				if (plr != null) {
					TombsOfAmascut.confiscateItems(plr);
					plr.sendMessage("Your party has failed the raid.");
					plr.getMovement().teleport(new Position(3359, 9113, 0));
					plr.setInvincible(false);
					plr.deathEndListener = null;
					plr.deathStartListener = null;
					plr.teleportListener = null;
					plr.currentToaEnterPosition = null;
					plr.currentToARaidId = -1;
					plr.getEquipment().sendUpdates();
					plr.getCombat().init(plr);
				}
			});
		}

		p.currentToaEnterPosition = null;
		p.currentToARaidId = -1;
		p.getEquipment().sendUpdates();
		TombsOfAmascutManager.removeRaid(p.getCurrentToARaid());
	}

	public void destroyMaps() {
		this.getRooms().forEach(TombsOfAmascut::destroyRoomsAndMaps);
	}

	public boolean hasPlayers() {

		for (var room : this.rooms.values()) {
			if (room == null) {
				continue;
			}

			if (room.map == null) {
				continue;
			}

			for (var region : room.map.getRegions()) {
				if (region.players == null) {
					continue;
				}
				if (!region.players.isEmpty()) {
					return true;
				}
			}
		}

		return false;
	}

	public void destroyMaps(Player player) {
		var raid = player.getCurrentToARaid();
		raid.getRooms().forEach(TombsOfAmascut::destroyRoomsAndMaps);
	}

	static void destroyRoomsAndMaps(String name, ToARoom room) {
		if (room.npcs != null) {
			log.debug("destroying {} NPCs in room {}", room.npcs.size(), name);
			room.npcs.forEach(NPC::remove);
		}
		if (room.map != null) {
			log.debug("destroying map in room {}", name);
			room.map.destroy();
		}
	}

	public static boolean allowTeleport(Player player) {
		player.sendMessage("You can't teleport away from the Tombs of Amascut.");
		return false;
	}

	public void movePlayerToLobby(Player player) {
		player.getMovement().teleport(rooms.get("lobby").getEnterPosition());
		for (Item item : player.getEquipment().getItems()) {
			if (item == null)
				continue;
			if (item.getDef().equipReqs != null) {
				for (int req : item.getDef().equipReqs) {
					if (req >= 75)
						player.getCurrentToARaid().butDamageFailed = true;
				}
			}
		}
		for (Item item : player.getInventory().getItems()) {
			if (item == null)
				continue;
			if (item.getDef().equipReqs != null) {
				for (int req : item.getDef().equipReqs) {
					if (req >= 75)
						player.getCurrentToARaid().butDamageFailed = true;
				}
			}
		}
		player.deathEndListener = (DeathListener.SimpleKiller) killer -> {
			handlePlayerDeath(player);
		};
		player.teleportListener = TombsOfAmascut::allowTeleport;
	}

}
