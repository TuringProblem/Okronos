package io.ruin.services;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.NpcID;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.utility.Broadcast;

import java.sql.*;

public class Votes implements Runnable {

	public static final String HOST = "208.109.26.112"; // website ip address
	public static final String USER = "drakosource";
	public static final String PASS = "*E@DV,s1_QLV";
	public static final String DATABASE = "priffvote";

	private Player player;
	private Connection conn;
	private Statement stmt;

	public Votes(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getName();//.replace(" ", "_");
			ResultSet rs = executeQuery("SELECT * FROM votes WHERE username='" + name + "' AND claimed=0 AND voted_on != -1");

			while (rs.next()) {
				String ipAddress = rs.getString("ip_address");
				int siteId = rs.getInt("site_id");

				// Vote boss -1 each vote, when 0 Spawn
				amount--;
				player.voteLimit--;
				player.getInventory().add(4067, 1);
//                if (Random.rollDie(5, 1)) {
//                    player.getInventory().add(1464, 1);
//                    Broadcast.WORLD.sendNews(player.getName() + " Just got a vote lottery ticket! Don't forget to vote!");
//                }

				//Broadcast.WORLD.sendNews(player.getName() + " Just voted for Prifddinas!, " + amount + " votes left until Vote Boss Spawns!");
				Broadcast.WORLD.sendNews(player.getName() + " Just voted for Reason!, " + amount + " votes left until Vote Boss Spawns!");
				// if (amount <= 0) {
				//  new NPC(1).spawn(1777, 3572, 0, Direction.WEST, 1).getCombat().setAllowRespawn(false);
				// Broadcast.GLOBAL.sendNews("<shad=000000>"+ "Avatar of creation Has just spawned! use ::voteboss to get there!</shad>");
				//amount = 50;
				// }
				//System.out.println("[Vote] Vote claimed by "+name+". (sid: "+siteId+", ip: "+ipAddress+")");
				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int executeUpdate(String query) {
		try {
			this.stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			return stmt.executeUpdate(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			return stmt.executeQuery(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static int amount = 50;

}
