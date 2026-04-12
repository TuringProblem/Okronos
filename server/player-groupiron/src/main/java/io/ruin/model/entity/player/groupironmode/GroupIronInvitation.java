package io.ruin.model.entity.player.groupironmode;

import io.ruin.model.entity.player.groupironmode.hook.Attributes;
import io.ruin.model.entity.player.groupironmode.db.*;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Bounds;

import java.time.Duration;
import java.time.Instant;

import core.task.Continuations;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import lombok.Data;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
@Data
public class GroupIronInvitation {
	public static final Bounds INVITE_ZONE = new Bounds(3724, 3641, 3780, 3685, 0);

	public final GroupIron group;
	public final long expirationTime;
	public final Player targetPlayer;

	private static final int INVITATION_EXPIRATION_TIME = 60;

	public static void sendInvitation(Player inviter, Player invitee, GroupIron group) {
		long expirationTime = System.currentTimeMillis() + (INVITATION_EXPIRATION_TIME * 1000L);

		inviter.getGroupIron().setInvitation(new GroupIronInvitation(group, expirationTime, invitee));

		invitee.dialogue(
				new MessageDialogue(inviter.getName()
						+ " has invited you to their Group Ironman team. Would you like to join? This cannot be undone."),
				new OptionsDialogue(
						new Option("Yes, I'll join", () -> {
							invitee.unlock();
							invitee.closeInterfaces();
							acceptInvitation(inviter, invitee);
						}),
						new Option("No thanks", () -> {
							invitee.closeInterfaces();
							inviter.sendMessage(invitee.getName() + " has declined your invitation.");
							invitee.unlock();
							inviter.unlock();
							inviter.closeInterfaces();
						})));
	}

	public static void acceptInvitation(Player inviter, Player player) {
		var group = inviter.getGroupIron();

		if (group.getMembers().size() >= 5) {
			player.sendMessage("This group is full.");
			return;
		}

		if (player.groupLeaveInEpoch != 0) {
			var now = Instant.now();
			var timePassed = now.getEpochSecond() - player.groupLeaveInEpoch;
			var requiredWaitTime = Duration.ofDays(7).getSeconds();

			if (timePassed < requiredWaitTime) {
				player.sendMessage("You must wait 7 days after leaving a group to join another one.");
				return;
			}
		}

		var invitation = group.getInvitation();

		if (invitation != null && invitation.getTargetPlayer() == player) {
			invitation = null;

			group.addMember(player.getName(), true);
			player.newGroupId = group.getGroupId();
			player.groupLeaveInEpoch = 0;

			Continuations.schedule(() -> {
				GroupIronDb.upsert(group).await();
				player.sendMessage("You have accepted the group invitation and joined the group.");
				GroupSettingsTabInterface.open(player);
				GroupSettingsTabInterface.open(inviter);
			});
		} else {
			player.sendMessage("You don't have a pending group invitation.");
		}
	}

	public static void invite(Player inviter, Player invitee) {
		if (inviter == null || invitee == null) {
			return;
		}

		var inviterGroup = inviter.getGroupIron();

		if (inviterGroup == null) {
			inviter
					.dialogue(new NPCDialogue(7941, "You must be in a group to invite someone, type ::creategroup to form one."));
			return;
		}

		if (!inviter.isGroupIronman() || !invitee.isGroupIronman()) {
			return;
		}

		if (inviterGroup.getMembers().size() >= 5) {
			inviter.sendMessage("You can't invite more than 5 players to your group.");
			return;
		}

		if (!inviter.getName().equalsIgnoreCase(inviterGroup.getLeader())) {
			return;
		}

		if (inviterGroup.members.contains(invitee.getName())) {
			inviter.sendMessage("This player is already in your group.");
			return;
		}

		if (inviterGroup.members.size() >= GroupIron.MAX_GROUP_MEMBERS) {
			inviter.sendMessage("Your group is already full.");
			return;
		}

		if (invitee.isVisibleInterface(Interface.SHOP) || invitee.isVisibleInterface(Interface.TRADE_SCREEN)
				|| invitee.isVisibleInterface(Interface.TRADE_CONFIRMATION) || invitee.isVisibleInterface(Interface.BANK)
				|| invitee.isVisibleInterface(Interface.NPC_DIALOGUE)
				|| invitee.isVisibleInterface(Interface.PLAYER_DIALOGUE)) {
			inviter.sendMessage("This player is currently busy.");
			return;
		}

		if (!invitee.getPosition().inBounds(INVITE_ZONE) || !inviter.getPosition().inBounds(INVITE_ZONE)) {
			inviter.sendMessage("Both players must be on the group island to invite someone to your team.");
			return;
		}

		if (invitee.isLocked()) {
			inviter.sendMessage("This player is currently busy.");
			return;
		}

		if (inviter.getGameMode() != GameMode.GROUP_IRONMAN && inviter.getGameMode() != GameMode.HARDCORE_GROUP_IRONMAN) {
			inviter.sendMessage("You need to be a group ironman to invite others.");
			return;
		}

		if (invitee.getGameMode() != GameMode.GROUP_IRONMAN && invitee.getGameMode() != GameMode.HARDCORE_GROUP_IRONMAN) {
			inviter.sendMessage("You can only invite other group iron man players.");
			return;
		}

		if (inviter.getGameMode().ordinal() != invitee.getGameMode().ordinal()) {
			inviter.sendMessage("You can only invite players with the same game mode as you.");
			return;
		}

		if (invitee.getGameMode() != inviter.getGameMode()) {
			inviter.sendMessage("This player isn't a group Ironman.");
			return;
		}

		if (!inviter.getName().equalsIgnoreCase(inviterGroup.getLeader())) {
			inviter.sendMessage("You have to be group leader to invite others.");
			return;
		}

		var inviteeGroup = invitee.getGroupIron();

		if (inviteeGroup != null) {
			inviter.sendMessage("This player is already in a group.");
			return;
		}

		if (invitee.groupLeaveInEpoch != 0) {
			var now = Instant.now();
			var timePassed = now.getEpochSecond() - invitee.groupLeaveInEpoch;
			var requiredWaitTime = Duration.ofDays(7).getSeconds();

			if (timePassed < requiredWaitTime) {
				inviter.sendMessage(
						"They are on a cooldown from leaving a group, they must wait 7 days before joining another one.");
				return;
			}
		}

		if (inviterGroup.getInvitation() != null
				&& inviterGroup.getInvitation().targetPlayer.getName().equalsIgnoreCase(invitee.getName())) {
			return;
		}

		sendInvitation(inviter, invitee, inviterGroup);
	}
}
