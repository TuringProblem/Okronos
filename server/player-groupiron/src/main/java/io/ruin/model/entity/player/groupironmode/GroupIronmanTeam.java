package io.ruin.model.entity.player.groupironmode;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.utility.Misc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GroupIronmanTeam {

	private String teamName;
	private String leaderName;
	private List<GroupIronmanMember> members;
	private Date dateStated;
	private Optional<String> invitation = Optional.empty();

	public static GroupIronmanTeam createTeam(Player player) {
		List<GroupIronmanMember> members = new ArrayList<>();
		members.add(new GroupIronmanMember(player));
		return new GroupIronmanTeam().setDateStated(Date.from(Instant.now()))
			.setLeaderName(player.getName())
			.setTeamName(player.getName())
			.setMembers(members);
	}

	public void acceptInvitation(Player player) {
		invitation = Optional.empty();
		members.add(new GroupIronmanMember(player));
	}

	public void sendInvitations(Player inviter, Player target) { // Why is there two? thi sorry feeding bby i vc u
		invitation = Optional.of(target.getName());
		target.sendMessage(inviter.getName() + ":invite:");
		inviter.sendMessage("Request send to " + target.getName());
	}

	public boolean isOnline() {
		return !getOnlineMembers().isEmpty();
	}

	public String getOnlineStatusText() {
		return isOnline() ? "@gre@Online" : "@red@Offline";
	}

	public void setTeamName(Player player, String teamName) {
		if (player.getName().equalsIgnoreCase(leaderName)) {
			this.teamName = teamName;
		}
	}

	/**
	 * Updates the player
	 *
	 * @param player - the player too update
	 */
	public void updatePlayer(Player player) {
		Optional<GroupIronmanMember> member = getMember(player.getName());
		if (!member.isPresent()) {
			return;
		}
		member.get().update(player);
	}


	public Optional<GroupIronmanMember> getMember(String username) {
		return members.stream().filter(e -> e.getUsername().equalsIgnoreCase(username)).findFirst();
	}

	/**
	 * Checks a player exists within the team
	 *
	 * @param player - the player we are checking
	 * @return true if the player exists
	 */
	public boolean playerExists(Player player) {
		return members.stream().filter(e -> e.getUsername().equalsIgnoreCase(player.getName())).findAny().isPresent();

	}

	public boolean isTeamMember(String name) {
		return members.stream().filter(e -> e.getUsername().equalsIgnoreCase(name)).findFirst().isPresent();
	}

	public boolean isTeamMember(Player player) {
		return isTeamMember(player.getName());
	}

	/**
	 * Gets a list of online players
	 *
	 * @return - a list of online players
	 */
	public List<Player> getOnlineMembers() {
		List<Player> players = new ArrayList<>();
		for (GroupIronmanMember member : members) {
			Optional<Player> online = member.getPlayer();
			if (online.isPresent()) {
				players.add(online.get());
			}
		}
		return players;
	}

	/**
	 * A method to try and invite a player to a clan
	 *
	 * @param inviter - the person inviting
	 * @param target  - the target to invite
	 * @return - true if the target can be invited
	 */
	public boolean canInvite(Player inviter, Player target) {//pw's is 1
		if (target.getStats().totalLevel >= 500) {
			inviter.sendMessage(Color.RED.wrap("You can't invite this player, they need to be below 500 total level."));
			return false;
		}
		if (!target.getGameMode().isGroupIronman()) {
			inviter.sendMessage(Color.RED.wrap("This player isn't a group Ironman."));
			return false;
		}

		if (GroupIronmanHandler.hasInvitation(target)) {
			inviter.sendMessage(Color.RED.wrap("This player already has a pending request."));
			return false;
		}

		if (isTeamMember(target)) {
			inviter.sendMessage(Color.RED.wrap("This player is already on your team."));
			return false;
		}

		if (!leaderName.equalsIgnoreCase(inviter.getName())) {
			inviter.sendMessage(Color.RED.wrap("You have to be group leader to invite others."));
			return false;
		}

		if (isTeamFull()) {
			inviter.sendMessage(Color.RED.wrap("Your team is already full."));
			return false;
		}

//        if(GroupIronmanHandler.getPlayersTeam(target).isPresent()) {
//            inviter.sendMessage(Color.RED.wrap("This player already has a team."));
//            return false;
//        }

		return true;
	}

	/**
	 * Sends an invitation to the person
	 *
	 * @param target - the target player
	 */
	public void sendInvitation(Player inviter, Player target) {
		invitation = Optional.of(target.getName());
		target.sendMessage(inviter.getName() + ":invite:");
		target.getPacketSender().sendMessage(inviter.getName() + " wishes to Invite you to suck dick with you.", target.getName(), 101);
		inviter.sendMessage("Request sent to " + target.getName());

	}

	public boolean isTeamFull() {
		return members.size() >= 4;
	}

	public int getAverageCombatLevel() {
		int count = members.size();
		int total = members.stream().mapToInt(GroupIronmanMember::getCombatLevel).sum();
		return (int) Math.abs((float) total / ((float) count));
	}

	public long getAverageTotalXp() {
		int count = members.size();
		long total = members.stream().mapToLong(GroupIronmanMember::getTotalXp).sum();
		return (long) Math.abs((float) total / ((float) count));
	}

	public int getAverageTotalLevel() {
		int count = members.size();
		int total = members.stream().mapToInt(GroupIronmanMember::getTotalLevel).sum();
		return (int) Math.abs((float) total / ((float) count));
	}

	public String getTeamName() {
		return teamName;
	}

	public GroupIronmanTeam setTeamName(String teamName) {
		this.teamName = teamName;
		return this;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public GroupIronmanTeam setLeaderName(String leaderName) {
		this.leaderName = leaderName;
		return this;
	}

	public List<GroupIronmanMember> getMembers() {
		return members;
	}

	public GroupIronmanTeam setMembers(List<GroupIronmanMember> members) {
		this.members = members;
		return this;
	}

	public Date getDateStated() {
		return dateStated;
	}

	public GroupIronmanTeam setDateStated(Date dateStated) {
		this.dateStated = dateStated;
		return this;
	}

	public boolean isLeader() {
		return members.stream().filter(m -> m.getUsername().equalsIgnoreCase(leaderName)).findAny().isPresent();
	}

	public void kickMember(Player p, String toKick) {
		//Optional<GroupIronmanTeam> getTeam = p.getGroupIronmanTeam();

		if (!p.getGameMode().isGroupIronman()) {
			p.sendMessage("Only Group Ironman players can use this command");
			return;
		}
//        if(!getTeam.isPresent()) {
//            p.sendMessage("You need to be in a ironman group to use this command");
//            return;
//        }
		if (!isLeader()) {
			p.sendMessage("Only your team leader can use this command");
			return;
		}
		if (toKick == null) {
			p.sendMessage("The player you are trying to kick doesn't exist");
			return;
		}
		if (isTeamMember(toKick)) {
			int index = getMemberByIndex(toKick);
			if (index != -1) {
				this.members.remove(index);
				GroupIronmanHandler.saveTeams();
				p.sendMessage("You have successfully kicked " + toKick + " from your Group");
				return;
			}
			return;
		}
		p.sendMessage("Unable to kick " + toKick + ". They are not apart of your team");
		return;
	}

	public int getMemberByIndex(String name) {
		for (int i = 0; i < members.size(); i++) {
			if (members.get(i) != null) {
				if (members.get(i).getUsername().equalsIgnoreCase(name))
					return i;
			}
		}
		return -1;
	}

	public Optional<String> getInvitation() {
		return invitation;
	}

	public GroupIronmanTeam setInvitation(Optional<String> invitation) {
		this.invitation = invitation;
		return this;
	}

	public String getMemberAsString() {
		StringBuilder builder = new StringBuilder();
		for (GroupIronmanMember member : members) {
			builder.append(member.getUsername()).append(", ");
		}
		return Misc.optimizeText(builder.toString().substring(0, builder.length() - 2));
	}

}
