package io.ruin.services;

import io.ruin.model.entity.player.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Vote implements Runnable {

	public static final String HOST = "92.205.5.29"; // website ip address
	public static final String USER = "reason_admin";
	public static final String PASS = "Norton102!";
	public static final String DATABASE = "reason";


	private Player player;
	private Connection conn;
	private Statement stmt;

	public Vote(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getName().replace(" ", "_");

			ResultSet rs = executeQuery("SELECT * FROM votes WHERE username='" + name + "' AND claimed=0 AMD voted_on != -1");


			while (rs.next()) {
				String ipAddress = rs.getString("ip_address");
				int siteId = rs.getInt("site_id");


				if (!player.getInventory().isFull()) {
					// System.out.println("Vote reward added to the inventory");
					player.getInventory().add(995, 500000);
					player.votePoints++;
					player.getInventory().addOrDrop(30596, 1);
					player.sendMessage("You now have " + player.votePoints + " vote points.");
					player.sendMessage("<img=1> Thank you for voting for this server!");
				} else {
					player.sendMessage("Please make some room in your inventory, then try again.");
					return;
				}

				//     System.out.println("[Vote] Vote claimed by "+name+". (sid: "+siteId+", ip: "+ipAddress+")");

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
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException | ClassNotFoundException e) {
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
			this.stmt = this.conn.createStatement(1005, 1008);
			int results = stmt.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
