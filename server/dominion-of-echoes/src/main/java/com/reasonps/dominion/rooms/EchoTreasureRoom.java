package com.reasonps.dominion.rooms;

import com.reasonps.dominion.DominionOfEchoes;
import com.reasonps.dominion.loot.RewardGenerator;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.Toxins;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Setter;

import java.util.List;

import static com.reasonps.dominion.DominionOfEchoes.convertTimeInLongToText;
import static core.combat.api.ReasonUtils.pos;
import static io.ruin.model.content.HomeHandler.getPetDonatorBoost;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class EchoTreasureRoom extends Dominion {

	public EchoTreasureRoom(Player player) {
		try {
			setMap(new DynamicMap().build(7316, 1));
			setHost(player);
			player.inDynamicMap = true;
			init();
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void init() {
		// flag some tiles as unwalkable
		List.of(
				pos(map, 9, 43), // North Stairs
				pos(map, 10, 43), // North Stairs
				pos(map, 11, 43), // North Stairs

				pos(map, 9, 26), // South stairs
				pos(map, 10, 26), // South stairs
				pos(map, 11, 26) // South stairs
		).forEach(position -> Tile.get(position).flagUnmovable());

		var keeper = new NPC(17012)
				.spawn(pos(map, 10, 32), Direction.NORTH);
		npcs.add(keeper);
	}

	private void replenishPlayer(Player player) {
		VarPlayerRepository.SPECIAL_ENERGY.set(player, 1000);
		player.getMovement().restoreEnergy(100);
		player.getStats().get(StatType.Prayer).restore();
		for (Stat stat : player.getStats().get()) {
			if (stat != player.getStats().get(StatType.Hitpoints) && stat.currentLevel < stat.fixedLevel)
				stat.alter(stat.fixedLevel);
		}
		player.setHp(player.getMaxHp());
		player.cureVenom(1);
		player.cureVenom(1);
		VarPlayerRepository.POISONED.set(player, 0);
		if (player.isEnvenomed())
			player.toxins.cure(Toxins.ToxinType.VENOM, 0);
		if (player.isPoisoned())
			player.toxins.cure(Toxins.ToxinType.POSION, 0);
		player.sendFilteredMessage("You feel replenished.");
		// reset veng issue
		VarPlayerRepository.VENG_COOLDOWN.set(player, 0);
		player.vengeanceActive = false;
		player.getPacketSender().sendWidgetTimerCustom(Widget.VENGEANCE, 0);
	}

	@Override
	public void movePlayerToInstance(Player player) {
		if (player.get("dominion_of_echos_completed").equals(true)) {
			player.doeBestTime = player.doeTimer.stop(player, player.doeBestTime);
			DominionOfEchoes.getActiveRaidForPlayer(player).getRooms().forEach(mapHandler -> {
				mapHandler.getPlayers().clear();
				mapHandler.destroy();
			});
			player.doeRewardItems
					.addAll(new RewardGenerator(player)
							.generateLoot(3 + Random.get(-1, 1), map)); // 2-4 rewards

			player.set("dominion_of_echos_rewarded", true);
			// Spawn a Reward Object into the room
			GameObject.spawn(60000, pos(map, 9, 37), 10, 0);
			GameObject.spawn(-1, pos(map, 8, 37), 4, 0);

			int petBaseRate = player.doePetRate;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
				petBaseRate *= c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				petBaseRate *= 0.8F;
			petBaseRate *= getPetDonatorBoost(player);
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				petBaseRate *= 0.85F;
			player.doePetRate = petBaseRate;
			if (Random.get(player.doePetRate) == 1)
				Pet.KEEP_OF_ECHOES.unlock(player, 17012);

			// heal the player
			replenishPlayer(player);

			// Notify the player of Raid time
			player.sendMessage("You have completed the raid: %s"
					.formatted(Color.RED.wrap(convertTimeInLongToText(player))));

		}
		player.currentDynamicMap = map;
		player.currentMapHandler = this;
		player.set("dominion_of_echos_completed", false);
		player.set("dominion_of_echos_stage", 0);
		player.getMovement().teleport(pos(map, 8, 34));
		players.add(player);

	}
}
