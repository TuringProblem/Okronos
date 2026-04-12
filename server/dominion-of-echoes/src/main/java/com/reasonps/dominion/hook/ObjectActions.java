package com.reasonps.dominion.hook;

import com.reasonps.dominion.DominionOfEchoes;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-10
 */
@Slf4j
public class ObjectActions {

	private static final int MAIN_ENTRANCE = 50749;
	private static final int MAIN_EXIT = 50750;
	private static final int RAID_ENTRANCE = 50751;

	public static void register() {
		ObjectAction.register(MAIN_ENTRANCE, "Enter", ObjectActions::enterDoePrep);
		ObjectAction.register(MAIN_EXIT, "Exit", ObjectActions::leaveDoe);
		ObjectAction.register(RAID_ENTRANCE, "Enter", ObjectActions::handelRaidEntryObject);
	}

	/**
	 * Handles the interaction when a player attempts to enter the Dominion of Echos via a game object.
	 * Displays a dialogue with options for the player to confirm or decline their entry into the area.
	 *
	 * @param player The player attempting to enter the Dominion of Echos.
	 * @param object The game object associated with the raid entrance interaction.
	 */
	private static void handelRaidEntryObject(Player player, GameObject object) {
		if (!DominionOfEchoes.isDoeActive()) {
			player.sendMessage("The Dominion of Echoes is currently inactive. Please try again later.");
			return;
		}
		player.dialogue(
			new MessageDialogue("You are about to enter the Dominion of Echoes.<br>From which there is no escape, death or victory is your only way out."),
			new OptionsDialogue("Do you wish to seal your fate?",
				new Option("Enter", () -> startRaid(player)),
				new Option("Stay back", Player::closeDialogue))
		);
	}

	/**
	 * Initiates the raid process for the player in the Dominion of Echos.
	 * Locks the player, applies visual effects, sets up event listeners, and
	 * prepares the player for the next instance in the raid environment.
	 *
	 * @param player The player who is starting the raid.
	 */
	private static void startRaid(Player player) {
		if (!DominionOfEchoes.isDoeActive()) {
			player.sendMessage("The Dominion of Echoes is currently inactive. Please try again later.");
			return;
		}
		player.lock();
		player.insideRaid = true;

		player.startDoeTimer();

		player.deathStartListener = (e, k, h) -> e.lock(LockType.FULL_NULLIFY_DAMAGE);
		player.deathEndListener = (e, k, h) -> {
			e.player.inDynamicMap = false;
			e.player.getMovement().teleport(Position.of(1793, 3107));
			DominionOfEchoes.getActiveRaidForPlayer(e.player).getRooms().forEach(MapHandler::destroy);
			e.player.getCombat().setTruelyDead(false);
			e.player.getCombat().setDead(false);
			e.player.currentDynamicMap = null;
			e.player.currentMapHandler = null;
			e.player.teleportListener = null;
			e.player.deathEndListener = null;
			e.player.deathStartListener = null;
			e.player.logoutListener = null;
			e.player.insideRaid = false;
			e.player.dialogue(new MessageDialogue("You have been defeated!"));
		};

		player.getPacketSender().fadeOut();
		player.startEvent(event -> {
			event.delay(2);
			DominionOfEchoes.getActiveRaidForPlayer(player).movePlayerToNextInstance(player);
			player.getPacketSender().fadeIn();
			player.unlock();
		});
	}

	/**
	 * Handles the player's exit from the "Dominion of Echos" area.
	 * Teleports the player to a designated position upon interaction with the exit object.
	 *
	 * @param player The player who is leaving the Dominion of Echos area.
	 * @param object The game object triggering the exit (e.g., the exit portal or door).
	 */
	private static void leaveDoe(Player player, GameObject object) {
		if (Objects.nonNull(DominionOfEchoes.getActiveRaidForPlayer(player))) {
			DominionOfEchoes.getActiveRaidForPlayer(player).getRooms().forEach(MapHandler::destroy);
			DominionOfEchoes.getActiveRaids().remove(player.uuid());
		}
		player.getMovement().teleport(Position.of(1794, 3106));
		player.currentDynamicMap = null;
		player.currentMapHandler = null;
		player.teleportListener = null;
		player.deathEndListener = null;
		player.deathStartListener = null;
		player.inDynamicMap = false;
		player.logoutListener = null;
		player.insideRaid = false;
		player.face((Direction.WEST));
	}

	/**
	 * Prepares the player for entry into the Dominion of Echos.
	 * Locks the player, applies visual transitions, and initializes related settings.
	 *
	 * @param player The player entering the Dominion of Echos.
	 * @param object The game object triggering the entry (e.g., the entrance portal).
	 */
	private static void enterDoePrep(Player player, GameObject object) {
		if (!DominionOfEchoes.isDoeActive()) {
			player.sendMessage("The Dominion of Echoes is currently inactive. Please try again later.");
			return;
		}
		if (DominionOfEchoes.playerIsInRaid(player)) {
			DominionOfEchoes.getActiveRaids().remove(player.uuid());
			log.warn("Player {} attempted to enter the Dominion of Echos while already in a raid.", player.getName());
		}
		player.startEvent(event -> {
			player.lock();
			player.getPacketSender().fadeOut();
			event.delay(2);
			player.set("dominion_of_echos_completed", false);
			new DominionOfEchoes(player);
			player.teleportListener = plr -> {
				plr.sendMessage("Your teleport attempt echos into the darkness of the dominion...");
				return false;
			};
			player.logoutListener = new LogoutListener().onLogout(p ->
				DominionOfEchoes.getActiveRaidForPlayer(p).getRooms().forEach(MapHandler::destroy));

			player.getPacketSender().fadeIn();
			event.delay(1);
			player.unlock();
		});
	}
}
