package io.ruin.services;

import io.ruin.model.entity.player.Player;

import java.sql.*;

/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:
 * new Thread(new Donation(player)).start();
 */
public class Store implements Runnable {

	public static final String HOST = "92.205.5.29"; // website ip address
	public static final String USER = "reason_admin";
	public static final String PASS = "Norton102!";
	public static final String DATABASE = "reason";


	private Player player;
	private Connection conn;
	private Statement stmt;

	/**
	 * The constructor
	 *
	 * @param player
	 */
	public Store(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			if (player.getInventory().isFull()) {
				player.sendMessage("Your backpack is full, in order to get your purchase make some room and relog.");
				return;
			}

			String name = player.getName().replace("_", " ");
			ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");

			while (rs.next()) {
				int item_number = rs.getInt("item_number");
				double paid = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");

				if (player.getInventory().getFreeSlots() < quantity) {
					player.sendMessage("You have not received your full donation, please make more space and relog to retrieve the full amount.");
					return;
				}

				switch (item_number) {// add products according to their ID in the ACP

					case 10: // $5 Donator bond
						player.getInventory().addOrDrop(30464, 3);
						break;
					case 11:
						player.getInventory().addOrDrop(30497, 3);
						break;
					case 17:
						player.getInventory().addOrDrop(30466, 3);
						break;
					case 18:
						player.getInventory().addOrDrop(30467, 3);
						break;
					case 19:
						player.getInventory().addOrDrop(30468, 3);
						break;

				}

				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param host     the host ip address or url
	 * @param database the name of the database
	 * @param user     the user attached to the database
	 * @param pass     the users password
	 * @return true if connected
	 */
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

	/**
	 * Disconnects from the MySQL server and destroy the connection
	 * and statement instances
	 */
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

	/**
	 * Executes an update query on the database
	 *
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
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

	/**
	 * Executres a query on the database
	 *
	 * @param query
	 * @return the results, never null
	 * @see {@link Statement#executeQuery(String)}
	 */
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
