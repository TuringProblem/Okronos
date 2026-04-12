package player.mongo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Arrays;

@SuppressWarnings("ALL")
public class EnumCodec<T extends Enum<T>> implements Codec<T> {
	private final Logger log = System.getLogger(EnumCodec.class.getName());
	private final T[] values;

	public EnumCodec(T[] values) {
		this.values = values;
	}

	@Override
	public void encode(final BsonWriter writer, final T value,
			final EncoderContext encoderContext) {
		writer.writeString(value.name());
	}

	@Override
	public T decode(final BsonReader reader,
			final DecoderContext decoderContext) {
		String name = reader.readString();
		if (name == null) {
			return null;
		}

		T value = Arrays.stream(this.values)
				.filter(it -> it.name().equals(name)).findFirst().orElse(null);

		if (value == null) {
			log.log(Level.ERROR,
					"Unable to find enum variant {0} available: {1}", name,
					Arrays.toString(this.values));
			return values[0];
		}

		return value;

	}

	@Override
	public Class<T> getEncoderClass() {
		return (Class<T>) this.values.getClass().getComponentType();
	}
}
