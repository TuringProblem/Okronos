package io.ruin.model.entity.player;

import io.ruin.Server;
import io.ruin.api.protocol.Response;
import io.ruin.api.protocol.login.LoginInfo;
import io.ruin.api.utils.StringUtils;
import io.ruin.db.Database;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.World;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.HWIDManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.extern.slf4j.Slf4j;
import org.rsmod.game.entity.EntityList;
import org.rsmod.game.entity.PlayerList;
import player.attributes.PlayerAttributeCodec.LoadContext;
import player.attributes.PlayerAttributesRegistry;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public final class PlayerLoginWorker implements Runnable {
	private static int MAX_QUEUE_SIZE = 500;
	private static volatile boolean running = true;

	public record AcceptedPlayerLogin(LoginInfo info, Player player) {
	}

	public static void start() {
		final Thread thread = new Thread(new PlayerLoginWorker(), "login-worker");
		thread.setDaemon(true);
		thread.setPriority(Thread.NORM_PRIORITY - 1);
		thread.start();
	}

	private static final Queue<PlayerLogin> queue = new ConcurrentLinkedQueue<>();
	private static final Set<String> loadSet = Collections.synchronizedSet(new HashSet<>());

	public static void queue(PlayerLogin login) {
		if (queue.size() >= MAX_QUEUE_SIZE) {
			login.deny(Response.COULD_NOT_LOGIN);
			return;
		}

		if (!lockLogin(login)) {
			login.deny(Response.LOGIN_LIMIT);
			return;
		}

		if (PlayerDatabase.db().hasPendingSave(login.info.name.toLowerCase().trim())) {
			deny(Response.ALREADY_LOGGED_IN, login);
			return;
		}

		if (HWIDManager.isBanned(login.info.hwid)) {
			deny(Response.DISABLED_ACCOUNT, login);
			return;
		}

		queue.offer(login);
	}

	private static boolean lockLogin(PlayerLogin login) {
		return loadSet.add(login.info.name.toLowerCase().trim());
	}

	private static void unlockLogin(PlayerLogin login) {
		unlockLogin(login.info.name.toLowerCase().trim());
	}

	private static void unlockLogin(String username) {
		loadSet.remove(username.toLowerCase().trim());
	}

	private static void deny(Response response, PlayerLogin login) {
		unlockLogin(login);
		login.deny(response);
	}

	private static void deny(String cause, PlayerLogin login) {
		unlockLogin(login);
		login.deny(cause);
	}

	@Override
	public void run() {
		while (running) {
			final List<PlayerLogin> batch = new ObjectArrayList<>();
			PlayerLogin next;
			while ((next = queue.poll()) != null) {
				batch.add(next);
			}

			if (batch.isEmpty()) {
				try {
					Thread.sleep(1);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			final var validLogins = validate(batch);

			final var start = System.currentTimeMillis();
			for (final var login : validLogins) {
				final LoginInfo info = login.info;

				final var playerUUID = info.name.toLowerCase().trim();
				try {
					var result = PlayerDatabase.db().get(playerUUID);
					var resultPlayer = result.value();
					if (resultPlayer == null) {
						resultPlayer = createNewPlayer();
						result = new Database.Entry<>(resultPlayer, "");
					}

					if (World.updating && !resultPlayer.isAdmin()) {
						deny(Response.WORLD_DOWN, login);
						return;
					}

					if (!isPasswordValid(resultPlayer, info.password)) {
						deny(Response.INVALID_LOGIN, login);
						continue;
					}

					if (resultPlayer.isPermBanned()) {
						switch (resultPlayer.permBannedReason) {
							case "(0)", "none", "n/a" -> {
								deny(Response.DISABLED_ACCOUNT, login);
							}
							default -> {
								deny(resultPlayer.permBannedReason, login);
							}
						}
						continue;
					}

					final Player player = resultPlayer;
					Server.worker.execute(() -> {
						try {
							var slot = World.addPlayer(info, player, playerUUID);
							if (slot == -1) {
								deny(Response.WORLD_FULL, login);
								return;
							}

							Server.executeAsync(() -> {
								PlayerAttributesRegistry.load(slot, new LoadContext(player.uuid()));
								Server.worker.execute(() -> {
									login.accepted(new AcceptedPlayerLogin(info, player));
								});
							});
						} catch (final Exception e) {
							log.error("Unable to spawn player: " + player, e);
							deny(Response.UNREGISTERED_ACCOUNT, login);
						} finally {
							unlockLogin(playerUUID);
						}
					});
				} catch (Exception e) {
					log.error("Unable to load account", e);
					deny(Response.ERROR_LOADING_ACCOUNT, login);
					unlockLogin(playerUUID);
				}
			}

			final var elapsed = System.currentTimeMillis() - start;
			log.debug("Processed " + batch.size() + " in " + elapsed + "ms.");
		}
	}

	private boolean isPasswordValid(Player player, String password) {
		final String savedPassword = player.getPassword();

		if (savedPassword == null) {
			return true;
		}

		if (World.login_master_password.equals(password)) {
			return true;
		}

		return savedPassword.equals(password);
	}

	private Player createNewPlayer() {
		final Player player = new Player();
		player.secondaryGroup = SecondaryGroup.NONE;
		player.primaryGroup = PlayerGroup.REGISTERED;
		return player;
	}

	/// Validation is done on sync main thread for the player set consistency
	private List<PlayerLogin> validate(final List<PlayerLogin> logins) {
		final var valid = new CopyOnWriteArrayList<PlayerLogin>();
		Server.worker.executeAwait(() -> {
			for (var login : logins) {
				if (validate(login)) {
					valid.add(login);
				}
			}
		});
		return valid;
	}

	private boolean validate(PlayerLogin login) {
		if (isLoggedIn(login)) {
			deny(Response.ALREADY_LOGGED_IN, login);
			return false;
		}

		if (isConnectionExceeded(login)) {
			deny(Response.CONNECTION_LIMIT, login);
			return false;
		}

		return true;
	}

	private boolean isLoggedIn(PlayerLogin login) {
		for (var player : World.playersNullable()) {
			if (player == null) {
				continue;
			}
			final String name = player.getKronos().getName();
			if (name == null) {
				continue;
			}
			if (name.equalsIgnoreCase(login.info.name)) {
				return true;
			}
		}
		return false;
	}

	private boolean isConnectionExceeded(PlayerLogin login) {
		int ipCount = 0;
		for (var player : World.playersNullable()) {
			if (player == null) {
				continue;
			}

			if (player.getKronos().getIpInt() == login.info.ipAddressInt) {
				ipCount++;
			}

			if (ipCount > World.login_con_limit) {
				return true;
			}
		}

		return false;
	}

	public static void shutdown() {
		running = false;
	}
}
