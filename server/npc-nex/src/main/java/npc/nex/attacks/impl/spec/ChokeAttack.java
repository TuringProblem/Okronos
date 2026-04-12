package npc.nex.attacks.impl.spec;

import npc.nex.old.Nex;
import npc.nex.attacks.Attack;
import npc.nex.scripts.NexCombat;
import npc.nex.utils.ZarosUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.map.Bounds;
import io.ruin.model.stat.StatType;

import static npc.nex.utils.ZarosUtils.COUGH;
import static npc.nex.utils.ZarosUtils.INFECTION;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-04
 */
public class ChokeAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		Player infectedPlayer = null;
		int lowestMagicDef = Integer.MAX_VALUE;
		// get the players in the Region
		var targets = combat.getNpc().getPosition().getRegion().players;
		for (var p : targets) {
			int bonus = p.getEquipment().bonuses[EquipmentStats.MAGIC_DEFENCE];
			if (bonus >= lowestMagicDef)
				continue;
			lowestMagicDef = bonus;
			infectedPlayer = p;
		}
		combat.getNpc().getCombat().delayAttack(4);
		combat.getNpc().forceText(CHOKE);
		combat.getNpc().npc.getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: %s".formatted(CHOKE));
		});
		combat.getNpc().faceTemp(infectedPlayer);
		combat.getNpc().animate(9186);

		// System.out.println("Infected lowest magic def:" + infectedPlayer.getName() +
		// " @ " + lowestMagicDef + " magic def");
		if (infectedPlayer == null)
			return;
		plagueEffect(infectedPlayer, false); // make them contagious
		spreadInfection(infectedPlayer); // make sure they are coughing a good cough.
	}

	private static boolean isInNexBounds(Player player) {
		var arena = new Bounds(2909, 5218, 2942, 5188, 0);
		return player.getPosition().inBounds(arena);
	}

	private StatType getDrainedStat(Player p) {
		int stat = 0;
		int bonus = 0;
		//mage
		if (p.getEquipment().bonuses[3] > bonus) {
			bonus = p.getEquipment().bonuses[3];
			stat = 3;
		}
		//range
		if (p.getEquipment().bonuses[4] > bonus) {
			bonus = p.getEquipment().bonuses[4];
			stat = 4;
		}
		//str
		if (p.getEquipment().bonuses[10] > bonus) {
			bonus = p.getEquipment().bonuses[10];
			stat = 10;
		}
		for (int i = 0; i < 2; i++) {
			if (p.getEquipment().bonuses[i] > bonus) {
				bonus = p.getEquipment().bonuses[i];
				stat = i;
			}
		}
		return switch (stat) {
			case 0 -> StatType.Defence;
			case 1 -> StatType.Attack;
			case 10 -> StatType.Strength;
			case 4 -> StatType.Ranged;
			case 3 -> StatType.Magic;
			default -> throw new IllegalStateException("Unexpected value: " + stat);
		};
	}

	private void plagueEffect(Player player, boolean wasSpread) {
		if (player == null || !isInNexBounds(player) || player.get(INFECTION) != null)
			return;

		player.forceText(COUGH);
		player.animate(1156); // COUGH COUGH LADS
		StatType type = getDrainedStat(player);
		final int duration = ZarosUtils.hasSlayerHelm(player) ? ZarosUtils.INFECTION_DURATION_TICKS / 2 : ZarosUtils.INFECTION_DURATION_TICKS;
		player.set(INFECTION, true);
		player.addEvent(event -> {
			player.hit(new Hit(HitType.DAMAGE).randDamage(4, 6));
			player.getPrayer().drain(ZarosUtils.hasSpectral(player) ? ZarosUtils.INFECTION_PRAYER_DRAIN_SPECTRAL : ZarosUtils.INFECTION_PRAYER_DRAIN);
			for (int i = 0; i < duration; i++) {
				if (!player.getPosition().inBounds(Nex.NEX_BOUNDS))
					return;
				if (i % 2 == 0) {
					player.forceText(COUGH);
					player.getPrayer().drain(ZarosUtils.hasSpectral(player) ? ZarosUtils.INFECTION_PRAYER_DRAIN_SPECTRAL : ZarosUtils.INFECTION_PRAYER_DRAIN);
					player.getStats().get(type).drain(2); // drain highest stat by 2
					player.getStats().get(StatType.Defence).drain(2); // drain def by 2
					player.localPlayers().forEach(other -> {
						if (other.getPosition().isWithinDistance(player.getPosition(), 1))
							plagueEffect(other, true);
					});
				}
				event.delay(1);
			}
			player.remove(INFECTION);
		});
	}

	private void spreadInfection(Player player) {
		if (player == null || !isInNexBounds(player) || player.get(INFECTION) != null)
			return;
		player.localPlayers().forEach(other -> {
			if (other.getPosition().isWithinDistance(player.getPosition(), 1))
				plagueEffect(other, true);
		});
	}
}
