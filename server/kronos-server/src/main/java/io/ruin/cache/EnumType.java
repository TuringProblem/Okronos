package io.ruin.cache;

import com.google.common.collect.Maps;
import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.cache.runetek5.vartype.constants.ScriptVarType;
import io.ruin.utility.StringTools;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumType {

	private static Map<Integer, EnumType> cached = Maps.newConcurrentMap();

	public static HashMap<Integer, String> gs(int id) {
		return get(id).getStringValues();
	}

	public static HashMap<Integer, Integer> gi(int id) {
		return get(id).getIntValues();
	}

	public static EnumType get(int id) {
		if (cached.containsKey(id)) {
			return cached.get(id);
		}
		if (id < 0) {
			return null;
		}
		IndexFile index = Server.fileStore.get(2);
		byte[] data = index.getFile(8, id);
		if (data == null) {
			return null;
		}
		EnumType map = new EnumType();
		if (data != null)
			map.decode(new InBuffer(data));
		cached.put(id, map);
		return map;
	}

	@Getter
	private char keyTypeLegacyChar;
	@Getter
	private char valTypeLegacyChar;
	@Getter
	private int defaultInt;
	@Getter
	private String defaultString = "null";
	@Getter
	private ScriptVarType keyType;
	@Getter
	private ScriptVarType valType;
	@Getter
	private HashMap<Integer, Integer> intValues;
	@Getter
	private HashMap<Integer, String> stringValues;
	public int length = 0;

	public int getSize() {
		return this.length;
	}

	public int lookup(int id) {
		return lookup(id, this.defaultInt);
	}

	public int lookup(int id, int defaultInt) {
		if (intValues == null) {
			return id;
		}
		return intValues.getOrDefault(id, defaultInt);
	}

	public int key(String value) {
		for (var entry : this.stringValues.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	public int key(String value, int defaultValue) {
		for (var entry : this.stringValues.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return defaultValue;
	}

	public int valueInt(int key) {
		return this.valuesInt().getOrDefault(key, this.defaultInt);
	}

	public String lookup(int id, String defaultString) {
		if (stringValues == null) {
			return defaultString;
		}
		return stringValues.getOrDefault(id, defaultString);
	}

	public Map<Integer, Integer> valuesInt() {
		return this.intValues;
	}

	void decode(InBuffer in) {
		for (; ; ) {
			int var3 = in.readUnsignedByte();
			if (var3 == 0)
				break;
			decode(in, var3);
		}
	}

	void decode(InBuffer in, int var2) {
		if (var2 == 1) {
			keyTypeLegacyChar = StringTools.cp1252ToChar(in.readByte());
			keyType = ScriptVarType.lookupByLegacy(keyTypeLegacyChar);
		} else if (var2 == 2) {
			valTypeLegacyChar = StringTools.cp1252ToChar(in.readByte());
			valType = ScriptVarType.lookupByLegacy(valTypeLegacyChar);
		} else if (var2 == 3)
			defaultString = in.readStringCp1252NullTerminated();
		else if (var2 == 4)
			defaultInt = in.readInt();
		else if (var2 == 5) {
			length = in.readUnsignedShort();
			stringValues = new HashMap<>(length);
			for (int i = 0; i < length; i++) {
				stringValues.put(in.readInt(), in.readStringCp1252NullTerminated());
			}
		} else if (var2 == 6) {
			length = in.readUnsignedShort();
			intValues = new HashMap<>(length);
			for (int i = 0; i < length; i++) {
				intValues.put(in.readInt(), in.readInt());
			}
		}
	}

	public Map<Integer, Integer> intValuesReversed() {
		return intValues.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	}

}
