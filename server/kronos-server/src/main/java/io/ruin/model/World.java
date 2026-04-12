package io.ruin.model;

import com.google.common.collect.Maps;

import core.task.Continuations;
import io.ruin.Server;
import io.ruin.api.database.DatabaseStatement;
import io.ruin.api.database.DatabaseUtils;
import io.ruin.api.protocol.login.LoginInfo;
import io.ruin.api.protocol.world.WorldFlag;
import io.ruin.api.protocol.world.WorldSetting;
import io.ruin.api.protocol.world.WorldStage;
import io.ruin.api.protocol.world.WorldType;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.central.utility.WorldList;
import io.ruin.db.DatabaseFile;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.activities.pvminstances.PVMInstance;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.activities.tempevents.RevenantMaledictusManager;
import io.ruin.model.activities.tempevents.TemporaryEvent;
import io.ruin.model.combat.Killer;
import io.ruin.model.content.PestControl;
import io.ruin.model.content.XPWeekend;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerLoginWorker;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.object.owned.OwnedObject;
import io.ruin.model.object.owned.impl.DwarfCannon;
import io.ruin.network.HWIDManager;
import io.ruin.process.event.EventWorker;
import io.ruin.rsprot.RSProtService;
import io.ruin.utility.Broadcast;
import io.ruin.utility.CharacterBackups;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.rsmod.game.entity.EntityList;
import org.rsmod.game.entity.Npc;
import org.rsmod.game.entity.NpcList;
import org.rsmod.game.entity.PlayerList;
import org.rsmod.util.RsmodGlobal;
import player.attributes.PlayerAttributesRegistry;
import player.attributes.PlayerAttributeCodec.LoadContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class World extends EventWorker {

	public static int id;

	public static int login_con_limit;

	public static boolean login_pow_enabled;

	public static String login_master_password;

	public static int db_threads = 4;

	public static String name;

	public static WorldStage stage = WorldStage.DEV;

	public static WorldType type = WorldType.DEV;

	public static WorldFlag flag;

	public static int settings;

	public static String address;

	public static int world_port;

	public static boolean apiEnabled;

	public static String apiPassword;

	public static String apiBaseUrl;

	public static TemporaryEvent revenantMaledictusEvent = null;
	public static TemporaryEvent hweenEvent = null;

	private static final AtomicInteger worldMessageCounter;
	private static final Map<String, Integer> userIds = new ConcurrentHashMap<>();

	static {
		final LocalDateTime now = LocalDateTime.now();
		final int dayOfYear = now.getDayOfYear();
		final int hourOfDay = now.getHour();
		final int hourOfYear = (dayOfYear - 1) * 24 + hourOfDay; // Subtract 1 since it is 0-based

		worldMessageCounter = new AtomicInteger(hourOfYear * 50_000);
	}

	public static int getWorldMessageCounter() {
		return worldMessageCounter.get();
	}

	public static int getNextWorldMessageCounter() {
		return worldMessageCounter.incrementAndGet();
	}

	private static String centralAddress;

	public static boolean goodwill;

	public static boolean isDev() {
		return stage == WorldStage.DEV;
	}

	public static boolean isBeta() {
		return stage == WorldStage.BETA;
	}

	public static boolean isLive() {
		return stage == WorldStage.LIVE;
	}

	public static boolean isPVP() {
		return type == WorldType.PVP;
	}

	public static boolean isEco() {
		return type == WorldType.ECO;
	}

	public static final int spawnableOffset = 100000;

	public static final Position HOME = new Position(3087, 3496, 0);
	public static final Position OLDCUSTOMHOME = new Position(2028, 3577, 0);
	public static final Position EDGEHOME = new Position(3092, 3497, 0);

	public static final Position DEATHS_DOMAIN = new Position(3174, 5727, 0);

	/**
	 * Players
	 */

	private static boolean saveWell() {
		// WellofGoodwill.save();
		return true;
	}

	public static PlayerList getPlayers() {
		return RsmodGlobal.getPlayerList();
	}

	public static Stream<Player> getPlayerStream() {
		return getPlayers()
				.getEntries()
				.stream()
				.filter(Objects::nonNull)
				.map(org.rsmod.game.entity.Player::getKronos);
	}

	@Nullable
	public static Player getPlayer(final int index) {
		final org.rsmod.game.entity.Player player = getPlayers().get(index);
		return player == null ? null : player.getKronos();
	}

	public static Player getPlayer(final String name) {
		for (final org.rsmod.game.entity.Player player : getPlayers()) {
			if (player == null)
				continue;
			final Player kronos = player.getKronos();
			if (name.equalsIgnoreCase(kronos.getName())) {
				return kronos;
			}
		}
		return null;
	}

	public static Player getPlayer(final int userId, final boolean onlineReq) {
		for (final org.rsmod.game.entity.Player player : getPlayers()) {
			if (player == null)
				continue;
			final Player kronos = player.getKronos();
			if (kronos.getUserId() == userId) {
				return kronos;
			}
		}
		return null;
	}

	public static void sendSupplyChestBroadcast(final String message) {
		getPlayers().forEach(player -> {
			final Player p = player.getKronos();
			if (p.broadcastSupplyChest)
				p.sendNotification(message);
		});
	}

	public static void sendGraphics(int id, int height, int delay, Position dest) {
		sendGraphics(id, height, delay, dest.getX(), dest.getY(), dest.getZ());
	}

	public static void sendGraphics(int id, int height, int delay, int x, int y, int z) {
		for (Player p : Region.get(x, y).players)
			p.getPacketSender().sendGraphics(id, height, delay, x, y, z);
	}

	/**
	 * Npcs
	 */

	public static NpcList getNpcs() {
		return RsmodGlobal.getNpcList();
	}

	public static Iterable<NPC> npcsSlots() {

		return new Iterable<>() {

			@Override
			public Iterator<NPC> iterator() {
				var entries = getNpcs().getEntries();
				return new Iterator<NPC>() {
					int cursor = 0;

					@Override
					public boolean hasNext() {
						while (cursor < entries.size()) {
							var entry = entries.get(cursor);
							if (entry != null) {
								return true;
							}
							cursor += 1;
						}
						return cursor < entries.size();
					}

					@Override
					public NPC next() {
						return entries.get(cursor++).getKronos();
					}
				};
			}
		};

	}

	public static boolean removeNPC(final NPC npc) {
		final int index = npc.getIndex();
		if (index == EntityList.INVALID_SLOT) {
			log.error("Attempted to remove NPC with invalid index: {}", npc);
			return false;
		}

		final NpcList npcs = getNpcs();

		final Npc otherNpc = npcs.get(index);
		if (npc.rsmod() != otherNpc) {
			log.error("NPC index mismatch! {} != {}", npc, otherNpc);
			return false;
		}

		npcs.remove(index);

		RSProtService.removeNPCAvatar(npc);

		Npc rsmod = npc.rsmod();
		if (rsmod != null) {
			RsmodGlobal.getNpcRegistry().zoneDel(rsmod);
		}

		npc.setIndex(EntityList.INVALID_SLOT);

		return true;
	}

	public static Stream<NPC> npcsNonNull() {
		return getNpcs()
				.getEntries()
				.stream()
				.filter(Objects::nonNull)
				.map(Npc::getKronos);
	}

	@Nullable
	public static NPC getNpc(final int index) {
		final Npc npc = getNpcs().get(index);
		return npc == null ? null : npc.getKronos();
	}

	/**
	 * PLAYER SAVERS
	 */

	public static boolean doublePkp;

	public static boolean doubleSlayer;

	public static boolean doublePest;

	public static boolean doubleWintertodt;

	public static int xpMultiplier = 0;

	public static int bmMultiplier = 0;

	public static boolean weekendExpBoost = false;
	public static boolean doubleExpActive = false;

	public static void toggleDoublePkp() {
		doublePkp = !doublePkp;
		Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Double Pkp has been " + (doublePkp ? "enabled" : "disabled") + ".");
	}

	public static String toggleDoubleSlayer() {
		boolean wasActivated = doubleSlayer;
		doubleSlayer = !doubleSlayer;

		if (doubleSlayer) {
			return "The Double Slayer Point Boost has just been activated!";
		} else {
			return null; // Return null if deactivated, handling in command file
		}
	}

	public static void toggleDoublePest() {
		doublePest = !doublePest;
		Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE,
				"Double Pest Control Points has been " + (doublePest ? "enabled" : "disabled") + ".");
	}

	public static void toggleDoubleWintertodt() {
		doubleWintertodt = !doubleWintertodt;
		Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE,
				"Double Wintertodt Points has been " + (doubleWintertodt ? "enabled" : "disabled") + ".");
	}

	public static void boostXp(int multiplier) {
		xpMultiplier = multiplier;
		if (xpMultiplier == 1)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Experience is now normal. (x1)");
		else if (xpMultiplier == 2)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Experience is now being doubled! (x2)");
		else if (xpMultiplier == 3)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Experience is now being tripled! (x3)");
		else if (xpMultiplier == 4)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Experience is now being quadrupled! (x4)");
		else
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Experience is now boosted! (x" + multiplier + ")");
	}

	/*
	 * Sets the base amount of blood money user can get per kill
	 */
	public static void setBaseBloodMoney(int baseBloodMoney) {
		Killer.BASE_BM_REWARD = baseBloodMoney;
	}

	public static void toggleWeekendExpBoost() {
		weekendExpBoost = !weekendExpBoost;
		if (weekendExpBoost) {
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "2x weekend experience boost is now activated!");
		} else {
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "2x weekend experience boost is now deactivated!");
		}
	}

	public static void boostBM(int multiplier) {
		bmMultiplier = multiplier;
		if (bmMultiplier == 1)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Blood money drops from player kills are now normal. (x1)");
		else if (bmMultiplier == 2)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Blood money drops from player kills are now being doubled! (x2)");
		else if (bmMultiplier == 3)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE, "Blood money drops from player kills are now being tripled! (x3)");
		else if (bmMultiplier == 4)
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE,
					"Blood money drops from player kills are now being quadrupled! (x4)");
		else
			Broadcast.WORLD.sendNews(Icon.RED_INFO_BADGE,
					"Blood money drops from player kills are now boosted! (x" + multiplier + ")");
	}

	public static void sendLoginMessages(Player player) {
		if (xpMultiplier == 2 || doubleExpActive) {
			player.sendMessage(Color.DARK_GREEN.tag() + "Double XP is currently active!");
		} else if (xpMultiplier == 3) {
			player.sendMessage(Color.GREEN.tag() + "X3 experience is currently active!");
		} else if (xpMultiplier == 4) {
			player.sendMessage(Color.GREEN.tag() + "X4 experience is currently active!");
		}

		if (doubleSlayer) {
			player.sendMessage(Color.DARK_GREEN.tag() + "Double Slayer Points are currently active!");
		}
	}

	public static boolean wildernessDeadmanKeyEvent = false;

	public static void toggleDmmKeyEvent() {
		wildernessDeadmanKeyEvent = !wildernessDeadmanKeyEvent;
	}

	public static boolean wildernessKeyEvent = false;

	public static void toggleWildernessKeyEvent() {
		wildernessKeyEvent = !wildernessKeyEvent;
	}

	public static Optional<Player> getPlayerByName(String userName) {
		return getPlayerStream().filter(plr -> plr.getName().equalsIgnoreCase(userName)).findFirst();
	}

	@Getter
	protected static final Map<String, OwnedObject> ownedObjects = Maps.newConcurrentMap();

	public static void registerOwnedObject(OwnedObject object) {
		ownedObjects.put(object.getOwnerName() + ":" + object.getIdentifier(), object);
	}

	public static int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}

	public static OwnedObject getOwnedObject(Player owner, String identifier) {
		return ownedObjects.get(owner.getName() + ":" + identifier);
	}

	public static void deregisterOwnedObject(OwnedObject object) {
		ownedObjects.remove(object.getOwnerName() + ":" + object.getIdentifier());
	}

	public static void checkCannon(Player player) {
		OwnedObject cannon = getOwnedObject(player, DwarfCannon.IDENTIFIER);

		if (cannon != null) {
			Server.gameDb.execute(connection -> {
				PreparedStatement statement = connection.prepareStatement("INSERT INTO lost_cannons (user_name) VALUES (?)");
				statement.setString(1, player.getName());
				statement.executeUpdate();
			});

			cannon.destroy();
		}
	}

	public static void doCannonReclaim(String userName, Consumer<Boolean> consumer) {
		Server.gameDb.execute(new DatabaseStatement() {

			private boolean result;

			@Override
			public void execute(Connection connection) throws SQLException {

				PreparedStatement statement = null;
				ResultSet rs = null;
				try {
					statement = connection.prepareStatement("SELECT 1 FROM lost_cannons WHERE user_name = ? LIMIT 1");
					statement.setString(1, userName);
					rs = statement.executeQuery();
					result = rs.next(); // If there is at least one row, the username exists

					Server.worker.execute(() -> consumer.accept(result));
				} finally {
					DatabaseUtils.close(statement, rs);
				}

			}
		});
	}

	public static void removeCannonReclaim(String userName) {
		Server.gameDb.execute(connection -> {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM lost_cannons WHERE user_name = ?");
			statement.setString(1, userName);
			statement.execute();
		});
	}

	/**
	 * Updating
	 */
	public static boolean updating = false;

	public static boolean update(int minutes) {
		if (minutes == 0) {
			updating = false;
			for (org.rsmod.game.entity.Player player : getPlayers()) {
				player.getKronos().getPacketSender().sendSystemUpdate(0);
			}
			return true;
		}

		if (minutes < 0) {
			minutes = 0;
		}

		if (updating) {
			return false;
		}

		updating = true;

		for (org.rsmod.game.entity.Player player : getPlayers()) {
			player.getKronos().getPacketSender().sendSystemUpdate(minutes * 60);
		}

		var minutesFinal = minutes;
		startEvent(e -> {
			int ticks = minutesFinal * 100;
			while (updating) {
				if (--ticks <= 0 && removePlayers()) {
					break;
				}
				e.delay(1);
			}

			if (!updating) {
				return;
			}
			Server.shutdownRequest();
		});
		return true;
	}

	public static boolean removePlayers() {
		boolean removed = false;
		for (org.rsmod.game.entity.Player player : getPlayers()) {
			final Player kronos = player.getKronos();
			World.checkCannon(kronos);
			kronos.forceLogout();
			removed = true;
		}
		if (removed) {
			return false;
		}
		PlayerDatabase.db().awaitNoPendingSaves();
		PVMInstance.destroyAll();
		return true;
	}

	/**
	 * Holiday themes
	 */
	public static boolean halloween;

	public static boolean isHalloween() {
		return halloween;
	}

	public static boolean christmas;

	public static boolean isChristmas() {
		return christmas;
	}

	/*
	 * Save event
	 */
	public static void register() {
		startEvent(e -> {
			while (true) {
				e.delay(10); // Every minute
				TheatrePartyManager.instance().cleanupAndValidateParties();
			}
		});

		RevenantMaledictusManager revenantMaledictusManager = new RevenantMaledictusManager();
		revenantMaledictusManager.init();
		// HweenEvent hweenEvent = new HweenEvent();
		// hweenEvent.init();
		List<String> announcements;
		announcements = Arrays.asList(
				"Need help? Join the \"Kal\" cc!",
				"Make sure to vote to help grow the server!",
				"Check out the point stores inside the bank at home!",
				"Join ::discord to get closer to the community!");
	}

	@SneakyThrows
	public static void parse(Properties properties) {
		World.id = Integer.parseInt(properties.getProperty("world_id"));
		World.login_master_password = properties.getProperty("login_master_password");
		World.login_con_limit = Integer.parseInt(properties.getProperty("login_con_limit"));
		World.login_pow_enabled = Boolean.parseBoolean(properties.getProperty("login_pow_enabled"));
		World.db_threads = Integer.parseInt(properties.getProperty("db_threads"));
		World.name = properties.getProperty("world_name");
		World.stage = WorldStage.valueOf(properties.getProperty("world_stage"));
		World.type = WorldType.valueOf(properties.getProperty("world_type"));
		World.flag = WorldFlag.valueOf(properties.getProperty("world_flag"));
		World.halloween = Boolean.parseBoolean(properties.getProperty("halloween"));
		World.christmas = Boolean.parseBoolean(properties.getProperty("christmas"));
		World.world_port = Integer.parseInt(properties.getProperty("world_port", "43594"));
		String worldSettings = properties.getProperty("world_settings");

		World.apiPassword = properties.getProperty("api_password");
		World.apiEnabled = Boolean.parseBoolean(properties.getProperty("api_enabled"));
		World.apiBaseUrl = properties.getProperty("api_base_url");

		for (String s : worldSettings.split(",")) {
			if (s == null || (s = s.trim()).isEmpty())
				continue;
			WorldSetting setting;
			try {
				setting = WorldSetting.valueOf(s);
			} catch (Exception e) {
				log.error("INVALID WORLD SETTING: " + s, e);
				continue;
			}
			World.settings |= setting.mask;
		}
		World.address = properties.getProperty("world_address", "127.0.0.1");
	}

	public static List<Player> getPlayersForNames(List<String> playerNames) {
		return playerNames
				.stream()
				.distinct()
				.filter(Objects::nonNull)
				.filter(name -> !name.isEmpty())
				.map(World::getPlayer)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	public static int staffCount() {
		int count = 0;
		for (org.rsmod.game.entity.Player player : getPlayers()) {
			if (player.getKronos().isStaff()) {
				count++;
			}
		}
		return count;
	}

	public static int addPlayer(LoginInfo info, Player player, String username) {
		var players = World.playersNullable();

		final Integer slot = players.nextFreeSlot();
		if (slot == null || slot == EntityList.INVALID_SLOT) {
			return -1;
		}

		final org.rsmod.game.entity.Player playerInSlot = players.get(slot);
		if (playerInSlot != null) {
			log.error("Player already exists in slot: {}", playerInSlot);
			return -1;
		}

		var rsmod = new org.rsmod.game.entity.Player(player);
		info.userId = userIds.computeIfAbsent(username, (ignore) -> userIds.size() + 1);
		info.name = StringUtils.capitalizeName(username);
		player.init(info);
		player.setRsmodPlayer(rsmod);
		player.setIndex(slot);
		players.set(slot, rsmod);
		return slot;
	}

	/// NOTE:
	/// This method is the last entry point
	/// that removes the player completelly and all it's associated data
	public static void removePlayer(final Player player) {
		final int index = player.getIndex();
		getPlayers().remove(index);
		org.rsmod.game.entity.Player rsmod = player.rsmod();
		if (rsmod != null) {
			RsmodGlobal.getPlayerRegistry().zoneDel(rsmod);
		}
		PlayerAttributesRegistry.unload(index);
	}

	@Nullable
	public static Player player(final int index) {
		final org.rsmod.game.entity.Player player = getPlayers().get(index);
		return player == null ? null : player.getKronos();
	}

	public static Iterable<Player> players() {
		return getPlayers()
				.getEntries()
				.stream()
				.filter(Objects::nonNull)
				.map(org.rsmod.game.entity.Player::getKronos)
				.collect(Collectors.toList());
	}

	public static PlayerList playersNullable() {
		return getPlayers();
	}

	public static Stream<Player> playersNonNull() {
		return getPlayerStream();
	}

	public static void playersScramble() {

	}

	public static int playerCount() {
		return getPlayers().size();
	}

}
