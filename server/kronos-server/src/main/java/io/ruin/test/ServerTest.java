package io.ruin.test;

import io.ruin.Server;
import io.ruin.api.filestore.FileStore;
import io.ruin.api.protocol.PlatformInfo;
import io.ruin.api.protocol.login.LoginInfo;
import io.ruin.api.utils.Random;
import io.ruin.cache.EnumMap;
import io.ruin.cache.EnumType;
import io.ruin.cache.InterfaceDef;
import io.ruin.cache.LocType;
import io.ruin.cache.ObjType;
import io.ruin.cache.SeqType;
import io.ruin.cache.runetek4.vartype.bit.VarBitType;
import io.ruin.data.DataFile;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import player.attributes.PlayerAttributesRegistry;
import player.attributes.PlayerAttributeCodec.LoadContext;
import java.util.concurrent.TimeUnit;
import org.rsmod.util.RsmodGlobal;

import core.module.api.IModule;

public class ServerTest {

	// flag used to for example show the player messages in console
	// or disable certain content
	public static boolean TEST_MODE = false;

	// Starts the server for testing context
	// load only required data/cache
	public static void start() {
		TEST_MODE = true;
		try {
			RsmodGlobal.init();
			Server.loadProperties();
			Server.fileStore = new FileStore(Server.getServerProperties().getProperty("cache_path"));
			VarBitType.load();
			InterfaceDef.load();
			ObjType.load();
			LocType.load();
			EnumMap.load();
			SeqType.load();
			Server.connectToDatabases();

			DataFile.load(new io.ruin.data.impl.items.weapon_types());
			DataFile.load(new io.ruin.data.impl.items.shield_types());
			DataFile.load(new io.ruin.data.impl.items.item_info());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static void startModules() {
		for (var mod : Server.iModules) {
			mod.start();
		}
	}

	public static void registerModule(Class<? extends IModule> module) {
		try {
			Server.startModule(module);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Player createPlayer() {
		var name = Random.getLong() + "";
		return createPlayer(name.substring(0, Math.min(10, name.length() - 1)));
	}

	public static Player createPlayer(String username) {
		var player = new Player();
		var info = new LoginInfo(
				0L,
				"127.0.0.1",
				username,
				"",
				"",
				(PlatformInfo) null,
				username,
				"hwid", 0, false, 0);
		var slot = World.addPlayer(info, player, username);
		PlayerAttributesRegistry.load(slot, new LoadContext(player.uuid()));
		player.start();
		return player;
	}


	public static void shutdown() {
		Server.asyncWorker.shutdown();
		try {
			Server.asyncWorker.awaitTermination(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
