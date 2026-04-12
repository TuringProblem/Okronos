package io.ruin.model.entity.shared.masks;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerClientRank;
import io.ruin.model.entity.shared.UpdateMask;

public class ChatUpdate extends UpdateMask {

	private boolean shadow;
	private int type;
	private int effects;
	private PlayerClientRank rankId;
	private int title;
	private String message;

	public void set(boolean shadow, int effects, PlayerClientRank rankId, int title, int type, String message) {
		this.shadow = shadow;
		this.effects = effects;
		this.rankId = rankId;
		this.title = title;
		this.type = type;
		this.message = message;
	}

	@Override
	public void reset() {
		this.message = null;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return this.message != null;
	}

	@Override
	public void send(boolean playerUpdate) {
	}

	@Override
	public void send(Player player) {
		var color = this.type;
		var effects = this.effects;
		var rankId = this.rankId;
		var autotype = this.type == 1;
		player.avatarExtended().setChat(color, effects, rankId.raw, autotype, this.message, null);
	}

	@Override
	public int get(boolean playerUpdate) {
		return 1;
	}

}
