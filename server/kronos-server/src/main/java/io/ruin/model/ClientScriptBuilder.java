package io.ruin.model;

import io.ruin.model.entity.player.Player;

import java.util.ArrayList;

public class ClientScriptBuilder {

	private final int scriptId;
	private String types;
	private ArrayList<Object> values;

	public ClientScriptBuilder(int scriptId) {
		this.scriptId = scriptId;
		this.types = "";
		this.values = new ArrayList<>();
	}

	public ClientScriptBuilder addInt(int value) {
		types += "i";
		values.add(value);
		return this;
	}

	public ClientScriptBuilder addString(String value) {
		types += "s";
		values.add(value);
		return this;
	}

	public ClientScriptBuilder addBoolean(boolean value) {
		types += "i";
		values.add(value ? 1 : 0);
		return this;
	}

	public ClientScriptBuilder addWidget(int parentId, int childId) {
		return addWidget(parentId << 16 | childId);
	}

	public ClientScriptBuilder addWidget(int hash) {
		types += "I";
		values.add(hash);
		return this;
	}

	public ClientScriptBuilder addInventory(int parentId, int childId) {
		return addInventory(parentId << 16 | childId);
	}

	public ClientScriptBuilder addInventory(int hash) {
		types += "v";
		values.add(hash);
		return this;
	}

	public void send(Player player) {
		player.getPacketSender().sendClientScript(scriptId, types, values.toArray());
		values = null;
		types = null;
	}
}