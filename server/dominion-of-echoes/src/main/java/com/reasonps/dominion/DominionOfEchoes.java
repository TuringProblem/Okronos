package com.reasonps.dominion;

import com.reasonps.dominion.rooms.*;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-30
 */
@Slf4j
public class DominionOfEchoes {

	@Getter
	private static boolean isDoeActive = true;

	public static void setDoeActive(boolean isDoeActive) {
		DominionOfEchoes.isDoeActive = isDoeActive;
	}

	// a static list to keep a running record of all active raids in the server
	@Getter
	private static final Map<String, DominionOfEchoes> activeRaids = new HashMap<>();

	@Getter
	private final List<MapHandler> rooms = new ArrayList<>();

	public DominionOfEchoes(Player player) {
		if (playerIsInRaid(player)) {
			log.debug("Player {} is already in a raid.", player.getName());
			return;
		}
		activeRaids.put(player.uuid(), this);

		player.dialogue(new MessageDialogue("Welcome to the Dominion of Echoes."));

		var area = new EchoTreasureRoom(player);
			area.movePlayerToInstance(player);
		rooms.add(area);
		player.set("dominion_of_echos_stage", 0);
	}

	public void movePlayerToNextInstance(Player player) {
		var stage = player.get("dominion_of_echos_stage").toString();
		var progress = Integer.parseInt(stage);
		switch (progress) {
			case 0: { // Move to Echo KQ
				var area = new EchoKalphiteHive(player);
				area.movePlayerToInstance(player);
				if (!rooms.isEmpty()) {
					rooms.getLast().getPlayers().remove(player);
					rooms.getLast().destroy();
				}
				rooms.add(area);
				player.set("dominion_of_echos_stage", 1);
				break;
			}
			case 1: { // Move to Echo KBD
				var area = new EchoKingBlackDragonDungeon(player);
				area.movePlayerToInstance(player);
				if (!rooms.isEmpty()) {
					rooms.getLast().getPlayers().remove(player);
					rooms.getLast().destroy();
				}
				rooms.add(area);
				player.set("dominion_of_echos_stage", 2);
				break;
			}
			case 2: {// Move to Echo Cerberus
				var area = new EchoCerberusMap(player);
				area.movePlayerToInstance(player);
				if (!rooms.isEmpty()) {
					rooms.getLast().getPlayers().remove(player);
					rooms.getLast().destroy();
				}
				rooms.add(area);
				player.set("dominion_of_echos_stage", 3);
				break;
			}
			case 3: {// Move to Echo Hunllef
				var area = new EchoHunllefMap(player);
				area.movePlayerToInstance(player);
				if (!rooms.isEmpty()) {
					rooms.getLast().getPlayers().remove(player);
					rooms.getLast().destroy();
				}
				rooms.add(area);
				player.set("dominion_of_echos_stage", 4);
				break;
			}
			case 4: {// Move to Echo Dagannoth Kings
				var area = new EchoDagannothCave(player);
				area.movePlayerToInstance(player);
				if (!rooms.isEmpty()) {
					rooms.getLast().getPlayers().remove(player);
					rooms.getLast().destroy();
				}
				rooms.add(area);
				player.set("dominion_of_echos_stage", 5);
				break;
			}
			case 5: {// Move to Echo Sol Heredit
				var area = new EchoSolHereditColiseum(player);
				area.movePlayerToInstance(player);
				if (!rooms.isEmpty()) {
					rooms.getLast().getPlayers().remove(player);
					rooms.getLast().destroy();
				}
				rooms.add(area);
				player.set("dominion_of_echos_stage", 6);
				break;
			}
			case 6: {// Move to Echo Treasure Room
				player.set("dominion_of_echos_completed", true);
				var area = new EchoTreasureRoom(player);
					area.movePlayerToInstance(player);
				if (!rooms.isEmpty()) {
					rooms.getLast().getPlayers().remove(player);
					rooms.getLast().destroy();
				}
				rooms.add(area);
				player.dominionOfEchoesKills.messageOnKill();
				player.dominionOfEchoesKills.increment(player);
				player.insideRaid = false;
				player.deathEndListener = null;
				player.deathStartListener = null;
				break;
			}
			default:
				throw new IllegalStateException("Unexpected value: " + progress);
		}
	}

	public static DominionOfEchoes getActiveRaidForPlayer(Player player) {
		return activeRaids.get(player.uuid());
	}

	public static boolean playerIsInRaid(Player player) {
		return activeRaids.containsKey(player.uuid());
	}

	public static String convertTimeInLongToText(Player player) {
		long time = player.doeTimer.getTime();
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
	}
}
