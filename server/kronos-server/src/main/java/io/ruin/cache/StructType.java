package io.ruin.cache;

import com.google.common.collect.Maps;
import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StructType {

	private static Map<Integer, StructType> cached = Maps.newConcurrentMap();

	public StructType(StructType structConfig) {
		this.params = structConfig.params;
		this.id = structConfig.id;
	}

	public StructType(int id) {
		this.id = id;
	}

	public static StructType get(int id) {
		if (cached.containsKey(id)) {
			return cached.get(id);
		}
		IndexFile index = Server.fileStore.get(2);
		byte[] data = index.getFile(34, id);
		StructType map = new StructType(id);
		if (data != null)
			map.decode(new InBuffer(data));
		cached.put(id, map);
		return map;
	}

	private int id;

	@Getter
	private HashMap<Integer, Object> params = new HashMap<>();

	public String getParam(int key, String defaultStr) {
		if (params == null) {
			return defaultStr;
		}
		Object value = params.get(key);
		if (value == null) {
			return defaultStr;
		}
		return (String) value;
	}

	public int getParam(int key, int defaultInt) {
		if (params == null) {
			return defaultInt;
		}
		Object value = params.get(key);
		if (value == null) {
			return defaultInt;
		}
		return (int) value;
	}

	public int intParam(int key, int defaultInt) {
		if (params == null) {
			return defaultInt;
		}
		Object value = params.get(key);
		if (value == null) {
			return defaultInt;
		}
		return (int) value;
	}

	public boolean hasParam(int key) {
		return params.containsKey(key);
	}

	public int intParam(int key) {
		Object value = params.get(key);
		return (int) value;
	}

	public boolean boolParam(int key) {
		Object value = params.get(key);
		return (int) value == 1;
	}

	public boolean boolParam(int key, boolean defaultValue) {
		Object value = params.get(key);
		if (value == null) {
			return defaultValue;
		}
		return (int) value == 1;
	}

	public String stringParam(int key) {
		Object value = params.get(key);
		return (String) value;
	}

	public String stringParam(int key, String defaultValue) {
		Object value = params.get(key);
		if (value == null) {
			return defaultValue;
		}
		return (String) value;
	}

	public int id() {
		return this.id;
	}

	public Map<Integer, Object> params() {
		return this.params;
	}

	public void decode(InBuffer buffer, int opcode) {
		if (opcode == 249) {
			int length = buffer.readUnsignedByte();
			for (int index = 0; index < length; index++) {
				boolean stringInstance = (buffer.readUnsignedByte()) == 1;
				int key = buffer.readMedium();
				Object val;
				if (stringInstance) {
					val = buffer.readStringCp1252NullTerminated();
				} else {
					val = buffer.readInt();
				}
				params.put(key, val);
			}
		}
	}

	void decode(InBuffer in) {
		for (; ; ) {
			int var3 = in.readUnsignedByte();
			if (var3 == 0)
				break;
			decode(in, var3);
		}
	}

}
