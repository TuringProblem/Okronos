package io.ruin.model.entity.player.groupironmode;

import io.ruin.Server;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.groupironmode.db.GroupIronDb;
import io.ruin.model.entity.player.groupironmode.hook.Attributes;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Broadcast;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.Date;

import core.task.Continuations;

@ExtensionMethod(Attributes.class)
public class GroupIron {
	public static final int MAX_GROUP_MEMBERS = 5;

	public static GroupIron create(Player leader) {
		GroupIron group = new GroupIron();
		group.setLeader(leader.getName());
		group.setHardcore(leader.getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN);
		group.addMember(leader.getName(), false);
		group.setCreationDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
		group.setGroupDifficulty(leader.getDifficulty());
		return group;
	}

	public static void chooseGroupName(Player player) {
		propmtForGroupCreation(player, "What would you like your group to be called?");
	}

	public static void createGroup(Player player, String groupName) {
		if (player.getGameMode() != GameMode.GROUP_IRONMAN && player.getGameMode() != GameMode.HARDCORE_GROUP_IRONMAN) {
			player.sendMessage("You must be a group ironman to create a group.");
			return;
		}

		Continuations.schedule(() -> {
			// Check if a group with the same leader name already exists
			if (GroupIronDb.groupExistsForLeader(player.getName()).await()) {
				player.sendMessage("You already have a group. You cannot create a new one.");
				return;
			}

			var group = create(player);
			group.setGroupName(groupName);
			if (player.getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN) {
				group.livesRemaining = 5;
			}

			var groupId = GroupIronDb.insert(group).await();
			group.setGroupId(groupId);
			GroupIronGroups.addGroup(group);
			player.sendMessage("You have successfully created the group: " + groupName + ".");
			player.newGroupId = group.getGroupId();
			GroupSettingsTabInterface.open(player);
		});

	}


	private static void propmtForGroupCreation(Player player, String prompt) {
		player.stringInput(prompt, s -> {
			Continuations.schedule(() -> {
				var available = GroupIronDb.isGroupNameAvailable(s).await();
				if (!available) {
					propmtForGroupCreation(player, "Sorry that group name is already taken, try another one.");
					return;
				}
				createGroup(player, s);
			});

		});
	}

	@Getter
	@Setter
	List<String> members = new ArrayList<>();

	@Setter
	@Getter
	boolean hardcore = false;

	@Getter
	@Setter
	int livesRemaining = 0;

	@Setter
	@Getter
	public boolean bankOccupied = false;

	@Setter
	@Getter
	public String bankOccupierName = "";

	@Setter
	@Getter
	private String leader;

	@Getter
	@Setter
	private String groupName;

	@Setter
	@Getter
	private String creationDate;

	@Setter
	@Getter
	private Difficulty groupDifficulty;

	@Getter
	private long[] playTimes = new long[5];

	@Getter
	@Setter
	private int groupId;

	@Setter
	@Getter
	private GroupIronInvitation invitation;

	public void addMember(String playerName, boolean newMember) {
		if (playerName == null || playerName.equalsIgnoreCase("null")) {
			return;
		}

		if (members.size() >= MAX_GROUP_MEMBERS) {
			return;
		}

		if (this.members.contains(playerName)) {
			return;
		}

		this.members.add(playerName);
		if (newMember) {
			notifyGroupMembers(playerName + " has joined the group!");
		}

		Continuations.schedule(() -> {
			GroupIronDb.upsert(this).await();
		});

	}

	public boolean playerInGroup(String playerName) {
		if (playerName.equalsIgnoreCase(this.leader)) {
			return true;
		}
		for (String member : members) {
			if (member.equalsIgnoreCase(playerName)) {
				return true;
			}
		}
		return false;
	}

	public void updateGroupLives(int change) {
		if (this.livesRemaining <= 0) {
			return;
		}
		this.livesRemaining += change;

		if (this.livesRemaining < 1) {
			Broadcast.GLOBAL.sendPlain(Color.RED.wrap(Icon.HCIM_DEATH.tag() + "The hardcore group " + getGroupName()
					+ " has lost all their lives and are now a regular group!"));
			this.members.forEach(member -> {
				var player = World.getPlayer(member);
				if (player == null) {
					return;
				}
				if (player.getGameMode() != GameMode.HARDCORE_GROUP_IRONMAN) {
					return;
				}

				player.sendMessage("Your group has lost all of their lives and your hardcore group status has been revoked.");
				VarPlayerRepository.IRONMAN_MODE.set(player, 4);
				this.setHardcore(false);
			});
		} else {
			notifyGroupMembers("Your group now has " + livesRemaining + " lives remaining.");
		}
		Continuations.schedule(() -> {
			GroupIronDb.upsert(this).await();
		});
	}

	public void leaveGroup(Player player) {
		if (player.getName().equalsIgnoreCase(leader)) {
			player.sendMessage("You can't leave the group as the leader!");
			return;
		}

		player.dialogue(
				new YesNoDialogue("Are you sure you want to leave the group?", "You will lose access to the group bank.",
						player.getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN ? new Item(26170) : new Item(26156), () -> {
							members.remove(player.getName());
							player.newGroupId = 0;
							Instant now = Instant.now();
							player.groupLeaveInEpoch = now.getEpochSecond();
							player.getMovement().teleport(3761, 3668, 0);
							player
									.sendMessage("You have left the group, you must join or create a new one before leaving the island");
							notifyGroupMembers(player.getName() + " has left the group.");
							Continuations.schedule(() -> {
								GroupIronDb.upsert(this).await();
							});
						}));
	}

	public void notifyGroupMembers(String message) {
		for (String member : members) {
			Player player = World.getPlayer(member);
			if (player != null)
				player.sendMessage(message);
		}
	}

	private String getMemberName(int index) {
		if (index < 0 || index >= members.size())
			return null;
		return members.get(index);
	}

}
