package io.ruin.cache;

import com.google.common.collect.Maps;
import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.cache.runetek5.vartype.constants.ScriptVarType;
import io.ruin.utility.StringTools;
import lombok.Getter;

import java.util.Map;

public class ParamType {

	private static Map<Integer, ParamType> cached = Maps.newConcurrentMap();

	public static int intParam(int id) {
		return get(id).defaultIntValue;
	}

	public static boolean booleanParam(int id) {
		return get(id).defaultIntValue != 0;
	}

	public static Boolean nullableBooleanParam(int id) {
		ParamType type = get(id);
		if (type.defaultIntValue == -1)
			return null;
		return Boolean.valueOf(type.defaultIntValue != 0);
	}

	public static String stringParam(int id) {
		return get(id).defaultStringValue;
	}

	public static String nullableStringParam(int id) {
		ParamType type = get(id);
		if (type.defaultIntValue == -1)
			return null;
		return type.defaultStringValue;
	}

	public static ParamType get(int id) {
		if (cached.containsKey(id)) {
			return cached.get(id);
		}
		IndexFile index = Server.fileStore.get(2);
		byte[] data = index.getFile(11, id);
		ParamType map = new ParamType();
		if (data != null)
			map.decode(new InBuffer(data));
		cached.put(id, map);
		return map;
	}


	@Getter
	private ScriptVarType scriptVarType;
	@Getter
	private String defaultStringValue;
	@Getter
	private int defaultIntValue;
	@Getter
	private char scriptVarTypeChar;
	@Getter
	private boolean autoDisable = true;

	protected void decode(InBuffer buffer, int opcode) {
		if (1 == opcode) {
			scriptVarTypeChar = StringTools.cp1252ToChar(buffer.readByte());
			scriptVarType = ScriptVarType.lookupByLegacy(scriptVarTypeChar);
		} else if (2 == opcode)
			defaultIntValue = buffer.readInt();
		else if (opcode == 4)
			autoDisable = false;
		else if (5 == opcode)
			defaultStringValue = buffer.readStringCp1252NullTerminated();
	}

	public boolean isStringVarType() {
		return scriptVarType == ScriptVarType.STRING;
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