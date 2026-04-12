package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class MapDirectionUpdate extends UpdateMask {

	private boolean instant = false;
	private boolean update;

	private int direction = -1;

	private int faceX = -1, faceY = -1;

	public void set(Player player, int faceX, int faceY) {
		int size = player.getSize();
		int anInt1783 = (player.getBaseLocalX() << 7) | (size << 6);
		int anInt1770 = (player.getBaseLocalY() << 7) | (size << 6);
		int baseRegionX = (player.getFirstChunkX() - 6) * 8;
		int baseRegionY = (player.getFirstChunkY() - 6) * 8;
		int i_35_ = (anInt1783 - (faceX - baseRegionX - baseRegionX) * 64);
		int i_36_ = (anInt1770 - (faceY - baseRegionY - baseRegionY) * 64);
		if (i_35_ != 0 || i_36_ != 0)
			direction = (int) (Math.atan2((double) i_35_, (double) i_36_) * 325.949) & 0x7ff;
		this.update = true;
	}

	// only for players!
	public void set(int direction) {
		this.direction = direction;
		this.update = true;
	}

	// only for npcs!
	public void set(int faceX, int faceY) {
		this.faceX = faceX;
		this.faceY = faceY;
		this.update = true;
	}

	public void markInstant() {
		this.instant = true;
	}

	@Override
	public void reset() {
		this.update = false;
		this.instant = false;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return update;
	}


	@Override
	public void send(Player player) {
		player.avatarExtended().setFaceAngle(this.direction);
	}

	@Override
	public void send(NPC npc) {
		var faceX = Math.min(16383, this.faceX);
		var faceY = Math.min(16383, this.faceY);
		npc.avatarExtended().setFaceCoord(faceX, faceY, this.instant);
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 16 : 1;
	}

}
