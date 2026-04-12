package io.ruin.cache;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Struct {

	public static Struct get(int id) {
		IndexFile index = Server.fileStore.get(2);
		byte[] data = index.getFile(34, id);
		Struct struct = new Struct();
		if (data != null)
			struct.decode(new InBuffer(data));
		return struct;
	}

	private Map<Object, Object> params = new HashMap<>();

	void decode(InBuffer in) {
		int var2 = in.readUnsignedByte();

		if (var2 == 0) {
			return;
		}

		readValues(in, var2);
	}

	void readValues(InBuffer in, int var2) {
		if (var2 == 249) {
			int size = in.readUnsignedByte();
			if (params == null)
				params = new HashMap<>();
			for (int i = 0; i < size; i++) {
				boolean stringType = in.readUnsignedByte() == 1;
				int key = in.readMedium();
				if (stringType)
					params.put(key, in.readString());
				else
					params.put(key, in.readInt());
			}
		}
	}

	public int getInt(int key) {
		return (int) params.get(key);
	}

	public Map<Object, Object> getParams() {
		return params;
	}

	public Set<Object> keys() {
		return params.keySet();
	}

}