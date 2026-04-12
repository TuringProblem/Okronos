package io.ruin.cache.runetek5.vartype;

import io.ruin.api.buffer.InBuffer;
import io.ruin.cache.runetek5.vartype.constants.ScriptVarType;
import io.ruin.cache.runetek5.vartype.constants.VarDomainType;
import io.ruin.cache.runetek5.vartype.constants.VarTransmitLevel;

import java.nio.ByteBuffer;

/**
 * OSRS uses a modified RS3 VarType system for varclan/varclansetting. There is no such thing as varclan/setting bits.
 * VarPlayer does not use this!!! Only VarBasicType uses it which is only instantiated for varclan/clansetting
 */

public abstract class VarType {


	public enum VarTypeEncodingKey {
		TYPE(1),
		TRANSMITLEVEL(2),
		VARNAME_HASH32(3),
		DEBUGNAME(10);

		int serialID;

		public int getId(int i) {
			return serialID;
		}

		VarTypeEncodingKey(int serialID) {
			this.serialID = serialID;
		}

		static VarTypeEncodingKey getById(int id) {
			for (VarTypeEncodingKey key : values()) {
				if (id == key.serialID) {
					return key;
				}
			}
			return null;
		}
	}

	public boolean aBool2151 = true;
	public VarDomainType domain;
	public int id;
	public int test;
	public VarTransmitLevel clientTransmitLevel;
	public ScriptVarType dataType;
	public String debugName;
	private int debugNameHash32;

	public VarType(int id, VarDomainType domain) {
		this.id = id;
		this.domain = domain;
	}

	public abstract void decode_inner(InBuffer packet, int opcode);

	public void decode(InBuffer buffer) {
		for (int opcode = buffer.readUnsignedByte(); opcode != 0; opcode = buffer.readUnsignedByte()) {
			VarTypeEncodingKey key = VarTypeEncodingKey.getById(opcode);
			if (key != null) {
				switch (key) {
					case TYPE: {
						char type = (char) buffer.readByte();
						dataType = ScriptVarType.lookupByLegacy(type);
						if (null == dataType) {
							throw new IllegalStateException("Invalid data type for var " + id + ": " + type);
						}
						break;
					}
					case TRANSMITLEVEL:
						clientTransmitLevel = VarTransmitLevel.getById(buffer.readUnsignedByte());
						break;
					case DEBUGNAME:
						debugName = buffer.readStringCp1252NullCircumfixed();
						break;
					case VARNAME_HASH32:
						debugNameHash32 = buffer.readInt();
						break;
					default:
						throw new IllegalStateException("Unsupported encoding key: " + key);
				}
			} else {
				decode_inner(buffer, opcode);
			}
		}
	}

	public void decode(ByteBuffer buffer, int code) {
		throw new IllegalStateException("Unsupported config code: " + code);
	}

	public Object getDefaultValue(int id) {
		return dataType.getDefaultValue();
	}

	@Override
	public String toString() {
		return debugName != null ? debugName : Integer.toString(id);
	}
}
