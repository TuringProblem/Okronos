package io.ruin.model.activities.raids.chambersrework;

import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class XericParty {
	/*
	Default layout: Skeletal Mystics, Muttadile, Tightrope, Guardians, Shamans, Vanguards

	Starting room has west door, north door or a east door
	Do we need connecting rooms?

	Starting room needs north exit, mystics is south entrance

	Starting
	412, 648 0, 413 648 0, 414 648 0, 412 649 0, 413 649 0, 414 649 0, 412 650 0, 413 650 0, 414 650 0, 412 651 0, 413 651 0, 414 651 0

	Mystics
	412 655 1, 413 655 1, 414 655 1, 415 655 1, 412 656 1, 413 656 1, 414 656 1, 415 656 1,  412 657 1, 413 657 1, 414 657 1, 415 657 1, 412 658 1, 413 658 1, 414 658 1, 415 658 1,
	 */
	@Getter
	private final String leader;
	@Getter
	private final List<String> members;
	@Getter
	private final List<String> applications;

	@Getter
	boolean started = false;

	ActivityTimer timer;

	public XericParty(String leaderName) {
		this.leader = leaderName;
		this.members = new ArrayList<>();
		this.members.add(leaderName);
		this.applications = new ArrayList<>();
		timer = new ActivityTimer();
	}

	public void invite(Player inviter, Player invitee) {
		if (members.contains(invitee.getName())) {
			return;
		}
		invitee.dialogue(new OptionsDialogue(inviter.getName() + " has invited you to join their party.",
			new Option("Accept", () -> {
				if (invitee.party != null) {
					inviter.dialogue(new MessageDialogue(invitee.getName() + " is already in a party."));
					return;
				}
				if (inviter.party == null) {
					invitee.sendMessage("Party no longer exists.");
					return;
				}
				if (inviter.party.getMembers().size() >= 10) {
					invitee.sendMessage("This party is full.");
					return;
				}
				inviter.party.getMembers().add(invitee.getName());
				invitee.party = inviter.party;
				VarPlayerRepository.RAIDS_PARTY.set(invitee, inviter.getIndex());
				invitee.sendMessage("You've joined " + inviter.getName() + "'s raid party.");
				inviter.dialogue(new MessageDialogue(invitee.getName() + " has joined your raid party."));
			}),
			new Option("Decline", () -> {
				inviter.dialogue(new MessageDialogue(invitee.getName() + " has declined your request to join your raid party."));
				invitee.sendMessage("Your decline " + inviter.getName() + "'s request to join their party.");
			})
		));

	}

	public void apply(Player player) {
		if (applications.contains(player.getName())) {
			return;
		}
		applications.add(player.getName());
	}

	public void acceptApplication(String applicantName) {
		if (applications.contains(applicantName) && !members.contains(applicantName)) {
			members.add(applicantName);
			applications.remove(applicantName);
			Player leader = World.getPlayer(this.leader);
			getMembers().forEach(name -> {
				Player player = World.getPlayer(name);
				if (player != null) {
					player.sendMessage(applicantName + " has been accepted into the party.");
				}
			});
		}
	}

	public void rejectApplication(String applicantName) {
		if (applications.contains(applicantName)) {
			applications.remove(applicantName);
		}
	}

	public void enterRaid(Player player) {
		if (!player.getName().equals(leader) && !started) {
			player.sendMessage("You are not the leader of the party.");
			return;
		}
		if (player.getName().equalsIgnoreCase(leader)) {

			for (int i = ChambersManager.activeRaids.size() - 1; i >= 0; i--) {
				CustomXericRaid raid = ChambersManager.activeRaids.get(i);
				if (raid.currentParty.leader.equalsIgnoreCase(player.getName())) {
					raid.currentParty.members.forEach(name -> {
						Player plr = World.getPlayer(name);
						if (plr != null) {
							if (plr.getName().equalsIgnoreCase(raid.currentParty.leader)) {
								ChambersManager.activeRaids.remove(raid);
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
			started = true;
			CustomXericRaid raid = new CustomXericRaid();
			// raid.buildRaid(this);
			int raidId = ChambersManager.getActiveRaids().size();
			ChambersManager.addRaid(raid);
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
		}
		World.startEvent(e -> {
			e.delay(2);
			player.getCurrentToARaid().movePlayerToLobby(player);
			player.setToaSuppliesClaimed(0);
		});
	}

	public void removeMember(String playerName) {
		if (members.contains(playerName)) {
			members.remove(playerName);
			applications.remove(playerName);
		} else {
		}
	}

	public void createParty(Player player) {
		if (ChambersManager.getRaidParty(player) != null) {
			player.sendMessage("You are already in a party.");
			return;
		}
		XericParty party = new XericParty(player.getName());
		ChambersManager.addRaidParty(party);
		player.sendMessage("You have created a party.");

	}

	public void disbandParty(Player player) {
		if (!player.getName().equals(leader)) {
			player.sendMessage("You are not the leader of the party.");
			return;
		}
		ChambersManager.getActiveRaidParties().remove(this);
		ChambersManager.getActiveRaidParties().forEach(p -> {
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

}
