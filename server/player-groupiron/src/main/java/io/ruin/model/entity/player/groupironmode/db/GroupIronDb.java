package io.ruin.model.entity.player.groupironmode.db;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import core.task.Continuation;
import io.ruin.Server;
import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.entity.player.groupironmode.GroupIron;
import io.ruin.model.entity.player.groupironmode.GroupIronGroups;

public class GroupIronDb {
	private static final String[] PLAYER_NAME_COLUMNS = { "player1_name", "player2_name", "player3_name", "player4_name",
			"player5_name" };
	private static final String GROUP_DATA_TABLE = "group_data";

	public static Continuation<List<GroupIron>> fetchAll() {
		return Server.gameDb.exec(con -> {
			var result = new ArrayList<GroupIron>();
			String selectGroupDataSQL = "SELECT * FROM " + GROUP_DATA_TABLE;
			try (var statement = con.prepareStatement(selectGroupDataSQL);
					var resultSet = statement.executeQuery()) {

				while (resultSet.next()) {
					GroupIron group = new GroupIron();
					group.setGroupId(resultSet.getInt("group_id"));
					group.setGroupName(resultSet.getString("group_name"));
					group.setLeader(resultSet.getString("leader_name"));
					group.setHardcore(resultSet.getBoolean("hardcore"));
					group.setLivesRemaining(resultSet.getInt("lives_remaining"));
					group.setBankOccupied(resultSet.getBoolean("bank_occupied"));
					group.setBankOccupierName(resultSet.getString("bank_occupier_name"));
					group.setCreationDate(resultSet.getString("creation_date"));
					group.setGroupDifficulty(Difficulty.values()[resultSet.getInt("group_difficulty")]);
					for (int i = 1; i <= PLAYER_NAME_COLUMNS.length; i++) {
						var playerName = resultSet.getString(PLAYER_NAME_COLUMNS[i - 1]);
						if (!playerName.equalsIgnoreCase("null")) {
							group.getMembers().add(playerName);
						}
					}
					var leader = group.getLeader();
					if (!group.getMembers().stream().anyMatch(it -> it.equalsIgnoreCase(leader))) {
						if (!group.getMembers().isEmpty()) {
							group.getMembers().set(0, leader);
						} else {
							group.getMembers().add(leader);
						}
					}
					result.add(group);
				}
			}
			return result;
		});
	}

	public static Continuation.Void upsert(GroupIron group) {
		return Server.gameDb.exec(con -> {
			String updateGroupDataSQL = "UPDATE " + GROUP_DATA_TABLE + " " +
					"SET hardcore=?, lives_remaining=?, bank_occupied=?, bank_occupier_name=?, creation_date=?, group_difficulty=?, "
					+
					PLAYER_NAME_COLUMNS[0] + "=?, " +
					PLAYER_NAME_COLUMNS[1] + "=?, " +
					PLAYER_NAME_COLUMNS[2] + "=?, " +
					PLAYER_NAME_COLUMNS[3] + "=?, " +
					PLAYER_NAME_COLUMNS[4] + "=?" +
					" WHERE group_name=?";

			try (var statement = con.prepareStatement(updateGroupDataSQL)) {
				statement.setBoolean(1, group.isHardcore());
				statement.setInt(2, group.getLivesRemaining());
				statement.setBoolean(3, group.isBankOccupied());
				statement.setString(4, group.getBankOccupierName());
				statement.setString(5, group.getCreationDate());
				statement.setInt(6, group.getGroupDifficulty().ordinal());

				var members = group.getMembers();
				for (int i = 0; i < PLAYER_NAME_COLUMNS.length; i++) {
					statement.setString(7 + i, "NULL");
				}
				for (int i = 0; i < members.size(); i++) {
					statement.setString(7 + i, members.get(i));
				}
				statement.setString(12, group.getGroupName());
				statement.executeUpdate();
			}
		});
	}

	public static Continuation<Boolean> isGroupNameAvailable(String groupName) {
		return Server.gameDb.exec(con -> {
			var checkGroupNameSQL = "SELECT 1 FROM group_data WHERE group_name=? LIMIT 1";
			try (var statement = con.prepareStatement(checkGroupNameSQL)) {
				statement.setString(1, groupName);
				try (var resultSet = statement.executeQuery()) {
					return !resultSet.next();
				}
			}
		});
	}

	/// Inserts new group into database
	/// returns primary key or -1 when couldn't insert
	public static Continuation<Integer> insert(GroupIron group) {
		return Server.gameDb.exec(con -> {
			String insertGroupDataSQL = "INSERT INTO group_data " +
					"(group_name, leader_name, hardcore, lives_remaining, bank_occupied, bank_occupier_name, creation_date, group_difficulty, "
					+
					"player1_name, player2_name, player3_name, player4_name, player5_name) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, NULL, NULL, NULL)";

			try (var statement = con.prepareStatement(insertGroupDataSQL, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, group.getGroupName());
				statement.setString(2, group.getLeader());
				statement.setBoolean(3, group.isHardcore());
				statement.setInt(4, group.getLivesRemaining());
				statement.setBoolean(5, group.isBankOccupied());
				statement.setString(6, group.getBankOccupierName());
				statement.setString(7, group.getCreationDate());
				statement.setInt(8, group.getGroupDifficulty().ordinal());
				// Set player1_name as the group leader
				statement.setString(9, group.getLeader());

				var rowsAffected = statement.executeUpdate();
				if (rowsAffected <= 0) {
					return -1;
				}

				var generatedKeys = statement.getGeneratedKeys();
				if (!generatedKeys.next()) {
					return -1;
				}

				return generatedKeys.getInt(1);
			}
		});
	}

	public static Continuation<Boolean> groupExistsForLeader(String leaderName) {
		return Server.gameDb.exec(con -> {
			var checkGroupSQL = "SELECT 1 FROM group_data WHERE leader_name=? LIMIT 1";
			try (var statement = con.prepareStatement(checkGroupSQL)) {
				statement.setString(1, leaderName);
				try (var resultSet = statement.executeQuery()) {
					return resultSet.next();
				}
			}
		});
	}
}
