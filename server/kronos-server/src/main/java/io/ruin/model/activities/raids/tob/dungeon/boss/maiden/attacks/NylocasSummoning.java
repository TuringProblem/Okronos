package io.ruin.model.activities.raids.tob.dungeon.boss.maiden.attacks;

import com.google.common.collect.Lists;
import io.ruin.model.activities.raids.tob.dungeon.RoomType;
import io.ruin.model.activities.raids.tob.dungeon.boss.maiden.MaidenNPC;
import io.ruin.model.activities.raids.tob.dungeon.room.TheatreRoom;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;

import java.util.List;


public class NylocasSummoning {

	public NylocasSummoning(TheatreParty party, int stage, NPC npc, MaidenNPC maiden) {
		nylocas(party, stage, npc, maiden);
	}

	private void nylocas(TheatreParty party, int stage, NPC npc, MaidenNPC maiden) {
		for (int i = 0; i < party.getUsers().size() * 2; i++) {
			NPC nylocas = new NPC(8366).spawn(getNylocasSpawns(party).get(i));
			nylocas.getCombat().setAllowRetaliate(false);
			nylocas.getCombat().setAllowRespawn(false);
			nylocas.startEvent(e -> {
				nylocas.getRouteFinder().routeEntity(npc);
				e.waitForMovement(nylocas);
				nylocas.hit(new Hit().fixedDamage(nylocas.getHp()));
				npc.hit(new Hit(HitType.HEAL).fixedDamage(nylocas.getHp()));
			}).setCancelCondition(nylocas::dead);
		}
		maiden.setStage(stage + 1);
	}

	private List<Position> getNylocasSpawns(TheatreParty party) {
		TheatreRoom room = party.getDungeon().getRooms().get(RoomType.MAIDEN);
		return Lists.newArrayList(
			Position.of(room.convertX(3174), room.convertY(4457)),
			Position.of(room.convertX(3174), room.convertY(4435)),
			Position.of(room.convertX(3179), room.convertY(4436)),
			Position.of(room.convertX(3178), room.convertY(4457)),
			Position.of(room.convertX(3182), room.convertY(4457)),
			Position.of(room.convertX(3182), room.convertY(4435)),
			Position.of(room.convertX(3187), room.convertY(4436)),
			Position.of(room.convertX(3186), room.convertY(4457)),
			Position.of(room.convertX(3186), room.convertY(4455)),
			Position.of(room.convertX(3186), room.convertY(4437))
		);
	}
}
