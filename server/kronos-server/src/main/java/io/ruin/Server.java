package io.ruin;

import core.module.api.IModule;
import core.task.Continuations;
import core.task.ContinuationsProfile;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.ruin.api.database.Database;
import io.ruin.api.database.DatabaseUtils;
import io.ruin.api.database.DummyDatabase;
import io.ruin.api.filestore.FileStore;
import io.ruin.api.process.ProcessWorker;
import io.ruin.api.utils.ExecutorUtils;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.cache.HeadbarType;
import io.ruin.cache.IdkType;
import io.ruin.cache.InterfaceDef;
import io.ruin.cache.LocType;
import io.ruin.cache.NPCType;
import io.ruin.cache.ObjType;
import io.ruin.cache.ScriptDef;
import io.ruin.cache.SeqType;
import io.ruin.cache.SpotAnimType;
import io.ruin.cache.runetek4.vartype.bit.VarBitType;
import io.ruin.central.utility.CentralSaves;
import io.ruin.central.utility.WorldList;
import io.ruin.data.DataFile;
import io.ruin.data.yaml.YamlLoader;
import io.ruin.db.DatabaseFile;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.World;
import io.ruin.model.combat.special.Special;
import io.ruin.model.content.XPWeekend;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.entity.npc.actions.edgeville.StarterGuide;
import io.ruin.model.entity.player.PlayerLoginWorker;
import io.ruin.model.item.containers.TournamentSuppliesInterface;
import io.ruin.model.skills.slayer.Slayer;
import io.ruin.network.HWIDManager;
import io.ruin.network.central.CentralSender;
import io.ruin.network.incoming.Incoming;
import io.ruin.network.incoming.handlers.referralSystem;
import io.ruin.process.CoreWorker;
import io.ruin.process.event.EventWorker;
import io.ruin.rsprot.RSProtService;
import io.ruin.services.Loggers;
import io.ruin.services.http.HttpClient;
import io.ruin.utility.CharacterBackups;
import io.ruin.utility.Utils;
import io.sentry.Sentry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import logging.sentry.SentrySpans;
import lombok.extern.slf4j.Slf4j;
import org.rsmod.util.RsmodGlobal;
import properties.ServerProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Slf4j
public final class Server extends ServerWrapper {

	public static interface Hook {
		record OnShutdown() implements Hook {
		}
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	public static final ProcessWorker worker = newWorker(
			"server-worker",
			600L,
			Thread.MAX_PRIORITY - 1);
	private static volatile boolean shutdown = false;

	public static final ExecutorService asyncWorker = Executors.newWorkStealingPool();

	public static void executeAsync(final Runnable runnable) {
		asyncWorker.execute(runnable);
	}

	public static CompletableFuture<Void> runAsync(final Runnable runnable) {
		return CompletableFuture.runAsync(runnable, asyncWorker);
	}

	public static <T> CompletableFuture<T> supplyAsync(final Supplier<T> supplier) {
		return CompletableFuture.supplyAsync(supplier, asyncWorker);
	}

	public static Database gameDb = new DummyDatabase();

	public static Database siteDb = new DummyDatabase();

	public static FileStore fileStore;

	public static CharacterBackups backups = new CharacterBackups();

	public static XPWeekend xpweekend = new XPWeekend();

	private static Properties properties;

	public static Properties getServerProperties() {
		return properties;
	}

	public static void register() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	public static void loadProperties() {
		log.info("Loading server settings...");
		properties = new Properties();
		File systemProps = new File("server.properties");
		log.info("Looking for system.properties in {}", systemProps.getAbsolutePath());
		try (InputStream in = new FileInputStream(systemProps)) {
			properties.load(in);
		} catch (IOException e) {
			logError("Failed to load server settings!", e);
			throw new IllegalStateException(e);
		}
		World.parse(properties);
	}

	public static final List<IModule> iModules = new ObjectArrayList<>();

	@SuppressWarnings("unchecked")
	private static void initIModules(final String... packageNames) throws Exception {
		log.info("Registering modules.");
		try (final ScanResult scan = new ClassGraph()
				.enableMethodInfo()
				.acceptPackages(packageNames)
				.initializeLoadedClasses()
				.scan()) {

			for (final Class<?> clazz : scan.getClassesImplementing(IModule.class).loadClasses()) {
				startModule((Class<? extends IModule>) clazz);
			}
		}
	}

	public static void startModule(Class<? extends IModule> clazz) throws Exception {
		final IModule iModule = (IModule) clazz.getDeclaredConstructor().newInstance();
		iModule.init();
		iModules.add(iModule);
		log.info("Registered module: " + iModule);
	}

	public static void loadData() throws Exception {
		log.info("Loading server data...");
		fileStore = new FileStore(properties.getProperty("cache_path"));

		RsmodGlobal.init();

		initIModules(
				"music.module",
				"clanchat.module",
				"collectionlog.module",
				"core.database.module",
				"core.module.test.module",
				"friendlist.module",
				"inter.charactercreator.module",
				"inter.ikod.module",
				"io.ruin.model.inter.handlers.advancedsettings.module",
				"io.ruin.model.inter.handlers.tabsettings.module",
				"com.reasonps.worldlist",
				"io.ruin.model.entity.player.groupironmode.module",
				"player.chat.filter.module",
				"tradepost.module",
				"royaltitans.module",
				"npc.nex.module",
				// "com.reasonps.dominion.module",
				"core.reason.module",
				"mokhaiotl.module",
				"yama.module",
				"tormenteddemon.module",
				"donationdeals.module",
				"gemstonecrab.module",
				"player.mongo.module");

		RSProtService.create();

		io.ruin.cache.EnumMap.load();
		VarBitType.load();
		IdkType.load();
		SeqType.load();
		SpotAnimType.load();
		ScriptDef.load();
		InterfaceDef.load();
		ObjType.load();
		NPCType.load();
		HeadbarType.load();
		LocType.load();
		DataFile.load();
	}

	public static void startCore() throws Exception {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("UncaughtException: ", e);
			}
		});
		loadProperties();

		long startTime = System.currentTimeMillis();
		init(Server.class);

		try {
			HttpClient.authenticate();
		} catch (Exception ignore) {
		}

		loadData();
		Slayer.loadTasks();
		xpweekend.start();
		CamelStatueHandler.scheduleWellClearing();
		StarterGuide.loadIps();
		referralSystem.loadIps();

		/*
		 * Database connections
		 */
		connectToDatabases();

		// GroupIron.resetAllBankOccupiedStatus();

		TournamentSuppliesInterface.registerTournamentSupplies();

		/*
		 * Loading (Scripts & handlers)
		 */
		log.info("Loading server scripts & handlers...");

		Special.load();
		Incoming.load();
		// When packaged, priority messes up and these load too late.
		// StrongholdSecurity.register();
		// Trapdoor.register();

		try {
			StaticInit.register();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		YamlLoader.initYamlFiles();

		backups.start();

		/*
		 * Processing
		 */
		log.info("Starting server workers...");
		worker.queue(() -> {

			// main worker will not progress when server is shut down
			// also main worker is the first to be shut down
			// so awaiting here instead outside
			if (shutdown) {
				shutdown();
				return false;
			}

			SentrySpans.start("core.tick", () -> {
				SentrySpans.start("core.logic.tick", () -> {
					CoreWorker.process();
				});

				SentrySpans.start("core.continuations.tick", () -> {
					Continuations.tick();
					var profile = ContinuationsProfile.compute(Continuations.current());
					for (var entry : profile.entries) {
						SentrySpans.customFinished(entry.origin(), entry.time(), Map.of("executoons", entry.executions()));
					}
				});

				SentrySpans.start("core.rsprot.tick", () -> {
					RSProtService.tick(World.players(), World.npcsSlots());
				});
			});

			return false;
		});

		/*
		 * GIM
		 */

		CentralSaves.normalizeFilenamesInDirectory();

		log.info("Started server in " + (System.currentTimeMillis() - startTime) + "ms.");
	}

	/**
	 * Timing
	 */
	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
	public static Instant bootedAt = Instant.now();

	public static long currentTick() {
		return worker.getExecutions();
	}

	public static long getEnd(long ticks) {
		return currentTick() + ticks;
	}

	public static boolean isPast(long end) {
		return currentTick() >= end;
	}

	public static int tickMs() {
		return (int) Server.worker.getPeriod();
	}

	public static int toTicks(int seconds) {
		return (seconds * 1000) / tickMs();
	}

	public static String getTime() {
		return timeFormatter.format(new Date());
	}

	public static String getUptime() {
		return Utils.getDurationAsString(Duration.between(bootedAt, Instant.now()));
	}

	public static float toSeconds(float ticks) {
		return (ticks / 1000) * tickMs();
	}

	public static void connectToDatabases() {
		if (!ServerProperties.get("game_db_enabled", false)) {
			return;
		}

		var dbHost = ServerProperties.get("game_db_host");
		var dbName = ServerProperties.get("game_db_name");
		var dbUsername = ServerProperties.get("game_db_username");
		var dbPassword = ServerProperties.get("game_db_password");

		log.info("Connecting to SQL databases...");
		gameDb = new Database(dbHost, dbName, dbUsername, dbPassword);
		if (gameDb == null) {
			log.info("Failed to connect to game database");
		}

		DatabaseUtils.connect(new Database[] { gameDb }, errors -> {
			if (!errors.isEmpty()) {
				for (Throwable t : errors)
					logError("Database error", t);
				System.exit(1);
			}
		});

		if (!DatabaseUtils.verifyConnection(gameDb)) {
			log.error("Failed to verify database connection");
			System.exit(1);
		}

		Loggers.clearOnlinePlayers(World.id);
	}

	public static void shutdownRequest() {
		shutdown = true;
	}

	private static void shutdown() {
		PlayerDatabase.db().awaitNoPendingSaves();
		Server.worker.shutdown();
		Server.gameDb.shutdown();
		Server.siteDb.shutdown();
		Continuations.shutdown();
		DatabaseFile.shutdown();
		CharacterBackups.shutdown();
		ExecutorUtils.shutdown(asyncWorker);
		HWIDManager.shutdown();
		CamelStatueHandler.shutdown();
		PlayerLoginWorker.shutdown();
		RSProtService.shutdown();
		WorldList.shutdown();
		CentralSender.shutdown();
		hooks.handle(new Hook.OnShutdown());
		log.info("Safe shutdown complete.");
	}

}
