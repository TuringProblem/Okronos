package io.ruin.model.skills.construction.servants;

import com.google.gson.annotations.Expose;
import io.ruin.model.item.ItemContainer;

public class ServantSave {

	private ServantDefinition hiredServant = null;
	private ItemContainer inventory = new ItemContainer();
	private int actions = 0;
	private ServantAction lastAction;
	private int lastActionItemId = -1;
	private int lastActionItemAmount = -1;

	public void init() {
		if (hiredServant != null)
			inventory.init(hiredServant.getItemCapacity(), false);
	}

	public ServantAction getLastAction() {
		return lastAction;
	}

	public void setLastAction(ServantAction lastAction) {
		this.lastAction = lastAction;
	}

	public int getLastActionItemId() {
		return lastActionItemId;
	}

	public void setLastActionItemId(int lastActionItemId) {
		this.lastActionItemId = lastActionItemId;
	}

	public int getLastActionItemAmount() {
		return lastActionItemAmount;
	}

	public void setLastActionItemAmount(int lastActionItemAmount) {
		this.lastActionItemAmount = lastActionItemAmount;
	}

	public ServantDefinition getHiredServant() {
		return hiredServant;
	}

	public ItemContainer getInventory() {
		return inventory;
	}

	public int getActions() {
		return actions;
	}

	public int incrementActions() {
		return ++actions;
	}

	public void resetActions() {
		actions = 0;
	}

	public void fire() {
		hiredServant = null;
		resetActions();
		inventory.clear();
	}

	public void hire(ServantDefinition servant) {
		hiredServant = servant;
		resetActions();
		if (inventory != null && inventory.getItems() != null)
			inventory.clear();
	}
}
