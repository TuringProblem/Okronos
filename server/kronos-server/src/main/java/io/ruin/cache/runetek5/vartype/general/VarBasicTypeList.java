package io.ruin.cache.runetek5.vartype.general;

import com.google.common.collect.Maps;
import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.cache.runetek5.vartype.constants.VarDomainType;
import io.ruin.cache.runetek5.vartype.VarType;

import java.util.Map;

public class VarBasicTypeList {
	private static Map<Integer, VarType> cached = Maps.newConcurrentMap();
	protected VarDomainType domain;
	public int size = -1;

	public VarBasicTypeList(VarDomainType domain) {
		this.domain = domain;
	}

	public VarType get(int id) {
		synchronized (this.cached) {
			VarBasicType varType = (VarBasicType) this.cached.get(id);
			if (varType == null) {
				varType = this.fetch(id);
				this.cached.put(id, varType);
			}

			return varType;
		}
	}

	private VarBasicType fetch(int id) {
		IndexFile index = Server.fileStore.get(2);
		if (size == -1) {
			size = Server.fileStore.get(2).getValidFilesCount(domain.groupId);
		}
		byte[] data = index.getFile(domain.groupId, id);
		VarBasicType map = new VarBasicType(id, domain);
		if (data != null)
			map.decode(new InBuffer(data));
		return map;
	}
}
