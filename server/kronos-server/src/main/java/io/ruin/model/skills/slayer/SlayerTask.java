package io.ruin.model.skills.slayer;

import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import lombok.Getter;
import lombok.Setter;

public class SlayerTask {
	@Setter
	private String taskName;
	@Setter
	@Getter
	private Item[] requiredItems;
	@Getter
	private final int[] npcIds;
	@Setter
	@Getter
	private Position location;
	@Getter
	private final int combatRequirement;
	@Getter
	private final int slayerRequirement;
	@Getter
	private final int minAmount;
	@Getter
	private final int maxAmount;
	@Getter
	private final int taskId;

	@Setter
	@Getter
	private int amountLeft;
	public boolean wilderness;

	public SlayerTask(int taskId, String taskName, Position location, Item[] requiredItems, int[] npcIds, int combatRequirement, int slayerRequirement, int minAmount, int maxAmount) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.requiredItems = requiredItems;
		this.npcIds = npcIds;
		this.location = location;
		this.combatRequirement = combatRequirement;
		this.slayerRequirement = slayerRequirement;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.amountLeft = 0;
	}

	public SlayerTask(int taskId, String taskName, Position location, Item[] requiredItems, int[] npcIds, int combatRequirement, int slayerRequirement, int minAmount, int maxAmount, boolean wilderness) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.requiredItems = requiredItems;
		this.npcIds = npcIds;
		this.location = location;
		this.combatRequirement = combatRequirement;
		this.slayerRequirement = slayerRequirement;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.amountLeft = 0;
		this.wilderness = wilderness;
	}

	public String getTaskName() {
		return "<col=cc0000>" + taskName + "</col>";
	}

}
