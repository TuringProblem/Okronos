package io.ruin.api.item;

import lombok.Data;
import lombok.Getter;

@Data
public class ItemSql {
	@Getter
	private final int containerId;
	@Getter
	private final int itemId;
	@Getter
	private final int amount;
	@Getter
	private final int uniqueValue;
	@Getter
	private final int slot;
}
