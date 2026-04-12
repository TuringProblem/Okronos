package io.ruin.rsprot;

import io.netty.buffer.ByteBuf;
import io.ruin.HooksV2;
import io.ruin.Server;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.UpdateMask;
import io.ruin.model.map.Position;
import io.sentry.Sentry;
import logging.sentry.SentrySpans;
import net.rsprot.protocol.api.NetworkService;
import net.rsprot.protocol.api.util.ZonePartialEnclosedCacheBuffer;
import net.rsprot.protocol.common.client.OldSchoolClientType;
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatar;
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcInfo;
import net.rsprot.protocol.game.outgoing.info.npcinfo.SetNpcUpdateOrigin;
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerInfo;
import net.rsprot.protocol.game.outgoing.misc.client.ServerTickEnd;
import net.rsprot.protocol.game.outgoing.worldentity.SetActiveWorldV2;
import net.rsprot.protocol.game.outgoing.zone.header.UpdateZonePartialEnclosed;
import net.rsprot.protocol.message.ZoneProt;
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

@Slf4j
public final class RSProtService {

	private static final Logger logger = LoggerFactory.getLogger(RSProtService.class);

	public interface Hook {
		record Accept(GameMessageConsumerRepositoryBuilder<Player> handlers) implements Hook {
		}
	}

	public static final HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	private static GameMessageConsumerRepositoryBuilder<Player> handlers;
	private static NetworkService<Player> service;
	private static ZonePartialEnclosedCacheBuffer zoneCache;

	public static void create() {
		handlers = new GameMessageConsumerRepositoryBuilder<>();
		hooks.handle(new Hook.Accept(handlers));
		service = RSProtServiceFactory.create(handlers);
		zoneCache = new ZonePartialEnclosedCacheBuffer();
	}

	public static void start() {
		service.start();
		RSProtNetworkSnapshotService.start();
	}

	public static void shutdown() {
		service.shutdown();
	}

	public static NetworkService<Player> service() {
		return service;
	}

	public static void removePlayer(final Player player) {
		service.getPlayerInfoProtocol().dealloc(player.rsprotPlayerInfo);
		service.getNpcInfoProtocol().dealloc(player.rsprotNPCInfo);
	}

	public interface ZoneProtPacketProvider {
		ZoneProt packet(int x, int y);
	}

	public static UpdateZonePartialEnclosed encodeZonePacket(
			Player player,
			int x, int y, int z,
			ZoneProtPacketProvider provider) {

		final int chunkX = (x >> 3) << 3;
		final int chunkY = (y >> 3) << 3;

		final int offsetX = x - chunkX;
		final int offsetY = y - chunkY;

		final int playerLocalX = Position.getLocal(chunkX, player.getFirstChunkX());
		final int playerLocalY = Position.getLocal(chunkY, player.getFirstChunkY());

		final EnumMap<OldSchoolClientType, ByteBuf> buffers = zoneCache
				.computeZone(List.of(provider.packet(offsetX, offsetY)));
		final ByteBuf byteBuf = buffers.get(OldSchoolClientType.DESKTOP);
		return new UpdateZonePartialEnclosed(playerLocalX, playerLocalY, z, byteBuf);
	}

	public static NpcAvatar createNPCAvatar(final NPC npc) {
		var position = npc.getPosition();
		var id = npc.getId();
		var factory = service.getNpcAvatarFactory();
		return factory.alloc(
				npc.getIndex(),
				id,
				position.getZ(),
				position.getX(),
				position.getY(),
				(int) Server.currentTick(),
				npc.getSpawnAvatarDirection(),
				npc.getSpawnAvatarPriority(),
				npc.isSpawnAvatarSpecific());
	}

	public static boolean removeNPCAvatar(final NPC npc) {
		final NpcAvatar avatar = npc.avatar;
		if (avatar == null) {
			logger.error(
					"Avatar was null, but removal was requested, possibly same-tick add/remove of npc (which has 0 client-sided effect)",
					new IllegalStateException());
			return false;
		}

		final var factory = service.getNpcAvatarFactory();
		factory.release(avatar);
		return true;
	}

	public static void tick(Iterable<Player> players, Iterable<NPC> npcs) {
		if (service == null) {
			return;
		}

		try {
			SentrySpans.start("core.rsprot.tick.update()", () -> {
				update(players, npcs);
			});

			SentrySpans.start("core.rsprot.tick.playersWriteUpdate()", () -> {
				playersWriteUpdate(players);
			});

			SentrySpans.start("core.rsprot.tick.playersFlush()", () -> {
				playersFlush(players);
			});

			SentrySpans.start("core.rsprot.tick.postUpdate()", () -> {
				postUpdate(players, npcs);
			});
		} catch (Throwable e) {
			log.error("Something really bad happened on update loop: ", e);
		} finally {
			zoneCache.releaseBuffers();
		}
	}

	private static void update(Iterable<Player> players, Iterable<NPC> npcs) {
		playersUpdateCoords(players);
		npcsUpdate(npcs);

		service.getPlayerInfoProtocol().update();
		service.getNpcInfoProtocol().update();
	}

	private static void playersUpdateCoords(Iterable<Player> players) {
		for (var player : players) {
			if (!player.isOnline()) {
				continue;
			}

			var position = player.getPosition();

			var z = position.getZ();
			var x = position.getX();
			var y = position.getY();

			player.rsprotPlayerInfo.updateCoord(z, x, y);
			player.rsprotNPCInfo.updateCoord(NpcInfo.ROOT_WORLD, z, x, y);

			player.rsprotPlayerInfo.updateRenderCoord(PlayerInfo.ROOT_WORLD, z, x, y);

			final var avatar = player.avatar();
			if (avatar != null) {
				avatar.setHidden(player.isHidden());
			}
		}
	}

	private static void postUpdate(Iterable<Player> players, Iterable<NPC> npcs) {
		npcsPostUpdate(npcs);
		playersPostUpdate(players);
	}

	private static void playersPostUpdate(Iterable<Player> players) {
		for (var player : players) {
			playerPostUpdate(player);
		}
	}

	private static void playerPostUpdate(Player player) {
		if (!player.isOnline()) {
			return;
		}
		player.resetUpdates();
		player.avatar().postUpdate();
		player.processed = false;
	}

	private static void npcsPostUpdate(Iterable<NPC> npcs) {
		for (var npc : npcs) {
			if (npc == null) {
				continue;
			}

			npcPostUpdate((NPC) npc);
		}
	}

	private static void npcPostUpdate(NPC npc) {
		npc.resetUpdates();
		npc.processed = false;
		if (npc.isRemoved()) {
			return;
		}
		if (npc.avatar() == null) {
			return;
		}

		npc.avatar().postUpdate();
	}

	private static void npcsUpdate(Iterable<NPC> npcs) {
		for (var npc : npcs) {
			if (npc == null) {
				continue;
			}

			npcUpdate((NPC) npc);
		}
	}

	private static void npcUpdate(final NPC npc) {
		try {
			if (npc.isRemoved()) {
				return;
			}

			// HOTFIX: core logic is fucked somewhere
			final NpcAvatar avatar = npc.avatar();
			if (avatar == null) {
				return;
			}

			final Position position = npc.getPosition();

			var x = position.getX();
			var y = position.getY();
			var z = position.getZ();

			var lastPosition = npc.getLastPosition();
			var movement = npc.getMovement();

			if (movement.hasTeleportUpdate()) {
				avatar.teleport(z, x, y, true);
			} else {
				for (int i = 0; i < movement.steps_len; i++) {
					var dx = movement.steps_x[i];
					var dy = movement.steps_y[i];
					if (movement.steps_type[i] == StepType.CRAWL) {
						avatar.crawl(dx, dy);
					} else {
						avatar.walk(dx, dy);
					}
				}
			}

			if (npc.isHidden()) {
				return;
			}

			// NOTE: just to be sure
			final boolean positionInvalid = x != avatar.x() || y != avatar.z();
			if (positionInvalid) {
				logger.warn("Invalid position detected. "
						+ npc.captureState()
						+ " last: " + lastPosition
						+ " current: " + position
						+ " avatar: " + avatar.x() + ", " + avatar.z());
				avatar.teleport(z, x, y, true);
			}

			for (final UpdateMask updateMask : npc.getMasks()) {
				if (updateMask.hasUpdate(true)) {
					updateMask.send(npc);
					updateMask.setSent(true);
				}
			}
		} catch (final Exception e) {
			logger.error("Unable to update NPC: "
					+ npc.getId()
					+ " @ " + npc.getPosition(),
					e);
		}
	}

	private static void playersWriteUpdate(Iterable<Player> players) {
		for (var player : players) {
			if (!player.isOnline()) {
				continue;
			}

			try {
				var activeWorld =
						new SetActiveWorldV2(new SetActiveWorldV2.RootWorldType(player.getPosition().getZ()));
				player.rsprotSession.queue(activeWorld);

				var playerPacket = player.rsprotPlayerInfo.toPacket();
				player.rsprotSession.queue(playerPacket);

				var npcOrigin = new SetNpcUpdateOrigin(player.getBaseLocalX(), player.getBaseLocalY());
				player.rsprotSession.queue(npcOrigin);

				var npcPacket = player.rsprotNPCInfo.toPacket(-1);
				player.rsprotSession.queue(npcPacket);

				var msg = ServerTickEnd.INSTANCE;
				player.rsprotSession.queue(msg);
			} catch (Exception e) {
				log.error("Unable to write update for player: " + player.getName(), e);
				player.forceLogout();
			}
		}
	}

	private static void playersFlush(Iterable<Player> players) {
		for (var player : players) {
			if (!player.isOnline()) {
				continue;
			}
			player.rsprotSession.flush();
		}
	}

}
