package io.ruin.cache.runetek5.vartype.constants;


import io.ruin.api.buffer.InBuffer;

import java.util.function.Function;

/**
 * @author Sundays211
 * @since 12/12/2015
 */
public enum BaseVarType {
	INTEGER(0, Integer.class, (x -> x.readInt())),
	LONG(1, Long.class, (x -> x.readLong())),
	STRING(2, String.class, (x -> x.readStringCp1252NullTerminated()));

	public final Class<?> javaClass;
	private int id;
	private Function<InBuffer, Object> decoder;

	private BaseVarType(int id, Class<?> javaClass, Function<InBuffer, Object> decoder) {
		this.id = id;
		this.javaClass = javaClass;
		this.decoder = decoder;
	}

	public int getId() {
		return id;
	}

	public Object decode(InBuffer buffer) {
		return decoder.apply(buffer);
	}

	public static BaseVarType getById(int id) {
		for (BaseVarType key : values()) {
			if (id == key.id) {
				return key;
			}
		}
		return null;
	}
}
