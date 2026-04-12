package player.attributes.api;

import io.ruin.model.entity.player.Player;
import player.attributes.PlayerAttributeCodec;
import player.attributes.PlayerAttributesRegistry;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * API
 */
public class API {
	public static <T> T attrib(Class<T> cls, Player player) {
		return attrib(cls, player.getIndex());
	}

	public static <T> T attrib(Class<T> cls, int index) {
		return PlayerAttributesRegistry.get(index, cls);
	}

	public static interface AttributesAPI {

		interface Register {
			default <T> void migration(Class<T> cls, Supplier<T> constructor, Function<PlayerAttributeCodec.LoadContext, T> migrationFn) {
				var persistent = PlayerAttributeCodec.persistent(cls, constructor);
				var codec = new PlayerAttributeCodec<T>() {
					@Override
					public T load(LoadContext ctx) {
						if (!persistent.hasData(ctx)) {
							var migrated = migrationFn.apply(ctx);
							return migrated;
						}
						return persistent.load(ctx);
					}

					@Override
					public void save(SaveContext ctx, T value) {
						persistent.save(ctx, value);
					}
				};
				PlayerAttributesRegistry.register(cls, codec);
			}

			default <T> void temporary(Class<T> cls, Supplier<T> constructor) {
				PlayerAttributesRegistry.register(cls, PlayerAttributeCodec.temporary(constructor));
			}

			default <T> void persistent(Class<T> cls, Supplier<T> constructor) {
				PlayerAttributesRegistry.register(cls, PlayerAttributeCodec.persistent(cls, constructor));
			}

			default <T> void custom(Class<T> cls, PlayerAttributeCodec<T> loader) {
				PlayerAttributesRegistry.register(cls, loader);
			}
		}

		default Register register() {
			return new Register() {
			};
		}

		default void unregister(Class<?> cls) {
			PlayerAttributesRegistry.unregister(cls);
		}

	}

	public static AttributesAPI attrib() {
		return new AttributesAPI() {
		};
	}
}
