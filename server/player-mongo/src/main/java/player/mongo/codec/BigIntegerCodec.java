package player.mongo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.math.BigInteger;

public final class BigIntegerCodec implements Codec<BigInteger> {

	@Override
	public void encode(
			final BsonWriter writer, final BigInteger value, final EncoderContext encoderContext) {
		writer.writeString(value.toString());
	}

	@Override
	public BigInteger decode(final BsonReader reader, final DecoderContext decoderContext) {
		String value = reader.readString();
		return new BigInteger(value);
	}

	@Override
	public Class<BigInteger> getEncoderClass() {
		return BigInteger.class;
	}
}
