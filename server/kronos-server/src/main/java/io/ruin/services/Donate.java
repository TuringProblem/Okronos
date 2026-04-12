package io.ruin.services;


import io.ruin.model.entity.player.Player;
import io.ruin.utility.Broadcast;

import java.sql.*;


/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:// i updated yesterday, and it still works fine from the vps, just not here
 * new Thread(new Donation(player)).start();
 */
public class Donate implements Runnable {

	public static final String HOST = "208.109.26.112"; // website ip address
	public static final String USER = "drakosource";
	public static final String PASS = "*E@DV,s1_QLV";
	public static final String DATABASE = "drakostore";

	private Player player;
	private Connection conn;
	private Statement stmt;

	/**
	 * The constructor
	 *
	 * @param player
	 */
	public Donate(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getName();
			ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");

			while (rs.next()) {
				int item_number = rs.getInt("item_number");
//				double paid = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				switch (item_number) {// add products according to their ID in the ACP
					case 1024: // 5 bond
						player.getInventory().add(30276, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x 5$ Bond!");
						break;
					case 1025: // 10 bond
						player.getInventory().add(30279, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x 10$ Bond!");
						break;
					case 1026: // 25 bond
						player.getInventory().add(30282, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x 25$ Bond!");
						break;
					case 1027: // 50 bond
						player.getInventory().add(30285, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x 50$ Bond!");
						break;
					case 1028: // 100 bond
						player.getInventory().add(30288, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x 100$ Bond!");
						break;
					case 1029: // 250 bond
						player.getInventory().add(30291, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x 250$ Bond!");
						break;
					case 1030: // 500 bond
						player.getInventory().add(30294, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x 500$ Bond!");
						break;
					case 1031: // Starter box
						player.getInventory().add(30432, quantity);

						player.sendMessage("Thank you for donating, Your items have been placed in your Inventory");
						Broadcast.WORLD.sendNews("[News]" + player.getName() + " has purchased " + quantity + "x Starter Pack!");
						player.storeAmountSpent += 120;
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
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Failing connecting to database!" + e);
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
