package io.ruin.cache.runetek5.vartype.general;

import io.ruin.api.buffer.InBuffer;
import io.ruin.cache.runetek5.vartype.constants.VarDomainType;
import io.ruin.cache.runetek5.vartype.VarType;


public final class VarBasicType extends VarType {

	public VarBasicType(int id, VarDomainType domain) {
		super(id, domain);
	}

	@Override
	public void decode_inner(InBuffer packet, int opcode) {
		System.err.println("VarBasicType " + opcode + " not supported for " + domain + "!");

	}
}