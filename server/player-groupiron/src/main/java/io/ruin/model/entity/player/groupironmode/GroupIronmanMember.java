package io.ruin.model.entity.player.groupironmode;

import io.ruin.model.entity.player.Player;

import java.util.Optional;

public class GroupIronmanMember {

	private String username;
	private int combatLevel;
	private long totalXp;
	private int totalLevel;

	public void update(Player player) {
		this.username = player.getName();
		this.combatLevel = player.getCombat().getLevel();
		this.totalXp = player.getStats().totalXp;
		this.totalLevel = player.getStats().totalLevel;
	}

	public GroupIronmanMember() {
	}

	public GroupIronmanMember(Player player) {
		update(player);
	}

	public String getUsername() {
		return username;
	}

	public Optional<Player> getPlayer() {
		return getPlayer();
	}

	public GroupIronmanMember setUsername(String username) {
		this.username = username;
		return this;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public GroupIronmanMember setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
		return this;
	}

	public long getTotalXp() {
		return totalXp;
	}

	public GroupIronmanMember setTotalXp(long totalXp) {
		this.totalXp = totalXp;
		return this;
	}

	public int getTotalLevel() {
		return totalLevel;
	}

	public GroupIronmanMember setTotalLevel(int totalLevel) {
		this.totalLevel = totalLevel;
		return this;
	}
}
