package io.ruin.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.entity.player.GameMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Highscores {//implements Runnable {
 /*

    private static final String HOST = "92.205.5.29";
    private static final String USER = "reason_admin";
    private static final String PASS = "Norton102!";
    private static final String DATABASE = "reason_hiscores";
    private static final String TABLE = "hs_users";

   // private static final HikariDataSource dataSource;
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void register() {
       /* HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + HOST + ":3306/" + DATABASE);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);

    }

    private final Player player;

    public Highscores(Player player) {
        this.player = player;
    }

    public static void submit(Player player) {
        executor.submit(new Highscores(player));
    }

    @Override
    public void run() {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Disable auto-commit for batch processing

            String name = player.getName();

            try (PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM " + TABLE + " WHERE username=?")) {
                stmt1.setString(1, name);
                stmt1.addBatch(); // Add to batch
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(generateQuery())) {
                stmt2.setString(1, name);
                stmt2.setInt(2, player.getRank());
                stmt2.setInt(3, getDifficultyCode(player.getDifficulty()));
                stmt2.setInt(4, getGameModeCode(player.getGameMode()));
                stmt2.setInt(5, player.getStats().totalLevel);
                stmt2.setLong(6, player.getStats().totalXp);

                for (int i = 0; i < 23; i++) {
                    stmt2.setInt(7 + i, (int) player.getStats().get(i).experience);
                }

                stmt2.addBatch(); // Add to batch
            }

            // Execute batch
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = dataSource.getConnection()) {
                conn.rollback(); // Rollback in case of failure
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    private int getDifficultyCode(Difficulty difficulty) {
        switch (difficulty) {
            case EASY: return 0;
            case INTERMEDIATE: return 1;
            case HARD: return 2;
            case EXTREME: return 3;
            default: return 0;
        }
    }

    private int getGameModeCode(GameMode gameMode) {
        if (gameMode.isIronMan()) return 1;
        if (gameMode.isHardcoreIronman()) return 2;
        if (gameMode.isUltimateIronman()) return 3;
        if (gameMode.isGroupIronman()) return 4;
        if (gameMode.isHardcoreGroupIronman()) return 5;
        return 0;
    }

    public static String generateQuery() {
        return "INSERT INTO " + TABLE + " (username, user_id, difficulty, mode, total_level, overall_xp, " +
                "attack_xp, defence_xp, strength_xp, constitution_xp, ranged_xp, prayer_xp, magic_xp, cooking_xp, " +
                "woodcutting_xp, fletching_xp, fishing_xp, firemaking_xp, crafting_xp, smithing_xp, mining_xp, " +
                "herblore_xp, agility_xp, thieving_xp, slayer_xp, farming_xp, runecrafting_xp, hunter_xp, " +
                "construction_xp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }
    */
}
