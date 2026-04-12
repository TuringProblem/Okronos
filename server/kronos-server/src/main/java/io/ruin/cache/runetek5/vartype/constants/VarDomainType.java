package io.ruin.cache.runetek5.vartype.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VarDomainType {
	PLAYER(16),
	CLIENT(19),
	CLAN(47),
	CLAN_SETTING(54);

	public int groupId;

}
