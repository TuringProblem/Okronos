package io.ruin.model.entity;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.var.VarPlayerRepository;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-06
 */
public class HealthHud {

	private static final int HP_HUD = 303;

	private static final Object[] OPEN_CS2_ARGS = {
		HP_HUD << 16,
		HP_HUD << 16 | 2,
		HP_HUD << 16 | 4,
		HP_HUD << 16 | 5,
		HP_HUD << 16 | 8,
		HP_HUD << 16 | 10,
		HP_HUD << 16 | 20,
		HP_HUD << 16 | 13,
		HP_HUD << 16 | 14,
		HP_HUD << 16 | 15,
		HP_HUD << 16 | 9,
		HP_HUD << 16 | 6,
		HP_HUD << 16 | 7,
		HP_HUD << 16 | 11,
		HP_HUD << 16 | 18,
		HP_HUD << 16 | 19,
		HP_HUD << 16 | 16,
		HP_HUD << 16 | 17,
		HP_HUD << 16 | 3
	};

	private static final Object[] CLOSE_CS2_ARGS = {
		HP_HUD << 16 | 6,
		HP_HUD << 16 | 7,
		HP_HUD << 16 | 9,
		HP_HUD << 16 | 11,
		HP_HUD << 16 | 13,
		HP_HUD << 16 | 14,
		HP_HUD << 16 | 15,
		HP_HUD << 16 | 20,
		HP_HUD << 16 | 18,
		HP_HUD << 16 | 19,
		HP_HUD << 16 | 16,
		HP_HUD << 16 | 17,
		0
	};

	private final Player player;

	public HealthHud(Player player) {
		this.player = player;
	}

	public void open(boolean centered, int npcId, int maxValue) {
		// Check if the overlay is toggled off
		if (VarPlayerRepository.BOSS_HEALTH_OVERLAY.get(player) == 1)
			return;
		// Update the vars
		VarPlayerRepository.HEALTH_HUD_NPC.set(player, npcId);
		VarPlayerRepository.HEALTH_HUD_BAR.set(player, centered ? 1 : 0);
		// update the data
		updateValue(maxValue);
		updateMaxValue(maxValue);

		player.getPacketSender().sendInterface(HP_HUD, ToplevelComponent.HP_HUD);
		player.getPacketSender().sendClientScript(2376, OPEN_CS2_ARGS);
	}

	public boolean isOpened() {
		return player.isVisibleInterface(HP_HUD);
	}

	public void close() {
		player.getPacketSender().sendClientScript(2889, CLOSE_CS2_ARGS);
		// after the fad out effect, close the interface
		World.startEvent(2, e -> player.closeInterface(ToplevelComponent.HP_HUD));
	}

	public void updateValue(int newValue) {
		VarPlayerRepository.HEALTH_HUD_CURRENT.set(player, newValue);
	}

	public void updateMaxValue(int newValue) {
		VarPlayerRepository.HEALTH_HUD_MAX.set(player, newValue);
	}
}
