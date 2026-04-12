package io.ruin.model.shop;

import lombok.Data;

@Data
public class RestockRules {

	public static final RestockRules generateDefault() {
		return new RestockRules(30, 1);//XXX this is OSRS default rate
	}

	public RestockRules(int restockTicks, int restockPerTick) {
		this.restockTicks = restockTicks;
		this.restockPerTick = restockPerTick;
	}

	public RestockRules() {
	}

	public int restockTicks;
	public int restockPerTick;

}
