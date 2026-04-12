package io.ruin.model.content.equipmentpresets;

import com.google.gson.annotations.Expose;
import io.ruin.model.item.Item;

import java.util.HashMap;
import java.util.List;

public class GearPreset {
	List<Item> inventory;
	HashMap<Integer, Item> equipment;
	String presetName;

	public GearPreset(String presetName, List<Item> inventory, HashMap<Integer, Item> equipment) {
		this.inventory = inventory;
		this.equipment = equipment;
		this.presetName = presetName;
	}
}
