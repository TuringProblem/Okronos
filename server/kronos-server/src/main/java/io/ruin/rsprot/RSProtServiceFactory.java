package io.ruin.rsprot;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.DecoderException;
import io.netty.util.AttributeKey;
import io.netty.util.internal.PlatformDependent;
import io.ruin.Server;
import io.ruin.api.protocol.Response;
import io.ruin.api.protocol.login.LoginInfo;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerLogin;
import io.ruin.model.entity.player.PlayerLoginWorker;
import io.ruin.model.entity.player.Player.LogoutStage;
import io.ruin.model.entity.player.PlayerLogin.Cause;
import io.ruin.network.update.CacheManager;
import io.ruin.rsprot.handler.RSProtHandlers;
import lombok.extern.slf4j.Slf4j;
import net.rsprot.compression.HuffmanCodec;
import net.rsprot.compression.provider.DefaultHuffmanCodecProvider;
import net.rsprot.compression.provider.HuffmanCodecProvider;
import net.rsprot.crypto.rsa.RsaKeyPair;
import net.rsprot.crypto.xtea.XteaKey;
import net.rsprot.protocol.api.AbstractNetworkServiceFactory;
import net.rsprot.protocol.api.ChannelExceptionHandler;
import net.rsprot.protocol.api.GameConnectionHandler;
import net.rsprot.protocol.api.NetworkService;
import net.rsprot.protocol.api.handlers.ExceptionHandlers;
import net.rsprot.protocol.api.handlers.INetAddressHandlers;
import net.rsprot.protocol.api.handlers.LoginHandlers;
import net.rsprot.protocol.api.implementation.DefaultInetAddressTracker;
import net.rsprot.protocol.api.implementation.DefaultInetAddressValidator;
import net.rsprot.protocol.api.implementation.DefaultLoginDecoderService;
import net.rsprot.protocol.api.implementation.DefaultSessionIdGenerator;
import net.rsprot.protocol.api.implementation.DefaultStreamCipherProvider;
import net.rsprot.protocol.api.js5.Js5GroupProvider;
import net.rsprot.protocol.api.login.GameLoginResponseHandler;
import net.rsprot.protocol.common.client.OldSchoolClientType;
import net.rsprot.protocol.loginprot.incoming.pow.NopProofOfWorkProvider;
import net.rsprot.protocol.loginprot.incoming.pow.ProofOfWorkProvider;
import net.rsprot.protocol.loginprot.incoming.pow.challenges.ChallengeWorker;
import net.rsprot.protocol.loginprot.incoming.pow.challenges.DefaultChallengeWorker;
import net.rsprot.protocol.loginprot.incoming.pow.challenges.sha256.DefaultSha256ProofOfWorkProvider;
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType;
import net.rsprot.protocol.loginprot.incoming.util.HostPlatformStats;
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType.PasswordAuthentication;
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock;
import net.rsprot.protocol.loginprot.outgoing.LoginResponse;
import net.rsprot.protocol.loginprot.outgoing.util.AuthenticatorResponse;
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepositoryBuilder;
import net.rsprot.protocol.message.codec.incoming.provider.DefaultGameMessageConsumerRepositoryProvider;
import net.rsprot.protocol.message.codec.incoming.provider.GameMessageConsumerRepositoryProvider;
import net.rsprot.protocol.metrics.NetworkTrafficMonitor;
import org.jetbrains.annotations.NotNull;
import org.rsmod.game.entity.EntityList;
import properties.ServerProperties;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;

/**
 * RSProtServiceFactory
 */
@Slf4j
public class RSProtServiceFactory extends AbstractNetworkServiceFactory<Player> {

	static {
		System.getProperties().setProperty("net.rsprot.protocol.internal.development", ServerProperties.get("rsprot_dev"));
		System.getProperties().setProperty("net.rsprot.protocol.internal.npcAvatarMaxId", "65534");
	}

	private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z0-9_ ]+");

	private final GameMessageConsumerRepositoryBuilder<Player> externalHandlers;
	private final int[] crcTable = new int[24];

	public RSProtServiceFactory(GameMessageConsumerRepositoryBuilder<Player> externalHandlers) {
		this.externalHandlers = externalHandlers;
		for (int idx = 0; idx < this.crcTable.length; idx++) {
			var index = Server.fileStore.get(idx);
			if (index == null) {
				continue;
			}
			crcTable[idx] = index.table.crc;
		}

	}

	public static NetworkService<Player> create(GameMessageConsumerRepositoryBuilder<Player> externalHandlers) {
		return new RSProtServiceFactory(externalHandlers).build();
	}

	@Override
	public @NotNull GameConnectionHandler<Player> getGameConnectionHandler() {
		return new GameConnectionHandler<Player>() {
			@Override
			public void onLogin(
					@NotNull GameLoginResponseHandler<Player> responseHandler,
					@NotNull LoginBlock<AuthenticationType> block) {
				var crcs = block.getCrc().toIntArray();
				for (int idx = 0; idx < crcs.length; idx++) {
					if (crcTable[idx] != crcs[idx]) {
						responseHandler.writeFailedResponse(LoginResponse.OutOfDateReload.INSTANCE);
						return;
					}
				}

				if (!isUsernameValid(block.getUsername())) {
					responseHandler.writeFailedResponse(LoginResponse.InvalidUsernameOrPassword.INSTANCE);
					return;
				}

				final int slot = World.playersNullable().nextFreeSlot();
				if (slot == EntityList.INVALID_SLOT) {
					responseHandler.writeFailedResponse(LoginResponse.ServerFull.INSTANCE);
					return;
				}

				final var authentication = block.getAuthentication();
				if (authentication instanceof PasswordAuthentication auth) {

					var hwid = hwid(block.getHostPlatformStats());
					var tfaCode = 0;
					var tfaTrust = false;
					var tfaTrustValue = 0;
					String uuid = null;
					var channel = responseHandler.getCtx().channel();
					var address = channel.remoteAddress().toString().split(":")[0].replace("/", "");
					var loginInfo = new LoginInfo(
							block.getSeed(),
							block.getSessionId(),
							address,
							block.getUsername(),
							auth.getPassword().asString(),
							block.getUsername(),
							null,
							uuid,
							hwid,
							tfaCode, tfaTrust, tfaTrustValue);
					var loginHandler = new PlayerLoginHandler(responseHandler, block);
					var loginRequest = new PlayerLogin(loginInfo, loginHandler);
					PlayerLoginWorker.queue(loginRequest);
				} else {
					System.err.println("Unimplemented auth type: " + authentication);
				}
			}

			@Override
			public void onReconnect(GameLoginResponseHandler<Player> handler, LoginBlock<XteaKey> loginBlock) {
				Server.worker.execute(() -> {
					var player = World.getPlayer(loginBlock.getUsername());
					if (player == null) {
						handler.getCtx().close();
						return;
					}

					var xteaKeys = loginBlock.getAuthentication().getKey();
					var xteaKeysNew = loginBlock.getSeed();

					if (!Arrays.equals(xteaKeys, player.lastLoginKeys)) {
						handler.getCtx().close();
						return;
					}

					// too late to safely reconnect
					if (player.logoutProcessStage.isAnyOf(LogoutStage.PostLogout, LogoutStage.LogoutAccepted)) {
						handler.getCtx().close();
						return;
					}

					player.logoutProcessStage = LogoutStage.NoLogout;
					var pInfo = player.rsprotPlayerInfo;
					var nInfo = player.rsprotNPCInfo;
					pInfo.onReconnect();
					nInfo.onReconnect();
					var session = handler.writeSuccessfulResponse(new LoginResponse.ReconnectOk(pInfo), loginBlock);
					player.onReconnectAccepted(session, xteaKeysNew);
					session.flush();
				});
			}
		};
	}

	@Override
	public NetworkTrafficMonitor<?> getNetworkTrafficMonitor() {
		return buildNetworkTrafficMonitor();
	}

	@Override
	public GameMessageConsumerRepositoryProvider<Player> getGameMessageConsumerRepositoryProvider() {
		RSProtHandlers.register(this.externalHandlers);
		return new DefaultGameMessageConsumerRepositoryProvider<>(this.externalHandlers.build());
	}

	@Override
	public Js5GroupProvider getJs5GroupProvider() {
		return new Js5GroupProvider() {
			record Key(int index, int archive) {
			}

			private final HashMap<Key, ByteBuf> cache = new HashMap<>();

			@Override
			public ByteBuf provide(int index, int archive) {
				var buffer = this.cache.computeIfAbsent(new Key(index, archive), (k) -> {
					try {
						var cacheData = CacheManager.get(index, archive, true);
						var input = cacheData.toBuffer();
						return Unpooled.unreleasableBuffer(input);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
				return buffer;
			}
		};
	}

	@Override
	public HuffmanCodecProvider getHuffmanCodecProvider() {
		return new DefaultHuffmanCodecProvider(createHuffmanCodec());
	}

	@Override
	public ByteBufAllocator getAllocator() {
		return new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
	}

	@Override
	public RsaKeyPair getRsaKeyPair() {
		var exp = new BigInteger(ServerProperties.get("RSA_EXPONENT"), 10);
		var mod = new BigInteger(ServerProperties.get("RSA_MODULUS"), 10);
		return new RsaKeyPair(exp, mod);
	}

	@Override
	public ExceptionHandlers<Player> getExceptionHandlers() {
		return new ExceptionHandlers<>(new ChannelExceptionHandler() {
			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
				var shouldLog = true;
				if (throwable == null) {
					shouldLog = false;
				}

				if (throwable instanceof IOException) {
					shouldLog = false;
				}

				if (throwable instanceof DecoderException) {
					shouldLog = false;
				}

				if (shouldLog) {
					log.error("Error on rsprot", throwable);
				}

				if (ctx.channel().isOpen()) {
					ctx.channel().config().setOption(ChannelOption.SO_LINGER, 0);
					ctx.channel().close();
				}
			}
		});
	}

	@Override
	public List<OldSchoolClientType> getSupportedClientTypes() {
		return List.of(OldSchoolClientType.DESKTOP);
	}

	@Override
	public List<Integer> getPorts() {
		return List.of(World.world_port);
	}

	private static HuffmanCodec createHuffmanCodec() {
		try {
			var huffmanData = Unpooled
					.wrappedBuffer(RSProtServiceFactory.class.getResourceAsStream("/huffman.dat").readAllBytes());
			return HuffmanCodec.Companion.create(huffmanData);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LoginHandlers getLoginHandlers() {
		ProofOfWorkProvider<?, ?> pow = World.login_pow_enabled ? new DefaultSha256ProofOfWorkProvider(1)
				: NopProofOfWorkProvider.INSTANCE;
		ChallengeWorker powWorker = DefaultChallengeWorker.INSTANCE;
		return new LoginHandlers(new DefaultSessionIdGenerator(), new DefaultStreamCipherProvider(),
				new DefaultLoginDecoderService(), pow, powWorker, ForkJoinPool.commonPool());
	};

	public INetAddressHandlers getINetAddressHandlers() {
		return new INetAddressHandlers(
				new DefaultInetAddressValidator(World.login_con_limit),
				new DefaultInetAddressTracker());
	};

	public static class PlayerLoginHandler implements PlayerLogin.Handler {
		private final GameLoginResponseHandler<Player> handler;
		private final LoginBlock<AuthenticationType> loginBlock;

		public PlayerLoginHandler(GameLoginResponseHandler<Player> handler, LoginBlock<AuthenticationType> loginBlock) {
			this.handler = handler;
			this.loginBlock = loginBlock;
		}

		@Override
		public void accepted(PlayerLoginWorker.AcceptedPlayerLogin login) {
			final var player = login.player();
			final var service = this.handler.getNetworkService();

			try {
				if (handler != null && handler.getCtx().isRemoved()) {
					log.info("Unable to accept login, connection reset.");
					World.removePlayer(player);
					return;
				}
				var rankId = player.getMessagingRank().raw;
				var session = this.handler.writeSuccessfulResponse(new LoginResponse.Ok(
						AuthenticatorResponse.NoAuthenticator.INSTANCE,
						rankId,
						player.isModerator(),
						player.getIndex(),
						true,
						0,
						0,
						0), loginBlock);
				player.onLoginAccepted(session, service, login.info());
				player.getPacketSender().sendRegion(true);
			} catch (Exception e) {
				log.error("Error while accepting login.", e);
				World.removePlayer(player);
			}
		}

		@Override
		public void denied(Cause cause) {
			var response = cause.response();
			if (response == null) {
				var text = cause.text() == null ? "n/a" : cause.text();
				this.handler.writeFailedResponse(new LoginResponse.DisallowedByScript("Unable to login.", text.toString(), ""));
				return;
			}

			this.handler.writeFailedResponse(from(response));
		}

		private static LoginResponse from(Response response) {
			return switch (response) {
				case Response.LOGIN_LIMIT:
					yield LoginResponse.AlreadyInQueue.INSTANCE;
				case Response.CONNECTION_LIMIT:
					yield LoginResponse.IPLimit.INSTANCE;
				case Response.ALREADY_LOGGED_IN:
					yield LoginResponse.Duplicate.INSTANCE;
				case Response.WORLD_FULL:
					yield LoginResponse.ServerFull.INSTANCE;
				case Response.ERROR_LOADING_ACCOUNT:
					yield LoginResponse.LoginServerLoadError.INSTANCE;
				case Response.INVALID_LOGIN:
					yield LoginResponse.InvalidUsernameOrPassword.INSTANCE;
				case Response.DISABLED_ACCOUNT:
					yield LoginResponse.Banned.INSTANCE;
				default:
					yield new LoginResponse.DisallowedByScript("Unable to login.", response.toString(), "");
			};
		}
	}

	private static String hwid(HostPlatformStats stats) {
		return stats.getDeviceName();
	}

	private static boolean isUsernameValid(String name) {
		if (name == null
				|| name.length() > 12
				|| name.length() < 2
				|| (name = name.trim()).isEmpty()
				|| !NAME_PATTERN.matcher(name).matches()) {
			return false;
		}
		return true;
	}
}
