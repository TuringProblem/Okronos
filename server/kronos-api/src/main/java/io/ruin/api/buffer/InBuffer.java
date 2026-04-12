package io.ruin.api.buffer;

import io.ruin.api.protocol.Protocol;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class InBuffer {

	private int position;

	private final byte[] payload;

	public InBuffer(byte[] payload) {
		if (payload == null)
			payload = new byte[0];
		this.payload = payload;
	}

	public void position(int position) {
		this.position = position;
	}

	public void skip(int skip) {
		position += skip;
	}

	public void readBytes(byte[] bytes) {
		readBytes(bytes, 0, bytes.length);
	}

	public void readBytes(byte[] bytes, int offset, int length) {
		for (int i = offset; i < offset + length; i++)
			bytes[i] = readByte();
	}

	public void skipByte() {
		position++;
	}

	public byte readByte() {
		if (position >= payload.length)
			return 0;
		return payload[position++];
	}

	public int readUnsignedIntSmartShortCompat() {
		int var1 = 0;

		int var2;
		for (var2 = this.readUSmart(); var2 == 32767; var2 = this.readUSmart()) {
			var1 += 32767;
		}

		var1 += var2;
		return var1;
	}

	public int readUSmart() {
		int peek = payload[position] & 0xFF;
		return peek < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 0x8000;
	}

	public byte readByteUnsafe() {
		return payload[position++];
	}

	public int readUnsignedByte() {
		return readByte() & 0xff;
	}

	public byte readByteA() {
		return (byte) (readByte() - 128);
	}

	public int readUnsignedByteA() {
		return readByteA() & 0xff;
	}

	public byte readByteC() {
		return (byte) -readByte();
	}

	public int readUnsignedByteC() {
		return readByteC() & 0xff;
	}

	public int readByteS() {
		return (byte) (128 - readByte());
	}

	public int readUnsignedByteS() {
		return readByteS() & 0xff;
	}

	public int readShort() {
		int i = (readUnsignedByte() << 8) + readUnsignedByte();
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readUnsignedShort() {
		return (readUnsignedByte() << 8) + readUnsignedByte();
	}

	public int readShortA() {
		int i = (readUnsignedByte() << 8) + readUnsignedByteA();
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readUnsignedShortA() {
		return (readUnsignedByte() << 8) + readUnsignedByteA();
	}

	public int readLEShort() {
		int i = readUnsignedByte() + (readUnsignedByte() << 8);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readUnsignedLEShort() {
		return readUnsignedByte() + (readUnsignedByte() << 8);
	}

	public int readLEShortA() {
		int i = readUnsignedByteA() + (readUnsignedByte() << 8);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readUnsignedLEShortA() {
		return readUnsignedByteA() + (readUnsignedByte() << 8);
	}

	public int readMedium() {
		return (readUnsignedByte() << 16) + (readUnsignedByte() << 8) + readUnsignedByte();
	}

	public int readInt() {
		return (readUnsignedByte() << 24) + (readUnsignedByte() << 16) + (readUnsignedByte() << 8) + readUnsignedByte();
	}

	public int readIntME() {
		return (readUnsignedByte() << 8) + readUnsignedByte() + (readUnsignedByte() << 24) + (readUnsignedByte() << 16);
	}

	public int readIntME2() {
		return (readUnsignedByte() << 16) + (readUnsignedByte() << 24) + readUnsignedByte() + (readUnsignedByte() << 8);
	}

	public int readIntLE() {
		return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16) + (readUnsignedByte() << 24);
	}

	public int readUShortSmart() {
		int i = payload[position] & 0xff;
		if (i < 128)
			return readUnsignedByte();
		return readUnsignedShort() - 32768;
	}

	public int gSmart1or2Inc() {
		int i = 0;
		int base = readUShortSmart();
		while (base == 32767) {
			base = readUShortSmart();
			i += 32767;
		}
		return i + base;
	}

	public int readShortSmart() {
		int i = payload[position] & 0xff;
		if (i < 128)
			return readUnsignedByte() - 64;
		return readUnsignedShort() - 49152;
	}

	public int gSmart2or4() {
		if (payload[position] >= 0)
			return readUnsignedShort();
		return readInt() & 0x7fffffff;
	}

	public int gSmart2or4null() {
		if (payload[position] < 0) { // L: 393
			return this.readInt() & Integer.MAX_VALUE;
		} else {
			int var1 = this.readUnsignedShort(); // L: 394
			return var1 == 32767 ? -1 : var1; // L: 395
		}
	}

	public long readLong() {
		long l1 = (long) readInt() & 0xffffffffL;
		long l2 = (long) readInt() & 0xffffffffL;
		return (l1 << 32) + l2;
	}

	public String readString() {
		StringBuilder s = new StringBuilder();
		byte b;
		while ((b = readByte()) != 0)
			s.append((char) b);
		return s.toString();
	}

	public String readSafeString() {
		if (payload[position] != 0)
			return readStringCp1252NullTerminated();
		position++;
		return null;
	}

	public String readStringCp1252NullTerminated() {
		StringBuilder s = new StringBuilder();
		int read;
		while ((read = readByte()) != 0) {
			read &= 0xff;
			if (read >= 128 && read < 160) {
				int curChar = Protocol.aCharArray710[read - 128];
				if (curChar == 0)
					curChar = 63;
				read = curChar;
			}
			s.append((char) read);
		}
		return s.toString();
	}

	public String readStringCp1252NullCircumfixed() {// gjstr2
		int check = readByte();
		if (check != 0) { // L: 299
			throw new IllegalStateException("Bad version number in gjstr2");
		} else {
			StringBuilder s = new StringBuilder();
			int read;
			while ((read = readByte()) != 0) {
				read &= 0xff;
				if (read >= 128 && read < 160) {
					int curChar = Protocol.aCharArray710[read - 128];
					if (curChar == 0)
						curChar = 63;
					read = curChar;
				}
				s.append((char) read);
			}
			return s.toString();
		}
	}

	public int readIntBits() {
		int value = 0;
		int bits = 0;

		while (true) {
			int read = this.readByte() & 0xff;
			value |= (read & 0x7F) << bits;
			bits += 7;
			if (read <= 127) {
				break;
			}
		}

		return value;
	}

	public int readBigSmart2() {
		if (this.payload[this.position] < 0) { // L: 369
			return this.readInt() & Integer.MAX_VALUE;
		} else {
			int var1 = this.readUnsignedShort(); // L: 370
			return var1 == 32767 ? -1 : var1; // L: 371
		}
	}

	public int readUnsignedShortSmartMinusOne() {
		int peek = this.payload[this.position] & 0xFF;
		return peek < 128 ? this.readUnsignedByte() - 1 : this.readUnsignedShort() - 0x8001;
	}

	public void decode(int[] keys, int offset, int length) {
		int var5 = position;
		position = offset;
		int var6 = (length - offset) / 8;
		for (int var7 = 0; var7 < var6; var7++) {
			int var8 = readInt();
			int var9 = readInt();
			int var10 = -957401312;
			int var11 = -1640531527;
			int var12 = 32;
			while (var12-- > 0) {
				var9 -= (var8 + (var8 << 4 ^ var8 >>> 5) ^ keys[var10 >>> 11 & 0x3] + var10);
				var10 -= var11;
				var8 -= (var9 + (var9 << 4 ^ var9 >>> 5) ^ var10 + keys[var10 & 0x3]);
			}
			position -= 8;
			// addInt
			payload[position++] = (byte) (var8 >> 24);
			payload[position++] = (byte) (var8 >> 16);
			payload[position++] = (byte) (var8 >> 8);
			payload[position++] = (byte) var8;
			// addInt
			payload[position++] = (byte) (var9 >> 24);
			payload[position++] = (byte) (var9 >> 16);
			payload[position++] = (byte) (var9 >> 8);
			payload[position++] = (byte) var9;
		}
		position = var5;
	}

	public static int method1909(int var0) {
		--var0; // L: 50
		var0 |= var0 >>> 1; // L: 51
		var0 |= var0 >>> 2; // L: 52
		var0 |= var0 >>> 4; // L: 53
		var0 |= var0 >>> 8; // L: 54
		var0 |= var0 >>> 16; // L: 55
		return var0 + 1; // L: 56
	}

	public Long2ObjectMap<Object> readStringIntParameters() {
		int var2 = readUnsignedByte(); // L: 16
		int var3 = method1909(var2); // L: 18
		Long2ObjectMap<Object> map = new Long2ObjectOpenHashMap<>(var3);

		for (var3 = 0; var3 < var2; ++var3) { // L: 21
			boolean var4 = readUnsignedByte() == 1; // L: 22
			int var5 = readMedium(); // L: 23
			Object var6;
			if (var4) { // L: 25
				var6 = readStringCp1252NullTerminated();
			} else {
				var6 = readInt(); // L: 26
			}

			map.put(var5, var6);
		}
		return map;
	}

	public int remaining() {
		return payload.length - position;
	}

	public int position() {
		return position;
	}

	public byte[] getPayload() {
		return payload;
	}

}
