package io.ruin.model.activities.raids.tob.party;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.RaidingRestorations;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.raids.tob.dungeon.TheatreDungeon;
import io.ruin.model.activities.raids.tob.dungeon.room.TheatreRoom;
import io.ruin.model.activities.raids.tob.dungeon.room.VerzikRoom;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.util.ListenedList;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Represents a party in the Theatre of Blood
 *
 * @author ReverendDread on 7/15/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project Kronos
 */
@Getter
public class TheatreParty {

	public String leaderName; //The leader username
	private final int slot; //The party slot
	public ListenedList<String> users; //Userids in the party
	private final ListenedList<String> applicants; //Applicants applying for party
	private final List<String> blocked; //Players blocked from applying
	private final long tickCreatedOn; //The server tick the party was created on.
	@Setter
	private int preferedSize = 5; //default to full party size
	@Setter
	private int preferedLevel = 126;
	@Getter
	public TheatreDungeon dungeon;

	public int deaths = 0;
	public boolean perfectXarpus = false;
	public boolean perfectVerzik = false;
	public boolean perfectMaiden = false;
	public boolean perfectBloat = false;
	public boolean perfectSotetseg = false;
	public boolean perfectVasillas = false;
	public boolean wearingBarrows = true;

	public boolean dawnbringerClaimed = false;

	public List<Player> playersInRaid = new ArrayList<>();

	public HashMap<Player, Integer> playerDeaths = new HashMap<>();
	HashMap<Player, Boolean> supplyChestDeath = new HashMap<>();
	List<Integer> purpleLootChests = new ArrayList<>();

	public ActivityTimer overallTimer = new ActivityTimer();
	public ActivityTimer maidenTimer = new ActivityTimer();
	public ActivityTimer bloatTimer = new ActivityTimer();
	public ActivityTimer vasillasTimer = new ActivityTimer();
	public ActivityTimer sotetsegTimer = new ActivityTimer();
	public ActivityTimer xarpusTimer = new ActivityTimer();
	public ActivityTimer verzikTimer = new ActivityTimer();

	public long overallTimerEnd;
	public long maidenTimerEnd;
	public long bloatTimerEnd;
	public long vasillasTimerEnd;
	public long sotetsegTimerEnd;
	public long xarpusTimerEnd;
	public long verzikTimerEnd;


	public List<Player> deadPlayers = new ArrayList<>();

	/**
	 * Creates a new theatre party with the desired userId as leader.
	 *
	 * @param username
	 */
	public TheatreParty(String username, int slot) {
		this.leaderName = username;
		this.slot = slot;
		this.users = new ListenedList<>();
		this.applicants = new ListenedList<>();
		this.blocked = new ArrayList<>();
		this.tickCreatedOn = Server.currentTick();
		this.dungeon = new TheatreDungeon(this);
		registerListeners();
		this.users.add(username);
		TheatrePartyManager.instance().forUsername(username).ifPresent(this::displayMembers);
	}


	/**
	 * Notifies players in the party that the party is disbanded.
	 */
	public void disbandNotifiy() {
		for (String user : getUsers()) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(player -> {
				if (player.isVisibleInterface(Interface.TOB_PARTY_DETAILS)) {
					TheatrePartyManager.instance().openPartyList(player);
				}
				getUsers().remove(user);
				TheatrePartyManager.instance().sendBlankPartyMembers(user);
			});
		}
	}

	public void getChallengeTime(Player player) {
		long time = maidenTimer.stop(player, 0);
	}

	/**
	 * Performs the leave action on a user.
	 *
	 * @param username
	 * @param resign
	 */
	public void leave(String username, boolean resign) {
		TheatrePartyManager.instance()
			.forUsername(username)
			.ifPresent(player -> {
				if (isLeader(player) && getUsers().size() > 1) {
					// Transfer leadership to next player
					String newLeader = getUsers().stream()
						.filter(name -> !name.equals(username))
						.findFirst()
						.get();
					leaderName = newLeader;
					TheatrePartyManager.instance()
						.forUsername(newLeader)
						.ifPresent(p -> VarPlayerRepository.TOB_PARTY_LEADER.set(p, p.getIndex()));
				}

				getUsers().remove(username);
				playersInRaid.remove(player);
				player.currentParty = null;
				player.closeInterface(ToplevelComponent.MAINMODAL);
				player.inTob = false;
				player.insideRaid = false;

				if (resign)
					player.getMovement().teleport(TheatrePartyManager.OUTSIDE);

				getPlayersInRaid().forEach(user ->
					TheatrePartyManager.instance()
						.forUsername(user.getName())
						.ifPresent(p -> {
							displayMembers(p);
							p.sendMessage(Color.RED.wrap("%s has left the party.".formatted(StringUtils.capitalizeFirst(player.getName()))));
						}));

				if (getUsers().isEmpty())
					TheatrePartyManager.instance().deregister(this);

				player.setInvincible(false);
				player.teleportListener = null;
				TheatrePartyManager.instance().sendBlankPartyMembers(username);
			});
	}

	/**
	 * @param player
	 */
	public void displayMembers(Player player) {
		StringBuilder sb = new StringBuilder();
		for (int index = 0; index < 5; index++) {
			if (index < getUsers().size()) {
				String username = getUsers().get(index);
				TheatrePartyManager.instance()
					.forUsername(username)
					.ifPresent(user ->
						sb.append(StringUtils.capitalizeFirst(user.getName())).append("<br>")
					);
			}
			else {
				sb.append("-");
				if (index < 4)
					sb.append("<br>");
			}
		}
		player.getPacketSender().sendString(Interface.TOB_PARTY_MEMBERS_OVERLAY, 12, sb.toString());
	}

	public void invite(Player inviter, Player invitee) {
		invitee.dialogue(new OptionsDialogue("%s has invited you to join their party.".formatted(inviter.getName()),
			new Option("Accept", () -> {
				getUsers().add(invitee.getName());
				TheatrePartyManager.instance()
					.forUsername(invitee.getName())
					.ifPresent(p -> {
						if (p.isVisibleInterface(Interface.TOB_PARTY_DETAILS))
							TheatrePartyManager.instance().openPartyDetails(p);
					});
			}),
			new Option("Decline", () -> invitee.closeInterfaces())
		));

	}

	/**
	 * Performs a consumer on every user in the party.
	 *
	 * @param consumer
	 */
	public void forPlayers(Consumer<Player> consumer) {
		getUsers().stream()
			.map(userId -> TheatrePartyManager.instance().forUsername(userId))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(consumer);
	}


	/**
	 * Updates the party status for a player. (Indicated on the party hud)
	 *
	 * @param player
	 * @param status
	 */
	public static void updatePartyStatus(Player player, PartyStatus status) {
		VarPlayerRepository.THEATRE_OF_BLOOD.set(player, status.ordinal());
	}

	/**
	 * Checks if the player is the leader of this party.
	 *
	 * @param player
	 * @return
	 */
	public boolean isLeader(Player player) {
		return player.getName().equalsIgnoreCase(getLeaderName());
	}

	/**
	 * Gets the leader for this party.
	 *
	 * @return
	 */
	public Optional<Player> getLeader() {
		return World.getPlayerByName(getLeaderName());
	}

	/**
	 * Registers listeners for list of users.
	 */
	private void registerListeners() {
		users.postAdd((i) -> TheatrePartyManager.instance().forUsername(i).ifPresent(player -> {
			TheatreParty.updatePartyStatus(player, PartyStatus.IN_PARTY);
			VarPlayerRepository.TOB_PARTY_LEADER.set(player, TheatrePartyManager.instance().forUsername(getLeaderName()).orElse(player).getIndex());
			forPlayers(p -> {
				TheatrePartyManager.instance().refreshPartyDetails(p, this);
				displayMembers(p);
			});
		}));
		users.postRemove((i) -> TheatrePartyManager.instance().forUsername(i).ifPresent(player -> {
			TheatreParty.updatePartyStatus(player, PartyStatus.NO_PARTY);
			VarPlayerRepository.TOB_PARTY_LEADER.set(player, -1);
			TheatrePartyManager.instance().sendBlankPartyMembers(i);
			player.logoutListener = null;
			forPlayers(p -> {
				TheatrePartyManager.instance().refreshPartyDetails(p, this);
				displayMembers(p);
			});
		}));
		applicants.postAdd((i) -> {
			//refresh the player who sent the app
			TheatrePartyManager.instance().forUsername(i).ifPresent(player -> {
				if (player.isVisibleInterface(Interface.TOB_PARTY_DETAILS))
					TheatrePartyManager.instance().refreshPartyDetails(player, this);
			});
			//refresh users in the party
			forPlayers(p -> {
				TheatrePartyManager.instance().refreshPartyDetails(p, this);
			});
		});
		applicants.postRemove((i) -> {
			//refresh the player who sent the app
			TheatrePartyManager.instance().forUsername(i).ifPresent(player -> {
				if (player.isVisibleInterface(Interface.TOB_PARTY_DETAILS))
					TheatrePartyManager.instance().refreshPartyDetails(player, this);
			});
			//refresh users in the party
			forPlayers(p -> {
				TheatrePartyManager.instance().refreshPartyDetails(p, this);
			});
		});
		getLeader().ifPresent(leader -> {
			leader.logoutListener = new LogoutListener()
				.onLogout(player ->
					TheatrePartyManager.instance()
						.deregister(this));
		});
	}

	public void cleanupRaid() {
		if (dungeon != null)
			dungeon.destroy();

		getUsers().forEach(username ->
			TheatrePartyManager.instance()
				.forUsername(username)
				.ifPresent(p -> {
					p.currentParty = null;
					p.inTob = false;
					p.insideRaid = false;
					p.theatreOfBloodStage = 0;
					p.teleportListener = null;
					p.deathEndListener = null;
					p.deathStartListener = null;
					p.setInvincible(false);
					p.getCombat().init(p);
					p.getMovement().teleport(TheatrePartyManager.OUTSIDE);
					VarPlayerRepository.THEATRE_OF_BLOOD.reset(p); // set to spectator
				}));

		this.deadPlayers.clear();
		this.deaths = 0;
		this.playerDeaths.clear();
		this.supplyChestDeath.clear();
		resetTimers();
	}

	private void resetTimers() {
		this.overallTimer = new ActivityTimer();
		this.maidenTimer = new ActivityTimer();
		this.bloatTimer = new ActivityTimer();
		this.vasillasTimer = new ActivityTimer();
		this.sotetsegTimer = new ActivityTimer();
		this.xarpusTimer = new ActivityTimer();
		this.verzikTimer = new ActivityTimer();

		this.overallTimerEnd = 0;
		this.maidenTimerEnd = 0;
		this.bloatTimerEnd = 0;
		this.vasillasTimerEnd = 0;
		this.sotetsegTimerEnd = 0;
		this.xarpusTimerEnd = 0;
		this.verzikTimerEnd = 0;
	}

	public void finishRaid() {
		if (dungeon != null) {
			// Clean up rooms
			dungeon.rooms.values()
				.forEach(room -> {
					room.removeMultiArea();
					room.destroy();
				});
			dungeon.rooms.clear();
		}
		TheatrePartyManager.instance()
			.deregister(this);
	}

	private static final StatType[] SCALED_STATS = {
		StatType.Hitpoints,
		StatType.Defence,
		StatType.Attack,
		StatType.Strength,
		StatType.Magic,
		StatType.Ranged
	};

	public void scaleNPC(NPC npc, boolean verzik) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;
		factor = 1.0 + (0.20 * users.size());
		if (factor != 0) {
			for (StatType type : SCALED_STATS) {
				if (type == StatType.Defence)
					factor = 1.1 + (0.20 * users.size());
				else {
					factor = 1.5 + (0.20 * users.size());
				}
				npc.getCombat().getStat(type).fixedLevel *= factor;
				npc.getCombat().getStat(type).restore();
			}
		}
		if (verzik)
			scaleHP(npc, verzik);

	}

	public void scaleHP(NPC npc, boolean verzik) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor = verzik ? 3 : 2.0;
		factor *= (0.25 * users.size());
		if (factor != 0) {
			double newLevel = npc.getCombat().getStat(StatType.Hitpoints).fixedLevel * factor;
			npc.getCombat().getStat(StatType.Hitpoints).fixedLevel = (int) newLevel;
			npc.getCombat().getStat(StatType.Hitpoints).restore();
		}

	}


}