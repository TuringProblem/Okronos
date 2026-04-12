package io.ruin.model.skills.magic.spells.arceuus;

import io.ruin.model.World;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;

// TODO: NPC combat somehow is resetting the target to null
public class Ressurect extends Spell {
	private static final Projectile projectile = new Projectile(1289, 40, 10, 5, 75, 15, 10, 64);

	public Ressurect(
		int lvlReq,
		double xp,
		int npcID,
		int maxAliveTimeInTicks,
		int maxDamageToBeDealt,
		Item... runeItems
	) {
		registerClick(lvlReq, xp, true, runeItems, (player, i) -> {
			Position oldPosition = new Position(player.getAbsX(), player.getAbsY(), 0);
			World.startEvent(event -> {
				player.animate(7198);
				player.graphics(1288);
				event.delay(1);
				projectile.send(player.getAbsX(), player.getAbsY(), oldPosition.getX(), oldPosition.getY());
				event.delay(2);
				World.sendGraphics(1290, 0, 0, oldPosition);
				event.delay(1);
				NPC animated = new NPC(1)
					.spawn(oldPosition.getX(), oldPosition.getY(), player.getHeight());
				animated.deathEndListener = (DeathListener.Simple) () -> {
					animated.remove();
				};
				player.unlock();

				// loop the target binding
				while (true) {
					if (animated.isRemoved()) {
						break;
					}

					Entity playerTarget = player.getCombat().getTarget();
					Entity currentTarget = animated.getCombat().getTarget();
					if (playerTarget != null
						&& playerTarget instanceof NPC
						&& currentTarget != playerTarget
					) {
						animated.targetNPC((NPC) playerTarget);
						animated.getCombat().setTarget((NPC) playerTarget);
						animated.attackTargetPlayer();
					}

					event.delay(1);
				}
			});
			return true;
		});
	}
}
