package io.ruin.network.incoming.handlers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ruin.Server;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.entity.player.Player;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class referralSystem implements Runnable {

	private Player player;
	public static int item1;
	public static int item2;
	public static int item3;
	public static int item4;
	public static int item5;

	public static int item1amount;
	public static int item2amount;
	public static int item3amount;
	public static int item4amount;
	public static int item5amount;

	public static final String HOST = "localhost"; // database host
	public static final String USER = "referrals"; // database username
	public static final String PASS = "ze5eJuH2He"; // database password
	public static final String DATABASE = "referrals"; // database name

	public referralSystem(Player player, String referral) {
		this.player = player;
		this.referral = referral;
	}

	public static List<String> ReferralipsClaimed = new ArrayList<>();

	public static boolean alreadyClaimed(Player player) {
		if (ReferralipsClaimed.contains(player.getIp()))
			return true;
		return false;
	}

	public static void addIpsClaimed(String ip) {
		if (ReferralipsClaimed.contains(ip))
			return;
		ReferralipsClaimed.add(ip);
		saveIps();
	}

	public static void saveIps() {
		Server.executeAsync(() -> {
			File file = new File(ServerWrapper.dataFolder, "/runtime/referral_claimed_ips.json");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				FileWriter fileWriter = new FileWriter(file);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String toJson = gson.toJson(ReferralipsClaimed);
				fileWriter.write(toJson);
				fileWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void loadIps() {
		File file = new File(ServerWrapper.dataFolder, "/referral_claimed_ips.json");
		if (!file.exists()) {
			try {
				file.createNewFile();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Type type = new TypeToken<ArrayList<String>>() {
		}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			ArrayList<String> temp = gson.fromJson(new FileReader(file), type);
			if (temp != null)
				ReferralipsClaimed = temp;
			log.info("Loaded " + ReferralipsClaimed.size() + " referral claimed ids.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String referral;

	public void update() {
		try {
			// create our mysql database connection
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE, USER, PASS);

			// create the java mysql update preparedstatement
			String query = "UPDATE referrals SET Used = Used + 1 WHERE user =" + "'" + referral + "'";
			PreparedStatement preparedStmt = conn.prepareStatement(query);

			// execute the java preparedstatement
			preparedStmt.executeUpdate();

			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}

	public void run() {
		boolean alreadyClaimed = alreadyClaimed(player);
		if (!alreadyClaimed)
			try {
				// create our mysql database connection
				Connection conn = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE, USER, PASS);

				// our SQL SELECT query.
				// if you only need a few columns, specify them by name instead of using "*"
				String query = "SELECT * FROM referrals WHERE user =" + "'" + referral + "'";

				// create the java statement
				Statement st = conn.createStatement();

				// execute the query, and get a java resultset
				ResultSet rs = st.executeQuery(query);

				// iterate through the java resultset
				while (rs.next()) {
					item1 = rs.getInt("ITEM1ID");
					item1amount = rs.getInt("ITEM1AMOUNT");
					item2 = rs.getInt("ITEM2ID");
					item2amount = rs.getInt("ITEM2AMOUNT");
					item3 = rs.getInt("ITEM3ID");
					item3amount = rs.getInt("ITEM3AMOUNT");
					item4 = rs.getInt("ITEM4ID");
					item4amount = rs.getInt("ITEM4AMOUNT");
					item5 = rs.getInt("ITEM5ID");
					item5amount = rs.getInt("ITEM5AMOUNT");

					player.getInventory().add(referralSystem.item1, referralSystem.item1amount);
					player.getInventory().add(referralSystem.item2, referralSystem.item2amount);
					player.getInventory().add(referralSystem.item3, referralSystem.item3amount);
					player.getInventory().add(referralSystem.item4, referralSystem.item4amount);
					player.getInventory().add(referralSystem.item5, referralSystem.item5amount);
					player.referral = true;
					addIpsClaimed(player.getIp());
					update();
				}
				st.close();
			} catch (Exception e) {
				player.sendMessage("This referral code doesn't exist!");
				System.err.println("Got an exception! ");
				System.err.println(e.getMessage());
			}
	}
}
