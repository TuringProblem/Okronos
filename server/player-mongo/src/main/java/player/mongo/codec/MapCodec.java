package player.mongo.codec;

import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.json.JsonReader;

import java.util.HashMap;
import java.util.Map;

public class MapCodec<K, T> implements Codec<Map<K, T>> {
	private final Class<Map<K, T>> encoderClass;
	private final Codec<K> keyCodec;
	private final Codec<T> valueCodec;

	MapCodec(final Class<Map<K, T>> encoderClass, final Codec<K> keyCodec, final Codec<T> valueCodec) {
		this.encoderClass = encoderClass;
		this.keyCodec = keyCodec;
		this.valueCodec = valueCodec;
	}

	@Override
	public void encode(final BsonWriter writer, final Map<K, T> map, final EncoderContext encoderContext) {
		try (var dummyWriter = new BsonDocumentWriter(new BsonDocument())) {
			dummyWriter.writeStartDocument();
			writer.writeStartDocument();
			for (final Map.Entry<K, T> entry : map.entrySet()) {
				dummyWriter.writeName(entry.getKey().toString());
				keyCodec.encode(dummyWriter, entry.getKey(), encoderContext);
				writer.writeName(entry.getKey().toString());

				if (entry.getValue() == null) {
					writer.writeNull();
				} else {
					valueCodec.encode(writer, entry.getValue(), encoderContext);
				}
			}
			dummyWriter.writeEndDocument();
		}
		writer.writeEndDocument();
	}

	@Override
	public Map<K, T> decode(final BsonReader reader, final DecoderContext context) {
		throw new IllegalStateException("unimplemented");
	}

	@Override
	public Class<Map<K, T>> getEncoderClass() {
		return encoderClass;
	}

	private Map<K, T> getInstance() {
		if (encoderClass.isInterface()) {
			return new HashMap<>();
		}
		try {
			return encoderClass.getDeclaredConstructor().newInstance();
		} catch (final Exception e) {
			throw new CodecConfigurationException(e.getMessage(), e);
		}
	}
}
