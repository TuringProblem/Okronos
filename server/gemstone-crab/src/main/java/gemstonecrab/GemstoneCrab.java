package gemstonecrab;

import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.utility.Random;

public class GemstoneCrab extends NPCCombat {
	Position middleTile = new Position(1353, 3112, 0);
	int attacks = 0;

	@Override
	public void init() {
		npc.attackNpcListener = (player, npc, message) -> canAttack(player);
		npc.hitListener = new HitListener().preDamage(this::preDamage);
	}

	private void preDamage(Hit hit) {
		if(hit.damage > 250)
			hit.damage = 250;
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		int missingHp = npc.getMaxHp() - npc.getHp();
		if(missingHp > 0)
			npc.hit(new Hit(HitType.HEAL).fixedDamage(missingHp));
		npc.face(target);
		npc.animate(12483);
		npc.localPlayers().forEach(p -> {
			if(p.getPosition().distance(middleTile) < 4) {
				target.hit(new Hit(npc).randDamage(1));
			}
		});
		if(attacks++ >= 500) {
			attacks = 0;
			npc.localPlayers().forEach(p -> {
				if(p.getPosition().distance(middleTile) < 12) {
					p.sendMessage("The gemstone crab teleports you away.");
				}
				p.getCombat().reset();
				GemstoneCrabCommands.teleportPlayer(p, Position.of(Random.get(1350, 1356), Random.get(3095, 3101), 0));
			});
		}
		return true;
	}

	private boolean canAttack(Player player) {
		if(player.getPosition().distance(middleTile) > 8) {
			player.sendMessage("You need to move closer to attack the gemstone crab.");
			return false;
		}
		return true;
	}

	@Override
	public void process() {

	}
}
