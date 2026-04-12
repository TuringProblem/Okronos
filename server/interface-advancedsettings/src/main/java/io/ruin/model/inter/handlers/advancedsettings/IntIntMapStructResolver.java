package io.ruin.model.inter.handlers.advancedsettings;

import java.util.Map;

/**
 * @author Jire
 */
public final class IntIntMapStructResolver implements SettingStructResolver {

	private final Map<Integer, Integer> map;

	public IntIntMapStructResolver(Map<Integer, Integer> map) {
		this.map = map;
	}

	@Override
	public Integer forStruct(int struct) {
		return map.get(struct);
	}

}
