package com.reasonps.dominion.bosses.dagannoth_kings;

import com.reasonps.dominion.rooms.EchoDagannothCave;
import io.ruin.model.entity.npc.NPC;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-28
 */
public interface IEchoDagannothKingCombat {

	/**
	 * Every 250 damage taken, the Kings will swap prayers.
	 * Rex will be next in line to drop prayers,
	 * then the next instance will be Prime,
	 * after which it loops back to Supreme.
	 */
	void togglePrayAgainstAll();

	/**
	 * Retrieves the first instance of an Echo Dagannoth King that currently has no overhead prayers active.
	 * This method filters through the list of active Echo Dagannoth Kings, identifies those that
	 * are not null and have no active overhead prayers, and returns the first such instance found.
	 *
	 * @return An instance of {@code EchoDagannothKingCombat} representing a Dagannoth King with no active
	 *         overhead prayers, or {@code null} if no such instance is found.
	 */
	default Optional<NPC> getKingWithNoPrayers(EchoDagannothCave instance) {
		if (instance == null) return Optional.empty();
		return Stream.of(
				instance.getEchoPrime(),
				instance.getEchoSupreme(),
				instance.getEchoRex()
			).filter(boss -> boss.overheadPrayer() == null)
			.findFirst();
	}
}
