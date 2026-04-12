package io.ruin.services;

import io.ruin.Server;
import io.ruin.model.World;
import io.ruin.model.activities.wilderness.Wilderness;

import java.sql.PreparedStatement;

import core.task.Continuation;
import core.task.Continuations;

import static core.task.api.API.*;

public final class PlayersOnline {

	public static void register() {
		if (!World.isDev() && !World.isBeta()) {
			queue(() -> {
				while (true) {
					sleep(10000 / 600); // 10 seconds
					Server.siteDb.execute(con -> {
						try (PreparedStatement statement = con.prepareStatement(
								"UPDATE players_online SET players_online = ?, wilderness_count=?, instance_count =?")) {
							statement.setInt(1, World.playerCount());
							statement.setInt(2, Wilderness.players.size());
							statement.executeUpdate();
						}
					});
				}
			});
		}

		if (!World.isDev()) {
			queue(() -> {
				while (true) {
					sleep(1800000 / 600); // 30 minutes
					Server.gameDb.execute(con -> {
						try (PreparedStatement statement = con
								.prepareStatement("INSERT INTO online_statistics (world, players) VALUES (?, ?)")) {
							statement.setInt(1, World.id);
							statement.setInt(2, World.playerCount());
							statement.executeUpdate();
						}
					});
				}
			});
		}
	}

}
