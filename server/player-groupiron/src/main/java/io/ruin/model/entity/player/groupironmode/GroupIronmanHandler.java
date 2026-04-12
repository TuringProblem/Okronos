package io.ruin.model.entity.player.groupironmode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.groupironmode.sorts.GroupIronmanTeamAverageSort;
import io.ruin.model.entity.player.groupironmode.sorts.GroupIronmanTeamDateSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class GroupIronmanHandler {

	private static final Logger log = LoggerFactory.getLogger(GroupIronmanHandler.class);

	private static List<GroupIronmanTeam> groupIronmanTeams = new ArrayList<>();

	private static final Gson gson = new Gson();

	private static final String SAVE = ServerWrapper.dataFolder.getAbsolutePath() + "/Groups/all_teams.json";

	public static Optional<GroupIronmanTeam> getPlayersTeam(Player player) {
		return groupIronmanTeams.stream().filter(e -> e.playerExists(player)).findFirst();
	}

	public static void handleLogout(Player player) {
		Optional<GroupIronmanTeam> getTeam = getPlayersTeam(player);
		if (getTeam.isPresent()) {
			getTeam.get().updatePlayer(player);
		}
//        saveTeams();
	}

	public static Optional<GroupIronmanTeam> createGroupIronmanTeam(Player player) {
		GroupIronmanTeam newTeam = GroupIronmanTeam.createTeam(player);

		if (getTeamByName(player.getName()).isPresent()) {
			return Optional.empty();
		}

		if (getPlayersTeam(player).isPresent()) {
			return Optional.empty();
		}
		groupIronmanTeams.add(newTeam);
		return Optional.of(newTeam);
	}

	public static Optional<GroupIronmanTeam> getTeamByName(String name) {
		return groupIronmanTeams.stream().filter(e -> e.getTeamName().equalsIgnoreCase(name)).findFirst();
	}

	public static boolean hasInvitation(Player player) {
		return getInvitation(player).isPresent();
	}

	public static void acceptInvitation(Player player) {
		Optional<GroupIronmanTeam> invitation = getInvitation(player);
		if (invitation.isPresent()) {
			invitation.get().acceptInvitation(player);
			player.sendMessage("You've accepted the invitation and joined the group");
		}
	}

	public static Optional<GroupIronmanTeam> getInvitation(Player player) {
		for (GroupIronmanTeam team : groupIronmanTeams) {
			Optional<String> invitation = team.getInvitation();
			if (invitation.isPresent()) {
//                System.out.println("Here is list of Team invites: " + groupIronmanTeams);
//                System.out.println("Here is list of Team invites: " + team);
				if (player.getName().equalsIgnoreCase(team.getInvitation().get())) {
					return Optional.of(team);
				}
			}
		}

		return Optional.empty();
	}

	public static List<GroupIronmanTeam> getLatestGroupTeams(int amount) {
		List<GroupIronmanTeam> teams = new ArrayList<>(groupIronmanTeams);
		Collections.sort(teams, GroupIronmanTeamDateSort.DATE_SORT.reversed());
		return teams.stream().filter(e -> e.getMembers().size() > 1)
			.limit(amount).collect(Collectors.toList());
	}

	public static List<GroupIronmanTeam> getBestGroupTeams(int count) {
		List<GroupIronmanTeam> teams = new ArrayList<>(groupIronmanTeams);
		Collections.sort(teams, GroupIronmanTeamAverageSort.AVERAGE_SORT);
		return teams.stream().filter(e -> e.getMembers().size() > 1)
			.limit(count).collect(Collectors.toList());
	}

	public static void deleteClan(GroupIronmanTeam team, Player player) {
		groupIronmanTeams = groupIronmanTeams.parallelStream()
			.filter(e -> !e.equals(team))
			.collect(Collectors.toList());
		player.sendMessage("You have deleted your clan");
	}

	public static void deleteClan(Player player) {
		Optional<GroupIronmanTeam> getTeam = getPlayersTeam(player);
		if (getTeam.isPresent()) {
			deleteClan(getTeam.get(), player);
		}
	}

	public static void loadTeams() {
//        System.out.println("we are loading file");
		try {
			File loadDirectory = new File(SAVE);
			List<String> contents = Files.readAllLines(loadDirectory.toPath());
			groupIronmanTeams = gson.fromJson(contents.get(0), new TypeToken<List<GroupIronmanTeam>>() {
			}.getType());
			log.info("Loaded " + groupIronmanTeams.size() + " group ironman teams.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveTeams() {
		try {
			File loadDirectory = new File(SAVE);
			String contents = gson.toJson(groupIronmanTeams);
//            Gson builder = new GsonBuilder().setPrettyPrinting().create();
			Files.write(loadDirectory.toPath(), contents.getBytes());
			groupIronmanTeams = gson.fromJson(contents, new TypeToken<List<GroupIronmanTeam>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private GroupIronmanHandler() throws IllegalAccessException {
		throw new IllegalAccessException("cannot init this class");
	}

}
