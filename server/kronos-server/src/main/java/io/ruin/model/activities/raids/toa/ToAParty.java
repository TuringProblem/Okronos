package io.ruin.model.activities.raids.toa;

import io.ruin.api.utils.StringUtils;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.dynamic.DynamicMap.DynamicMapBuildException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import lombok.Setter;
import org.slf4j.LoggerFactory;

import core.task.Continuation;
import core.task.Continuations;

import org.slf4j.Logger;

import static core.task.api.API.*;

public class ToAParty {
	private static final Logger log = LoggerFactory.getLogger(ToAParty.class);

	@Getter
	private String leader;
	@Getter
	private final List<String> members;
	@Getter
	private final List<String> membersInRaid = new ArrayList<>();
	@Getter
	private final List<String> applications;

	ActivityTimer timer;

	@Setter
	@Getter
	private int currentInvocationValue = 0;

	@Getter
	List<Invocations> activeInvocations = new ArrayList<>();

	public int raidId = -1;

	public ToAParty(String leaderName) {
		this.leader = leaderName;
		this.members = new ArrayList<>();
		this.members.add(leaderName);
		this.applications = new ArrayList<>();
		timer = new ActivityTimer();
	}

	public void updateCurrentInvocationValue(int value) {
		currentInvocationValue += value;
	}

	public void addInvocation(Invocations invocation) {
		activeInvocations.add(invocation);
		currentInvocationValue += invocation.invocationLevel;
	}

	public void applyForParty(String playerName) {
		if (applications.contains(playerName)) {
			return;
		}
		applications.add(playerName);
	}

	public void displayMembers(Player player) {
		StringBuilder sb = new StringBuilder();
		for (int index = 0; index < 5; index++) {
			if (index < getMembers().size()) {
				String username = getMembers().get(index);
				getMembers().forEach(user -> sb.append(StringUtils.capitalizeFirst(user)).append("<br>"));
			} else {
				sb.append("-");
				if (index < 4)
					sb.append("<br>");
			}
		}
		player.openInterface(ToplevelComponent.OVERLAY, Interface.TOB_PARTY_MEMBERS_OVERLAY);
		player.getPacketSender().sendString(Interface.TOB_PARTY_MEMBERS_OVERLAY, 12, sb.toString());
	}

	private boolean playerAlreadyInAParty(Player player) {
		if (TombsOfAmascutManager.getRaidParty(player) != null) {
			player.sendMessage("You are already in a party.");
			return true;
		}
		return false;
	}

	public void acceptApplication(String applicantName) {
		if (playerAlreadyInAParty(World.getPlayer(applicantName))) {
			applications.remove(applicantName);
			return;
		}
		if (applications.contains(applicantName) && !members.contains(applicantName)) {
			members.add(applicantName);
			applications.remove(applicantName);
			Player leader = World.getPlayer(this.leader);
			getMembers().forEach(name -> {
				Player player = World.getPlayer(name);
				if (player != null) {
					if (leader != null)
						player.currentToARaidId = leader.currentToARaidId;
					displayMembers(player);
				}
			});
		} else {
			System.out.println("No pending application found for " + applicantName);
		}
	}

	public void rejectApplication(String applicantName) {
		if (applications.contains(applicantName)) {
			applications.remove(applicantName);
			System.out.println(applicantName + "'s application has been rejected.");
		} else {
			System.out.println("No pending application found for " + applicantName);
		}
	}

	@Getter
	boolean started = false;

	boolean disabled = false;

	public void enterRaid(Player player) {
		if (disabled) {
			player.sendMessage("Temporarily disabled.");
			return;
		}
		if (!player.getName().equals(leader) && !started) {
			player.sendMessage("You are not the leader of the party.");
			return;
		}
		if (player.getName().equalsIgnoreCase(leader)) {
			for (int i = TombsOfAmascutManager.activeRaids.size() - 1; i >= 0; i--) {
				TombsOfAmascut raid = TombsOfAmascutManager.activeRaids.get(i);
				if (raid.currentParty.leader.equalsIgnoreCase(player.getName())) {
					new ArrayList<>(raid.currentParty.members).forEach(name -> {
						Player plr = World.getPlayer(name);
						if (plr != null) {
							if (plr.getName().equalsIgnoreCase(raid.currentParty.leader)) {
								TombsOfAmascutManager.activeRaids.remove(raid);
							} else {
								raid.currentParty.removeMember(plr.getName());
							}
						}
					});
				}
			}
			started = false;
		}

		if (!started) {
			start(player);
		}

		player.toaRaidTimer = new ActivityTimer();
		player.teleportListener = TombsOfAmascut::allowTeleport;
		World.startEvent(e -> {
			e.delay(2);
			if (player.getCurrentToARaid() == null) {
				log.error("Player " + player.getName() + " not in party, probably already started.");
				return;
			}
			player.getCurrentToARaid().movePlayerToLobby(player);
			player.setToaSuppliesClaimed(0);
		});
	}

	private void start(Player player) {
		TombsOfAmascut raid = new TombsOfAmascut();
		try {
			raid.buildRaid(this);
		} catch (DynamicMapBuildException e) {
			return;
		}
		started = true;
		int raidId = TombsOfAmascutManager.getActiveRaids().size();
		TombsOfAmascutManager.addRaid(raid);
		player.setToaSuppliesClaimed(0);
		player.currentToARaidId = raidId;
		player.getCurrentToARaid().currentParty.raidId = raidId;
		members.forEach(p -> {
			Player plr = World.getPlayer(p);
			if (plr != null) {
				plr.currentToARaidId = raidId;
				plr.sendMessage("The leader has begun the raid.");
			}
		});
		cleanupLoop(raid);
	}

	// Continous loop that waits for all members to be removed.
	private void cleanupLoop(TombsOfAmascut raid) {
		Continuations.schedule(() -> {
			var emptyTicks = 10;
			while (true) {
				// When everyone leaves the areas for 10 ticks, force destroy everything
				if (raid.hasPlayers())
					emptyTicks = 10;
				else
					emptyTicks -= 1;

				var removeAll = emptyTicks == 0;

				var toRemove = new HashSet<String>();
				for (var member : members) {
					var pMember = World.getPlayer(member);
					if (removeAll || pMember == null || !pMember.isOnline()) {
						toRemove.add(member);
					}
				}

				toRemove.forEach(members::remove);

				if (this.members.size() == 0) {
					if (TombsOfAmascutManager.activeRaids.contains(raid)) {
						raid.destroyMaps();
						TombsOfAmascutManager.removeRaid(raid);
					}
					break;
				}
				sleep(1);
			}
		});
	}

	public void removeMember(String playerName) {
		if (members.contains(playerName)) {
			members.remove(playerName);
			applications.remove(playerName);
		}
	}

	public void createParty(Player player) {
		if (TombsOfAmascutManager.getRaidParty(player) != null) {
			player.sendMessage("You are already in a party.");
			return;
		}
		ToAParty party = new ToAParty(player.getName());
		TombsOfAmascutManager.addRaidParty(party);
		player.sendMessage("You have created a party.");
		displayMembers(player);
	}

	public void disbandParty(Player player) {
		if (!player.getName().equals(leader)) {
			player.sendMessage("You are not the leader of the party.");
			return;
		}
		TombsOfAmascutManager.getActiveRaidParties().remove(this);
		TombsOfAmascutManager.getActiveRaidParties().forEach(p -> {
			p.getMembers().forEach(m -> {
				if (player.getName().equalsIgnoreCase(m)) {
					p.getMembers().remove(m);
				}
			});
		});
		members.forEach(p -> {
			Player plr = World.getPlayer(p);
			if (plr != null) {
				plr.sendMessage("The party has been disbanded.");
			}
		});
	}

	public String convertTimeInLongToText() {
		long time = timer.getTime();
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
	}

	public void toggleInvocation(Invocations invocation) {
		if (activeInvocations.contains(invocation)) {
			if (invocation == Invocations.OVERCLOCKED2) {
				if (activeInvocations.contains(Invocations.INSANITY)) {
					activeInvocations.remove(Invocations.INSANITY);
					currentInvocationValue -= Invocations.INSANITY.invocationLevel;
				}
			}
			if (invocation == Invocations.OVERCLOCKED) {
				if (activeInvocations.contains(Invocations.OVERCLOCKED2)) {
					activeInvocations.remove(Invocations.OVERCLOCKED2);
					currentInvocationValue -= Invocations.OVERCLOCKED2.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.INSANITY)) {
					activeInvocations.remove(Invocations.INSANITY);
					currentInvocationValue -= Invocations.INSANITY.invocationLevel;
				}
			}
			if (invocation == Invocations.WALK_THE_PATH) {
				if (activeInvocations.contains(Invocations.PATHSEEKER)) {
					activeInvocations.remove(Invocations.PATHSEEKER);
					currentInvocationValue -= Invocations.PATHSEEKER.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.PATHFINDER)) {
					activeInvocations.remove(Invocations.PATHFINDER);
					currentInvocationValue -= Invocations.PATHFINDER.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.PATHMASTER)) {
					activeInvocations.remove(Invocations.PATHMASTER);
					currentInvocationValue -= Invocations.PATHMASTER.invocationLevel;
				}
			}
			activeInvocations.remove(invocation);
			currentInvocationValue -= invocation.invocationLevel;
		} else {
			if (invocation == Invocations.TRY_AGAIN || invocation == Invocations.PERSISTANCE
					|| invocation == Invocations.SOFTCORE_RUN || invocation == Invocations.HARDCORE_RUN) {
				if (activeInvocations.contains(Invocations.TRY_AGAIN)) {
					activeInvocations.remove(Invocations.TRY_AGAIN);
					currentInvocationValue -= Invocations.TRY_AGAIN.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.PERSISTANCE)) {
					activeInvocations.remove(Invocations.PERSISTANCE);
					currentInvocationValue -= Invocations.PERSISTANCE.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.SOFTCORE_RUN)) {
					activeInvocations.remove(Invocations.SOFTCORE_RUN);
					currentInvocationValue -= Invocations.SOFTCORE_RUN.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.HARDCORE_RUN)) {
					activeInvocations.remove(Invocations.HARDCORE_RUN);
					currentInvocationValue -= Invocations.HARDCORE_RUN.invocationLevel;
				}

			}
			if (invocation == Invocations.WALK_FOR_IT || invocation == Invocations.JOG_FOR_IT
					|| invocation == Invocations.RUN_FOR_IT || invocation == Invocations.SPRINT_FOR_IT) {
				if (activeInvocations.contains(Invocations.WALK_FOR_IT)) {
					activeInvocations.remove(Invocations.WALK_FOR_IT);
					currentInvocationValue -= Invocations.WALK_FOR_IT.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.JOG_FOR_IT)) {
					activeInvocations.remove(Invocations.JOG_FOR_IT);
					currentInvocationValue -= Invocations.JOG_FOR_IT.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.RUN_FOR_IT)) {
					activeInvocations.remove(Invocations.RUN_FOR_IT);
					currentInvocationValue -= Invocations.RUN_FOR_IT.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.SPRINT_FOR_IT)) {
					activeInvocations.remove(Invocations.SPRINT_FOR_IT);
					currentInvocationValue -= Invocations.SPRINT_FOR_IT.invocationLevel;

				}
			}
			if (invocation == Invocations.NEED_SOME_HELP || invocation == Invocations.NEED_LESS_HELP
					|| invocation == Invocations.NO_HELP_NEEDED) {
				if (activeInvocations.contains(Invocations.NEED_SOME_HELP)) {
					activeInvocations.remove(Invocations.NEED_SOME_HELP);
					currentInvocationValue -= Invocations.NEED_SOME_HELP.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.NEED_LESS_HELP)) {
					activeInvocations.remove(Invocations.NEED_LESS_HELP);
					currentInvocationValue -= Invocations.NEED_LESS_HELP.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.NO_HELP_NEEDED)) {
					activeInvocations.remove(Invocations.NO_HELP_NEEDED);
					currentInvocationValue -= Invocations.NO_HELP_NEEDED.invocationLevel;
				}
			}
			if (invocation == Invocations.INSANITY) {
				if (!activeInvocations.contains(Invocations.OVERCLOCKED2)) {
					Player player = World.getPlayer(leader);
					if (player != null) {
						player.sendMessage("You must have the Overclocked 2 invocation active to add this invocation.");
						return;
					}
				}
			}
			if (invocation == Invocations.OVERCLOCKED2) {
				if (!activeInvocations.contains(Invocations.OVERCLOCKED)) {
					Player player = World.getPlayer(leader);
					if (player != null) {
						player.sendMessage("You must have the Overclocked invocation active to add this invocation.");
						return;
					}
				}
			}
			if (invocation == Invocations.PATHSEEKER || invocation == Invocations.PATHFINDER
					|| invocation == Invocations.PATHMASTER) {
				if (!activeInvocations.contains(Invocations.WALK_THE_PATH)) {
					Player player = World.getPlayer(leader);
					if (player != null) {
						player.sendMessage("You must have the Walk The Path invocation active to add this invocation.");
						return;
					}
				}
				if (activeInvocations.contains(Invocations.PATHSEEKER)) {
					activeInvocations.remove(Invocations.PATHSEEKER);
					currentInvocationValue -= Invocations.PATHSEEKER.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.PATHFINDER)) {
					activeInvocations.remove(Invocations.PATHFINDER);
					currentInvocationValue -= Invocations.PATHFINDER.invocationLevel;
				}
				if (activeInvocations.contains(Invocations.PATHMASTER)) {
					activeInvocations.remove(Invocations.PATHMASTER);
					currentInvocationValue -= Invocations.PATHMASTER.invocationLevel;
				}
			}

			activeInvocations.add(invocation);
			currentInvocationValue += invocation.invocationLevel;
		}
	}

}
