package npc.nex.attacks.impl.spec;

import io.ruin.api.utils.Random;
import npc.nex.attacks.Attack;
import npc.nex.scripts.NexCombat;
import npc.nex.utils.ZarosUtils;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.player.Player;
import io.ruin.utility.Misc;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-04
 */
public class BloodSscrificeAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		// attempt to choose a player within 7 squares or else get a random player.
		final var closePlayer = combat.getNpc().getPosition()
			.getRegion().players.stream()
			.filter(p -> Misc.getDistance(p.getPosition().copy(), combat.getNpc().getPosition().copy()) < 7)
			.findFirst();
		final var chosen = closePlayer.orElseGet(() ->
			Random.get(combat.getNpc().getPosition().getRegion().players));

		assert chosen != null : "Chosen target is null";
		combat.getNpc().forceText(BLOOD_SACRIFICE);
		combat.getNpc().getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: " + BLOOD_SACRIFICE);
		});
		chosen.addEvent(event -> {
			event.setCancelCondition(combat::targetIsNotInBossRegion);
			chosen.graphics(BLOOD_SACRIFICE_GFX_SM);
			event.delay(2);
			chosen.graphics(BLOOD_SACRIFICE_GFX_MED);
			event.delay(2);
			chosen.graphics(BLOOD_SACRIFICE_GFX_LG);
			event.delay(2);

			final var distance = Misc.getDistance(chosen.getPosition().copy(), combat.getNpc().getPosition().copy());
			// System.out.println("BloodSac dist: " + distance);
			if (distance < 7) { // if closer than 7
				chosen.graphics(2015);
				final var damage = Random.get(1, 50);
				var hit = new Hit(combat.getNpc(), AttackStyle.MAGICAL_MELEE, null)
					.randDamage(damage)
					.ignorePrayer()
					.ignoreDefence();
				hit.postDamage((t) -> {
					chosen.getPrayer().drain(ZarosUtils.hasSpectral(chosen) ? (int) (damage * 0.2) : (int) (damage * 0.25));
					chosen.sendFilteredMessage(PRAYER_DRAIN);
					if (hit.damage > 0) {
						chosen.privateSound(105, 1, 0);
						chosen.graphics(377, 0, 0);
						combat.getNpc().incrementHp((int) (hit.damage * 0.25)); // heal 1/4 of dmg done
					}
					else {
						chosen.privateSound(227, 1, 0);
						chosen.graphics(85, 124, 0); // splash
						hit.hide();
					}
				});
				chosen.hit(hit);
				Hit healhit = new Hit(HitType.HEAL).fixedDamage(25).hide();
				combat.getNpc().hit(healhit);
			}
		});

		List<Player> targets = combat.getNpc().getPosition().getRegion().players;
		targets.forEach(p -> {
			if (p == chosen || Random.get(1, 2) == 1)
				return;

			int delay = BLOOD_MAGIC_PROJECTILE.send(combat.getNpc(), p);
			var hit = new Hit(combat.getNpc(), AttackStyle.MAGIC, null)
				.randDamage(12)
				.ignorePrayer()
				.ignoreDefence()
				.clientDelay(delay);
			hit.postDamage((t) -> {
				t.player.getPrayer().drain(ZarosUtils.hasSpectral(t.player) ? 3 : 4);
				t.player.sendFilteredMessage(PRAYER_DRAIN);
				if (hit.damage > 0) {
					t.privateSound(105, 1, 0);
					t.graphics(377, 0, 0);
					combat.getNpc().incrementHp((int) (hit.damage * 0.25)); // heal 1/4 of dmg done
				}
				else {
					t.privateSound(227, 1, 0);
					t.graphics(85, 124, 0); // splash
					hit.hide();
				}
			});
			p.hit(hit);
		});
	}
}
