package player.mongo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Duration;

public class DurationCodec implements Codec<Duration> {
	@Override
	public Duration decode(BsonReader reader, DecoderContext decoderContext) {
		reader.readStartDocument();
		var seconds = reader.readInt64();
		var nanos = reader.readInt64();
		reader.readEndDocument();
		return Duration.ofSeconds(seconds, nanos);
	}

	@Override
	public void encode(BsonWriter writer, Duration value, EncoderContext encoderContext) {
		writer.writeStartDocument();
		writer.writeInt64("seconds", value.getSeconds());
		writer.writeInt64("nanos", value.getNano());
		writer.writeEndDocument();
	}

	@Override
	public Class<Duration> getEncoderClass() {
		return Duration.class;
	}
}
