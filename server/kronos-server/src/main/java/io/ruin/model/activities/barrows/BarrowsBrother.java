package io.ruin.model.activities.barrows;

import io.ruin.cache.NPCType;
import io.ruin.model.activities.barrows.brothers.*;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.map.Position;
import io.ruin.model.map.route.RouteFinder;
import io.ruin.utility.Random;

import java.util.List;
import java.util.Objects;

public enum BarrowsBrother {

	AHRIM(
		1672,
		VarPlayerRepository.AHRIM_KILLED,
		4708, 4710, 4712, 4714
	),
	DHAROK(
		1673,
		VarPlayerRepository.DHAROK_KILLED,
		4716, 4718, 4720, 4722
	),
	GUTHAN(
		1674,
		VarPlayerRepository.GUTHAN_KILLED,
		4724, 4726, 4728, 4730
	),
	KARIL(
		1675,
		VarPlayerRepository.KARIL_KILLED,
		4732, 4734, 4736, 4738
	),
	TORAG(
		1676,
		VarPlayerRepository.TORAG_KILLED,
		4745, 4747, 4749, 4751
	),
	VERAC(
		1677,
		VarPlayerRepository.VERAC_KILLED,
		4753, 4755, 4757, 4759
	);

	public final int npcId;
	public final VarPlayerRepository config;
	public final Integer[] pieces;

	BarrowsBrother(int npcId, VarPlayerRepository config, Integer... pieces) {
		this.npcId = npcId;
		this.config = config;
		this.pieces = pieces;
	}

	List<Position> getSpawnPositions(int id) {
		switch (id) {
			case 1673://Dharok
				return List.of(new Position(3551, 9712, 3),
					new Position(3551, 9717, 3), new Position(3558, 9714, 3));
			case 1672://Ahrim
				return List.of(new Position(3558, 9699, 3),
					new Position(3553, 9701, 3), new Position(3554, 9703, 3));
			case 1674://Guthan
				return List.of(new Position(3536, 9701, 3),
					new Position(3537, 9706, 3), new Position(3541, 9706, 3));
			case 1675://Karil
				return List.of(new Position(3548, 9686, 3),
					new Position(3547, 9682, 3), new Position(3550, 9680, 3));
			case 1676://Torag
				return List.of(new Position(3566, 9685, 3),
					new Position(3568, 9688, 3), new Position(3569, 9683, 3));
			case 1677://Verac
				return List.of(new Position(3577, 9708, 3),
					new Position(3576, 9703, 3), new Position(3573, 9703, 3));
		}
		return null;
	}

	public void spawn(Player player, boolean chest) {
		if (player.npcTarget) {
			player.sendMessage("You are under attack!");
			return;
		}
		List<Position> positions = getSpawnPositions(npcId);
		if(positions == null || positions.isEmpty()) {
			player.sendMessage("Failed to spawn brother, try again.");
			return;
		}
		Position pos = Random.get(positions);
		if (pos == null) {
			player.sendMessage("Failed to spawn brother, try again.");
			return;
		}
		if(chest)
			pos = new Position(3550, 9694, 0);
		NPC npc = new NPC(npcId).spawn(pos).targetPlayer(player, true);
		npc.forceText("You dare disturb my rest!");
		npc.attackTargetPlayer(() -> !player.getPosition().isWithinDistance(npc.getPosition()));
		npc.deathEndListener = (DeathListener.SimpleKiller) killer -> {
			if (killer != null)
				config.set(killer.player, 1);
			if (npc.getCombat() instanceof Dharok) {
				if (!((Dharok) npc.getCombat()).attackWithNonMagic)
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_WHAT_DEFENCE.ordinal())).
						getCombatAchievement()).check(player);
			} else if (npc.getCombat() instanceof Karil) {
				if (!((Karil) npc.getCombat()).attackWithNonMagic)
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_WHAT_DEFENCE.ordinal())).
						getCombatAchievement()).check(player);
			} else if (npc.getCombat() instanceof Ahrim) {
				if (!((Ahrim) npc.getCombat()).attackWithNonMagic)
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_WHAT_DEFENCE.ordinal())).
						getCombatAchievement()).check(player);
			} else if (npc.getCombat() instanceof Guthan) {
				if (!((Guthan) npc.getCombat()).attackWithNonMagic)
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_WHAT_DEFENCE.ordinal())).
						getCombatAchievement()).check(player);
			} else if (npc.getCombat() instanceof Torag) {
				if (!((Torag) npc.getCombat()).attackWithNonMagic)
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_WHAT_DEFENCE.ordinal())).
						getCombatAchievement()).check(player);
			} else if (npc.getCombat() instanceof Verac) {
				if (!((Verac) npc.getCombat()).attackWithNonMagic)
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_WHAT_DEFENCE.ordinal())).
						getCombatAchievement()).check(player);
			}
			npc.remove();
		};
	}

	public static final BarrowsBrother[] VALUES = values();

	public static void register() {
		for (BarrowsBrother brother : BarrowsBrother.VALUES) {
			NPCType.get(brother.npcId).ignoreOccupiedTiles = true;
		}
	}

	public boolean isKilled(Player player) {
		return config.get(player) == 1;
	}

}
